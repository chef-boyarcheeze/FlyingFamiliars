package com.beesechurger.flyingfamiliars.registries;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.effect.DousedEffect;
import com.beesechurger.flyingfamiliars.effect.DryadsCurseEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FFEffects
{
    public static final DeferredRegister<MobEffect> MOB_EFFECT_REG = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, FlyingFamiliars.MOD_ID);

    public static final RegistryObject<MobEffect> DOUSED = MOB_EFFECT_REG.register("doused", () -> new DousedEffect(MobEffectCategory.BENEFICIAL, 226472));
    public static final RegistryObject<MobEffect> DRYADS_CURSE = MOB_EFFECT_REG.register("dryads_curse", () -> new DryadsCurseEffect(MobEffectCategory.HARMFUL, 226472));
}
