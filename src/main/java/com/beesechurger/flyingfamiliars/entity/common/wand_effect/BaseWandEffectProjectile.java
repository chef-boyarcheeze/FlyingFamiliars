package com.beesechurger.flyingfamiliars.entity.common.wand_effect;

import com.beesechurger.flyingfamiliars.item.common.wand_effect.IWandEffect;
import com.beesechurger.flyingfamiliars.registries.FFSounds;
import com.beesechurger.flyingfamiliars.util.FFAnimationController;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class BaseWandEffectProjectile extends Projectile implements IWandEffect, GeoEntity
{
    private static final EntityDataAccessor<Boolean> DEAD = SynchedEntityData.defineId(BaseWandEffectProjectile.class, EntityDataSerializers.BOOLEAN);

    protected Player player = null;
    private int deadTimer = 0;

    protected NonNullList<FFAnimationController> animationControllers = NonNullList.create();
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public BaseWandEffectProjectile(EntityType<? extends BaseWandEffectProjectile> proj, Level level)
    {
        super(proj, level);
    }

    public BaseWandEffectProjectile(EntityType<? extends BaseWandEffectProjectile> proj, LivingEntity entity, Level level)
    {
        super(proj, level);

        if(entity instanceof Player)
            player = (Player) entity;

        setOwner(entity);
        this.setPos(entity.getX(), entity.getY() + 1, entity.getZ());
    }

    public BaseWandEffectProjectile(EntityType<? extends BaseWandEffectProjectile> proj, double x, double y, double z, Level level)
    {
        super(proj, level);
        this.setPos(x, y, z);
    }

///////////////////////////
// Additional Save Data: //
///////////////////////////

    @Override
    public void readAdditionalSaveData(CompoundTag tag)
    {
        super.readAdditionalSaveData(tag);
        setDead(tag.getBoolean("isDead"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag)
    {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("isDead", isDead());
    }

    @Override
    protected void defineSynchedData()
    {
        entityData.define(DEAD, false);
    }

///////////////////////
// Entity accessors: //
///////////////////////

// Booleans:

    public boolean isDead()
    {
        return entityData.get(DEAD);
    }

// Integers:

    public int getDeadTimerMax()
    {
        return 20;
    }

// Floats:

    protected abstract float getGravity();

// Doubles:

    public double getPitch(double partialTicks)
    {
        if(xRotO == getXRot())
            return getXRot();

        return partialTicks == 1.0 ? getXRot() : Mth.lerp(partialTicks, xRotO, getXRot());
    }

    public double getYaw(double partialTicks)
    {
        if(yRotO == getYRot())
            return getYRot();

        return partialTicks == 1.0 ? getYRot() : Mth.lerp(partialTicks, yRotO, getYRot());
    }

// Misc:

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache()
    {
        return cache;
    }

//////////////////////
// Entity mutators: //
//////////////////////

// Booleans:

    public void setDead(boolean dead)
    {
        entityData.set(DEAD, dead);
    }

////////////////
// Entity AI: //
////////////////

    @Override
    public void tick()
    {
        super.tick();

        //////////////////////////

        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        boolean flag = false;
        if (hitresult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = ((BlockHitResult)hitresult).getBlockPos();
            BlockState blockstate = this.level().getBlockState(blockpos);
            if (blockstate.is(Blocks.NETHER_PORTAL)) {
                this.handleInsidePortal(blockpos);
                flag = true;
            } else if (blockstate.is(Blocks.END_GATEWAY)) {
                BlockEntity blockentity = this.level().getBlockEntity(blockpos);
                if (blockentity instanceof TheEndGatewayBlockEntity && TheEndGatewayBlockEntity.canEntityTeleport(this)) {
                    TheEndGatewayBlockEntity.teleportEntity(this.level(), blockpos, blockstate, this, (TheEndGatewayBlockEntity)blockentity);
                }

                flag = true;
            }
        }

        if (hitresult.getType() != HitResult.Type.MISS && !flag && !ForgeEventFactory.onProjectileImpact(this, hitresult)) {
            this.onHit(hitresult);
        }

        this.checkInsideBlocks();
        Vec3 vec3 = this.getDeltaMovement();
        double d2 = this.getX() + vec3.x;
        double d0 = this.getY() + vec3.y;
        double d1 = this.getZ() + vec3.z;

        if(!isDead())
            this.updateRotation();

        float f;
        if (this.isInWater()) {
            for(int i = 0; i < 4; ++i) {
                float f1 = 0.25F;
                this.level().addParticle(ParticleTypes.BUBBLE, d2 - vec3.x * 0.25, d0 - vec3.y * 0.25, d1 - vec3.z * 0.25, vec3.x, vec3.y, vec3.z);
            }

            f = 0.8F;
        } else {
            f = 0.99F;
        }

        this.setDeltaMovement(vec3.scale((double)f));
        if (!this.isNoGravity()) {
            Vec3 vec31 = this.getDeltaMovement();
            this.setDeltaMovement(vec31.x, vec31.y - (double)this.getGravity(), vec31.z);
        }

        this.setPos(d2, d0, d1);

        /////////////////////

        if(isDead())
        {
            setDeltaMovement(Vec3.ZERO);
            setNoGravity(true);

            deadTimer++;

            if(deadTimer >= getDeadTimerMax())
                remove(RemovalReason.KILLED);
        }

        for(FFAnimationController controller : animationControllers)
            controller.updateProgress();
    }

    protected void updateTimers()
    {
        if(isDead())
            ++deadTimer;
    }

/////////////
// Sounds: //
/////////////

    protected void playLocalSound(SoundEvent event)
    {
        level().playSound((Player)null, getX(), getY(), getZ(),
                event, SoundSource.PLAYERS, 0.5f, 2.0f * FFSounds.getPitch());
    }
}
