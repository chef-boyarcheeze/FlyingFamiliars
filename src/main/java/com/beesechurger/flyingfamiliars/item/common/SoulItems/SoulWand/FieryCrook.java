package com.beesechurger.flyingfamiliars.item.common.SoulItems.SoulWand;

import com.beesechurger.flyingfamiliars.entity.common.projectile.SoulWand.capture.CaptureProjectile;
import com.beesechurger.flyingfamiliars.item.EntityTagItemHelper;
import com.beesechurger.flyingfamiliars.keys.FFKeys;
import net.minecraft.ChatFormatting;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.beesechurger.flyingfamiliars.util.FFValueConstants.*;

public class FieryCrook extends BaseSoulWand
{
    public FieryCrook(Properties properties)
    {
        super(properties);

        attackColorInt = CHAT_DARK_RED;
        defenseColorInt = CHAT_GOLD;
        attackColorChat = ChatFormatting.DARK_RED;
        defenseColorChat = ChatFormatting.GOLD;
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
