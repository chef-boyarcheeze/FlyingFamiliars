
package com.beesechurger.flyingfamiliars.packet;

import com.beesechurger.flyingfamiliars.item.common.entity_items.SoulWand.BaseSoulWand;
import com.beesechurger.flyingfamiliars.tags.WandEffectTagRef;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.beesechurger.flyingfamiliars.util.FFConstants.STORAGE_WAND_EFFECT_SELECTION;

public class WandEffectSelectionC2SPacket
{
    private final int newSelectionIndex;
    private final int currentSelectionIndex;

    public WandEffectSelectionC2SPacket(int newSelection, int currentSelection)
    {
        newSelectionIndex = newSelection;
        currentSelectionIndex = currentSelection;
    }

    public WandEffectSelectionC2SPacket(FriendlyByteBuf buf)
    {
        newSelectionIndex = buf.readInt();
        currentSelectionIndex = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf)
    {
        buf.writeInt(newSelectionIndex);
        buf.writeInt(currentSelectionIndex);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() -> {
            Player player = supplier.get().getSender();
            Level level = player.level();
            ItemStack stack = player.getMainHandItem();

            if(stack.getItem() instanceof BaseSoulWand item)
            {
                CompoundTag storageTag = stack.getOrCreateTag();
                ListTag entryList = WandEffectTagRef.INSTANCE.getEntryList(storageTag);

                entryList.getCompound(newSelectionIndex).put(STORAGE_WAND_EFFECT_SELECTION, new CompoundTag());
                entryList.getCompound(currentSelectionIndex).remove(STORAGE_WAND_EFFECT_SELECTION);
            }
        });

        supplier.get().setPacketHandled(true);
        return true;
    }
}