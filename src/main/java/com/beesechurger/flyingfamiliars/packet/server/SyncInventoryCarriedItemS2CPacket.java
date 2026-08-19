package com.beesechurger.flyingfamiliars.packet.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncInventoryCarriedItemS2CPacket
{
	private final ItemStack carriedStack;

	public SyncInventoryCarriedItemS2CPacket(ItemStack carriedStack)
	{
		this.carriedStack = carriedStack;
	}

	public SyncInventoryCarriedItemS2CPacket(FriendlyByteBuf buf)
	{
		this.carriedStack = buf.readItem();
	}
	
	public void toBytes(FriendlyByteBuf buf)
	{
		buf.writeItem(carriedStack);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> supplier)
	{
		supplier.get().enqueueWork(() -> {
			if (Minecraft.getInstance().screen instanceof AbstractContainerScreen<?> containerScreen)
			{
				containerScreen.getMenu().setCarried(carriedStack);
				containerScreen.getMenu().setRemoteCarried(carriedStack);
			}
		});
		
		supplier.get().setPacketHandled(true);
		return true;
	}
}
