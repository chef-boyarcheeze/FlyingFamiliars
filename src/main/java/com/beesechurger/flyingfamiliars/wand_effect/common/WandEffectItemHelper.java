package com.beesechurger.flyingfamiliars.wand_effect.common;

import com.beesechurger.flyingfamiliars.wand_effect.common.charm.CloudCallWandEffect;
import com.beesechurger.flyingfamiliars.wand_effect.common.charm.TectonicSunderWandEffect;
import com.beesechurger.flyingfamiliars.wand_effect.common.projectile.CaptureWandEffect;
import com.beesechurger.flyingfamiliars.wand_effect.common.projectile.FireballWandEffect;
import com.google.common.collect.Maps;
import net.minecraft.Util;

import java.util.Map;

public class WandEffectItemHelper
{
/////////////////////////////////////
/// Wand Effect String Constants: ///
/////////////////////////////////////

/// Charms:
    public static final String WAND_EFFECT_CLOUD_CALL = "cloud_call_charm";
    public static final String WAND_EFFECT_CRYSTAL_SPIKE = "crystal_spike_charm";
    public static final String WAND_EFFECT_TECTONIC_SUNDER = "tectonic_sunder_charm";
    public static final String WAND_EFFECT_ZEPHYREAN_GUST = "zephyrean_gust_charm";

/// Projectiles:
    public static final String WAND_EFFECT_CAPTURE = "capture_projectile";
    public static final String WAND_EFFECT_FIREBALL = "fireball_projectile";

/// Sentries:

//////////////////////////////
/// Wand Effect Instances: ///
//////////////////////////////

/// Charm wand effects:
    private static final CloudCallWandEffect CLOUD_CALL = new CloudCallWandEffect();
    //private static final CrystalSpikeWandEffect CRYSTAL_SPIKE = new CrystalSpikeWandEffect();
    private static final TectonicSunderWandEffect TECTONIC_SUNDER = new TectonicSunderWandEffect();

/// Projectile wand effects:
    private static final CaptureWandEffect CAPTURE = new CaptureWandEffect();
    private static final FireballWandEffect FIREBALL = new FireballWandEffect();

/// Sentry wand effects:

    private static final Map<String, BaseWandEffect> WAND_EFFECT_MAP = (Map) Util.make(Maps.newHashMap(), (map) -> {
    /// Charms:
        map.put(WAND_EFFECT_CLOUD_CALL, CLOUD_CALL);
        map.put(WAND_EFFECT_TECTONIC_SUNDER, TECTONIC_SUNDER);

    /// Projectiles:
        map.put(WAND_EFFECT_CAPTURE, CAPTURE);
        map.put(WAND_EFFECT_FIREBALL, FIREBALL);

    /// Sentries:

    });

    public static BaseWandEffect getSelectedWandEffect(String selection)
    {
        return WAND_EFFECT_MAP.get(selection);
    }
}
