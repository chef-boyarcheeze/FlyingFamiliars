package com.beesechurger.flyingfamiliars.networking.packet;

import java.util.function.Supplier;

import com.beesechurger.flyingfamiliars.items.FFItems;
import com.beesechurger.flyingfamiliars.items.custom.SoulWand;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class SoulWandSelectC2SPacket
{
	public SoulWandSelectC2SPacket() {}
	
	public SoulWandSelectC2SPacket(FriendlyByteBuf buf) {}
	
	public void toBytes(FriendlyByteBuf buf) {}
	
	public boolean handle(Supplier<NetworkEvent.Context> supplier)
	{
		Player player = supplier.get().getSender();

		supplier.get().enqueueWork(() -> {
			ItemStack stack = player.getMainHandItem();
			if (!stack.isEmpty() && stack.getItem() == FFItems.SOUL_WAND.get())
			{
				if(!player.isShiftKeyDown()) cycleUp(stack);
				else cycleDown(stack);
			}
		});
		
		supplier.get().setPacketHandled(true);
		return true;
	}
	
	private void cycleUp(ItemStack stack)
	{
		CompoundTag compound = stack.getTag();
		
		if (compound != null)
		{
			ListTag temp = compound.getList("entity", 10);
			ListTag tagList = new ListTag();
			
			tagList.add(temp.get(SoulWand.MAX_ENTITIES-1));
			
			for(int i = 0; i < SoulWand.MAX_ENTITIES-1; i++)
			{
				tagList.add(temp.get(i));
			}
			
			compound.put("entity", tagList);
			stack.setTag(compound);
		}		
	}
	
	private void cycleDown(ItemStack stack)
	{
		CompoundTag compound = stack.getTag();
		
		if (compound != null)
		{
			ListTag temp = compound.getList("entity", 10);
			ListTag tagList = new ListTag();
			
			for(int i = 1; i < SoulWand.MAX_ENTITIES; i++)
			{
				tagList.add(temp.get(i));
			}
			
			tagList.add(temp.get(0));
			
			compound.put("entity", tagList);
			stack.setTag(compound);
		}		
	}
}
