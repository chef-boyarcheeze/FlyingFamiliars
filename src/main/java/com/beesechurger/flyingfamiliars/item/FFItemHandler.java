package com.beesechurger.flyingfamiliars.item;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.item.common.BaseStorageTagItem;
import com.beesechurger.flyingfamiliars.item.common.entity.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.item.common.entity.Phylactery;
import com.beesechurger.flyingfamiliars.item.common.entity.Spirit;
import com.beesechurger.flyingfamiliars.item.common.entity.soul_wand.BaseSoulWand;
import com.beesechurger.flyingfamiliars.packet.EntityCycleC2SPacket;
import com.beesechurger.flyingfamiliars.packet.WandEffectAttackC2SPacket;
import com.beesechurger.flyingfamiliars.registries.FFItems;
import com.beesechurger.flyingfamiliars.registries.FFPackets;
import com.beesechurger.flyingfamiliars.tags.EntityTagRef;
import com.beesechurger.flyingfamiliars.wand_effect.client.WandEffectSelectionScreen;
import com.google.common.collect.Iterables;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.beesechurger.flyingfamiliars.registries.FFKeys.WAND_EFFECT_SELECT_STATE;
import static com.beesechurger.flyingfamiliars.registries.FFKeys.update;

@Mod.EventBusSubscriber(modid = FlyingFamiliars.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class FFItemHandler
{
	public static final FFItemHandler INSTANCE = new FFItemHandler();

	@SubscribeEvent
	public void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event)
	{
		ItemStack stack = event.getItemStack();

		if (!stack.isEmpty() && stack.getItem() instanceof BaseStorageTagItem)
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
			ItemStack stack = player.getMainHandItem();

			if (stack.getItem() instanceof BaseEntityTagItem item && item.canCycle(player, stack))
			{
				FFPackets.sendToServer(new EntityCycleC2SPacket((int) event.getScrollDelta()));
				event.setCanceled(true);
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

	public static List<ItemStack> getEntityStackList(Player player)
	{
		if (player == null)
		{
			return Collections.emptyList();
		}

		List<ItemStack> stacks = new ArrayList<>();

		ItemStack mainHand = player.getMainHandItem();

		// soul wand in main hand
		if (mainHand != null && mainHand.getItem() instanceof BaseSoulWand)
		{
			stacks.add(mainHand);
		}

		// phylacteries anywhere in inventory
		for (ItemStack stack : Iterables.concat(player.getInventory().offhand, player.getInventory().items))
		{
			if (!stack.isEmpty() && stack.getItem() instanceof Phylactery)
			{
				stacks.add(stack);
			}
		}

		// phylactery in curio slot
		if (getCurioCharmTagItem(player) != null)
        {
            stacks.add(getCurioCharmTagItem(player));
        }

		return stacks;
	}

    // swapping of item to be in player's hand, derived from Botania's version in their Player helper class
    public static Pair<InteractionResult, BlockPos> substituteUse(UseOnContext context, ItemStack toUse)
    {
        ItemStack save = ItemStack.EMPTY;
        BlockHitResult hit = new BlockHitResult(context.getClickLocation(), context.getClickedFace(), context.getClickedPos(), context.isInside());
        UseOnContext newcontext;

        if (context.getPlayer() != null)
		{
            save = context.getPlayer().getItemInHand(context.getHand());
            context.getPlayer().setItemInHand(context.getHand(), toUse);
            // Need to construct a new one still to refresh the itemstack
            newcontext = new UseOnContext(context.getPlayer(), context.getHand(), hit);
        }
        else
        {
            newcontext = new UseOnContext(context.getLevel(), null, context.getHand(), toUse, hit);
        }

        BlockPos finalPos = new BlockPlaceContext(newcontext).getClickedPos();

        InteractionResult result = toUse.useOn(newcontext);

        if (context.getPlayer() != null)
		{
            context.getPlayer().setItemInHand(context.getHand(), save);
        }

        return Pair.of(result, finalPos);
    }

	public static ItemStack getCurioCharmTagItem(Player player)
	{
		ItemStack curioCharm = findItem(player, FFItems.PHYLACTERY.get());

		if(curioCharm != null && curioCharm.getItem() instanceof Phylactery item)
        {
            return curioCharm;
        }

		return null;
	}

	public static ItemStack findItem(Player player, Item item)
	{
		return CuriosApi.getCuriosInventory(player)
				.map(i -> i.findFirstCurio(item).map(SlotResult::stack).orElse(ItemStack.EMPTY))
				.orElse(ItemStack.EMPTY);
	}

	public static class TooltipLockHandler
	{
		public static final TooltipLockHandler INSTANCE = new TooltipLockHandler();

		private int lockX = 0;
		private int lockY = 0;

		private Slot mouseLockSlot = null;

		public ItemStack getLockedStack()
		{
			return mouseLockSlot.getItem();
		}

		public int getLockedX()
		{
			return lockX;
		}

		public int getLockedY()
		{
			return lockY;
		}

		public boolean isLocked()
		{
			Minecraft mc = Minecraft.getInstance();

			if (Screen.hasShiftDown() && Screen.hasControlDown())
			{
				if (mouseLockSlot == null && mc.screen instanceof AbstractContainerScreen<?> containerScreen)
				{
					Slot hoveredSlot = containerScreen.getSlotUnderMouse();

					if (hoveredSlot != null && hoveredSlot.getItem().getItem() instanceof BaseEntityTagItem && !EntityTagRef.INSTANCE.isEmpty(hoveredSlot.getItem().getOrCreateTag()))
					{
						mouseLockSlot = hoveredSlot;
						lockX = (int) (mc.mouseHandler.xpos() / mc.getWindow().getGuiScale());
						lockY = (int) (mc.mouseHandler.ypos() / mc.getWindow().getGuiScale());
					}
					else
					{
						mouseLockSlot = null;
						lockX = 0;
						lockY = 0;
					}
				}
			}
			else
			{
				mouseLockSlot = null;
				lockX = 0;
				lockY = 0;
			}

			return mouseLockSlot != null;
		}
	}
}
