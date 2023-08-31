package com.beesechurger.flyingfamiliars.entity.custom;

import com.beesechurger.flyingfamiliars.FFKeys;
import com.beesechurger.flyingfamiliars.sound.FFSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
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
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
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
	public static final float MAX_HEALTH = 20.00f;
	public static final float FOLLOW_RANGE = 32;
	public static final float FLYING_SPEED = 0.2f;
	public static final float ATTACK_DAMAGE = 3.0f;
	public static final float ATTACK_SPEED = 2.0f;

	private final Item FOOD_ITEM = Items.CHORUS_FRUIT;
	private final Item TAME_ITEM = Items.CHORUS_FLOWER;

	private final AnimationFactory factory = new AnimationFactory(this);

	public GriffonflyEntity(EntityType<GriffonflyEntity> entityType, Level level)
	{
		super(entityType, level);
		selectVariant(this.random.nextInt(5));
		moveControl = new FlyingMoveControl(this, 5, false);
		lookControl = new LookControl(this);
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

	
	private void selectVariant(int variant)
	{
		switch(variant)
		{
			case 0:
				setVariant("Yellow");
				break;
			case 1:
				setVariant("Green");
				break;
			case 2:
				setVariant("Blue");
				break;
			case 3:
				setVariant("Purple");
				break;
			case 4:
				setVariant("Red");
				break;
		}
	}
	
// GeckoLib animation controls:
	
	private <E extends IAnimatable> PlayState antennaeController(AnimationEvent<E> event)
	{
		event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.griffonfly.antennae_idle", EDefaultLoopTypes.LOOP));

		return PlayState.CONTINUE;
	}
	
	private <E extends IAnimatable> PlayState legsController(AnimationEvent<E> event)
	{
		if(this.isFlying())
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
		if(this.isFlying())
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.griffonfly.wings_flying", EDefaultLoopTypes.LOOP));
			
			double speed = 2;			
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
		data.addAnimationController(new AnimationController<>(this, "wingsController", 1, this::wingsController));
	}

	@Override
	public AnimationFactory getFactory()
	{
		return this.factory;
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
	
// Mob AI methods:

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
		float speed = (float) getAttributeValue(Attributes.FLYING_SPEED);

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
			}
			else if (driver instanceof Player)
			{
				setDeltaMovement(Vec3.ZERO);
				calculateEntityAnimation(this, true);
				return;
			}
		}
		else
		{
			if(!isFlying())
			{
				setDeltaMovement(Vec3.ZERO);
			}
		}

		if(isFlying())
		{
			// allows motion
			moveRelative(speed, vec3);
			move(MoverType.SELF, getDeltaMovement());

			// Decelerate after not moving
			setDeltaMovement(getDeltaMovement().scale(0.8f));
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
			boolean flying = shouldFly();
			if(flying != isFlying())
			{
				setFlying(flying);

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

		if(this.hasPassenger(rider))
		{			
			double x = 0;
			double y = getPassengersRidingOffset() + rider.getMyRidingOffset();
			double z = getScale() - 1;

			Vec3 pos = new Vec3(x, y, z).yRot((float) Math.toRadians(-yBodyRot)).add(position());
			rider.setPos(pos);

			// fix rider rotation
			if (rider instanceof LivingEntity)
			{
				driver.xRotO = driver.getXRot();
				driver.yRotO = driver.getYRot();
				driver.setYBodyRot(yBodyRot);
			}
		}
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
}
