package com.beesechurger.flyingfamiliars.mixin;

import com.beesechurger.flyingfamiliars.effect.FFEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityDousedInWaterMixin<T extends Entity>
{
    @Inject(method = "Lnet/minecraft/world/entity/Entity;isInWaterOrRain()Z", at = @At("HEAD"), cancellable = true)
    public void dousedEffectIsInWater(CallbackInfoReturnable<Boolean> cir)
    {
        if((Object) this instanceof LivingEntity entity)
        {
            MobEffectInstance doused = entity.getEffect(FFEffects.DOUSED.get());
            if(doused != null)
            {
                entity.clearFire();
                cir.setReturnValue(true);
            }
        }
    }
}
