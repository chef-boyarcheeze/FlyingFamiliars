package com.beesechurger.flyingfamiliars.networking.packet;

import java.util.function.Supplier;

import com.beesechurger.flyingfamiliars.items.FFItems;
import com.beesechurger.flyingfamiliars.items.custom.SoulWand;
import com.beesechurger.flyingfamiliars.sound.FFSounds;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

public class SoulWandSelectC2SPacket
{
	public SoulWandSelectC2SPacket() {}
	
	public SoulWandSelectC2SPacket(FriendlyByteBuf buf) {}
	
	public void toBytes(FriendlyByteBuf buf) {}
	
	public boolean handle(Supplier<NetworkEvent.Context> supplier)
	{
		Player player = supplier.get().getSender();
		Level level = player.level;

		supplier.get().enqueueWork(() -> {
			ItemStack stack = player.getMainHandItem();
			if (!stack.isEmpty() && stack.getItem() == FFItems.SOUL_WAND.get())
			{
				if(!player.isShiftKeyDown()) cycleUp(player, stack);
				else cycleDown(player, stack);
				
				level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), FFSounds.SOUL_WAND_SWAP.get(), SoundSource.NEUTRAL, 0.5F, FFSounds.getPitch());
				player.displayClientMessage(new TranslatableComponent("message.flyingfamiliars.soul_wand.select").withStyle(ChatFormatting.AQUA).append(": " + getID(SoulWand.MAX_ENTITIES-1, stack)), true);
			}
		});
		
		supplier.get().setPacketHandled(true);
		return true;
	}
	
	private void cycleUp(Player player, ItemStack stack)
	{		
		if(stack.getTag() == null) SoulWand.populateTag(stack);
		
		CompoundTag compound = stack.getTag();
		
		ListTag temp = compound.getList("flyingfamiliars.entity", 10);
		ListTag tagList = new ListTag();
		
		tagList.add(temp.get(SoulWand.MAX_ENTITIES-1));
		
		for(int i = 0; i < SoulWand.MAX_ENTITIES-1; i++)
		{
			tagList.add(temp.get(i));
		}
		
		compound.put("flyingfamiliars.entity", tagList);
		stack.setTag(compound);		
	}
	
	private void cycleDown(Player player, ItemStack stack)
	{
		if(stack.getTag() == null) SoulWand.populateTag(stack);
		
		CompoundTag compound = stack.getTag();
		
		ListTag temp = compound.getList("flyingfamiliars.entity", 10);
		ListTag tagList = new ListTag();
		
		for(int i = 1; i < SoulWand.MAX_ENTITIES; i++)
		{
			tagList.add(temp.get(i));
		}
		
		tagList.add(temp.get(0));
		
		compound.put("flyingfamiliars.entity", tagList);
		stack.setTag(compound);
	}
	
	public String getID(int listValue, ItemStack stack)
    {
        return stack.getTag().getList("flyingfamiliars.entity", 10).getCompound(listValue).getString("flyingfamiliars.entity");
    }
}
