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
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class GriffonflyEntity extends AbstractFamiliarEntity implements IAnimatable
{
	private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(GriffonflyEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(GriffonflyEntity.class, EntityDataSerializers.BOOLEAN);

	public static final float MAX_HEALTH = 50.00f;
	public static final float FOLLOW_RANGE = 16;
	public static final float FOLLOW_RANGE_FLYING = FOLLOW_RANGE * 2;
	public static final float FLYING_SPEED = 0.4f;
	public static final float ATTACK_DAMAGE = 3.0f;
	public static final float ATTACK_SPEED = 2.0f;

	private final Item FOOD_ITEM = Items.APPLE;
	private final Item TAME_ITEM = Items.GLOW_BERRIES;

	private final AnimationFactory factory = new AnimationFactory(this);

	public GriffonflyEntity(EntityType<GriffonflyEntity> entityType, Level level)
	{
		super(entityType, level);
		this.moveControl = new FlyingMoveControl(this, 5, false);
		this.lookControl = new GriffonflyLookControl(this);
	}

	public static AttributeSupplier setAttributes()
	{
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, MAX_HEALTH)
				.add(Attributes.FOLLOW_RANGE, FOLLOW_RANGE)
				.add(Attributes.FLYING_SPEED, FLYING_SPEED)
				.add(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE)
				.add(Attributes.ATTACK_SPEED, ATTACK_SPEED).build();
	}

	@Override
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
		return new BodyRotationControl(this);
	}

// GeckoLib animation controls:
	
	private <E extends IAnimatable> PlayState antennaeController(AnimationEvent<E> event)
	{
		event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.griffonfly.antennae_idle", EDefaultLoopTypes.LOOP));

		return PlayState.CONTINUE;
	}
	
	private <E extends IAnimatable> PlayState legsController(AnimationEvent<E> event)
	{
		if(this.isMoving() || this.isFlying())
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.griffonfly.legs_flying", EDefaultLoopTypes.LOOP));
		}
		else
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.griffonfly.legs_idle", EDefaultLoopTypes.LOOP));
		}

		event.getController().setAnimationSpeed(0.8f);
		return PlayState.CONTINUE;
	}
	
	private <E extends IAnimatable> PlayState tailController(AnimationEvent<E> event)
	{
		event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.griffonfly.tail_idle", EDefaultLoopTypes.LOOP));
		event.getController().setAnimationSpeed(0.6f);

		return PlayState.CONTINUE;
	}
	
	private <E extends IAnimatable> PlayState wingsController(AnimationEvent<E> event)
	{
		if(this.isMoving() || this.isFlying())
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.griffonfly.wings_flying", EDefaultLoopTypes.LOOP));
			
			double speed = 1;
			if(FFKeys.ascend.isDown()) speed += 0.2;
			if(FFKeys.descend.isDown()) speed -= 0.2;
			
			event.getController().setAnimationSpeed(speed);
		}
		else
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.griffonfly.wings_idle", EDefaultLoopTypes.LOOP));
			event.getController().setAnimationSpeed(0.8f);
		}

		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimationData data)
	{
		data.addAnimationController(new AnimationController<>(this, "antennaeController", 0, this::antennaeController));
		data.addAnimationController(new AnimationController<>(this, "legsController", 2, this::legsController));
		data.addAnimationController(new AnimationController<>(this, "tailController", 0, this::tailController));
		data.addAnimationController(new AnimationController<>(this, "wingsController", 3, this::wingsController));
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

// Sound-controlling methods:

	@Override
	public int getAmbientSoundInterval()
	{
		return 200;
	}

	@Override
	protected void playStepSound(BlockPos position, BlockState blockState)
	{
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		if (this.isFlying())
		{
			int select = (int) Math.floor(Math.random() * 3);

			return select == 0 ? FFSounds.GRIFFONFLY_FLAP1.get()
					: select == 1 ? FFSounds.GRIFFONFLY_FLAP2.get() : FFSounds.GRIFFONFLY_FLAP3.get();
		}
		
		int select = (int) Math.floor(Math.random() * 2);

		return select == 0 ? FFSounds.GRIFFONFLY_CHITTER1.get() : FFSounds.GRIFFONFLY_CHITTER2.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource attack)
	{
		return FFSounds.GRIFFONFLY_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return FFSounds.GRIFFONFLY_DEATH.get();
	}

	@Override
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

	@Override
	public boolean isFood(ItemStack stack)
	{
		return stack.is(FOOD_ITEM);
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
		float speed = (float) getAttributeValue(Attributes.FLYING_SPEED) * 0.25f;

		if(canBeControlledByRider())
		{
			LivingEntity driver = (LivingEntity) getControllingPassenger();
			this.setYRot(driver.getYRot());
			this.yRotO = this.getYRot();
			this.setXRot(driver.getXRot() * 0.5F);
            this.setRot(this.getYRot(), this.getXRot());
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.getYRot();

			if(isControlledByLocalInstance())
			{
				if(!isFlying() && FFKeys.ascend.isDown()) jumpFromGround();

				vec3 = getHoverVector(vec3, driver);
				setSpeed(speed);
			}
			else if (driver instanceof Player)
			{
				calculateEntityAnimation(this, true);
				setDeltaMovement(Vec3.ZERO);
				return;
			}
		}

		if(isFlying())
		{
			// allows motion
			moveRelative(speed, vec3);
			move(MoverType.SELF, getDeltaMovement());

			// Decelerate after not moving
			setDeltaMovement(getDeltaMovement().scale(0.9f));

			calculateEntityAnimation(this, true);
		}
		else super.travel(vec3);
	}

	@Override
	public void tick()
	{
		super.tick();

		if(!this.level.isClientSide)
		{
			// update flying state based on the distance to the ground
			boolean flying = shouldFly();
			if(flying != isFlying())
			{
				// notify client
				setFlying(flying);

				// update AI follow range (needs to be updated before creating
				// new PathNavigate!)
				getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(flying ? FOLLOW_RANGE_FLYING : FOLLOW_RANGE);

				// update pathfinding method
				if(flying)
					navigation = new FlyingPathNavigation(this, level);
				else
					navigation = new GroundPathNavigation(this, level);
			}
		}
	}
	
	@Override
	public void positionRider(Entity rider)
	{
		LivingEntity driver = (LivingEntity) rider;

		if (this.hasPassenger(rider))
		{
			float posOffset = 0;
			double zOffset = 1;

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
		return (this.getDimensions(this.getPose()).height * 0.6);
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob griffonfly)
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

	static class GriffonflyLookControl extends LookControl
	{
		private final GriffonflyEntity griffonfly;

		public GriffonflyLookControl(GriffonflyEntity entity)
		{
			super(entity);
			griffonfly = entity;
		}

		@Override
		public void tick()
		{
			if (griffonfly.yBodyRot != griffonfly.getYHeadRot())
			{
				griffonfly.yHeadRot = Mth.rotLerp(0.05F, griffonfly.getYHeadRot(), griffonfly.yBodyRot);
			}
		}
	}

	static class GriffonflyWanderGoal extends Goal
	{
		private final GriffonflyEntity griffonfly;

		GriffonflyWanderGoal(GriffonflyEntity entity)
		{
			setFlags(EnumSet.of(Goal.Flag.MOVE));
			griffonfly = entity;
		}

		@Override
		public boolean canUse()
		{
			return griffonfly.navigation.isDone() && griffonfly.random.nextInt(3) == 0
					&& !griffonfly.isVehicle();
		}

		@Override
		public boolean canContinueToUse()
		{
			return griffonfly.navigation.isInProgress() && !griffonfly.isVehicle();
		}

		@Override
		public void start()
		{
			Vec3 vec3 = this.findPos();
			if (vec3 != null)
			{
				griffonfly.navigation.moveTo(griffonfly.navigation.createPath(new BlockPos(vec3), 3), 1.0D);
			}
		}

		@Override
		public void stop()
		{
			griffonfly.navigation.stop();
		}

		@Nullable
		private Vec3 findPos()
		{
			Vec3 vec3 = griffonfly.getViewVector(0.5F);

			Vec3 vec32 = HoverRandomPos.getPos(griffonfly, 20, 20, vec3.x, vec3.z, (float) Math.PI, 50, 15);
			vec32 = vec32 != null ? vec32
					: AirAndWaterRandomPos.getPos(griffonfly, 20, 20, -2, vec3.x, vec3.z, ((float) Math.PI));

			return vec32;
		}
	}
}
