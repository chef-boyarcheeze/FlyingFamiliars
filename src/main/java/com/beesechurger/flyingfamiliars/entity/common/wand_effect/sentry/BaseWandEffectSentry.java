package com.beesechurger.flyingfamiliars.entity.common.wand_effect.sentry;

import com.beesechurger.flyingfamiliars.entity.client.FFAnimationController;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class BaseWandEffectSentry extends Entity implements GeoEntity
{
    private static final EntityDataAccessor<Boolean> SPAWNING = SynchedEntityData.defineId(BaseWandEffectSentry.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DEAD = SynchedEntityData.defineId(BaseWandEffectSentry.class, EntityDataSerializers.BOOLEAN);

    @Nullable
    private UUID ownerUUID;
    @Nullable
    private Entity cachedOwner;
    private int deadTimer = 0;

    protected NonNullList<FFAnimationController> animationControllers = NonNullList.create();
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public BaseWandEffectSentry(EntityType<? extends com.beesechurger.flyingfamiliars.entity.common.wand_effect.charm.BaseWandEffectCharm> charm, Level level)
    {
        super(charm, level);
    }

    public BaseWandEffectSentry(EntityType<? extends com.beesechurger.flyingfamiliars.entity.common.wand_effect.charm.BaseWandEffectCharm> charm, LivingEntity entity, Vec3 pos, Level level)
    {
        super(charm, level);

        setOwner(entity);
        this.setPos(pos.x(), pos.y() + 1, pos.z());
    }

///////////////////////////
// Additional Save Data: //
///////////////////////////

    @Override
    public void readAdditionalSaveData(CompoundTag tag)
    {
        readAdditionalSaveData(tag);
        setSpawning(tag.getBoolean("isSpawning"));
        setDead(tag.getBoolean("isDead"));

        if (tag.hasUUID("Owner"))
        {
            this.ownerUUID = tag.getUUID("Owner");
            this.cachedOwner = null;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag)
    {
        addAdditionalSaveData(tag);
        tag.putBoolean("isDead", isDead());

        if (this.ownerUUID != null)
        {
            tag.putUUID("Owner", this.ownerUUID);
        }
    }

    @Override
    protected void defineSynchedData()
    {
        entityData.define(SPAWNING, true);
        entityData.define(DEAD, false);
    }

////////////////
// Accessors: //
////////////////

    // Booleans:
    public boolean isSpawning()
    {
        return entityData.get(SPAWNING);
    }

    public boolean isDead()
    {
        return entityData.get(DEAD);
    }

    protected boolean ownedBy(Entity entity)
    {
        return entity.getUUID().equals(this.ownerUUID);
    }

    // Integers:
    public int getDeadTimerMax()
    {
        return 20;
    }

    // Misc:
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache()
    {
        return cache;
    }

    @Nullable
    public Entity getOwner()
    {
        if (this.cachedOwner != null && !this.cachedOwner.isRemoved())
        {
            return this.cachedOwner;
        }
        else if (this.ownerUUID != null && this.level() instanceof ServerLevel)
        {
            this.cachedOwner = ((ServerLevel) this.level()).getEntity(this.ownerUUID);
            return this.cachedOwner;
        }
        else
        {
            return null;
        }
    }

///////////////
// Mutators: //
///////////////

    // Booleans:
    public void setSpawning(boolean spawning)
    {
        entityData.set(SPAWNING, spawning);
    }

    public void setDead(boolean dead)
    {
        entityData.set(DEAD, dead);
    }

    // Misc:
    public void setOwner(@Nullable Entity entity)
    {
        if (entity != null)
        {
            this.ownerUUID = entity.getUUID();
            this.cachedOwner = entity;
        }
    }
}
