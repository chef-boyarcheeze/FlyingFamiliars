package com.beesechurger.flyingfamiliars.item;

import com.beesechurger.flyingfamiliars.item.common.entity_items.BaseEntityTagItem;

import com.beesechurger.flyingfamiliars.item.common.entity_items.Phylactery;
import com.beesechurger.flyingfamiliars.registries.FFItems;
import com.beesechurger.flyingfamiliars.registries.FFPackets;
import com.beesechurger.flyingfamiliars.packet.SoulItemSelectC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

@OnlyIn(Dist.CLIENT)
public class FFItemHandler
{
	public static final FFItemHandler INSTANCE = new FFItemHandler();
	
	@SubscribeEvent
	public void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event)
	{
		/*ItemStack stack = event.getItemStack();
		if (!stack.isEmpty() && stack.getItem() instanceof BaseEntityTagItem)
			FFMessages.sendToServer(new SoulWandSelectC2SPacket());*/
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerScrollWheel(InputEvent.MouseScrollingEvent event)
	{
		Player player = Minecraft.getInstance().player;

		if(player != null && player.isShiftKeyDown())
		{
			ItemStack stack = player.getMainHandItem();
			if (!stack.isEmpty() && stack.getItem() instanceof BaseEntityTagItem)
			{
				FFPackets.sendToServer(new SoulItemSelectC2SPacket((int) event.getScrollDelta()));
				event.setCanceled(true);
			}
		}
	}

	public static ItemStack getOffHandTagItem(Player player)
	{
		ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);

		if(offHand != null && offHand.getItem() instanceof Phylactery item)
			return offHand;

		return null;
	}

	public static ItemStack getCurioCharmTagItem(Player player)
	{
		ItemStack[] checkCurios = new ItemStack[7];
		checkCurios[0] = findItem(player, FFItems.PHYLACTERY_BLUE.get());
		checkCurios[1] = findItem(player, FFItems.PHYLACTERY_GREEN.get());
		checkCurios[2] = findItem(player, FFItems.PHYLACTERY_YELLOW.get());
		checkCurios[3] = findItem(player, FFItems.PHYLACTERY_GOLD.get());
		checkCurios[4] = findItem(player, FFItems.PHYLACTERY_RED.get());
		checkCurios[5] = findItem(player, FFItems.PHYLACTERY_BLACK.get());
		checkCurios[6] = findItem(player, FFItems.PHYLACTERY_WHITE.get());

		ItemStack curioCharm = null;

		for(ItemStack stack : checkCurios)
		{
			if(stack != null)
			{
				curioCharm = stack;
				break;
			}
		}

		if(curioCharm != null && curioCharm.getItem() instanceof Phylactery item)
			return curioCharm;

		return null;
	}

	public static ItemStack findItem(Player player, Item item)
	{
		return CuriosApi.getCuriosInventory(player)
				.map(i -> i.findFirstCurio(item).map(SlotResult::stack).orElse(ItemStack.EMPTY))
				.orElse(ItemStack.EMPTY);
	}
}
