package com.beesechurger.flyingfamiliars.entity.custom;

import org.jetbrains.annotations.Nullable;

import com.beesechurger.flyingfamiliars.FFKeys;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;

public abstract class AbstractFamiliarEntity extends TamableAnimal
{
	private static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(AbstractFamiliarEntity.class, EntityDataSerializers.STRING);
	private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(AbstractFamiliarEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(AbstractFamiliarEntity.class, EntityDataSerializers.BOOLEAN);
	
	public static final float FLIGHT_THRESHOLD = 0.5f;
	
	public static float ANGLE_INTERVAL = 2.0f;
    public static float ANGLE_LIMIT = 10;
	public float pitchO = 0, pitch = 0;
	public float rollO = 0, roll = 0;

	protected AbstractFamiliarEntity(EntityType<? extends TamableAnimal> entity, Level level)
	{
		super(entity, level);
		this.setTame(false);
		
	}
	
	@Override
	protected BodyRotationControl createBodyControl()
	{
		return new AbstractFamiliarBodyRotationControl(this);
	}
	
// Additional Save Data:
	
	@Override
	public void readAdditionalSaveData(CompoundTag tag)
	{
		super.readAdditionalSaveData(tag);
		setSitting(tag.getBoolean("isSitting"));
		setVariant(tag.getString("variant"));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag)
	{
		super.addAdditionalSaveData(tag);
		tag.putBoolean("isSitting", this.isSitting());
		tag.putString("variant", getVariant());
	}

	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		entityData.define(VARIANT, "Yellow");
		entityData.define(SITTING, false);
		entityData.define(FLYING, false);
	}
	
// Entity accessors:
	
	public String getVariant()
	{
		return entityData.get(VARIANT);
	}
	
	public boolean hasVariant()
	{
		return false;
	}

	public boolean isSitting()
	{
		return entityData.get(SITTING);
	}

	public boolean isFlying()
	{
		return entityData.get(FLYING);
	}
	
	public boolean isMoving()
	{
		double d0 = this.getX() - this.xo;
		double d1 = this.getZ() - this.zo;
		return d0 * d0 + d1 * d1 > 2.5000003E-7F;
	}
	
	public boolean shouldFly()
	{
		var standingOn = blockPosition().mutable().move(0, -1, 0);
		//return !this.isOnGround() && !level.getBlockState(standingOn).getMaterial().isSolid();
		return level.getBlockState(standingOn).isAir();
	}
	
	public boolean isTamedFor(Player player)
	{
		return isTame() && isOwnedBy(player);
	}
	
	@Override
	public boolean canBeControlledByRider()
	{
		return this.getControllingPassenger() instanceof LivingEntity;
	}
	
// Entity mutators:
	
	protected void setVariant(String variant)
	{
		entityData.set(VARIANT, variant);
	}

	protected void setSitting(boolean sitting)
	{
		entityData.set(SITTING, sitting);
		setOrderedToSit(sitting);
	}

	protected void setFlying(boolean flying)
	{
		entityData.set(FLYING, flying);
	}
	
// Player and entity interaction:
	
	@Override
	public Team getTeam()
	{
		return super.getTeam();
	}
	
	@Nullable
	@Override
	public Entity getControllingPassenger()
	{
		return this.getFirstPassenger();
	}
	
	public void setRidingPlayer(Player player)
	{
		player.setYRot(getYRot());
		player.setXRot(getXRot());
		player.startRiding(this);
	}
	
	@Override
	public void onPassengerTurned(Entity rider)
	{
		rider.setYBodyRot(this.getYRot());
	}
	
	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob griffonfly)
	{
		return null;
	}
	
// Misc:
	
	@Override
	public boolean causeFallDamage(float p_148750_, float p_148751_, DamageSource p_148752_)
	{
		return false;
	}
	
	@Override
	protected void checkFallDamage(double p_27754_, boolean p_27755_, BlockState p_27756_, BlockPos p_27757_)
	{
	}
	
	@Override
	public double getPassengersRidingOffset()
	{
		return (this.getDimensions(this.getPose()).height * getOffsetScale());
	}
	
	private double getOffsetScale()
	{
		if(this instanceof GriffonflyEntity) return 0.6;
		else return 0.6;
	}
	
	public Vec3 getHoverVector(Vec3 vec3, LivingEntity driver)
	{
		double xMove = vec3.x + driver.xxa;
		double yMove = vec3.y;
		double zMove = vec3.z + driver.zza;
		
		if(FFKeys.ascend.isDown()) yMove += 0.6;
		if(FFKeys.descend.isDown()) yMove -= 0.6;
		
		return new Vec3(xMove, yMove, zMove);
	}
	
	public double getPitch(double partialTicks)
	{
		if(pitchO == pitch) return pitch;
		return partialTicks == 1.0 ? pitch : Mth.lerp(partialTicks, pitchO, pitch);
	}
	
	public double getRoll(double partialTicks)
	{
		if(rollO == roll) return roll;
		return partialTicks == 1.0 ? roll : Mth.lerp(partialTicks, rollO, roll);
	}
	
////////////////////////////////
// Entity AI control classes: //
////////////////////////////////
	
	static class AbstractFamiliarBodyRotationControl extends BodyRotationControl
	{
        private final AbstractFamiliarEntity familiar;

        public AbstractFamiliarBodyRotationControl(AbstractFamiliarEntity familiar)
        {
            super(familiar);
            this.familiar = familiar;
        }

        @Override
        public void clientTick()
        {        	
        	LivingEntity driver = (LivingEntity) familiar.getFirstPassenger();
    		
            int forwardMove = Math.round(driver != null ? driver.zza : familiar.zza);
            int sideMove = Math.round(driver != null ? driver.xxa : familiar.xxa);
            
            if(familiar.isFlying())
            {
            	switch(forwardMove)
            	{
            		case 0:
            			centerPitch(familiar);
            			break;
            		case 1:
            			familiar.pitchO = familiar.pitch;
            			if(familiar.pitch < ANGLE_LIMIT) familiar.pitch += ANGLE_INTERVAL;
            			break;
            		case -1:
            			familiar.pitchO = familiar.pitch;
            			if(familiar.pitch > -ANGLE_LIMIT) familiar.pitch -= ANGLE_INTERVAL;
            			break;
            	}
            }
            else
            {
            	centerPitch(familiar);
            }
        	
        	if(familiar.isFlying())
        	{
        		switch(sideMove)
            	{
            		case 0:
            			centerRoll(familiar);
            			break;
            		case 1:
            			familiar.rollO = familiar.roll;
            			if(familiar.roll < ANGLE_LIMIT) familiar.roll += ANGLE_INTERVAL;
            			break;
            		case -1:
            			familiar.rollO = familiar.roll;
            			if(familiar.roll > -ANGLE_LIMIT) familiar.roll -= ANGLE_INTERVAL;
            			break;
            	}
        	}
        	else
        	{
        		centerRoll(familiar);
        	}
        }
           	
    	private void centerPitch(AbstractFamiliarEntity familiar)
    	{
    		familiar.pitchO = familiar.pitch;
    		if(familiar.pitch < 0) familiar.pitch += ANGLE_INTERVAL;
    		if(familiar.pitch > 0) familiar.pitch -= ANGLE_INTERVAL;
    	}
    	
    	private void centerRoll(AbstractFamiliarEntity familiar)
    	{
    		familiar.rollO = familiar.roll;
    		if(familiar.roll < 0) familiar.roll += ANGLE_INTERVAL;
    		if(familiar.roll > 0) familiar.roll -= ANGLE_INTERVAL;
    	}
    }
}
