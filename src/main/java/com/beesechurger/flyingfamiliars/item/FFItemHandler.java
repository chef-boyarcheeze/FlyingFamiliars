package com.beesechurger.flyingfamiliars.item;

import com.beesechurger.flyingfamiliars.item.common.soul_items.BaseEntityTagItem;

import com.beesechurger.flyingfamiliars.registries.FFPackets;
import com.beesechurger.flyingfamiliars.packet.SoulItemSelectC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
}
