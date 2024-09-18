package com.beesechurger.flyingfamiliars.wand_effect.charm;

import com.beesechurger.flyingfamiliars.wand_effect.BaseWandEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static com.beesechurger.flyingfamiliars.util.FFValueConstants.CHAT_GOLD;

public class CrystalSpikeWandEffect extends BaseWandEffect
{
////////////////
// Accessors: //
////////////////

    // summon single spike at target nearest to player look location, which both sets the victim to ride the spike (impaling), but also damages it, and persists for a short time before retracting

// Strings:

    public String getName()
    {
        return "crystal_spike_charm";
    }

    public String getTranslatableName()
    {
        return "tooltip.flyingfamiliars.wand_effect.crystal_spike_charm";
    }

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
