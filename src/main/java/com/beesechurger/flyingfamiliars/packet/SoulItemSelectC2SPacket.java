package com.beesechurger.flyingfamiliars.packet;

import java.util.function.Supplier;

import com.beesechurger.flyingfamiliars.item.common.entity_items.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.item.common.entity_items.IEntityCycleItem;
import com.beesechurger.flyingfamiliars.registries.FFSounds;

import com.beesechurger.flyingfamiliars.tags.EntityTagRef;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

public class SoulItemSelectC2SPacket
{
	private final int direction;

	public SoulItemSelectC2SPacket(int di)
	{
		direction = di;
	}
	
	public SoulItemSelectC2SPacket(FriendlyByteBuf buf)
	{
		direction = buf.readInt();
	}
	
	public void toBytes(FriendlyByteBuf buf)
	{
		buf.writeInt(direction);
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

				item.cycle(player, direction);

				CompoundTag entryTag = item.entities.getSelectedEntry(stackTag);
				ChatFormatting format = EntityTagRef.isEntityTamed(entryTag) ? ChatFormatting.GREEN : ChatFormatting.WHITE;

				if(player.getRandom().nextInt(15) == 0)
					level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), FFSounds.SOUL_WAND_SWAP.get(), SoundSource.NEUTRAL, 0.5f, FFSounds.getPitch());

				player.displayClientMessage(Component.translatable("message.flyingfamiliars.entity_tag.select")
						.append(": " + EntityTagRef.getEntityID(entryTag))
						.withStyle(format), true);
			}
		});
		
		supplier.get().setPacketHandled(true);
		return true;
	}
}
