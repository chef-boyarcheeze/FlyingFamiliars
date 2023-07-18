package com.beesechurger.flyingfamiliars.networking.packet;

import java.util.function.Supplier;

import com.beesechurger.flyingfamiliars.blocks.entity.custom.BrazierBlockEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class EntityListS2CPacket
{
	private final NonNullList<String> entities;
	private final BlockPos pos;
	
	public EntityListS2CPacket(NonNullList<String> e, BlockPos p)
	{
		entities = e;
		pos = p;
	}
	
	public EntityListS2CPacket(FriendlyByteBuf buf)
	{
		entities = NonNullList.withSize(buf.readInt(), "");
		
		for(int i = 0; i < entities.size(); i++)
		{
			entities.set(i, buf.readUtf());
		}
		
		pos = buf.readBlockPos();
	}
	
	public void toBytes(FriendlyByteBuf buf)
	{
		buf.writeInt(entities.size());
		
		for(int i = 0; i < entities.size(); i++)
		{
			buf.writeUtf(entities.get(i));
		}

		buf.writeBlockPos(pos);
	}
	
	@SuppressWarnings("resource")
	public boolean handle(Supplier<NetworkEvent.Context> supplier)
	{
		supplier.get().enqueueWork(() -> 
		{
			if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof BrazierBlockEntity blockEntity)
			{
				blockEntity.setClientEntities(entities);
			}
		});
		
		supplier.get().setPacketHandled(true);
		return true;
	}
	
	public void printEntities()
	{
		for(String e : entities)
		{
			System.out.println(e);
		}
	}
}
