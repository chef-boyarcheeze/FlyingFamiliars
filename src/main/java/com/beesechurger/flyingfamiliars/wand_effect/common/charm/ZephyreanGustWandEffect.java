package com.beesechurger.flyingfamiliars.wand_effect.common.charm;

import com.beesechurger.flyingfamiliars.wand_effect.common.BaseWandEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static com.beesechurger.flyingfamiliars.util.FFConstants.CHAT_YELLOW;
import static com.beesechurger.flyingfamiliars.wand_effect.common.WandEffectItemHelper.WAND_EFFECT_ZEPHYREAN_GUST;

public class ZephyreanGustWandEffect extends BaseWandEffect
{
//////////////////
/// Accessors: ///
//////////////////

/// Strings:
    @Override
    public String getName()
    {
        return WAND_EFFECT_ZEPHYREAN_GUST;
    }

/// Integers:
    @Override
    public int getCost()
    {
        return 0;
    }

    @Override
    public int getCooldown()
    {
        return 5;
    }

    @Override
    public int getBarColor()
    {
        return CHAT_YELLOW;
    }

/// Misc:
    @Override
    public ChatFormatting getTooltipColor()
    {
        return ChatFormatting.YELLOW;
    }

///////////////////////////
/// Wand effect action: ///
///////////////////////////

    @Override
    public void use(Level level, Player player)
    {

    }
}
