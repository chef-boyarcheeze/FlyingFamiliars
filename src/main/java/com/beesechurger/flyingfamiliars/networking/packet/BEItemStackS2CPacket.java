package com.beesechurger.flyingfamiliars.networking.packet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import com.beesechurger.flyingfamiliars.blocks.entity.custom.BrazierBlockEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkEvent;

public class BEItemStackS2CPacket
{
	private final ItemStackHandler itemStackHandler;
	private final BlockPos pos;
	
	public BEItemStackS2CPacket(ItemStackHandler stack, BlockPos p)
	{
		itemStackHandler = stack;
		pos = p;
	}
	
	public BEItemStackS2CPacket(FriendlyByteBuf buf)
	{
		List<ItemStack> collection = buf.readCollection(ArrayList::new, FriendlyByteBuf::readItem);
		itemStackHandler = new ItemStackHandler(collection.size());
		
		for(int i = 0; i < collection.size(); i++)
		{
			itemStackHandler.insertItem(i, collection.get(i), false);
		}
		
		pos = buf.readBlockPos();
	}
	
	public void toBytes(FriendlyByteBuf buf)
	{
		Collection<ItemStack> list = new ArrayList<>();
		
		for(int i = 0; i < itemStackHandler.getSlots(); i++)
		{
			list.add(itemStackHandler.getStackInSlot(i));
		}
		
		buf.writeCollection(list, FriendlyByteBuf::writeItem);
		buf.writeBlockPos(pos);
	}
	
	@SuppressWarnings("resource")
	public boolean handle(Supplier<NetworkEvent.Context> supplier)
	{
		supplier.get().enqueueWork(() -> 
		{
			if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof BrazierBlockEntity blockEntity)
			{
				blockEntity.setHandler(this.itemStackHandler);
			}
		});
		
		supplier.get().setPacketHandled(true);
		return true;
	}
}
