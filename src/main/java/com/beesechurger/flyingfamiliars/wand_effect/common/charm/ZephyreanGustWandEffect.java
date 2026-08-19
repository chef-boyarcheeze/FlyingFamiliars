package com.beesechurger.flyingfamiliars.wand_effect.common.charm;

import com.beesechurger.flyingfamiliars.item.FFItems;
import com.beesechurger.flyingfamiliars.util.FFTypes;
import com.beesechurger.flyingfamiliars.wand_effect.common.BaseWandEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

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
    public Map<String, Integer> getCost()
    {
        return Map.ofEntries(
                Map.entry(FFItems.GUSTING_SPIRIT_FRAGMENT.get().toString(), 10)
        );
    }

    @Override
    public int getCooldown()
    {
        return 50;
    }

    @Override
    public int getColor()
    {
        return FFTypes.FAMILIAR_TYPE_AIR.color;
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
    public void use(Level level, Player player, int duration)
    {
        Vec3 angle = player.getLookAngle();
        angle = angle.normalize();

        player.push(angle.x, angle.y, angle.z);
    }
}
