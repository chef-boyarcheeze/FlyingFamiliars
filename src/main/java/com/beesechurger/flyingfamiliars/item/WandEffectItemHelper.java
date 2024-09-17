package com.beesechurger.flyingfamiliars.item;

import com.beesechurger.flyingfamiliars.wand_effect.BaseWandEffect;
import com.beesechurger.flyingfamiliars.wand_effect.projectile.CaptureWandEffect;
import com.beesechurger.flyingfamiliars.wand_effect.projectile.FireballWandEffect;
import com.google.common.collect.Maps;
import net.minecraft.Util;

import java.util.Map;

public class WandEffectItemHelper
{
    // Charm wand effects:
    //private static final ObsidianSpikeWandEffect OBSIDIAN_SPIKE = new ObsidianSpikeWandEffect();

    // Projectile wand effects:
    private static final CaptureWandEffect CAPTURE = new CaptureWandEffect();
    private static final FireballWandEffect FIREBALL = new FireballWandEffect();

    // Sentry wand effects:

    private static Map<String, BaseWandEffect> WAND_EFFECT_MAP = (Map) Util.make(Maps.newHashMap(), (map) -> {
        // Charm wand effects:


        // Projectile wand effects:
        map.put("capture_projectile", CAPTURE);
        map.put("fireball_projectile", FIREBALL);

        // Sentry wand effects:

    });

    public static BaseWandEffect getSelectedWandEffect(String selection)
    {
        return WAND_EFFECT_MAP.get(selection);
    }
}
