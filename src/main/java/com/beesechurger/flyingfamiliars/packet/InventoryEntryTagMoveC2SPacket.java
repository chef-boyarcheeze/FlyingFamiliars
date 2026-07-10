package com.beesechurger.flyingfamiliars.packet;

import com.beesechurger.flyingfamiliars.item.common.entity.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.item.common.entity.Phylactery;
import com.beesechurger.flyingfamiliars.registries.FFPackets;
import com.beesechurger.flyingfamiliars.tags.EntityTagRef;
import com.beesechurger.flyingfamiliars.tags.SpiritTagRef;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import org.lwjgl.glfw.GLFW;

import java.util.function.Supplier;

import static com.beesechurger.flyingfamiliars.util.FFConstants.STORAGE_SPIRIT_TYPE;

public class InventoryEntryTagMoveC2SPacket
{
	private final ItemStack clientCarriedStack;
	private final int hoverStackIndex;
	private final int button;

	public InventoryEntryTagMoveC2SPacket(ItemStack clientCarriedStack, int hoverStackIndex, int button)
	{
		this.clientCarriedStack = clientCarriedStack;
		this.hoverStackIndex = hoverStackIndex;
		this.button = button;
	}

	public InventoryEntryTagMoveC2SPacket(FriendlyByteBuf buf)
	{
		this.clientCarriedStack = buf.readItem();
		this.hoverStackIndex = buf.readInt();
		this.button = buf.readInt();
	}
	
	public void toBytes(FriendlyByteBuf buf)
	{
		buf.writeItem(clientCarriedStack);
		buf.writeInt(hoverStackIndex);
		buf.writeInt(button);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> supplier)
	{
		supplier.get().enqueueWork(() -> {
			ServerPlayer player = supplier.get().getSender();
			ItemStack hoverStack = player.getInventory().items.get(hoverStackIndex);
			ItemStack carriedStack = player.isCreative() ? clientCarriedStack : player.containerMenu.getCarried();

			if(!Screen.hasShiftDown() && carriedStack.getItem() instanceof BaseEntityTagItem && hoverStack.getItem() instanceof BaseEntityTagItem)
			{
				if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT)
				{
					if (EntityTagRef.INSTANCE.moveEntry(hoverStack.getOrCreateTag(), carriedStack.getOrCreateTag()))
					{
						FFPackets.sendToClients(new SyncInventoryCarriedItemS2CPacket(carriedStack));
						// do stuff, play sound
					}
				}
				else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT)
				{
					if (EntityTagRef.INSTANCE.moveEntry(carriedStack.getOrCreateTag(), hoverStack.getOrCreateTag()))
					{
						FFPackets.sendToClients(new SyncInventoryCarriedItemS2CPacket(carriedStack));
						// do stuff, play sound
					}
				}
				else
				{
					throw new IllegalStateException("Flying Familiars InventoryEntryTagMoveC2SPacket invalid button value (neither left nor right mouse button)");
				}
			}
			else if (Screen.hasShiftDown() && carriedStack.getItem() instanceof Phylactery && hoverStack.getItem() instanceof Phylactery)
			{
				if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT)
				{
					if (handleMoveSpirit(hoverStack, carriedStack))
					{
						FFPackets.sendToClients(new SyncInventoryCarriedItemS2CPacket(carriedStack));
						// do stuff, play sound
					}
				}
				else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT)
				{
					if (handleMoveSpirit(carriedStack, hoverStack))
					{
						FFPackets.sendToClients(new SyncInventoryCarriedItemS2CPacket(carriedStack));
						// do stuff, play sound
					}
				}
				else
				{
					throw new IllegalStateException("Flying Familiars InventoryEntryTagMoveC2SPacket invalid button value (neither left nor right mouse button)");
				}
			}
		});
		
		supplier.get().setPacketHandled(true);
		return true;
	}

	protected boolean handleMoveSpirit(ItemStack sourceStack, ItemStack targetStack)
	{
		ListTag sourceEntryList = SpiritTagRef.INSTANCE.getEntryList(sourceStack.getOrCreateTag());
		ListTag targetEntryList = SpiritTagRef.INSTANCE.getEntryList(targetStack.getOrCreateTag());

		int targetMaxStorage = SpiritTagRef.INSTANCE.getMaxStorage(targetStack.getOrCreateTag());
		boolean successFlag = false;

		for (int i = 0; i < sourceEntryList.size();)
		{
			CompoundTag sourceEntryTag = (CompoundTag) sourceEntryList.get(i);
			boolean entryExistsFlag = false;

			for (Tag targetTag : SpiritTagRef.INSTANCE.getEntryList(targetStack.getOrCreateTag()))
			{
				CompoundTag targetEntryTag = (CompoundTag) targetTag;

				if (sourceEntryTag.getString(STORAGE_SPIRIT_TYPE).equals(targetEntryTag.getString(STORAGE_SPIRIT_TYPE)))
				{
					entryExistsFlag = true;

					if (SpiritTagRef.INSTANCE.moveSpirit(sourceEntryTag, targetEntryTag, targetMaxStorage) && !successFlag) // always want to evaluate moveSpirit first
					{
						successFlag = true;
					}

					if (!(SpiritTagRef.INSTANCE.isEntryEmpty(sourceEntryTag) && SpiritTagRef.INSTANCE.removeEntry(sourceStack.getOrCreateTag(), sourceEntryTag)))
					{
						i++; // did not remove entry from sourceStack, advance normally
					}

					break;
				}
			}

			if (!entryExistsFlag)
			{
				if (SpiritTagRef.INSTANCE.moveEntry(sourceStack.getOrCreateTag(), targetStack.getOrCreateTag(), sourceEntryTag))
				{
					successFlag = true;
				}
				else
				{
					i++; // could not move entry from sourceStack into targetStack, advance normally
				}
			}
		}

		return successFlag;
	}
}
