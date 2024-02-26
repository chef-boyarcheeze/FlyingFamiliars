package com.beesechurger.flyingfamiliars.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class DousedEffect extends MobEffect
{
    public DousedEffect(MobEffectCategory category, int color)
    {
        super(category, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier)
    {
        super.applyEffectTick(entity, amplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier)
    {
        return true;
    }
}
