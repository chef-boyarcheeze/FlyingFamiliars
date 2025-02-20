package com.beesechurger.flyingfamiliars.entity.common.wand_effect.charm;

import com.beesechurger.flyingfamiliars.entity.client.FFAnimationController;
import com.beesechurger.flyingfamiliars.entity.client.wand_effect.charm.crystal_spike_charm.CrystalSpikeCharmRenderer;
import com.beesechurger.flyingfamiliars.entity.common.wand_effect.projectile.CaptureProjectile;
import com.beesechurger.flyingfamiliars.registries.FFEntityTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class CrystalSpikeCharm extends BaseWandEffectCharm
{
    public CrystalSpikeCharm(EntityType<? extends CrystalSpikeCharm> charm, Level level)
    {
        super(charm, level);
    }

    public CrystalSpikeCharm(Level level, LivingEntity entity, Vec3 vec)
    {
        super(FFEntityTypes.CRYSTAL_SPIKE_CHARM.get(), entity, vec, level);
    }

/////////////////////////////////
// GeckoLib animation control: //
/////////////////////////////////

    private <E extends GeoAnimatable> PlayState bodyController(AnimationState<E> event)
    {
        FFAnimationController controller = (FFAnimationController) event.getController();

        if(isSpawning())
        {
            controller.setAnimation(RawAnimation.begin()
                    .thenPlay("animation.crystal_spike_charm.spawn"));
        }
        else if(!isDead())
        {
            controller.setAnimation(RawAnimation.begin()
                    .thenLoop("animation.crystal_spike_charm.idle"));

            controller.setAnimationSpeed(1.0f);
        }
        else
        {
            controller.setAnimation(RawAnimation.begin()
                    .thenLoop("animation.crystal_spike_charm.death"));

            controller.setAnimationSpeed(1.0f);
        }

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data)
    {
        FFAnimationController bodyController = new FFAnimationController(this, "bodyController", 2, 0, this::bodyController);

        data.add(bodyController);
    }

///////////////////////
// Entity accessors: //
///////////////////////

// Integers:
    @Override
    public int getSpawnTimerMax()
    {
        return 10;
    }

    @Override
    public int getDeadTimerMax()
    {
        return 10;
    }
}
