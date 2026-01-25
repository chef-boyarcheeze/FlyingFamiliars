package com.beesechurger.flyingfamiliars.wand_effect.common.charm;

import com.beesechurger.flyingfamiliars.wand_effect.common.BaseWandEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import static com.beesechurger.flyingfamiliars.util.FFConstants.CHAT_DARK_AQUA;
import static com.beesechurger.flyingfamiliars.util.FFConstants.MAX_CHARGE_TIME;
import static com.beesechurger.flyingfamiliars.wand_effect.common.WandEffectItemHelper.WAND_EFFECT_CLOUD_CALL;

public class CloudCallWandEffect extends BaseWandEffect
{
//////////////////
/// Accessors: ///
//////////////////

// Strings:
    @Override
    public String getName()
    {
        return WAND_EFFECT_CLOUD_CALL;
    }

// Integers:

    @Override
    public int getUseDurationMin()
    {
        return 20;
    }

    @Override
    public int getUseDurationMax()
    {
        return MAX_CHARGE_TIME;
    }

    @Override
    public int getCost()
    {
        return 0;
    }

    @Override
    public int getCooldown()
    {
        return 60;
    }

    @Override
    public int getBarColor()
    {
        return CHAT_DARK_AQUA;
    }

    // Misc:
    @Override
    public ChatFormatting getTooltipColor()
    {
        return ChatFormatting.DARK_AQUA;
    }

    @Override
    public UseAnim getUseAnimation()
    {
        return UseAnim.BOW;
    }

///////////////////////////
/// Wand effect action: ///
///////////////////////////

    @Override
    public void attack(Level level, Player player)
    {

    }

    @Override
    public void use(Level level, Player player)
    {
        boolean shouldRain;
        boolean shouldThunder;

        if (!level.isClientSide())
        {
            //((ServerLevel) level).setWeatherParameters();
        }

    }
}
