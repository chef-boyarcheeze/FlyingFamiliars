package com.beesechurger.flyingfamiliars.packet;

import com.beesechurger.flyingfamiliars.item.common.entity.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.registries.FFPackets;
import com.beesechurger.flyingfamiliars.tags.EntityTagRef;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import org.lwjgl.glfw.GLFW;

import java.util.function.Supplier;

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

			if(carriedStack.getItem() instanceof BaseEntityTagItem && hoverStack.getItem() instanceof BaseEntityTagItem)
			{
				if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT)
				{
					if (EntityTagRef.INSTANCE.moveEntry(hoverStack.getOrCreateTag(), carriedStack.getOrCreateTag()))
					{
						// do stuff, play sound, idk
					}
				}
				else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT)
				{
					if (EntityTagRef.INSTANCE.moveEntry(carriedStack.getOrCreateTag(), hoverStack.getOrCreateTag()))
					{
						// do stuff, play sound, idk
					}
				}
				else
				{
					throw new IllegalStateException("Flying Familiars InventoryEntryTagMoveC2SPacket invalid button value (neither left nor right mouse button)");
				}

				FFPackets.sendToClients(new SyncInventoryCarriedItemS2CPacket(carriedStack));
			}
		});
		
		supplier.get().setPacketHandled(true);
		return true;
	}
}
