package com.beesechurger.flyingfamiliars.wand_effect.common;

import com.beesechurger.flyingfamiliars.wand_effect.common.charm.CloudCallWandEffect;
import com.beesechurger.flyingfamiliars.wand_effect.common.charm.TectonicSunderWandEffect;
import com.beesechurger.flyingfamiliars.wand_effect.common.charm.TimberCleaveWandEffect;
import com.beesechurger.flyingfamiliars.wand_effect.common.charm.ZephyreanGustWandEffect;
import com.beesechurger.flyingfamiliars.wand_effect.common.projectile.CaptureWandEffect;
import com.beesechurger.flyingfamiliars.wand_effect.common.projectile.FireballWandEffect;
import com.beesechurger.flyingfamiliars.wand_effect.common.projectile.FlamethrowerWandEffect;
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
    public static final String WAND_EFFECT_TIMBER_CLEAVE = "timber_cleave_charm";
    public static final String WAND_EFFECT_ZEPHYREAN_GUST = "zephyrean_gust_charm";

/// Projectiles:
    public static final String WAND_EFFECT_CAPTURE = "capture_projectile";
    public static final String WAND_EFFECT_FIREBALL = "fireball_projectile";
    public static final String WAND_EFFECT_FLAMETHROWER = "flamethrower_projectile";

/// Sentries:

//////////////////////////////
/// Wand Effect Instances: ///
//////////////////////////////

/// Charm wand effects:
    private static final CloudCallWandEffect CLOUD_CALL = new CloudCallWandEffect();
    //private static final CrystalSpikeWandEffect CRYSTAL_SPIKE = new CrystalSpikeWandEffect();
    private static final TectonicSunderWandEffect TECTONIC_SUNDER = new TectonicSunderWandEffect();
    private static final TimberCleaveWandEffect TIMBER_CLEAVE = new TimberCleaveWandEffect();
    private static final ZephyreanGustWandEffect ZEPHYREAN_GUST = new ZephyreanGustWandEffect();

/// Projectile wand effects:
    private static final CaptureWandEffect CAPTURE = new CaptureWandEffect();
    private static final FireballWandEffect FIREBALL = new FireballWandEffect();
    private static final FlamethrowerWandEffect FLAMETHROWER = new FlamethrowerWandEffect();

/// Sentry wand effects:

    private static final Map<String, BaseWandEffect> WAND_EFFECT_MAP = (Map) Util.make(Maps.newHashMap(), (map) -> {
    /// Charms:
        map.put(WAND_EFFECT_CLOUD_CALL, CLOUD_CALL);
        map.put(WAND_EFFECT_TECTONIC_SUNDER, TECTONIC_SUNDER);
        map.put(WAND_EFFECT_TIMBER_CLEAVE, TIMBER_CLEAVE);
        map.put(WAND_EFFECT_ZEPHYREAN_GUST, ZEPHYREAN_GUST);

    /// Projectiles:
        map.put(WAND_EFFECT_CAPTURE, CAPTURE);
        map.put(WAND_EFFECT_FIREBALL, FIREBALL);
        map.put(WAND_EFFECT_FLAMETHROWER, FLAMETHROWER);

    /// Sentries:

    });

    public static BaseWandEffect getSelectedWandEffect(String selection)
    {
        return WAND_EFFECT_MAP.get(selection);
    }
}
