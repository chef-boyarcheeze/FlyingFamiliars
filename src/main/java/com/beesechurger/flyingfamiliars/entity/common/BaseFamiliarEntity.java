package com.beesechurger.flyingfamiliars.entity.common;

import com.beesechurger.flyingfamiliars.keys.FFKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public abstract class BaseFamiliarEntity extends TamableAnimal
{
	private static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(BaseFamiliarEntity.class, EntityDataSerializers.STRING);
	private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(BaseFamiliarEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(BaseFamiliarEntity.class, EntityDataSerializers.BOOLEAN);

	public static final float BEGIN_FOLLOW_DISTANCE = 16;
	public static final float END_FOLLOW_DISTANCE = 8;
	
	public float pitchO = 0, pitch = 0;
	public float rollO = 0, roll = 0;

	protected int familiarActionTimer = 0;
	protected int familiarLandTimer = 0;

	protected BaseFamiliarEntity(EntityType<? extends TamableAnimal> entity, Level level)
	{
		super(entity, level);
		this.setTame(false);
		moveControl = new FamiliarMoveControl(this);
		lookControl = new LookControl(this);
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
		double d0 = getX() - xo;
		double d1 = getY() - yo;
		double d2 = getZ() - zo;
		return d0 * d0 + d1 * d1 + d2 * d2 > 2.5000003E-7F;
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
		}
	}
	
	public boolean isTamedFor(Player player)
	{
		return isTame() && isOwnedBy(player);
	}

	public boolean isOwnerDoingFamiliarAction()
	{
		return getControllingPassenger() == getOwner() && FFKeys.FAMILIAR_ACTION.isDown();
	}
	
	@Override
	public boolean canBeControlledByRider()
	{
		return getControllingPassenger() instanceof Player;
	}

	public boolean notCarryingMobPassengers()
	{
		return !(getFirstPassenger() instanceof Mob);
	}

	@Override
	public boolean canBeLeashed(Player player)
	{
		return false;
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

	@Override
	public boolean shouldRiderSit()
	{
		return getControllingPassenger() != null;
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
	public double getPassengersRidingOffset()
	{
		if(this instanceof GriffonflyEntity)
			return (getDimensions(getPose()).height * 0.8);
		else if(this instanceof CloudRayEntity)
			return (getDimensions(getPose()).height * 0.6);
		else
			return getDimensions(getPose()).height;
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

	@Override
	protected int getExperienceReward(Player player)
	{
		return 0;
	}

	protected void resetFamiliarActionTimer()
	{
		familiarActionTimer = 20;
	}

	protected void resetFamiliarLandTimer()
	{
		familiarLandTimer = 100;
	}
	
// Damage:
	
	@Override
	public boolean causeFallDamage(float p_148750_, float p_148751_, DamageSource p_148752_)
	{
		return false;
	}
	
	@Override
	protected void checkFallDamage(double p_27754_, boolean p_27755_, BlockState p_27756_, BlockPos p_27757_)
	{
	}

// Movement:

	@Override
	protected PathNavigation createNavigation(Level level)
	{
		FamiliarFlyingPathNavigation navigation = new FamiliarFlyingPathNavigation(this, level);
		navigation.setCanOpenDoors(false);
		navigation.setCanFloat(true);
		navigation.setCanPassDoors(true);
		return navigation;
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

	@Override
	public void tick()
	{
		super.tick();

		setFlying(shouldFly());

		if(familiarActionTimer > 0) --familiarActionTimer;
		if(familiarLandTimer > 0) --familiarLandTimer;
	}

// Entity values:
	
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
	
	static class FamiliarBodyRotationControl extends BodyRotationControl
	{
        private final BaseFamiliarEntity familiar;

		private final String rotationType;
		private final float angleLimit;
		private final float angleInterval;
		private int headStableTime;
		private float lastStableYHeadRot;

        public FamiliarBodyRotationControl(BaseFamiliarEntity familiar, String rotationType, float angleLimit, float angleInterval)
        {
            super(familiar);
            this.familiar = familiar;
			this.rotationType = rotationType;
			this.angleLimit = angleLimit;
			this.angleInterval = angleInterval;
        }

        @Override
        public void clientTick()
        {
			switch(rotationType)
			{
				case "hover" -> rotationHover();
				case "forward" -> rotationForward();
			}

			if(familiar.isMoving())
			{
				familiar.yBodyRot = familiar.getYRot();
				rotateHeadIfNecessary();
				lastStableYHeadRot = familiar.yHeadRot;
				headStableTime = 0;
			}
			else
			{
				if(familiar.notCarryingMobPassengers())
				{
					if(Math.abs(familiar.yHeadRot - lastStableYHeadRot) > 15.0F)
					{
						headStableTime = 0;
						lastStableYHeadRot = familiar.yHeadRot;
						rotateBodyIfNecessary();
					}
					else
					{
						++headStableTime;
						if(headStableTime > 10)
						{
							rotateHeadTowardsFront();
						}
					}
				}
			}
        }

		private void rotationHover()
		{
			LivingEntity driver = (LivingEntity) familiar.getControllingPassenger();

			float forwardMove = driver != null ? driver.zza : familiar.zza;
			float sideMove = driver != null ? driver.xxa : familiar.xxa;

			if(familiar.isFlying() && driver != null)
			{
				if(forwardMove > 0)
					incrementPitch();
				else if(forwardMove < 0)
					decrementPitch();
				else
					centerPitch();
			}
			else
				centerPitch();

			if(familiar.isFlying() && driver != null)
			{
				if(sideMove > 0)
					incrementRoll();
				else if(sideMove < 0)
					decrementRoll();
				else
					centerRoll();
			}
			else
				centerRoll();
		}

		private void rotationForward()
		{
			LivingEntity driver = (LivingEntity) familiar.getControllingPassenger();

			float wantedRotY;

			if (driver != null)
				wantedRotY = driver.getYRot();
			else
				wantedRotY = familiar.getYRot();

			float yRotDifference = Mth.wrapDegrees(familiar.getYRot() - wantedRotY);

			if(familiar.isFlying() && driver != null)
			{
				if(yRotDifference > 0.2f * angleLimit)
					incrementRoll();
				else if(yRotDifference < -0.2f * angleLimit)
					decrementRoll();
				else
					centerRoll();
			}
			else
				centerRoll();
		}

		private void incrementPitch()
		{
			familiar.pitchO = familiar.pitch;
			if(familiar.pitch < angleLimit) familiar.pitch += angleInterval;
		}

		private void decrementPitch()
		{
			familiar.pitchO = familiar.pitch;
			if(familiar.pitch > -angleLimit) familiar.pitch -= angleInterval;
		}

		private void incrementRoll()
		{
			familiar.rollO = familiar.roll;
			if(familiar.roll < angleLimit) familiar.roll += angleInterval;
		}

		private void decrementRoll()
		{
			familiar.rollO = familiar.roll;
			if(familiar.roll > -angleLimit) familiar.roll -= angleInterval;
		}

		private void centerPitch()
		{
			familiar.pitchO = familiar.pitch;
			if(familiar.pitch > 0) familiar.pitch -= angleInterval;
			if(familiar.pitch < 0) familiar.pitch += angleInterval;
		}

		private void centerRoll()
		{
			familiar.rollO = familiar.roll;
			if(familiar.roll > 0) familiar.roll -= angleInterval;
			if(familiar.roll < 0) familiar.roll += angleInterval;
		}

		private void rotateBodyIfNecessary() {
			familiar.yBodyRot = Mth.rotateIfNecessary(familiar.yBodyRot, familiar.yHeadRot, (float) familiar.getMaxHeadYRot());
		}

		private void rotateHeadIfNecessary() {
			familiar.yHeadRot = Mth.rotateIfNecessary(familiar.yHeadRot, familiar.yBodyRot, (float) familiar.getMaxHeadYRot());
		}

		private void rotateHeadTowardsFront() {
			int i = headStableTime - 10;
			float f = Mth.clamp((float)i / 10.0F, 0.0F, 1.0F);
			float f1 = (float) familiar.getMaxHeadYRot() * (1.0F - f);
			familiar.yBodyRot = Mth.rotateIfNecessary(familiar.yBodyRot, familiar.yHeadRot, f1);
		}
    }

	static class FamiliarMoveControl extends MoveControl
	{
		private final BaseFamiliarEntity familiar;

		public FamiliarMoveControl(BaseFamiliarEntity familiar)
		{
			super(familiar);
			this.familiar = familiar;
		}

		@Override
		public void tick()
		{
			if(!familiar.isFlying())
			{
				super.tick();
				return;
			}

			if(operation == Operation.MOVE_TO)
			{
				operation = MoveControl.Operation.WAIT;
				familiar.setNoGravity(true);

				double speed = familiar.getAttributeValue(Attributes.MOVEMENT_SPEED);

				float distX = (float) (wantedX - familiar.getX());
				float distY = (float) (wantedY - familiar.getY());
				float distZ = (float) (wantedZ - familiar.getZ());

				double planeDist = Math.sqrt(distX * distX + distZ * distZ);
				double yDistMod = 1.0D - (double) Mth.abs(distY * 0.7F) / planeDist;

				distX = (float) ((double) distX * yDistMod);
				distZ = (float) ((double) distZ * yDistMod);

				planeDist = Mth.sqrt(distX * distX + distZ * distZ);

				double dist = Math.sqrt(distX * distX + distY * distY + distZ * distZ);
				if (dist > 1.0f)
				{
					float yaw = (float) Math.toDegrees(Mth.atan2(distZ, distX)) - 90.0f;
					float pitch = (float) -Math.toDegrees(Mth.atan2(-distY, planeDist));

					if(dist > BEGIN_FOLLOW_DISTANCE)
						familiar.setYRot(rotlerp(familiar.getYRot(), yaw, (float) (3 * 10 * familiar.getAttributeValue(Attributes.MOVEMENT_SPEED))));
					familiar.setXRot(pitch);

					double xAddVector = Math.cos(Math.toRadians(familiar.getYRot() + 90.0f)) * Math.abs((double) distX / dist);
					double yAddVector = Math.sin(Math.toRadians(pitch)) * Math.abs((double) distY / dist);
					double zAddVector = Math.sin(Math.toRadians(familiar.getYRot() + 90.0f)) * Math.abs((double) distZ / dist);

					xAddVector = dist > BEGIN_FOLLOW_DISTANCE ? xAddVector : 0;
					zAddVector = dist > BEGIN_FOLLOW_DISTANCE ? zAddVector : 0;

					xAddVector = Math.abs(xAddVector) > speed * speedModifier ? xAddVector < 0 ? -speed * speedModifier : speed * speedModifier : xAddVector;
					yAddVector = Math.abs(yAddVector) > speed * speedModifier ? yAddVector < 0 ? -speed * speedModifier : speed * speedModifier : yAddVector;
					zAddVector = Math.abs(zAddVector) > speed * speedModifier ? zAddVector < 0 ? -speed * speedModifier : speed * speedModifier : zAddVector;

					familiar.setDeltaMovement(familiar.getDeltaMovement().add(xAddVector, yAddVector, zAddVector));
				}
			}
		}
	}

	static class FamiliarFlyingPathNavigation extends FlyingPathNavigation
	{
		public FamiliarFlyingPathNavigation(BaseFamiliarEntity familiar, Level level)
		{
			super(familiar, level);
		}

		@Override
		public void tick()
		{
			if (!isDone() && canUpdatePath())
			{
				BaseFamiliarEntity familiar = (BaseFamiliarEntity) mob;

				BlockPos target = getTargetPos();
				if (target != null)
				{
					mob.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), 1.0f);

					maxDistanceToWaypoint = mob.getBbWidth() * mob.getBbWidth();
					Vec3i position = new Vec3i(getTempMobPos().x, getTempMobPos().y, getTempMobPos().z);

					if (target.distSqr(position) <= maxDistanceToWaypoint) path = null;
				}
			}
		}

		@Override
		public boolean isStableDestination(BlockPos pos)
		{
			return true;
		}
	}

	static class FamiliarFollowOwnerGoal extends Goal
	{
		private final BaseFamiliarEntity familiar;
		private final double speed;
		Level world;
		float endFollow;
		float beginFollow;
		private LivingEntity owner;
		private int timeToRecalcPath;
		private float oldWaterCost;

		public FamiliarFollowOwnerGoal(BaseFamiliarEntity familiar, double speed, float beginFollow, float endFollow)
		{
			this.familiar = familiar;
			this.world = familiar.level;
			this.speed = speed * familiar.getAttributeValue(Attributes.MOVEMENT_SPEED);
			this.beginFollow = beginFollow;
			this.endFollow = endFollow;
			this.setFlags(EnumSet.of(Flag.MOVE));
		}

		@Override
		public boolean canUse()
		{
			LivingEntity owner = this.familiar.getOwner();
			if(owner != null)
			{
				if(!((owner instanceof Player && owner.isSpectator())
						|| familiar.isOrderedToSit()
						|| familiar.distanceToSqr(owner) < beginFollow * beginFollow))
				{
					this.owner = owner;
					return true;
				}
			}

			return false;
		}

		@Override
		public boolean canContinueToUse()
		{
			return !noPath() && familiar.distanceToSqr(owner) > endFollow * endFollow
					&& !familiar.isOrderedToSit();
		}

		private boolean noPath()
		{
			return familiar.getNavigation().getPath() == null;
		}

		@Override
		public void start()
		{
			timeToRecalcPath = 0;
			oldWaterCost = familiar.getPathfindingMalus(BlockPathTypes.WATER);
			familiar.setPathfindingMalus(BlockPathTypes.WATER, 0);
		}

		@Override
		public void stop()
		{
			owner = null;
			familiar.getNavigation().stop();
			familiar.setPathfindingMalus(BlockPathTypes.WATER, oldWaterCost);
		}

		@Override
		public void tick()
		{
			if(!familiar.isFlying())
			{
				familiar.jumpFromGround();
				return;
			}

			if(owner != null)
			{
				familiar.getLookControl().setLookAt(owner, 10.0f, familiar.getMaxHeadXRot());

				if (--timeToRecalcPath <= 0)
				{
					timeToRecalcPath = adjustedTickDelay(10);

					if(familiar.distanceToSqr(owner) <= beginFollow * beginFollow)
					{
						familiar.getNavigation().stop();
					}
					else if(familiar.distanceToSqr(owner) > beginFollow * beginFollow * 16)
					{
						teleportToOwner();
					}
					else
					{
						familiar.getMoveControl().setWantedPosition(owner.getX(), owner.getY() + owner.getEyeHeight(), owner.getZ(), speed);
					}
				}
			}
		}

		private void teleportToOwner()
		{
			BlockPos blockpos = owner.blockPosition();

			for(int i = 0; i < 10; ++i)
			{
				int j = randomIntInclusive(-3, 3);
				int k = randomIntInclusive(-1, 1);
				int l = randomIntInclusive(-3, 3);
				boolean flag = maybeTeleportTo(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);

				if (flag)
					return;
			}
		}

		private boolean maybeTeleportTo(int currentX, int currentY, int currentZ)
		{
			if(Math.abs((double) currentX - owner.getX()) < 2.0D && Math.abs((double) currentZ - owner.getZ()) < 2.0D)
			{
				return false;
			}
			else if(!canTeleportTo(new BlockPos(currentX, currentY, currentZ)))
			{
				return false;
			}
			else
			{
				familiar.moveTo((double) currentX + 0.5D, (double) currentY, (double) currentZ + 0.5D, familiar.getYRot(), familiar.getXRot());
				familiar.getNavigation().stop();
				return true;
			}
		}

		protected boolean canTeleportTo(BlockPos pos)
		{
			BlockState blockstate = world.getBlockState(pos);
			return	world.isEmptyBlock(pos.above()) && world.isEmptyBlock(pos.above((int) (familiar.getBbHeight() + 1)));
		}

		private int randomIntInclusive(int p_25301_, int p_25302_)
		{
			return familiar.getRandom().nextInt(p_25302_ - p_25301_ + 1) + p_25301_;
		}
	}

	static class FamiliarLandGoal extends Goal
	{
		private final BaseFamiliarEntity familiar;
		private final double speed;
		private final int landingSearchDistance;
		private int timeToRecalcPath;
		private BlockPos.MutableBlockPos goal;
		Level world;

		public FamiliarLandGoal(BaseFamiliarEntity familiar, double speed, int landingSearchDistance)
		{
			this.familiar = familiar;
			this.world = familiar.level;
			this.speed = speed * familiar.getAttributeValue(Attributes.MOVEMENT_SPEED);
			this.landingSearchDistance = landingSearchDistance;
			this.setFlags(EnumSet.of(Flag.MOVE));
		}

		@Override
		public boolean canUse()
		{
			return familiar.isFlying();
		}

		@Override
		public boolean canContinueToUse()
		{
			return !noPath() && familiar.isFlying() && goal != null;
		}

		private void solidBlockBelow()
		{
			for(int i = 0; i < landingSearchDistance; i++)
			{
				var block = familiar.blockPosition().mutable().move(0, -i, 0);

				if(familiar.isFlying() && !world.getBlockState(block).isAir())
				{
					goal = block;
					break;
				}
			}
		}

		private boolean noPath()
		{
			return familiar.getNavigation().getPath() == null;
		}

		@Override
		public void start()
		{
			timeToRecalcPath = 0;
			familiar.setPathfindingMalus(BlockPathTypes.WATER, 0);
		}

		@Override
		public void stop()
		{
			goal = null;
			familiar.getNavigation().stop();
		}

		@Override
		public void tick()
		{
			solidBlockBelow();

			if (--timeToRecalcPath <= 0)
			{
				timeToRecalcPath = adjustedTickDelay(10);

				if(goal != null)
				{
					if(!familiar.isFlying())
					{
						familiar.getNavigation().stop();
					}
					else
					{
						familiar.getMoveControl().setWantedPosition(goal.getX(), goal.getY(), goal.getZ(), speed);
					}
				}
			}
		}
	}
}
