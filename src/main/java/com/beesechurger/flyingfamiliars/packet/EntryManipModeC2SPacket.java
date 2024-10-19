package com.beesechurger.flyingfamiliars.packet;

import com.beesechurger.flyingfamiliars.item.common.entity_items.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.registries.FFSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class EntryManipModeC2SPacket
{
	public EntryManipModeC2SPacket()
	{
	}

	public EntryManipModeC2SPacket(FriendlyByteBuf buf)
	{
	}
	
	public void toBytes(FriendlyByteBuf buf)
	{
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> supplier)
	{
		supplier.get().enqueueWork(() -> {

			Player player = supplier.get().getSender();
			Level level = player.level();
			ItemStack stack = player.getMainHandItem();

			if(stack.getItem() instanceof BaseEntityTagItem item)
			{
				CompoundTag stackTag = stack.getOrCreateTag();

				item.toggleManipMode(stack);

				level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), FFSounds.SOUL_WAND_SWAP.get(), SoundSource.NEUTRAL, 0.5f, FFSounds.getPitch());

				MutableComponent message = item.getManipMode(stack)
						? Component.translatable("message.flyingfamiliars.item_info_tag.toggle_manip_mode.place")
						: Component.translatable("message.flyingfamiliars.item_info_tag.toggle_manip_mode.remove");

				player.displayClientMessage(message.withStyle(ChatFormatting.WHITE), true);
			}
		});
		
		supplier.get().setPacketHandled(true);
		return true;
	}
}
