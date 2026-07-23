package com.beesechurger.flyingfamiliars.event;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.familiar.BaseFamiliarEntity;
import com.beesechurger.flyingfamiliars.item.FFItemHandler;
import com.beesechurger.flyingfamiliars.item.common.entity.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.item.common.entity.Phylactery;
import com.beesechurger.flyingfamiliars.item.common.entity.Spirit;
import com.beesechurger.flyingfamiliars.item.common.entity.soul_wand.BaseSoulWand;
import com.beesechurger.flyingfamiliars.packet.EntityCycleC2SPacket;
import com.beesechurger.flyingfamiliars.packet.InventoryEntryTagMoveC2SPacket;
import com.beesechurger.flyingfamiliars.packet.WandEffectAttackC2SPacket;
import com.beesechurger.flyingfamiliars.registries.FFPackets;
import com.beesechurger.flyingfamiliars.tags.EntityTagUtil;
import com.beesechurger.flyingfamiliars.tags.SpiritTagUtil;
import com.beesechurger.flyingfamiliars.wand_effect.client.WandEffectSelectionScreen;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.beesechurger.flyingfamiliars.registries.FFKeys.WAND_EFFECT_SELECT_STATE;
import static com.beesechurger.flyingfamiliars.registries.FFKeys.update;
import static com.beesechurger.flyingfamiliars.util.FFConstants.STORAGE_SPIRIT_TYPE;

@Mod.EventBusSubscriber(modid = FlyingFamiliars.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class FFEvents
{
	public static final FFEvents INSTANCE = new FFEvents();

//////////////////
/// Rendering: ///
//////////////////

    @SubscribeEvent
    public static void onCameraSetup(ViewportEvent.ComputeCameraAngles event)
    {
    	CameraType camera = Minecraft.getInstance().options.getCameraType();
    	Player player = Minecraft.getInstance().player;
        
    	if(player.getVehicle() != null)
    	{
    		if(player.getVehicle() instanceof BaseFamiliarEntity familiar)
            {
    			double cameraZoom = camera == CameraType.FIRST_PERSON ? 0.5 : 1.25;
    			double cameraRotMod = 0.5f;
    			
    			float renderPitch = (float) (cameraRotMod * familiar.getPitch(event.getPartialTick()) +
    					(camera == CameraType.THIRD_PERSON_FRONT ?
    							-player.getViewXRot((float) event.getPartialTick()) :
    								player.getViewXRot((float) event.getPartialTick())));
    			
    			float renderRoll = (float) (cameraRotMod * (camera == CameraType.THIRD_PERSON_FRONT ?
    					familiar.getRoll(event.getPartialTick()) :
    						-familiar.getRoll(event.getPartialTick())));
    			
    			event.setPitch(renderPitch);
				event.setRoll(renderRoll);
    			
    			event.getCamera().move(-event.getCamera().getMaxZoom(cameraZoom), 0, 0);
            }
    	}
    }

	public static List<UUID> BLOCK_RENDER_LIST = new ArrayList<>();

    @SubscribeEvent
    public static void preLivingRender(RenderLivingEvent.Pre event)
    {
        Entity passenger = event.getEntity();
        Entity vehicle = passenger.getVehicle();

        if (vehicle instanceof BaseFamiliarEntity familiar && BLOCK_RENDER_LIST.contains(passenger.getUUID()))
        {
            event.setCanceled(true);
        }
    }

	@SubscribeEvent
	public static void onRenderGui(RenderGuiOverlayEvent.Pre event)
	{
		if (event.getOverlay().id().equals(VanillaGuiOverlay.CHAT_PANEL.id()))
		{
			if (WandEffectSelectionScreen.INSTANCE.isActive())
			{
				event.setCanceled(true);
			}
		}
	}

//////////////////
/// Inventory: ///
//////////////////

	@SubscribeEvent
	public static void onGatherTooltipComponents(RenderTooltipEvent.GatherComponents event)
	{
		ItemStack tooltipStack = event.getItemStack();
		ItemStack lockedStack = FFItemHandler.TooltipLockHandler.INSTANCE.getLockedStack();

		if (!lockedStack.isEmpty())
		{
			tooltipStack = lockedStack;
		}

		if (tooltipStack.getItem() instanceof BaseEntityTagItem item)
		{
			event.getTooltipElements().addAll(
					1,
					item.getTooltipComponents(tooltipStack)
							.stream()
							.map(Either::<FormattedText, TooltipComponent>right)
							.collect(Collectors.toList())
			);
		}
	}

	private static boolean TOOLTIP_LOCK = false;

	@SubscribeEvent
	public static void onRenderTooltip(RenderTooltipEvent.Pre event)
	{
		if (!TOOLTIP_LOCK && !FFItemHandler.TooltipLockHandler.INSTANCE.getLockedStack().isEmpty())
		{
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onScreenRender(ScreenEvent.Render.Post event)
	{
		if (FFItemHandler.TooltipLockHandler.INSTANCE.lock())
		{
			var lockedStack = FFItemHandler.TooltipLockHandler.INSTANCE.getLockedStack();

			if (!lockedStack.isEmpty() && lockedStack.getItem() instanceof BaseEntityTagItem)
			{
				TOOLTIP_LOCK = true;

				event.getGuiGraphics().renderComponentTooltip(
						Minecraft.getInstance().font,
						Screen.getTooltipFromItem(Minecraft.getInstance(), lockedStack),
						FFItemHandler.TooltipLockHandler.INSTANCE.getLockedX(),
						FFItemHandler.TooltipLockHandler.INSTANCE.getLockedY()
				);

				TOOLTIP_LOCK = false;
			}
		}
	}

	@SubscribeEvent
	public static void onScreenClose(ScreenEvent.Closing event)
	{
		if (event.getScreen() instanceof AbstractContainerScreen)
		{
			FFItemHandler.TooltipLockHandler.INSTANCE.unlock();
		}
	}

///////////////////////////////////
/// Client-side Player Actions: ///
///////////////////////////////////

	@SubscribeEvent
	public void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event)
	{
		if (WandEffectSelectionScreen.INSTANCE.isActive())
		{
			event.setCanceled(true);
			return;
		}

		ItemStack stack = event.getItemStack();

		if (!stack.isEmpty() && stack.getItem() instanceof BaseSoulWand)
		{
			FFPackets.sendToServer(new WandEffectAttackC2SPacket());
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerScrollWheel(InputEvent.MouseScrollingEvent event)
	{
		if (WandEffectSelectionScreen.INSTANCE.isActive())
		{
			event.setCanceled(true);
			return;
		}

		Player player = Minecraft.getInstance().player;

		if (player != null && player.isShiftKeyDown())
		{
			ItemStack scrollStack = player.getMainHandItem();
			List<ItemStack> allStacks = FFItemHandler.getEntityStackList(player);

			if (scrollStack.getItem() instanceof BaseEntityTagItem item && item.canCycle(player, scrollStack, allStacks))
			{
				FFPackets.sendToServer(new EntityCycleC2SPacket(player.getInventory().findSlotMatchingItem(scrollStack), (int) event.getScrollDelta(), false));
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void onInventoryMouseRelease(ScreenEvent.MouseButtonReleased.Pre event)
	{
		int button = event.getButton();
		boolean leftMouseFlag = button == GLFW.GLFW_MOUSE_BUTTON_LEFT;
		boolean rightMouseFlag = button == GLFW.GLFW_MOUSE_BUTTON_RIGHT;

		if (leftMouseFlag || rightMouseFlag)
		{
			if (event.getScreen() instanceof AbstractContainerScreen<?> containerScreen)
			{
				Player player = Minecraft.getInstance().player;

				if (player != null)
				{
					Slot hoveredSlot = containerScreen.getSlotUnderMouse();

					if (hoveredSlot != null && hoveredSlot.hasItem())
					{
						ItemStack carriedStack = containerScreen.getMenu().getCarried();
						ItemStack hoveredStack = hoveredSlot.getItem();

						if (carriedStack.getItem() instanceof BaseEntityTagItem && hoveredStack.getItem() instanceof BaseEntityTagItem
								&& ((leftMouseFlag && !EntityTagUtil.INSTANCE.isEmpty(hoveredStack.getOrCreateTag()) && !EntityTagUtil.INSTANCE.isFull(carriedStack.getOrCreateTag()))
									|| (rightMouseFlag && !EntityTagUtil.INSTANCE.isEmpty(carriedStack.getOrCreateTag()) && !EntityTagUtil.INSTANCE.isFull(hoveredStack.getOrCreateTag()))
								&& !Screen.hasShiftDown()))
						{
							FFPackets.sendToServer(new InventoryEntryTagMoveC2SPacket(carriedStack, player.getInventory().findSlotMatchingItem(hoveredStack), button));
							event.setCanceled(true);
						}
						else if (carriedStack.getItem() instanceof Phylactery && hoveredStack.getItem() instanceof Phylactery && Screen.hasShiftDown())
						{
							System.out.println("hello");

							if (leftMouseFlag)
							{
								int carriedMaxStorage = SpiritTagUtil.INSTANCE.getMaxStorage(carriedStack.getOrCreateTag());

								for (Tag hoveredTag : SpiritTagUtil.INSTANCE.getEntryList(hoveredStack.getOrCreateTag()))
								{
									boolean entryExistsFlag = false;

									for (Tag carriedTag : SpiritTagUtil.INSTANCE.getEntryList(carriedStack.getOrCreateTag()))
									{
										CompoundTag hoveredEntryTag = (CompoundTag) hoveredTag;
										CompoundTag carriedEntryTag = (CompoundTag) carriedTag;

										// entry's type matches in both items and the target entry is not full
										if (hoveredEntryTag.getString(STORAGE_SPIRIT_TYPE).equals(carriedEntryTag.getString(STORAGE_SPIRIT_TYPE)))
										{
											entryExistsFlag = true;

											// entry exists in target stack and is not full - send packet
											if (!SpiritTagUtil.INSTANCE.isEntryFull(carriedEntryTag, carriedMaxStorage))
											{
												FFPackets.sendToServer(new InventoryEntryTagMoveC2SPacket(carriedStack, player.getInventory().findSlotMatchingItem(hoveredStack), button));
												event.setCanceled(true);

												return;
											}
										}
									}

									// entry does not exist in target stack, and target stack is not full - send packet
									if (!entryExistsFlag && !SpiritTagUtil.INSTANCE.isFull(carriedStack.getOrCreateTag()))
									{
										FFPackets.sendToServer(new InventoryEntryTagMoveC2SPacket(carriedStack, player.getInventory().findSlotMatchingItem(hoveredStack), button));
										event.setCanceled(true);

										return;
									}
								}
							}
							else if (rightMouseFlag)
							{
								int hoveredMaxStorage = SpiritTagUtil.INSTANCE.getMaxStorage(hoveredStack.getOrCreateTag());

								for (Tag carriedTag : SpiritTagUtil.INSTANCE.getEntryList(carriedStack.getOrCreateTag()))
								{
									boolean entryExistsFlag = false;

									for (Tag hoveredTag : SpiritTagUtil.INSTANCE.getEntryList(hoveredStack.getOrCreateTag()))
									{
										CompoundTag carriedEntryTag = (CompoundTag) carriedTag;
										CompoundTag hoveredEntryTag = (CompoundTag) hoveredTag;

										// entry's type matches in both items and the target entry is not full
										if (carriedEntryTag.getString(STORAGE_SPIRIT_TYPE).equals(hoveredEntryTag.getString(STORAGE_SPIRIT_TYPE)))
										{
											entryExistsFlag = true;

											// entry exists in target stack and is not full - send packet
											if (!SpiritTagUtil.INSTANCE.isEntryFull(hoveredEntryTag, hoveredMaxStorage))
											{
												FFPackets.sendToServer(new InventoryEntryTagMoveC2SPacket(carriedStack, player.getInventory().findSlotMatchingItem(hoveredStack), button));
												event.setCanceled(true);

												return;
											}
										}
									}

									// entry does not exist in target stack, and target stack is not full - send packet
									if (!entryExistsFlag && !SpiritTagUtil.INSTANCE.isFull(hoveredStack.getOrCreateTag()))
									{
										FFPackets.sendToServer(new InventoryEntryTagMoveC2SPacket(carriedStack, player.getInventory().findSlotMatchingItem(hoveredStack), button));
										event.setCanceled(true);

										return;
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onInventoryScrollWheel(ScreenEvent.MouseScrolled.Pre event)
	{
		if (event.getScreen() instanceof AbstractContainerScreen<?> containerScreen)
		{
			Player player = Minecraft.getInstance().player;

			if (player != null && containerScreen.hasShiftDown())
			{
				Slot hoveredSlot = containerScreen.getSlotUnderMouse();

				if (hoveredSlot != null && hoveredSlot.hasItem())
				{
					ItemStack scrollStack = hoveredSlot.getItem();
					List<ItemStack> allStacks = List.of(scrollStack);

					if (scrollStack.getItem() instanceof BaseEntityTagItem item && item.canCycle(player, scrollStack, allStacks))
					{
						FFPackets.sendToServer(new EntityCycleC2SPacket(player.getInventory().findSlotMatchingItem(scrollStack), (int) event.getScrollDelta(), true));
						event.setCanceled(true);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onKeyInput(InputEvent.Key event)
	{
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;

		if (player != null)
		{
			ItemStack stack = player.getMainHandItem();

			if (mc.screen == null && stack.getItem() instanceof BaseSoulWand)
			{
				if (WAND_EFFECT_SELECT_STATE.wasPressed())
				{
					WandEffectSelectionScreen.INSTANCE.open(stack);
				}
				else if (WAND_EFFECT_SELECT_STATE.wasReleased() && WandEffectSelectionScreen.INSTANCE.isActive())
				{
					WandEffectSelectionScreen.INSTANCE.close();
				}
			}

			update();
		}
	}

///////////////////////////////////
/// Server-side Player Actions: ///
///////////////////////////////////

	@SubscribeEvent
	public static void onItemPickup(EntityItemPickupEvent event)
	{
		ItemStack stack = event.getItem().getItem();

		if (stack.getItem() instanceof Spirit && Phylactery.onPickupItem(event.getItem(), event.getEntity()))
		{
			event.setCanceled(true);
		}
	}
}