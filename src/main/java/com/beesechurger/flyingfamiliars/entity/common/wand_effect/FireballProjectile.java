package com.beesechurger.flyingfamiliars.entity.common.wand_effect;

import com.beesechurger.flyingfamiliars.item.EntityTagItemHelper;
import com.beesechurger.flyingfamiliars.item.common.soul_items.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.registries.FFEntityTypes;
import com.beesechurger.flyingfamiliars.registries.FFSounds;
import com.beesechurger.flyingfamiliars.util.FFAnimationController;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.BASE_ENTITY_TAGNAME;
import static com.beesechurger.flyingfamiliars.util.FFStringConstants.ENTITY_EMPTY;

public class FireballProjectile extends BaseWandEffectProjectile
{
    public FireballProjectile(EntityType<? extends CaptureProjectile> proj, Level level)
    {
        super(proj, level);
    }

    public FireballProjectile(Level level, LivingEntity entity)
    {
        super(FFEntityTypes.CAPTURE_PROJECTILE.get(), entity, level);
    }

    public FireballProjectile(Level level, double x, double y, double z)
    {
        super(FFEntityTypes.CAPTURE_PROJECTILE.get(), x, y, z, level);
    }

/////////////////////////////////
// GeckoLib animation control: //
/////////////////////////////////

    private <E extends GeoAnimatable> PlayState bodyController(AnimationState<E> event)
    {
        FFAnimationController controller = (FFAnimationController) event.getController();

        if(!isDead())
        {
            controller.setAnimation(RawAnimation.begin()
                    .thenLoop("animation.capture_projectile.idle"));

            controller.setAnimationSpeed(1.0f);
        }
        else
        {
            controller.setAnimation(RawAnimation.begin()
                    .thenLoop("animation.capture_projectile.death"));

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
    public int getDeadTimerMax()
    {
        return 10;
    }

////////////////////////////////////
// Player and entity interaction: //
////////////////////////////////////

    @Override
    protected void onHitEntity(EntityHitResult result)
    {
        if(!isDead())
        {
            if(!level().isClientSide() && player != null)
            {
                if(explode())
                {
                    level().broadcastEntityEvent(this, (byte) 3);
                    level().playSound((Player)null, getX(), getY(), getZ(),
                            FFSounds.SOUL_WAND_THROW.get(), SoundSource.PLAYERS, 0.5f, 2.0f * FFSounds.getPitch());
                }
            }

            setDead(true);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result)
    {
        if(!isDead())
        {
            if(!level().isClientSide() && player != null)
            {
                if(explode())
                {
                    level().broadcastEntityEvent(this, (byte) 3);
                    level().playSound((Player)null, getX(), getY(), getZ(),
                            FFSounds.SOUL_WAND_THROW.get(), SoundSource.PLAYERS, 0.5f, 2.0f * FFSounds.getPitch());
                }
            }

            setDead(true);
        }
    }

    private Boolean explode()
    {
        // do explodey stuff

        return true;
    }

////////////////
// Entity AI: //
////////////////

    @Override
    public void tick()
    {
        super.tick();
        if(level().isClientSide() && !isDead())
        {
            Vec3 vec3d = getDeltaMovement();
            double d0 = getX() + vec3d.x;
            double d1 = getY() + vec3d.y;
            double d2 = getZ() + vec3d.z;
            level().addParticle(ParticleTypes.FLAME, d0 - vec3d.x * 0.25D, d1 - vec3d.y * 0.25D, d2 - vec3d.z * 0.25D, 0, 0, 0);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleEntityEvent(byte id)
    {
        if (id == 3)
        {
            for (int i = 0; i < 360; i++)
            {
                // capture
                if(i % 5 == 0) level().addParticle(ParticleTypes.CLOUD, getX(), getY(), getZ(), 0.1 * Math.cos(i), 0.05 * (Math.cos(i * 9) * Math.sin(i * 9)), 0.1 * Math.sin(i));
            }
        }
    }
}
