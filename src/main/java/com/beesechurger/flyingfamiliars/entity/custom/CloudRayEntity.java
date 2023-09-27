package com.beesechurger.flyingfamiliars.entity.custom;

import com.beesechurger.flyingfamiliars.keys.FFKeys;
import com.beesechurger.flyingfamiliars.sound.FFSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
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

public class CloudRayEntity extends BaseFamiliarEntity implements IAnimatable
{
	private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(CloudRayEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> FLYING = SynchedEntityData.defineId(CloudRayEntity.class, EntityDataSerializers.BOOLEAN);

	public static final float MAX_HEALTH = 40.0f;
	public static final float MOVEMENT_SPEED = 0.3f;

	private final Item FOOD_ITEM = Items.APPLE;
	private final Item TAME_ITEM = Items.GLOW_BERRIES;

	private final AnimationFactory factory = new AnimationFactory(this);

	public CloudRayEntity(EntityType<CloudRayEntity> entityType, Level level)
	{
		super(entityType, level);
	}

	public static AttributeSupplier setAttributes()
	{
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, MAX_HEALTH)
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
		return new FamiliarBodyRotationControl(this, "forward", 30, 3.0f);
	}

// GeckoLib animation controls:

	private <E extends IAnimatable> PlayState predicateGeneral(AnimationEvent<E> event)
	{
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
		float speed = (float) getAttributeValue(Attributes.MOVEMENT_SPEED);
		float drivingSpeedMod = 0.25f;

		if(canBeControlledByRider())
		{
			LivingEntity driver = (LivingEntity) getControllingPassenger();

			if(isControlledByLocalInstance())
			{
				if(!isFlying() && FFKeys.familiar_ascend.isDown())
					jumpFromGround();

				vec3 = getForwardVector(vec3, 2.5f * drivingSpeedMod, driver);

				if(vec3.z != 0)
					speed *= drivingSpeedMod;
				else if(vec3.y != 0)
					speed *= drivingSpeedMod;

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

		if(isFlying())
		{
			moveRelative(speed, vec3);
			move(MoverType.SELF, getDeltaMovement());

			setDeltaMovement(getDeltaMovement().scale(0.9f));
			calculateEntityAnimation(this, true);
		}
		else
		{
			setDeltaMovement(getDeltaMovement().scale(0.9f));
			super.travel(vec3);
		}
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

	@Override
	public void positionRider(Entity rider)
	{
		if(this.hasPassenger(rider))
		{
			rider.setPos(getRiderPosition(rider).yRot((float) Math.toRadians(-yBodyRot)).add(position()));

			rider.xRotO = rider.getXRot();
			rider.yRotO = rider.getYRot();
			rider.setYBodyRot(yBodyRot);
		}
	}

	public Vec3 getRiderPosition(Entity rider)
	{
		double x = 0;
		double y = getPassengersRidingOffset() + rider.getMyRidingOffset();
		double z = getScale() - 1;

		if(getPassengers().size() > 1)
		{
			if(rider == getControllingPassenger())
				x = 0.5f;
			else
				x = -0.5f;
		}

		return new Vec3(x, y, z);
	}
}
