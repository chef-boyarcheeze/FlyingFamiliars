package com.beesechurger.flyingfamiliars.packet.client;

import com.beesechurger.flyingfamiliars.item.common.entity.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.util.tags.WandEffectTagUtil;
import com.beesechurger.flyingfamiliars.wand_effect.common.BaseWandEffect;
import com.beesechurger.flyingfamiliars.wand_effect.common.WandEffectItemHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class WandEffectAttackC2SPacket
{
	public WandEffectAttackC2SPacket()
	{
	}

	public WandEffectAttackC2SPacket(FriendlyByteBuf buf)
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
                BaseWandEffect selectedWandEffect = WandEffectItemHelper.getSelectedWandEffect(WandEffectTagUtil.INSTANCE.getSelectedWandEffect(stack.getOrCreateTag()));

                if (selectedWandEffect != null)
                {
                    selectedWandEffect.attack(level, player);
                }
			}
		});
		
		supplier.get().setPacketHandled(true);
		return true;
	}
}
