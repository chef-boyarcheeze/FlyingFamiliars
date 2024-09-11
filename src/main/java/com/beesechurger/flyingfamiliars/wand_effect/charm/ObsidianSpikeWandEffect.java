package com.beesechurger.flyingfamiliars.wand_effect.charm;

import com.beesechurger.flyingfamiliars.wand_effect.BaseWandEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static com.beesechurger.flyingfamiliars.util.FFValueConstants.CHAT_GOLD;
import static com.beesechurger.flyingfamiliars.util.FFValueConstants.CHAT_YELLOW;

public class ObsidianSpikeWandEffect extends BaseWandEffect
{
////////////////
// Accessors: //
////////////////

// Integers:

    public int getCost()
    {
        return 0;
    }

    public int getCooldown()
    {
        return 15;
    }

    public int getBarColor()
    {
        return CHAT_GOLD;
    }

// Strings:

    public String getName()
    {
        return "obsidian_spike_charm";
    }

    public String getTranslatableName()
    {
        return "tooltip.flyingfamiliars.wand_effect.obsidian_spike_charm";
    }

// Misc:

    public ChatFormatting getTooltipColor()
    {
        return ChatFormatting.GOLD;
    }

/////////////////////////
// Wand effect action: //
/////////////////////////

    public void action(Level level, Player player)
    {
        
    }
}
