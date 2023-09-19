package com.beesechurger.flyingfamiliars.entity.custom;

import com.beesechurger.flyingfamiliars.FFKeys;
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

public abstract class AbstractFamiliarEntity extends TamableAnimal
{
	private static final EntityDataAccessor<String> VARIANT = SynchedEntityData.defineId(AbstractFamiliarEntity.class, EntityDataSerializers.STRING);
	private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(AbstractFamiliarEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(AbstractFamiliarEntity.class, EntityDataSerializers.BOOLEAN);

	public static final float BEGIN_FOLLOW_DISTANCE = 16;
	public static final float END_FOLLOW_DISTANCE = 8;
	
	public static float ANGLE_INTERVAL = 2.0f;
    public static float ANGLE_LIMIT = 10;
	public float pitchO = 0, pitch = 0;
	public float rollO = 0, roll = 0;

	protected AbstractFamiliarEntity(EntityType<? extends TamableAnimal> entity, Level level)
	{
		super(entity, level);
		this.setTame(false);
		moveControl = new FamiliarMoveControl(this);
		lookControl = new LookControl(this);
	}
	
	@Override
	protected BodyRotationControl createBodyControl()
	{
		return new FamiliarBodyRotationControl(this);
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
		double d0 = this.getX() - this.xo;
		double d1 = this.getY() - this.yo;
		double d2 = this.getZ() - this.zo;
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
			if(!isOnGround() && (getControllingPassenger() instanceof Player && FFKeys.familiar_ascend.isDown() || level.getBlockState(standingOn).isAir()))
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
		return getControllingPassenger() == this.getOwner() && FFKeys.familiar_action.isDown();
	}
	
	@Override
	public boolean canBeControlledByRider()
	{
		return this.getControllingPassenger() instanceof LivingEntity;
	}

	public boolean notCarryingMobPassengers() {
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
		for (Entity passenger : this.getPassengers())
		{
			if (passenger instanceof Player player && this.getTarget() != passenger)
			{
				if (this.isTame() && this.getOwnerUUID() != null && this.getOwnerUUID().equals(player.getUUID()))
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
		return this.getControllingPassenger() != null;
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
	public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob familiar)
	{
		return null;
	}

	@Override
	protected int getExperienceReward(Player player)
	{
		return 0;
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
	public void positionRider(Entity rider)
	{
		LivingEntity driver = (LivingEntity) rider;

		if(this.hasPassenger(rider))
		{
			double x = 0;
			double y = getPassengersRidingOffset() + rider.getMyRidingOffset();
			double z = getScale() - 1;

			Vec3 pos = new Vec3(x, y, z).yRot((float) Math.toRadians(-yBodyRot)).add(position());
			rider.setPos(pos);

			// fix rider rotation
			driver.xRotO = driver.getXRot();
			driver.yRotO = driver.getYRot();
			driver.setYBodyRot(yBodyRot);
		}
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

	public Vec3 getHoverVector(Vec3 vec3, LivingEntity driver)
	{
		double xMove = vec3.x + driver.xxa;
		double yMove = vec3.y;
		double zMove = vec3.z + driver.zza;
		
		if(FFKeys.familiar_ascend.isDown()) yMove += 0.6;
		if(FFKeys.familiar_descend.isDown()) yMove -= 0.6;
		
		return new Vec3(xMove, yMove, zMove);
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
	
////////////////////////////////
// Entity AI control classes: //
////////////////////////////////
	
	static class FamiliarBodyRotationControl extends BodyRotationControl
	{
        private final AbstractFamiliarEntity familiar;

		private int headStableTime;
		private float lastStableYHeadRot;

        public FamiliarBodyRotationControl(AbstractFamiliarEntity familiar)
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

		private void rotateBodyIfNecessary() {
			familiar.yBodyRot = Mth.rotateIfNecessary(familiar.yBodyRot, familiar.yHeadRot, (float) familiar.getMaxHeadYRot());
		}

		private void rotateHeadIfNecessary() {
			familiar.yHeadRot = Mth.rotateIfNecessary(familiar.yHeadRot, familiar.yBodyRot, (float) familiar.getMaxHeadYRot());
		}

		private void rotateHeadTowardsFront() {
			int i = this.headStableTime - 10;
			float f = Mth.clamp((float)i / 10.0F, 0.0F, 1.0F);
			float f1 = (float) familiar.getMaxHeadYRot() * (1.0F - f);
			familiar.yBodyRot = Mth.rotateIfNecessary(familiar.yBodyRot, familiar.yHeadRot, f1);
		}
    }

	static class FamiliarMoveControl extends MoveControl
	{
		private final AbstractFamiliarEntity familiar;

		public FamiliarMoveControl(AbstractFamiliarEntity familiar)
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

			if(this.operation == Operation.MOVE_TO)
			{
				double flyingSpeed = familiar.getAttributeValue(Attributes.FLYING_SPEED);

				float distX = (float) (wantedX - familiar.getX());
				float distY = (float) (wantedY - familiar.getY());
				float distZ = (float) (wantedZ - familiar.getZ());

				double planeDist = Math.sqrt(distX * distX + distZ * distZ);
				double yDistMod = 1.0D - (double) Mth.abs(distY * 0.7F) / planeDist;

				distX = (float) ((double) distX * yDistMod);
				distZ = (float) ((double) distZ * yDistMod);

				planeDist = Mth.sqrt(distX * distX + distZ * distZ);

				double dist = Math.sqrt(distX * distX + distZ * distZ + distY * distY);
				if (dist > BEGIN_FOLLOW_DISTANCE)
				{
					float yaw = (float) Math.toDegrees(Mth.atan2(distZ, distX)) - 90.0F;
					familiar.setYRot(rotlerp(familiar.getYRot(), yaw, 6));

					float pitch = (float) -Math.toDegrees(Mth.atan2(-distY, planeDist));
					familiar.setXRot(pitch);

					double xAddVector = Math.cos(Math.toRadians(familiar.getYRot() + 90.0f)) * Math.abs((double) distX / dist);
					double yAddVector = Math.sin(Math.toRadians(pitch)) * Math.abs((double) distY / dist);
					double zAddVector = Math.sin(Math.toRadians(familiar.getYRot() + 90.0f)) * Math.abs((double) distZ / dist);

					xAddVector = Math.abs(xAddVector) > flyingSpeed * speedModifier ? xAddVector < 0 ? -flyingSpeed * speedModifier : flyingSpeed * speedModifier : xAddVector;
					yAddVector = Math.abs(yAddVector) > flyingSpeed * speedModifier ? yAddVector < 0 ? -flyingSpeed * speedModifier : flyingSpeed * speedModifier : yAddVector;
					zAddVector = Math.abs(zAddVector) > flyingSpeed * speedModifier ? zAddVector < 0 ? -flyingSpeed * speedModifier : flyingSpeed * speedModifier : zAddVector;

					familiar.setDeltaMovement(familiar.getDeltaMovement().add(xAddVector, yAddVector, zAddVector));
				}
			}
		}
	}

	static class FamiliarFlyingPathNavigation extends FlyingPathNavigation
	{
		public FamiliarFlyingPathNavigation(AbstractFamiliarEntity familiar, Level level)
		{
			super(familiar, level);
		}

		@Override
		public void tick()
		{
			if (!isDone() && canUpdatePath())
			{
				AbstractFamiliarEntity familiar = (AbstractFamiliarEntity) mob;

				BlockPos target = getTargetPos();
				if (target != null)
				{
					mob.getMoveControl().setWantedPosition(target.getX(), target.getY() + 1, target.getZ(), 1.0f);

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
		private final AbstractFamiliarEntity familiar;
		private final double followSpeed;
		Level world;
		float endFollow;
		float beginFollow;
		private LivingEntity owner;
		private int timeToRecalcPath;
		private float oldWaterCost;

		public FamiliarFollowOwnerGoal(AbstractFamiliarEntity familiar, double followSpeed, float beginFollow, float endFollow)
		{
			this.familiar = familiar;
			this.world = familiar.level;
			this.followSpeed = followSpeed;
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
						|| this.familiar.isOrderedToSit()
						|| this.familiar.distanceToSqr(owner) < this.beginFollow * this.beginFollow))
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
			return !noPath() && this.familiar.distanceToSqr(this.owner) > this.endFollow * this.endFollow
					&& !this.familiar.isOrderedToSit();
		}

		private boolean noPath()
		{
			return this.familiar.getNavigation().getPath() == null;
		}

		@Override
		public void start()
		{
			this.timeToRecalcPath = 0;
			this.oldWaterCost = this.familiar.getPathfindingMalus(BlockPathTypes.WATER);
			this.familiar.setPathfindingMalus(BlockPathTypes.WATER, 0);
		}

		@Override
		public void stop()
		{
			this.owner = null;
			this.familiar.getNavigation().stop();
			this.familiar.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
		}

		@Override
		public void tick()
		{
			if(!this.familiar.isFlying())
			{
				this.familiar.jumpFromGround();
				return;
			}

			if(this.owner != null)
			{
				this.familiar.getLookControl().setLookAt(this.owner, 10.0f, this.familiar.getMaxHeadXRot());

				if (--this.timeToRecalcPath <= 0)
				{
					this.timeToRecalcPath = this.adjustedTickDelay(10);

					if(this.familiar.distanceToSqr(this.owner) <= this.beginFollow * this.beginFollow)
					{
						if(this.familiar.isFlying())
						this.familiar.getNavigation().stop();
					}
					else if(this.familiar.distanceToSqr(this.owner) > this.beginFollow * this.beginFollow * 16)
					{
						teleportToOwner();
					}
					else
					{
						this.familiar.getNavigation().moveTo(this.owner, this.followSpeed);
					}
				}
			}
		}

		private boolean tryMoveTo()
		{
			if (!familiar.isFlying())
			{
				return familiar.getNavigation().moveTo(this.owner, this.followSpeed);
			}
			else
			{
				this.familiar.getMoveControl().setWantedPosition(this.owner.getX(), this.owner.getY() + this.owner.getEyeHeight(), this.owner.getZ(), 1.0f);
				return true;
			}
		}

		private void teleportToOwner()
		{
			BlockPos blockpos = this.owner.blockPosition();

			for(int i = 0; i < 10; ++i)
			{
				int j = this.randomIntInclusive(-3, 3);
				int k = this.randomIntInclusive(-1, 1);
				int l = this.randomIntInclusive(-3, 3);
				boolean flag = this.maybeTeleportTo(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);

				if (flag)
					return;
			}
		}

		private boolean maybeTeleportTo(int currentX, int currentY, int currentZ)
		{
			if(Math.abs((double) currentX - this.owner.getX()) < 2.0D && Math.abs((double) currentZ - this.owner.getZ()) < 2.0D)
			{
				return false;
			}
			else if(!this.canTeleportTo(new BlockPos(currentX, currentY, currentZ)))
			{
				return false;
			}
			else
			{
				this.familiar.moveTo((double) currentX + 0.5D, (double) currentY, (double) currentZ + 0.5D, this.familiar.getYRot(), this.familiar.getXRot());
				this.familiar.getNavigation().stop();
				return true;
			}
		}

		protected boolean canTeleportTo(BlockPos pos)
		{
			BlockState blockstate = this.world.getBlockState(pos);
			return	this.world.isEmptyBlock(pos.above()) && this.world.isEmptyBlock(pos.above((int) (familiar.getBbHeight() + 1)));
		}

		private int randomIntInclusive(int p_25301_, int p_25302_)
		{
			return this.familiar.getRandom().nextInt(p_25302_ - p_25301_ + 1) + p_25301_;
		}
	}
}
