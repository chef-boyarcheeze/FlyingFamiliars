package com.beesechurger.flyingfamiliars.wand_effect.common;

import com.beesechurger.flyingfamiliars.wand_effect.common.charm.TectonicCrushWandEffect;
import com.beesechurger.flyingfamiliars.wand_effect.common.projectile.CaptureWandEffect;
import com.beesechurger.flyingfamiliars.wand_effect.common.projectile.FireballWandEffect;
import com.google.common.collect.Maps;
import net.minecraft.Util;

import java.util.Map;

public class WandEffectItemHelper
{
    // Charm wand effects:
    //private static final CrystalSpikeWandEffect CRYSTAL_SPIKE = new CrystalSpikeWandEffect();
    private static final TectonicCrushWandEffect TECTONIC_CRUSH = new TectonicCrushWandEffect();

    // Projectile wand effects:
    private static final CaptureWandEffect CAPTURE = new CaptureWandEffect();
    private static final FireballWandEffect FIREBALL = new FireballWandEffect();

    // Sentry wand effects:

    private static final Map<String, BaseWandEffect> WAND_EFFECT_MAP = (Map) Util.make(Maps.newHashMap(), (map) -> {
        // Charm wand effects:
        map.put("tectonic_crush", TECTONIC_CRUSH);

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
