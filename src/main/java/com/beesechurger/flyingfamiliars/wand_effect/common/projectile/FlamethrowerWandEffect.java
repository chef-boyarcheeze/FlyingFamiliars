package com.beesechurger.flyingfamiliars.wand_effect.common.projectile;

import com.beesechurger.flyingfamiliars.entity.common.wand_effect.projectile.FlamethrowerProjectile;
import com.beesechurger.flyingfamiliars.util.FFConstants;
import com.beesechurger.flyingfamiliars.wand_effect.common.BaseWandEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static com.beesechurger.flyingfamiliars.wand_effect.common.WandEffectItemHelper.WAND_EFFECT_FLAMETHROWER;

public class FlamethrowerWandEffect extends BaseWandEffect
{
//////////////////
/// Accessors: ///
//////////////////

/// Strings:

    @Override
    public String getName()
    {
        return WAND_EFFECT_FLAMETHROWER;
    }

/// Booleans:

    @Override
    public boolean canBeContinuouslyDrawn()
    {
        return true;
    }

/// Integers:

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
        return 3;
    }

    @Override
    public int getColor()
    {
        return FFConstants.FAMILIAR_TYPE_FIRE;
    }

///////////////////////////
/// Wand effect action: ///
///////////////////////////

    @Override
    public void use(Level level, Player player)
    {
        FlamethrowerProjectile flamethower = new FlamethrowerProjectile(level, player);
        flamethower.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 1.2f, 1.0f);
        level.addFreshEntity(flamethower);
    }
}
