package com.beesechurger.flyingfamiliars.packet;

import com.beesechurger.flyingfamiliars.block.entity.BaseEntityTagBE;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BEFluidLevelS2CPacket
{
	private final CompoundTag entities;
	private final BlockPos pos;

	public BEFluidLevelS2CPacket(CompoundTag e, BlockPos p)
	{
		entities = e;
		pos = p;
	}

	public BEFluidLevelS2CPacket(FriendlyByteBuf buf)
	{
		entities = buf.readNbt();
		pos = buf.readBlockPos();
	}
	
	public void toBytes(FriendlyByteBuf buf)
	{
		buf.writeNbt(entities);
		buf.writeBlockPos(pos);
	}
	
	@SuppressWarnings("resource")
	public boolean handle(Supplier<NetworkEvent.Context> supplier)
	{
		supplier.get().enqueueWork(() -> 
		{
			if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof BaseEntityTagBE blockEntity)
			{
				blockEntity.setClientEntities(entities);
			}
		});
		
		supplier.get().setPacketHandled(true);
		return true;
	}
}
