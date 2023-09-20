package com.beesechurger.flyingfamiliars.entity.custom;

import java.util.EnumSet;

import org.jetbrains.annotations.Nullable;

import com.beesechurger.flyingfamiliars.FFKeys;
import com.beesechurger.flyingfamiliars.sound.FFSounds;

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
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class CloudRayEntity extends AbstractFamiliarEntity implements IAnimatable
{
	private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(CloudRayEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(CloudRayEntity.class, EntityDataSerializers.BOOLEAN);

	public static final float MAX_HEALTH = 50.00f;
	public static final float FLYING_SPEED = 0.4f;
	public static final float MOVEMENT_SPEED = 0.3f;

	private final Item FOOD_ITEM = Items.APPLE;
	private final Item TAME_ITEM = Items.GLOW_BERRIES;

	private final AnimationFactory factory = new AnimationFactory(this);

	private int animTickCounter = 0;

	public CloudRayEntity(EntityType<CloudRayEntity> entityType, Level level)
	{
		super(entityType, level);
		this.moveControl = new FlyingMoveControl(this, 5, false);
		this.lookControl = new CloudRayLookControl(this);
	}

	public static AttributeSupplier setAttributes()
	{
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, MAX_HEALTH)
				.add(Attributes.FLYING_SPEED, FLYING_SPEED)
				.add(Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED).build();
	}

	@Override
	protected void registerGoals()
	{
		this.goalSelector.addGoal(0, new SitWhenOrderedToGoal(this));
		this.goalSelector.addGoal(1, new FamiliarFollowOwnerGoal(this, 0.75f, BEGIN_FOLLOW_DISTANCE, END_FOLLOW_DISTANCE));
		this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.00));
		this.goalSelector.addGoal(3, new FamiliarLandGoal(this, 0.3f, 10));
		this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0f));
		this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
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

		if(this.isFlying())
		{
			if(FFKeys.familiar_ascend.isDown() && !FFKeys.familiar_descend.isDown())
			{
				event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.cloud_ray.flying_ascend"));
			}
			else if(!FFKeys.familiar_ascend.isDown() && FFKeys.familiar_descend.isDown())
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
		if(getRandom().nextDouble(1) == 0)
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.cloud_ray.head_move", EDefaultLoopTypes.PLAY_ONCE));
		}

		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimationData data)
	{
		data.addAnimationController(new AnimationController<>(this, "controllerGeneral", 8, this::predicateGeneral));
		data.addAnimationController(new AnimationController<>(this, "controllerFins", 1, this::predicateFins));
		data.addAnimationController(new AnimationController<>(this, "controllerHead", 0, this::predicateHead));
	}

	@Override
	public AnimationFactory getFactory()
	{
		return this.factory;
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

	@Override
	protected boolean canAddPassenger(Entity rider)
	{
		return this.getPassengers().size() < 2;
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

	@Override
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

	@Override
	protected float getSoundVolume()
	{
		return 0.3f;
	}

// Control utilities:

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
		if (isTamedFor(player) && (!stack.is(FOOD_ITEM) || getHealth() < getAttribute(Attributes.MAX_HEALTH).getValue()))
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
					if (FFKeys.familiar_ascend.isDown())
						verticalMove += 0.6;
					if (FFKeys.familiar_descend.isDown())
						verticalMove += -0.6;
					if (forwardMove > 0)
						verticalMove += -driver.getXRot() * (Math.PI / 180);
				}
				else if (FFKeys.familiar_ascend.isDown()) jumpFromGround();

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

		if(isFlying())
			navigation = new FamiliarFlyingPathNavigation(this, level);
		else
			navigation = new GroundPathNavigation(this, level);
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

	static class CloudRayWanderGoal extends Goal
	{
		private final CloudRayEntity cloudRay;

		CloudRayWanderGoal(CloudRayEntity entity)
		{
			setFlags(EnumSet.of(Goal.Flag.MOVE));
			cloudRay = entity;
		}

		@Override
		public boolean canUse()
		{
			return cloudRay.navigation.isDone() && cloudRay.random.nextInt(3) == 0
					&& !cloudRay.isVehicle();
		}

		@Override
		public boolean canContinueToUse()
		{
			return cloudRay.navigation.isInProgress() && !cloudRay.isVehicle();
		}

		@Override
		public void start()
		{
			Vec3 vec3 = this.findPos();
			if (vec3 != null)
			{
				cloudRay.navigation.moveTo(cloudRay.navigation.createPath(new BlockPos(vec3), 3), 1.0D);
			}
		}

		@Override
		public void stop()
		{
			cloudRay.navigation.stop();
		}

		@Nullable
		private Vec3 findPos()
		{
			Vec3 vec3 = cloudRay.getViewVector(0.5F);

			Vec3 vec32 = HoverRandomPos.getPos(cloudRay, 20, 20, vec3.x, vec3.z, (float) Math.PI, 50, 15);
			vec32 = vec32 != null ? vec32
					: AirAndWaterRandomPos.getPos(cloudRay, 20, 20, -2, vec3.x, vec3.z, ((float) Math.PI));

			return vec32;
		}
	}
}
