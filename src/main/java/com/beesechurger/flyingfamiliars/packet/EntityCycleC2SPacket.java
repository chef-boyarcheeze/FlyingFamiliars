package com.beesechurger.flyingfamiliars.packet;

import com.beesechurger.flyingfamiliars.item.common.entity.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.registries.FFSounds;
import com.beesechurger.flyingfamiliars.tags.EntityTagUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class EntityCycleC2SPacket
{
	private final int scrollStackIndex;
	private final int direction;
	private final boolean singleStack;

	public EntityCycleC2SPacket(int scrollStackIndex, int direction, boolean singleStack)
	{
		this.scrollStackIndex = scrollStackIndex;
		this.direction = direction;
		this.singleStack = singleStack;
	}

	public EntityCycleC2SPacket(FriendlyByteBuf buf)
	{
		this.scrollStackIndex = buf.readInt();
		this.direction = buf.readInt();
		this.singleStack = buf.readBoolean();
	}
	
	public void toBytes(FriendlyByteBuf buf)
	{
		buf.writeInt(scrollStackIndex);
		buf.writeInt(direction);
		buf.writeBoolean(singleStack);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> supplier)
	{
		supplier.get().enqueueWork(() -> {
			Player player = supplier.get().getSender();
			ItemStack scrollStack = player.getInventory().items.get(scrollStackIndex);

			if(scrollStack.getItem() instanceof BaseEntityTagItem item)
			{
				item.cycle(player, scrollStackIndex, direction, singleStack);

				CompoundTag entryTag = EntityTagUtil.INSTANCE.getSelectedEntry(EntityTagUtil.INSTANCE.getPlayerFullEntityListTag(player));
				ChatFormatting format = EntityTagUtil.isEntityTamed(entryTag) ? ChatFormatting.GREEN : ChatFormatting.YELLOW;

				player.displayClientMessage(Component.translatable("message.flyingfamiliars.entity_tag.select")
						.append(": " + EntityTagUtil.getEntityID(entryTag))
						.withStyle(format), true);

				if(player.getRandom().nextInt(15) == 0)
				{
					player.level().playSound((Player) null, player.getX(), player.getY(), player.getZ(), FFSounds.SOUL_WAND_SWAP.get(), SoundSource.PLAYERS, 0.5f, FFSounds.getPitch());
				}
			}
		});
		
		supplier.get().setPacketHandled(true);
		return true;
	}
}
