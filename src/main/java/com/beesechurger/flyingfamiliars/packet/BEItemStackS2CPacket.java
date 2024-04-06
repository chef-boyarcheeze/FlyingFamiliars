package com.beesechurger.flyingfamiliars.packet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import com.beesechurger.flyingfamiliars.block.entity.BaseEntityTagBE;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class BEItemStackS2CPacket
{
	private final NonNullList<ItemStack> items;
	private final BlockPos pos;
	
	public BEItemStackS2CPacket(NonNullList<ItemStack> i, BlockPos p)
	{
		items = i;
		pos = p;
	}
	
	public BEItemStackS2CPacket(FriendlyByteBuf buf)
	{
		List<ItemStack> collection = buf.readCollection(ArrayList::new, FriendlyByteBuf::readItem);
		items = NonNullList.withSize(collection.size(), ItemStack.EMPTY);
		
		for(int i = 0; i < collection.size(); i++)
		{
			items.set(i, collection.get(i));
		}
		
		pos = buf.readBlockPos();
	}
	
	public void toBytes(FriendlyByteBuf buf)
	{
		Collection<ItemStack> list = new ArrayList<>();
		
		for(int i = 0; i < items.size(); i++)
		{
			list.add(items.get(i));
		}
		
		buf.writeCollection(list, FriendlyByteBuf::writeItem);
		buf.writeBlockPos(pos);
	}
	
	@SuppressWarnings("resource")
	public boolean handle(Supplier<NetworkEvent.Context> supplier)
	{
		supplier.get().enqueueWork(() -> 
		{
			if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof BaseEntityTagBE blockEntity)
			{
				blockEntity.setClientItems(items);
			}
		});
		
		supplier.get().setPacketHandled(true);
		return true;
	}
}
