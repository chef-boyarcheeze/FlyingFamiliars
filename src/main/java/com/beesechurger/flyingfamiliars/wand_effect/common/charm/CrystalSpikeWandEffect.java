package com.beesechurger.flyingfamiliars.wand_effect.common.charm;

import com.beesechurger.flyingfamiliars.util.FFTypes;
import com.beesechurger.flyingfamiliars.wand_effect.common.BaseWandEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Map;

import static com.beesechurger.flyingfamiliars.wand_effect.common.WandEffectItemHelper.WAND_EFFECT_CRYSTAL_SPIKE;

public class CrystalSpikeWandEffect extends BaseWandEffect
{
//////////////////
/// Accessors: ///
//////////////////

    // summon single spike at target nearest to player look location, which both sets the victim to ride the spike (impaling), but also damages it, and persists for a short time before retracting

/// Strings:
    @Override
    public String getName()
    {
        return WAND_EFFECT_CRYSTAL_SPIKE;
    }

/// Integers:
    @Override
    public Map<String, Integer> getCost()
    {
        return Map.of();
    }

    @Override
    public int getCooldown()
    {
        return 15;
    }

    @Override
    public int getColor()
    {
        return FFTypes.FAMILIAR_TYPE_EARTH;
    }

///////////////////////////
/// Wand effect action: ///
///////////////////////////

    @Override
    public void use(Level level, Player player)
    {
        
    }
}
