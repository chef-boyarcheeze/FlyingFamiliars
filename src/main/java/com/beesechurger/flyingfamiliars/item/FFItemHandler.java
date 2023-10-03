package com.beesechurger.flyingfamiliars.item;

import com.beesechurger.flyingfamiliars.item.common.SoulItems.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.networking.FFMessages;
import com.beesechurger.flyingfamiliars.networking.packet.SoulWandSelectC2SPacket;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FFItemHandler
{
	public static final FFItemHandler INSTANCE = new FFItemHandler();
	
	@SubscribeEvent
	public void onPlayerLeftClick(PlayerInteractEvent.LeftClickEmpty event)
	{
		ItemStack stack = event.getItemStack();
		if (!stack.isEmpty() && stack.getItem() instanceof BaseEntityTagItem)
		{
			FFMessages.sendToServer(new SoulWandSelectC2SPacket());
		}
	}
}
