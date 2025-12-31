package com.beesechurger.flyingfamiliars.wand_effect.common.charm;

import com.beesechurger.flyingfamiliars.wand_effect.common.BaseWandEffect;
import com.electronwill.nightconfig.core.conversion.Conversion;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static com.beesechurger.flyingfamiliars.util.FFConstants.CHAT_YELLOW;

public class ZephyreanGustWandEffect extends BaseWandEffect
{
////////////////
// Accessors: //
////////////////

// Strings:
    @Override
    public String getName()
    {
        return "boreal_gust_charm";
    }

    @Override
    public String getTranslatableName()
    {
        return "tooltip.flyingfamiliars.wand_effect.boreal_gust_charm";
    }

// Integers:
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

// Misc:
    @Override
    public ChatFormatting getTooltipColor()
    {
        return ChatFormatting.YELLOW;
    }

/////////////////////////
// Wand effect action: //
/////////////////////////

    @Override
    public void action(Level level, Player player)
    {

    }
}
