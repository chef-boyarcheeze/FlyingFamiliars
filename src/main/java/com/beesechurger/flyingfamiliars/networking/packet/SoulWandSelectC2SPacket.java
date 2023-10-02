package com.beesechurger.flyingfamiliars.networking.packet;

import java.util.function.Supplier;

import com.beesechurger.flyingfamiliars.item.common.SoulItems.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.item.common.SoulItems.ISoulCycleItem;
import com.beesechurger.flyingfamiliars.sound.FFSounds;

import net.minecraft.ChatFormatting;
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
		supplier.get().enqueueWork(() -> {

			Player player = supplier.get().getSender();
			Level level = player.level;
			ItemStack stack = player.getMainHandItem();

			if(!stack.isEmpty()
					&& stack.getItem() instanceof ISoulCycleItem cycle
					&& stack.getItem() instanceof BaseEntityTagItem base)
			{
				cycle.cycleSoul(player);
				
				level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), FFSounds.SOUL_WAND_SWAP.get(), SoundSource.NEUTRAL, 0.5f, FFSounds.getPitch());
				player.displayClientMessage(new TranslatableComponent("message.flyingfamiliars.entity_tag.select")
						.append(": " + cycle.getID(base.getMaxEntities()-1, stack))
						.withStyle(ChatFormatting.WHITE), true);
			}
		});
		
		supplier.get().setPacketHandled(true);
		return true;
	}
}
