package com.beesechurger.flyingfamiliars.wand_effect.common.projectile;

import com.beesechurger.flyingfamiliars.registries.FFItems;
import com.beesechurger.flyingfamiliars.util.FFTypes;
import com.beesechurger.flyingfamiliars.wand_effect.common.BaseWandEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Map;

import static com.beesechurger.flyingfamiliars.wand_effect.common.WandEffectItemHelper.WAND_EFFECT_PRISMATIC_BEAM;

public class PrismaticBeamWandEffect extends BaseWandEffect
{
//////////////////
/// Accessors: ///
//////////////////

/// Strings:

    @Override
    public String getName()
    {
        return WAND_EFFECT_PRISMATIC_BEAM;
    }

/// Integers:

    @Override
    public Map<String, Integer> getCost()
    {
        return Map.ofEntries(
                Map.entry(FFItems.LUMINOUS_SPIRIT_FRAGMENT.get().toString(), 5)
        );
    }

    @Override
    public int getCooldown()
    {
        return 20;
    }

    @Override
    public int getColor()
    {
        return FFTypes.FAMILIAR_TYPE_LIGHT;
    }

///////////////////////////
/// Wand effect action: ///
///////////////////////////

    @Override
    public void use(Level level, Player player, int duration)
    {

    }
}
