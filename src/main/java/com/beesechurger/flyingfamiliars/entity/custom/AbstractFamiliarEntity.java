package com.beesechurger.flyingfamiliars.entity.custom;

import org.jetbrains.annotations.Nullable;

import com.beesechurger.flyingfamiliars.FFKeys;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;

public abstract class AbstractFamiliarEntity extends TamableAnimal
{
	private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(AbstractFamiliarEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(AbstractFamiliarEntity.class, EntityDataSerializers.BOOLEAN);
	
	public static final float FLIGHT_THRESHOLD = 0.5f;

	protected AbstractFamiliarEntity(EntityType<? extends TamableAnimal> entity, Level level)
	{
		super(entity, level);
		this.setTame(false);
	}
	
// Additional Save Data:
	
	@Override
	public void readAdditionalSaveData(CompoundTag tag)
	{
		super.readAdditionalSaveData(tag);
		setSitting(tag.getBoolean("isSitting"));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag)
	{
		super.addAdditionalSaveData(tag);
		tag.putBoolean("isSitting", this.isSitting());
	}

	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(SITTING, false);
		this.entityData.define(FLYING, false);
	}
	
// Entity booleans:

	public boolean isSitting()
	{
		return this.entityData.get(SITTING);
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
		var pointer = blockPosition().mutable().move(0, -1, 0);
		var min = level.dimensionType().minY();
		var i = 0;

		while (i <= FLIGHT_THRESHOLD && pointer.getY() > min && !level.getBlockState(pointer).getMaterial().isSolid())
			pointer.setY(getBlockY() - ++i);

		return i >= FLIGHT_THRESHOLD;
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
	
// Control methods:

	public void setSitting(boolean sitting)
	{
		this.entityData.set(SITTING, sitting);
		this.setOrderedToSit(sitting);
	}

	public void setFlying(boolean flying)
	{
		this.entityData.set(FLYING, flying);
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
}
