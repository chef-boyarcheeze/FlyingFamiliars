package com.beesechurger.flyingfamiliars.item.common.soul_items.SoulWand;

import com.beesechurger.flyingfamiliars.item.EntityTagItemHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.beesechurger.flyingfamiliars.util.FFValueConstants.*;

public class VoidShard extends BaseSoulWand
{
    public VoidShard(Properties properties)
    {
        super(properties);

        attackColorInt = CHAT_LIGHT_PURPLE;
        defenseColorInt = CHAT_DARK_PURPLE;
        attackColorChat = ChatFormatting.LIGHT_PURPLE;
        defenseColorChat = ChatFormatting.DARK_PURPLE;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        EntityTagItemHelper.ensureTagPopulated(stack);

        int mode = this.getMode(stack);

        switch(mode)
        {
            //case 1 -> ;
            //case 2 -> ;
            default -> super.use(level, player, hand);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
