package com.beesechurger.flyingfamiliars.wand_effect.common.charm;

import com.beesechurger.flyingfamiliars.util.FFConstants;
import com.beesechurger.flyingfamiliars.wand_effect.common.BaseWandEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

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

/// Booleans:

    @Override
    public boolean canBePartiallyDrawn()
    {
        return true;
    }

/// Integers:

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
        return 50;
    }

    @Override
    public int getColor()
    {
        return FFConstants.FAMILIAR_TYPE_AIR;
    }

/// Misc:

    @Override
    public UseAnim getUseAnimation()
    {
        return UseAnim.SPEAR;
    }

///////////////////////////
/// Wand effect action: ///
///////////////////////////

    @Override
    public void use(Level level, Player player)
    {
        Vec3 angle = player.getLookAngle();
        angle = angle.normalize();

        player.push(angle.x, angle.y, angle.z);
    }
}
