package com.beesechurger.flyingfamiliars.entity.common;

import com.beesechurger.flyingfamiliars.entity.ai.FamiliarBodyRotationControl;
import com.beesechurger.flyingfamiliars.entity.ai.FamiliarFlyingPathNavigation;
import com.beesechurger.flyingfamiliars.entity.ai.FamiliarMoveControl;
import com.beesechurger.flyingfamiliars.keys.FFKeys;
import com.beesechurger.flyingfamiliars.util.FFEnumValues;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.Nullable;

public abstract class BaseFamiliarEntity extends TamableAnimal
{
	private static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(BaseFamiliarEntity.class, EntityDataSerializers.STRING);
	private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(BaseFamiliarEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(BaseFamiliarEntity.class, EntityDataSerializers.BOOLEAN);

	public static final float BEGIN_FOLLOW_DISTANCE = 16;
	public static final float END_FOLLOW_DISTANCE = 8;

	private FFEnumValues.FamiliarStatus goalStatus = FFEnumValues.FamiliarStatus.WANDERING;
	
	public float pitchO = 0, pitch = 0;
	public float rollO = 0, roll = 0;

	protected int actionTimer = 0;
	protected int landTimer = 0;
	protected int wanderJumpTimer = 0;

	protected int resetActionTimerAmount = 20;
	protected int resetLandTimerAmount = 100;
	protected int resetWanderJumpTimerAmount = 200;

	protected BaseFamiliarEntity(EntityType<? extends TamableAnimal> entity, Level level)
	{
		super(entity, level);
		this.setTame(false);
		moveControl = new FamiliarMoveControl.FlightControl(this);
		lookControl = new LookControl(this);
	}

	@Override
	protected BodyRotationControl createBodyControl()
	{
		return new FamiliarBodyRotationControl(this, getMoveControlType(), 0, 0);
	}

///////////////////////////
// Additional Save Data: //
///////////////////////////
	
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
		tag.putBoolean("isSitting", isSitting());
		tag.putString("variant", getVariant());
	}

	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		entityData.define(VARIANT, "");
		entityData.define(SITTING, false);
		entityData.define(FLYING, false);
	}

///////////////////////
// Entity accessors: //
///////////////////////

// Strings:

	public String getVariant()
	{
		return entityData.get(VARIANT);
	}

	abstract FFEnumValues.FamiliarMoveTypes getMoveControlType();

// Enums:

	public FFEnumValues.FamiliarStatus getGoalStatus()
	{
		return goalStatus;
	}

// Booleans:

	public boolean hasVariant()
	{
		return getVariant() != "";
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
		double d0 = getX() - xo;
		double d1 = getY() - yo;
		double d2 = getZ() - zo;
		return d0 * d0 + d1 * d1 + d2 * d2 > 2.5000003E-7f;
	}

	public boolean shouldFly()
	{
		var standingOn = blockPosition().mutable().move(0, -1, 0);

		if(isFlying())
		{
			if(isOnGround())
				return false;
			else 
				return true;
		}
		else
		{
			if(!isOnGround() && (getControllingPassenger() instanceof Player && FFKeys.FAMILIAR_ASCEND.isDown() || level.getBlockState(standingOn).isAir()))
				return true;
			else
				return false;
		}//*/
	}
	
	public boolean isTamedFor(Player player)
	{
		return isTame() && isOwnedBy(player);
	}

	public boolean isOwnerDoingFamiliarAction()
	{
		return getControllingPassenger() == getOwner() && FFKeys.FAMILIAR_ACTION.isDown();
	}

	public boolean canOwnerRide()
	{
		return true;
	}
	
	@Override
	public boolean canBeControlledByRider()
	{
		return getControllingPassenger() instanceof Player;
	}

	public boolean notCarryingPassengers()
	{
		return getPassengers().size() == 0;
	}

	@Override
	public boolean canBeLeashed(Player player)
	{
		return false;
	}

	abstract boolean isTameItem(ItemStack stack);

	abstract boolean isFoodItem(ItemStack stack);

	abstract boolean canWalk();

	@Override
	public boolean shouldRiderSit()
	{
		return getControllingPassenger() != null;
	}

	public boolean isOwnerNear(double radius)
	{
		for(Entity entity : this.level.getEntities(this, this.getBoundingBox().inflate(radius)))
		{
			if(entity == getOwner())
				return true;
		}

		return false;
	}

// Integers:

	public int getActionTimer()
	{
		return actionTimer;
	}

	public int getLandTimer()
	{
		return landTimer;
	}

	public int getWanderJumpTimer()
	{
		return wanderJumpTimer;
	}

	@Override
	protected int getExperienceReward(Player player)
	{
		return 0;
	}

// Floats:

	@Override
	protected float getJumpPower()
	{
		return this.getBbHeight() * this.getBlockJumpFactor() / 2;
	}

	protected float getDrivingSpeedMod()
	{
		return 0;
	}

// Doubles:

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

	public double getDistanceFromGround()
	{
		BlockPos.MutableBlockPos pos = blockPosition().mutable();

		while (pos.getY() > 0 && !level.getBlockState(pos.move(Direction.DOWN)).getMaterial().isSolid());
		return getY() - pos.getY();
	}

	@Override
	public double getPassengersRidingOffset()
	{
		if(this instanceof GriffonflyEntity)
			return (getDimensions(getPose()).height * 0.8);
		else if(this instanceof CloudRayEntity)
			return (getDimensions(getPose()).height * 0.6);
		else
			return getDimensions(getPose()).height;
	}

//////////////////////
// Entity mutators: //
//////////////////////

// Strings:

	protected void setVariant(String variant)
	{
		entityData.set(VARIANT, variant);
	}

// Enums:

	public void setGoalStatus(FFEnumValues.FamiliarStatus goalStatus)
	{
		this.goalStatus = goalStatus;
	}

// Booleans:

	protected void setSitting(boolean sitting)
	{
		entityData.set(SITTING, sitting);
		setOrderedToSit(sitting);

		if(sitting)
			goalStatus = FFEnumValues.FamiliarStatus.SITTING;
	}

	protected void setFlying(boolean flying)
	{
		entityData.set(FLYING, flying);
	}

////////////////////////////////////
// Player and entity interaction: //
////////////////////////////////////

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);

		InteractionResult stackResult = stack.interactLivingEntity(player, this, hand);
		if (stackResult.consumesAction())
			return stackResult;

		final InteractionResult SUCCESS = InteractionResult.sidedSuccess(this.level.isClientSide);

		// Tame
		if (!isTame())
		{
			if (isTameItem(stack))
			{
				if (!player.getAbilities().instabuild)
				{
					stack.shrink(1);
				}

				if (this.random.nextInt(10) == 0
						&& !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player))
				{
					this.tame(player);
					this.navigation.stop();
					this.setTarget(null);
					this.level.broadcastEntityEvent(this, (byte) 7);
				}
				else
				{
					this.level.broadcastEntityEvent(this, (byte) 6);
				}

				return InteractionResult.SUCCESS;
			}
		}

		// Heal
		if (getHealth() < getAttribute(Attributes.MAX_HEALTH).getValue() && isFoodItem(stack))
		{
			heal(5);
			playSound(getEatingSound(stack), 0.7f, 1);
			stack.shrink(1);

			return SUCCESS;
		}

		// Sit
		if (isTamedFor(player) && player.isShiftKeyDown())
		{
			if (!this.level.isClientSide)
			{
				navigation.stop();
				setSitting(!isSitting());

				if (isOrderedToSit())
				{
					setTarget(null);
					player.displayClientMessage(new TranslatableComponent("message.flyingfamiliars.sitting"), true);
				}
				else
				{
					player.displayClientMessage(new TranslatableComponent("message.flyingfamiliars.standing"), true);
				}
			}
			return SUCCESS;
		}

		// Start riding
		if (canOwnerRide() && isTamedFor(player))
		{
			if (!this.level.isClientSide)
			{
				setRidingPlayer(player);
				resetActionTimer();
				navigation.stop();
				setTarget(null);
			}

			return SUCCESS;
		}

		return super.mobInteract(player, hand);
	}
	
	@Override
	public Team getTeam()
	{
		return super.getTeam();
	}
	
	@Nullable
	@Override
	public Entity getControllingPassenger()
	{
		for (Entity passenger : getPassengers())
		{
			if (passenger instanceof Player player && getTarget() != passenger)
			{
				if (isTame() && getOwnerUUID() != null && getOwnerUUID().equals(player.getUUID()))
				{
					return player;
				}
			}
		}

		return null;
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
		rider.setYBodyRot(getYRot());
	}

	@Override
	public Vec3 getDismountLocationForPassenger(LivingEntity entity)
	{
		if(isFlying() && !(entity instanceof Player))
			return new Vec3(getX(), getBoundingBox().minY - 1, getZ());
		else
			return super.getDismountLocationForPassenger(entity);
	}
	
	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob familiar)
	{
		return null;
	}

	protected void resetActionTimer()
	{
		actionTimer = resetActionTimerAmount;
	}

	protected void resetLandTimer()
	{
		landTimer = resetLandTimerAmount;
	}

	protected void resetWanderJumpTimer()
	{
		wanderJumpTimer = resetWanderJumpTimerAmount;
	}

/////////////
// Damage: //
/////////////
	
	@Override
	public boolean causeFallDamage(float p_148750_, float p_148751_, DamageSource p_148752_)
	{
		return false;
	}
	
	@Override
	protected void checkFallDamage(double p_27754_, boolean p_27755_, BlockState p_27756_, BlockPos p_27757_)
	{
	}

///////////////
// Movement: //
///////////////

	@Override
	public void travel(Vec3 vec3)
	{
		switch(getMoveControlType())
		{
			case HOVER -> moveHoverType(vec3);
			case FORWARD -> moveForwardType(vec3);
			default -> performMotion((float) getAttributeValue(Attributes.FLYING_SPEED), vec3);
		};
	}

	private void moveHoverType(Vec3 vec3)
	{
		float speed = (float) getAttributeValue(Attributes.FLYING_SPEED);

		if(canBeControlledByRider())
		{
			LivingEntity driver = (LivingEntity) getControllingPassenger();

			if(isControlledByLocalInstance())
			{
				if(isFlying())
					vec3 = getHoverVector(vec3, getDrivingSpeedMod(), driver);
				else if(FFKeys.FAMILIAR_ASCEND.isDown())
					jumpFromGround();

				speed *= getDrivingSpeedMod();
			}
			else if(driver instanceof LivingEntity)
			{
				setDeltaMovement(Vec3.ZERO);
				calculateEntityAnimation(this, true);
				return;
			}

			setYRot(driver.getYRot());
			yRotO = getYRot();
			setXRot(driver.getXRot() * 0.5F);
			setRot(getYRot(), getXRot());
			yBodyRot = getYRot();
			yHeadRot = getYRot();
		}

		performMotion(speed, vec3);
	}

	private void moveForwardType(Vec3 vec3)
	{
		float speed = (float) getAttributeValue(Attributes.FLYING_SPEED);

		if(canBeControlledByRider())
		{
			LivingEntity driver = (LivingEntity) getControllingPassenger();

			if(isControlledByLocalInstance())
			{
				if(!isFlying() && FFKeys.FAMILIAR_ASCEND.isDown())
					jumpFromGround();

				vec3 = getForwardVector(vec3, 2.5f * getDrivingSpeedMod(), driver);

				if(vec3.z != 0)
					speed *= getDrivingSpeedMod();
				else if(vec3.y != 0)
					speed *= getDrivingSpeedMod();

				setSpeed(speed);
			}
			else
			{
				setDeltaMovement(Vec3.ZERO);
				calculateEntityAnimation(this, true);
				return;
			}

			yRotO = getYRot();
			setYRot(driver.getYHeadRot());
			yBodyRot = getYRot();
			yHeadRot = getYRot();
		}

		performMotion(speed, vec3);
	}

	private void performMotion(float speed, Vec3 vec3)
	{
		if(getControllingPassenger() == null && level.isClientSide())
			return;

		if(isFlying())
		{
			moveRelative(speed, vec3);
			move(MoverType.SELF, getDeltaMovement());
			setDeltaMovement(getDeltaMovement().scale(0.8f));
			calculateEntityAnimation(this, true);
		}
		else
		{
			setDeltaMovement(getDeltaMovement().scale(0.8f));
			super.travel(vec3);
		}
	}

	public void startFlying()
	{
		jumpFromGround();
		resetLandTimer();
	}

	public Vec3 getHoverVector(Vec3 vec3, float drivingSpeedMod, LivingEntity driver)
	{
		double xMove = vec3.x + driver.xxa;
		double yMove = vec3.y;
		double zMove = vec3.z + driver.zza;

		if(FFKeys.FAMILIAR_ASCEND.isDown())
			yMove += drivingSpeedMod;
		if(FFKeys.FAMILIAR_DESCEND.isDown())
			yMove -= drivingSpeedMod;

		return new Vec3(xMove, yMove, zMove);
	}

	public Vec3 getForwardVector(Vec3 vec3, float drivingSpeedMod, LivingEntity driver)
	{
		double xMove = vec3.x;
		double yMove = vec3.y;
		double zMove = driver.zza > 0 ? vec3.z + Math.max(driver.zza, 0) : 0;

		if(FFKeys.FAMILIAR_ASCEND.isDown())
			yMove += drivingSpeedMod;
		if(FFKeys.FAMILIAR_DESCEND.isDown())
			yMove -= drivingSpeedMod;
		if (zMove > 0)
			yMove += Math.toRadians(-driver.getXRot()) * drivingSpeedMod;
		if(!FFKeys.FAMILIAR_ASCEND.isDown() && !FFKeys.FAMILIAR_DESCEND.isDown() && zMove <= 0)
			yMove = 0;

		return new Vec3(xMove, yMove, zMove);
	}

/////////////
// Mob AI: //
/////////////

	@Override
	public void tick()
	{
		super.tick();
		setFlying(shouldFly());
		this.setNoGravity(isFlying());

		if(isFlying())
			navigation = createNavigation(level);
		else
			navigation.stop();

		if(actionTimer > 0) --actionTimer;
		if(landTimer > 0) --landTimer;
	}

	@Override
	protected PathNavigation createNavigation(Level level)
	{
		FamiliarFlyingPathNavigation navigation = new FamiliarFlyingPathNavigation(this, level);
		navigation.setCanOpenDoors(false);
		navigation.setCanFloat(true);
		navigation.setCanPassDoors(true);
		return navigation;
	}
}
