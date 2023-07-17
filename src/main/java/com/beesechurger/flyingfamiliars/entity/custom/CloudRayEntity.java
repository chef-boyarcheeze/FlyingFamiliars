package com.beesechurger.flyingfamiliars.entity.custom;

import java.util.EnumSet;

import org.jetbrains.annotations.Nullable;

import com.beesechurger.flyingfamiliars.FFKeys;
import com.beesechurger.flyingfamiliars.sound.FFSounds;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import net.minecraftforge.client.event.ScreenEvent.KeyboardKeyPressedEvent;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class CloudRayEntity extends TamableAnimal implements IAnimatable
{
	private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(CloudRayEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(CloudRayEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> INVERTED = SynchedEntityData.defineId(CloudRayEntity.class, EntityDataSerializers.BOOLEAN);

	public static final float FLIGHT_THRESHOLD = 0.1f;
	public static final float MAX_HEALTH = 50.00f;
	public static final float FOLLOW_RANGE = 16;
	public static final float FOLLOW_RANGE_FLYING = FOLLOW_RANGE * 2;
	public static final float FLYING_SPEED = 0.4f;
	public static final float MOVEMENT_SPEED = 0.3f;
	public static final float ATTACK_DAMAGE = 3.0f;
	public static final float ATTACK_SPEED = 2.0f;

	private final Item FOOD_ITEM = Items.APPLE;
	private final Item TAME_ITEM = Items.GLOW_BERRIES;

	private final AnimationFactory factory = new AnimationFactory(this);
	
	private int animTickCounter = 0;
	private int animSpeedCounter = 0;

	public CloudRayEntity(EntityType<CloudRayEntity> entityType, Level level)
	{
		super(entityType, level);
		this.setTame(false);
		this.moveControl = new FlyingMoveControl(this, 5, false);
		this.lookControl = new CloudRayLookControl(this);
	}

	public static AttributeSupplier setAttributes()
	{
		return TamableAnimal.createMobAttributes().add(Attributes.MAX_HEALTH, MAX_HEALTH)
				.add(Attributes.FOLLOW_RANGE, FOLLOW_RANGE).add(Attributes.FLYING_SPEED, FLYING_SPEED)
				.add(Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED).add(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE)
				.add(Attributes.ATTACK_SPEED, ATTACK_SPEED).build();
	}

	protected void registerGoals()
	{
		this.goalSelector.addGoal(0, new SitWhenOrderedToGoal(this));
		//this.goalSelector.addGoal(1, new CloudRayWanderGoal(this));
		// this.goalSelector.addGoal(1, new MeleeAttackGoal((PathfinderMob)
		// this.getTarget(), ATTACK_DAMAGE, false));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0f));
		this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1, FOLLOW_RANGE, 8, true));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.00));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
	}

	@Override
	protected BodyRotationControl createBodyControl()
	{
		return new CloudRayBodyRotationControl(this);
	}

// GeckoLib animation controls:

	private <E extends IAnimatable> PlayState predicateGeneral(AnimationEvent<E> event)
	{	
		if(event.getController().getAnimationState() != AnimationState.Running)
		{
			animTickCounter = 0;
		}
		
		animTickCounter += 1;
		animSpeedCounter = (int) event.getController().getAnimationSpeed();
		
		if(this.isFlying())
		{
			if(FFKeys.ascend.isDown() && !FFKeys.descend.isDown())
			{
				event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.cloud_ray.flying_ascend"));
			}
			else if(!FFKeys.ascend.isDown() && FFKeys.descend.isDown())
			{
				event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.cloud_ray.flying_descend"));
			}
			else if(this.isMoving())
			{
				event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.cloud_ray.flying"));
			}
			else
			{
				event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.cloud_ray.flying_stationary"));
			}
		}
		else
		{
			if (this.isMoving())
			{
				event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.cloud_ray.walking"));
			}
			else
			{
				event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.cloud_ray.grounded_long"));
			}
		}
		
		if(event.getController().getCurrentAnimation() != null)
		{
			//System.out.print(event.getController().getCurrentAnimation().animationLength + "\n");
			//event.getController().getCurrentAnimation().customInstructionKeyframes;
		}
		
		return PlayState.CONTINUE;
	}

	private <E extends IAnimatable> PlayState predicateFins(AnimationEvent<E> event)
	{
		event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.cloud_ray.move_fins", EDefaultLoopTypes.LOOP));
		return PlayState.CONTINUE;
	}
	
	private <E extends IAnimatable> PlayState predicateHead(AnimationEvent<E> event)
	{
		if(getRandom().nextDouble() < 0.005)
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.cloud_ray.move_fins", EDefaultLoopTypes.PLAY_ONCE));
		}
		
		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimationData data)
	{
		data.addAnimationController(new AnimationController<>(this, "controllerGeneral", 5, this::predicateGeneral));
		data.addAnimationController(new AnimationController<>(this, "controllerFins", 0, this::predicateFins));
		data.addAnimationController(new AnimationController<>(this, "controllerHead", 0, this::predicateHead));
	}

	@Override
	public AnimationFactory getFactory()
	{
		return this.factory;
	}
	

// Save data preservation:	

	@Override
	public void readAdditionalSaveData(CompoundTag tag)
	{
		super.readAdditionalSaveData(tag);
		setSitting(tag.getBoolean("isSitting"));
		setInverted(tag.getBoolean("isInverted"));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag)
	{
		super.addAdditionalSaveData(tag);
		tag.putBoolean("isSitting", this.isSitting());
		tag.putBoolean("isInverted", this.isInverted());
	}

	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(SITTING, false);
		this.entityData.define(FLYING, false);
		this.entityData.define(INVERTED, false);
	}
	
// Entity booleans:

	@Override
	public boolean rideableUnderWater()
	{
		return true;
	}

	@Override
	public boolean canBreatheUnderwater()
	{
		return true;
	}

	public boolean isSitting()
	{
		return this.entityData.get(SITTING);
	}

	public boolean isFlying()
	{
		return entityData.get(FLYING);
	}
	
	public boolean isInverted()
	{
		return this.entityData.get(INVERTED);
	}
	
	public boolean isMoving()
	{
		double d0 = this.getX() - this.xo;
		double d1 = this.getZ() - this.zo;
		return d0 * d0 + d1 * d1 > (double) 2.5000003E-7F;
	}

	public boolean canBeLeashed(Player player)
	{
		return false;
	}

	@Override
	public boolean causeFallDamage(float p_148750_, float p_148751_, DamageSource p_148752_)
	{
		return false;
	}

	@Override
	protected void checkFallDamage(double p_27754_, boolean p_27755_, BlockState p_27756_, BlockPos p_27757_)
	{
	}

	public boolean shouldFly()
	{
		return isHighEnough(FLIGHT_THRESHOLD);
	}

	public boolean isHighEnough(float height)
	{
		var pointer = blockPosition().mutable().move(0, -1, 0);
		var min = level.dimensionType().minY();
		var i = 0;

		while (i <= height && pointer.getY() > min && !level.getBlockState(pointer).getMaterial().isSolid())
			pointer.setY(getBlockY() - ++i);

		return i >= height;
	}

// Sound-controlling methods:

	@Override
	public int getAmbientSoundInterval()
	{
		return 300;
	}

	protected SoundEvent getStepSound()
	{
		int select = (int) Math.floor(Math.random() * 3);

		return select == 0 ? FFSounds.CLOUD_RAY_STEP1.get()
				: select == 1 ? FFSounds.CLOUD_RAY_STEP2.get() : FFSounds.CLOUD_RAY_STEP3.get();
	}

	protected void playStepSound(BlockPos position, BlockState blockState)
	{
		this.playSound(this.getStepSound(), 0.3F, 1.0F);
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		if (this.isOnGround())
		{
			return FFSounds.CLOUD_RAY_IDLE1.get();
		}
		
		return this.random.nextInt(4) == 0 ? FFSounds.CLOUD_RAY_IDLE2.get() : FFSounds.CLOUD_RAY_IDLE3.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource attack)
	{
		return FFSounds.CLOUD_RAY_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return FFSounds.CLOUD_RAY_DEATH.get();
	}

	protected float getSoundVolume()
	{
		return 0.3f;
	}

// Control utilities:

	public void setSitting(boolean sitting)
	{
		this.entityData.set(SITTING, sitting);
		this.setOrderedToSit(sitting);
	}

	public void setFlying(boolean flying)
	{
		this.entityData.set(FLYING, flying);
	}
	
	public void setInverted(boolean inverted)
	{
		this.entityData.set(INVERTED, inverted);
	}

	@Override
	public Team getTeam()
	{
		return super.getTeam();
	}

	@Override
	public boolean canBeControlledByRider()
	{
		return this.getControllingPassenger() instanceof LivingEntity;
	}

	@Nullable
	@Override
	public Entity getControllingPassenger()
	{
		return this.getFirstPassenger();
	}

	@Override
	public boolean isFood(ItemStack stack)
	{
		return stack.is(FOOD_ITEM);
	}

	public boolean isTamedFor(Player player)
	{
		return isTame() && isOwnedBy(player);
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);

		InteractionResult stackResult = stack.interactLivingEntity(player, this, hand);
		if (stackResult.consumesAction())
			return stackResult;

		final InteractionResult SUCCESS = InteractionResult.sidedSuccess(this.level.isClientSide);

		// tame
		if (!isTame())
		{
			if (stack.is(TAME_ITEM))
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
					this.setOrderedToSit(true);
					this.level.broadcastEntityEvent(this, (byte) 7);
				}
				else
				{
					this.level.broadcastEntityEvent(this, (byte) 6);
				}

				return InteractionResult.SUCCESS;
			}
		}

		// heal
		if (getHealth() < getAttribute(Attributes.MAX_HEALTH).getValue() && stack.is(FOOD_ITEM))
		{
			heal(5);
			playSound(getEatingSound(stack), 0.7f, 1);
			stack.shrink(1);

			return SUCCESS;
		}

		// sit
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

		// ride on
		if (isTamedFor(player) && (!isFood(stack) || getHealth() < getAttribute(Attributes.MAX_HEALTH).getValue()))
		{
			if (!this.level.isClientSide)
			{
				setRidingPlayer(player);
				navigation.stop();
				setTarget(null);
			}
			
			return SUCCESS;
		}

		return super.mobInteract(player, hand);
	}

	@Override
	public void travel(Vec3 vec3)
	{
		float speed = (float) getAttributeValue(isFlying() ? Attributes.FLYING_SPEED : Attributes.MOVEMENT_SPEED) * 0.25f;

		if (canBeControlledByRider())
		{
			LivingEntity driver = (LivingEntity) getControllingPassenger();
			double moveSideways = vec3.x;
			double verticalMove = vec3.y;
			double forwardMove = Math.min(Math.abs(driver.zza) + Math.abs(driver.xxa), 1);

			// rotate head to match driver
			float yaw = driver.yHeadRot;
			if (forwardMove > 0) // rotate in the direction of the drivers controls
				yaw += (float) (Mth.atan2(driver.zza, driver.xxa) * (180f / (float) Math.PI) - 90);
			yHeadRot = yaw;

			// rotate body towards the head
			setYRot(Mth.rotateIfNecessary(yHeadRot, getYRot(), 8));
			setXRot(driver.getXRot());

			if (isControlledByLocalInstance()) // Client applies motion
			{
				if (isFlying())
				{
					forwardMove = forwardMove > 0 ? forwardMove : 0;
					if (FFKeys.ascend.isDown())
						verticalMove += 0.6;
					if (FFKeys.descend.isDown())
						verticalMove += -0.6;
					if (forwardMove > 0)
						verticalMove += -driver.getXRot() * (Math.PI / 180);
				}
				else if (FFKeys.ascend.isDown()) jumpFromGround();

				vec3 = new Vec3(moveSideways, verticalMove, forwardMove);
				setSpeed(speed);
			}
			else if (driver instanceof Player)
			{
				calculateEntityAnimation(this, true);
				setDeltaMovement(Vec3.ZERO);
				return;
			}
		}

		if (isFlying())
		{
			// allows motion
			moveRelative(speed, vec3);
			move(MoverType.SELF, getDeltaMovement());

			// impose speed limit, and acceleration/deceleration
			setDeltaMovement(getDeltaMovement().scale(0.9f));

			calculateEntityAnimation(this, true);
		}
		else super.travel(vec3);
	}

	@Override
	public void tick()
	{
		super.tick();

		if (!this.level.isClientSide)
		{
			// update flying state based on the distance to the ground
			boolean flying = shouldFly();
			if (flying != isFlying())
			{
				// notify client
				setFlying(flying);

				// update AI follow range (needs to be updated before creating
				// new PathNavigate!)
				getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(flying ? FOLLOW_RANGE_FLYING : FOLLOW_RANGE);

				// update pathfinding method
				if (flying)
					navigation = new FlyingPathNavigation(this, level);
				else
					navigation = new GroundPathNavigation(this, level);
			}
		}
	}

	public void setRidingPlayer(Player player) 
	{
		player.setYRot(getYRot());
		player.setXRot(getXRot());
		player.startRiding(this);
	}

	@Override
	public void positionRider(Entity rider)
	{
		LivingEntity driver = (LivingEntity) rider;
		
		if (this.hasPassenger(rider))
		{
			float posOffset = 0;
			double zOffset = 0.5;
			
			//System.out.print(animTickCounter + "\n");

			Vec3 pos = new Vec3(0, (getPassengersRidingOffset() + rider.getMyRidingOffset()), getScale() - zOffset)
					.yRot((float) Math.toRadians(-yBodyRot)).add(position());
			rider.setPos(pos.x, pos.y + posOffset, pos.z);

			// fix rider rotation
			if (rider instanceof LivingEntity)
			{
				driver.xRotO = driver.getXRot();
				driver.yRotO = driver.getYRot();
				driver.yBodyRot = yBodyRot;
			}
		}
	}

	@Override
	public double getPassengersRidingOffset()
	{
		if(isFlying()) return ((double) this.getDimensions(this.getPose()).height * 0.6) - 0.1 * Math.sin(animTickCounter * Math.PI / 100);
		return ((double) this.getDimensions(this.getPose()).height * 0.6);
	}

	@Override
	public void onPassengerTurned(Entity rider)
	{
		rider.setYBodyRot(this.getYRot());
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob cloudRay)
	{
		return null;
	}

	@Override
	protected int getExperienceReward(Player player)
	{
		return 12 + this.level.random.nextInt(5);
	}

	@Override
	public float getWalkTargetValue(BlockPos pos, LevelReader level)
	{
		return 10.0F;
	}

	@Override
	protected PathNavigation createNavigation(Level level)
	{
		FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level);
		flyingpathnavigation.setCanOpenDoors(false);
		flyingpathnavigation.setCanFloat(false);
		flyingpathnavigation.setCanPassDoors(false);
		return flyingpathnavigation;
	}

////////////////////////////////
// Entity AI control classes: //
////////////////////////////////

	static class CloudRayLookControl extends LookControl
	{
		private final CloudRayEntity cloudRay;

		public CloudRayLookControl(CloudRayEntity entity)
		{
			super(entity);
			cloudRay = entity;
		}

		@Override
		public void tick()
		{
			if (cloudRay.yBodyRot != cloudRay.getYHeadRot())
			{
				cloudRay.yHeadRot = Mth.rotLerp(0.05F, cloudRay.getYHeadRot(), cloudRay.yBodyRot);
			}
		}
	}

	static class CloudRayBodyRotationControl extends BodyRotationControl
	{
		private final CloudRayEntity cloudRay;
		private int headStableTime;
		private float lastStableYHeadRot;

		public CloudRayBodyRotationControl(CloudRayEntity entity)
		{
			super(entity);
			cloudRay = entity;
		}

		@Override
		public void clientTick()
		{
			if (cloudRay.isMoving())
			{
				cloudRay.yBodyRot = Mth.rotLerp(0.05F, cloudRay.yBodyRot, cloudRay.getYRot());
				rotateHeadIfNecessary();
				lastStableYHeadRot = cloudRay.yHeadRot;
				headStableTime = 0;
			}
			else
			{
				if (notCarryingMobPassengers() && cloudRay.isFlying())
				{
					if (Math.abs(cloudRay.yHeadRot - lastStableYHeadRot) > 15.0F)
					{
						headStableTime = 0;
						lastStableYHeadRot = cloudRay.yHeadRot;
						rotateHeadIfNecessary();
					}
					else
					{
						++headStableTime;
						if (headStableTime > 10)
						{
							rotateHeadTowardsFront();
						}
					}
				}
			}
		}

		private void rotateHeadIfNecessary()
		{
			cloudRay.yHeadRot = Mth.rotLerp(0.05F, cloudRay.yHeadRot, cloudRay.yBodyRot);
		}

		private void rotateHeadTowardsFront()
		{
			cloudRay.yHeadRot = Mth.rotLerp(0.05F, cloudRay.yHeadRot, cloudRay.yBodyRot);
		}

		private boolean notCarryingMobPassengers()
		{
			return !(cloudRay.getFirstPassenger() instanceof Mob);
		}
	}

	static class CloudRayWanderGoal extends Goal {
		private final CloudRayEntity cloudRay;

		CloudRayWanderGoal(CloudRayEntity entity) {
			setFlags(EnumSet.of(Goal.Flag.MOVE));
			cloudRay = entity;
		}

		@Override
		public boolean canUse() {
			return cloudRay.navigation.isDone() && cloudRay.random.nextInt(3) == 0
					&& !cloudRay.isVehicle();
		}

		@Override
		public boolean canContinueToUse() {
			return cloudRay.navigation.isInProgress() && !cloudRay.isVehicle();
		}

		@Override
		public void start() {
			Vec3 vec3 = this.findPos();
			if (vec3 != null) {
				cloudRay.navigation.moveTo(cloudRay.navigation.createPath(new BlockPos(vec3), 3), 1.0D);
			}
		}

		@Override
		public void stop() {
			cloudRay.navigation.stop();
		}

		@Nullable
		private Vec3 findPos() {
			Vec3 vec3 = cloudRay.getViewVector(0.5F);

			Vec3 vec32 = HoverRandomPos.getPos(cloudRay, 20, 20, vec3.x, vec3.z, (float) Math.PI, 50, 15);
			vec32 = vec32 != null ? vec32
					: AirAndWaterRandomPos.getPos(cloudRay, 20, 20, -2, vec3.x, vec3.z, ((float) Math.PI));

			return vec32;
		}
	}
}
