package com.beesechurger.flyingfamiliars.entity.common.projectile;

import com.beesechurger.flyingfamiliars.entity.util.FFAnimationController;
import com.beesechurger.flyingfamiliars.registries.FFEntityTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class BaseWandEffectEntity extends ThrowableItemProjectile implements GeoEntity
{
    private static final EntityDataAccessor<Boolean> DEAD = SynchedEntityData.defineId(BaseWandEffectEntity.class, EntityDataSerializers.BOOLEAN);

    protected Player player = null;
    private int deadTimer = 0;

    protected NonNullList<FFAnimationController> animationControllers = NonNullList.create();
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public BaseWandEffectEntity(EntityType<? extends BaseWandEffectEntity> proj, Level level)
    {
        super(proj, level);
    }

    public BaseWandEffectEntity(EntityType<? extends BaseWandEffectEntity> proj, LivingEntity entity, Level level)
    {
        super(proj, entity, level);

        if(entity instanceof Player)
            player = (Player) entity;
    }

    public BaseWandEffectEntity(EntityType<? extends BaseWandEffectEntity> proj, double x, double y, double z, Level level)
    {
        super(proj, x, y, z, level);
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
        super.defineSynchedData();
        entityData.define(DEAD, false);
    }

///////////////////////
// Entity accessors: //
///////////////////////

// Integers:

    public int getDeadTimerMax()
    {
        return 20;
    }

// Booleans:

    public boolean isDead()
    {
        return entityData.get(DEAD);
    }

// Misc:

    @Override
    protected Item getDefaultItem()
    {
        return Items.AIR;
    }

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
}
