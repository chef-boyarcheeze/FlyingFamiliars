package com.beesechurger.flyingfamiliars.items;

import com.beesechurger.flyingfamiliars.networking.FFMessages;
import com.beesechurger.flyingfamiliars.networking.packet.SoulWandSelectC2SPacket;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FFItemHandler
{
	public static final FFItemHandler INSTANCE = new FFItemHandler();
	
	@SubscribeEvent
	public void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event)
	{
		ItemStack stack = event.getItemStack();
		if (!stack.isEmpty() && stack.getItem() == FFItems.SOUL_WAND.get())
		{
			FFMessages.sendToServer(new SoulWandSelectC2SPacket());
		}
	}
}
