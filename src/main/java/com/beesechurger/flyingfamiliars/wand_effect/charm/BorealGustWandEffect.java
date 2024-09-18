package com.beesechurger.flyingfamiliars.wand_effect.charm;

import com.beesechurger.flyingfamiliars.wand_effect.BaseWandEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static com.beesechurger.flyingfamiliars.util.FFValueConstants.CHAT_YELLOW;

public class BorealGustWandEffect extends BaseWandEffect
{
////////////////
// Accessors: //
////////////////

// Strings:

    public String getName()
    {
        return "boreal_gust_charm";
    }

    public String getTranslatableName()
    {
        return "tooltip.flyingfamiliars.wand_effect.boreal_gust_charm";
    }

// Integers:
    public int getCost()
    {
        return 0;
    }

    public int getCooldown()
    {
        return 5;
    }

    public int getBarColor()
    {
        return CHAT_YELLOW;
    }

// Misc:

    public ChatFormatting getTooltipColor()
    {
        return ChatFormatting.YELLOW;
    }

/////////////////////////
// Wand effect action: //
/////////////////////////

    public void action(Level level, Player player)
    {

    }
}
