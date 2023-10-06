package com.beesechurger.flyingfamiliars.effect;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FFEffects
{
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, FlyingFamiliars.MOD_ID);

    public static final RegistryObject<MobEffect> DOUSED = EFFECTS.register("doused", () -> new DousedEffect(MobEffectCategory.BENEFICIAL, 226472));

    public static void register(IEventBus eventBus)
    {
        EFFECTS.register(eventBus);
    }
}
