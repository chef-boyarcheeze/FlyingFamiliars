package com.beesechurger.flyingfamiliars.networking.packet;

import com.beesechurger.flyingfamiliars.item.EntityTagItemHelper;
import com.beesechurger.flyingfamiliars.item.common.SoulItems.IModeCycleItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ModeChangeC2SPacket
{
    public ModeChangeC2SPacket() {
    }

    public ModeChangeC2SPacket(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    @SuppressWarnings("resource")
    public boolean handle(Supplier<NetworkEvent.Context> supplier)
    {
        supplier.get().enqueueWork(() -> {

            Player player = supplier.get().getSender();
            Level level = player.level;
            ItemStack stack = player.getMainHandItem();

            if (!stack.isEmpty() && stack.getItem() instanceof IModeCycleItem item)
            {
                if(!stack.hasTag())
                    EntityTagItemHelper.populateTag(stack);

                item.cycleMode(stack, player.isShiftKeyDown());

                //level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), FFSounds.SOUL_WAND_SWAP.get(), SoundSource.NEUTRAL, 0.5f, FFSounds.getPitch());
            }
        });

        supplier.get().setPacketHandled(true);
        return true;
    }
}
