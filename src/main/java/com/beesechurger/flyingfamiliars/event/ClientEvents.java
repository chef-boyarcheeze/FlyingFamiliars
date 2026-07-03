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
import com.beesechurger.flyingfamiliars.tags.EntityTagRef;
import com.beesechurger.flyingfamiliars.wand_effect.client.WandEffectSelectionScreen;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
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

@Mod.EventBusSubscriber(modid = FlyingFamiliars.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents
{
	public static final ClientEvents INSTANCE = new ClientEvents();

    public static List<UUID> blockRenderList = new ArrayList<>();

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

    @SubscribeEvent
    public static void preLivingRender(RenderLivingEvent.Pre event)
    {
        Entity passenger = event.getEntity();
        Entity vehicle = passenger.getVehicle();

        if (vehicle instanceof BaseFamiliarEntity familiar && blockRenderList.contains(passenger.getUUID()))
        {
            event.setCanceled(true);
        }
    }

	@SubscribeEvent
	public static void onGatherTooltipComponents(RenderTooltipEvent.GatherComponents event)
	{
		ItemStack stack = event.getItemStack();

		if (stack.getItem() instanceof BaseEntityTagItem item)
		{
			event.getTooltipElements().addAll(
					1,
					item.getTooltipComponents(stack)
							.stream()
							.map(Either::<FormattedText, TooltipComponent>right)
							.collect(Collectors.toList())
			);
		}
	}

	@SubscribeEvent
	public void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event)
	{
		ItemStack stack = event.getItemStack();

		if (!stack.isEmpty() && stack.getItem() instanceof BaseSoulWand)
		{
			FFPackets.sendToServer(new WandEffectAttackC2SPacket());
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerScrollWheel(InputEvent.MouseScrollingEvent event)
	{
		Player player = Minecraft.getInstance().player;

		if(player != null && player.isShiftKeyDown())
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
								&& ((leftMouseFlag && !EntityTagRef.INSTANCE.isEmpty(hoveredStack.getOrCreateTag()) && !EntityTagRef.INSTANCE.isFull(carriedStack.getOrCreateTag()))
									|| (rightMouseFlag && !EntityTagRef.INSTANCE.isEmpty(carriedStack.getOrCreateTag()) && !EntityTagRef.INSTANCE.isFull(hoveredStack.getOrCreateTag()))))
						{
							FFPackets.sendToServer(new InventoryEntryTagMoveC2SPacket(carriedStack, player.getInventory().findSlotMatchingItem(hoveredStack), button));
							event.setCanceled(true);
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

		if (player == null)
			return;

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

	@SubscribeEvent
	public static void onItemPickup(EntityItemPickupEvent event)
	{
		ItemStack stack = event.getItem().getItem();

		if (stack.getItem() instanceof Spirit)
		{
			Phylactery.onPickupItem(event.getItem(), event.getEntity());
			event.setCanceled(true);
		}
	}
}