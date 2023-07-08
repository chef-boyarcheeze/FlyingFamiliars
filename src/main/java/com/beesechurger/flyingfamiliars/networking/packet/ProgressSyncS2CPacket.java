package com.beesechurger.flyingfamiliars.networking.packet;

import java.util.function.Supplier;

import com.beesechurger.flyingfamiliars.blocks.entity.custom.BrazierBlockEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class ProgressSyncS2CPacket
{
	private final int progress;
	private final BlockPos pos;
	
	public ProgressSyncS2CPacket(int pr, BlockPos bp)
	{
		progress = pr;
		pos = bp;
	}
	
	public ProgressSyncS2CPacket(FriendlyByteBuf buf)
	{
		progress = buf.readInt();
		//System.out.println(progress);
		pos = buf.readBlockPos();
	}
	
	public void toBytes(FriendlyByteBuf buf)
	{				
		buf.writeInt(progress);
		buf.writeBlockPos(pos);
	}
	
	@SuppressWarnings("resource")
	public boolean handle(Supplier<NetworkEvent.Context> supplier)
	{
		supplier.get().enqueueWork(() -> 
		{
			if(Minecraft.getInstance().level.getBlockEntity(pos) instanceof BrazierBlockEntity blockEntity)
			{
				blockEntity.setProgress(progress);
			}
		});
		
		supplier.get().setPacketHandled(true);
		return true;
	}
}
