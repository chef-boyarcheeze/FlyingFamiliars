package com.beesechurger.flyingfamiliars.entity.custom;

import com.beesechurger.flyingfamiliars.keys.FFKeys;
import com.beesechurger.flyingfamiliars.sound.FFSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
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

import java.util.List;

public class GriffonflyEntity extends BaseFamiliarEntity implements IAnimatable
{
	public static final float MAX_HEALTH = 20.00f;
	public static final float MOVEMENT_SPEED = 0.5f;
	public static final float ARMOR = 4.0f;

	private final Item FOOD_ITEM = Items.SPIDER_EYE;
	private final Item TAME_ITEM = Items.FERMENTED_SPIDER_EYE;

	private final AnimationFactory factory = new AnimationFactory(this);

	public GriffonflyEntity(EntityType<GriffonflyEntity> entityType, Level level)
	{
		super(entityType, level);
		selectVariant(this.random.nextInt(5));
	}

	public static AttributeSupplier setAttributes()
	{
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, MAX_HEALTH)
				.add(Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED)
				.add(Attributes.ARMOR, ARMOR).build();
	}

	@Override
	protected void registerGoals()
	{
		this.goalSelector.addGoal(0, new SitWhenOrderedToGoal(this));
		this.goalSelector.addGoal(1, new FamiliarFollowOwnerGoal(this, 0.75f, BEGIN_FOLLOW_DISTANCE, END_FOLLOW_DISTANCE));
		this.goalSelector.addGoal(2, new FamiliarLandGoal(this, 0.3f, 10));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0f));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
	}

	@Override
	protected BodyRotationControl createBodyControl()
	{
		return new FamiliarBodyRotationControl(this, "hover", 10, 2.0f);
	}

	private void selectVariant(int variant)
	{
		if(!hasVariant())
		{
            switch (variant) {
                case 0 -> setVariant("Yellow");
                case 1 -> setVariant("Green");
                case 2 -> setVariant("Blue");
                case 3 -> setVariant("Purple");
                case 4 -> setVariant("Red");
            }
		}
	}
	
	@Override
	public MobType getMobType()
	{
		return MobType.ARTHROPOD;
	}
	
// GeckoLib animation controls:
	
	private <E extends IAnimatable> PlayState antennaeController(AnimationEvent<E> event)
	{
		event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.griffonfly.antennae_idle", EDefaultLoopTypes.LOOP));

		return PlayState.CONTINUE;
	}
	
	private <E extends IAnimatable> PlayState legsController(AnimationEvent<E> event)
	{
		if(this.isCarryingMob())
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.griffonfly.legs_grab", EDefaultLoopTypes.LOOP));
		}
		else if(this.isFlying())
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
			
			double speed = 2.5;
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

// Entity booleans:

	@Override
	protected boolean canAddPassenger(Entity rider)
	{
		return this.getPassengers().size() < 2;
	}

	private boolean isCarryingMob()
	{
		for(Entity candidate : getPassengers())
		{
			if(candidate != getOwner() && candidate != getControllingPassenger())
				return true;
		}

		return false;
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
				resetFamiliarActionTimer();
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
		float drivingSpeedMod = 0.5f;

		if(canBeControlledByRider())
		{
			LivingEntity driver = (LivingEntity) getControllingPassenger();

			if(isControlledByLocalInstance())
			{
				if(isFlying())
					vec3 = getHoverVector(vec3, drivingSpeedMod, driver);
				else if(FFKeys.familiar_ascend.isDown())
					jumpFromGround();

				speed *= drivingSpeedMod;
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

	@Override
	public void tick()
	{
		super.tick();

		if(isFlying())
			navigation = createNavigation(level);

		if(!level.isClientSide())
		{
			if(familiarActionTimer == 0 && isOwnerDoingFamiliarAction() && isFlying())
			{
				if(pickUpMob())
					resetFamiliarActionTimer();
			}
			if(getControllingPassenger() == null && getPassengers().size() != 0)
			{
				this.ejectPassengers();
				level.playSound(null, getX(), getY(), getZ(), FFSounds.GRIFFONFLY_RELEASE.get(), SoundSource.NEUTRAL, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.4F);
			}
		}
	}

	private boolean pickUpMob()
	{
		List<Entity> list = this.level.getEntities(this, this.getBoundingBox().expandTowards(0, -this.getBbHeight(), 0));

		for(Entity candidate : list)
		{
			if(candidate != getOwner() && candidate != getControllingPassenger() && candidate instanceof LivingEntity)
			{
				// Pick up mob in bounding box (start riding this)
				if(getPassengers().size() <= 1)
				{
					candidate.startRiding(this);
					level.playSound(null, getX(), getY(), getZ(), FFSounds.GRIFFONFLY_GRAB.get(), SoundSource.NEUTRAL, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.4F);
					return true;
				}
				// Release carried mob
				else
				{
					for(Entity e : getPassengers())
					{
						if(candidate == e)
						{
							candidate.stopRiding();
							level.playSound(null, getX(), getY(), getZ(), FFSounds.GRIFFONFLY_RELEASE.get(), SoundSource.NEUTRAL, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.4F);
							return true;
						}
					}
				}
			}
		}

		return false;
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
		double y;
		double z = getScale() - 1;

		if(rider == getControllingPassenger())
			y = getPassengersRidingOffset() + rider.getMyRidingOffset();
		else
			y = -0.5 * (getPassengersRidingOffset() + rider.getMyRidingOffset() + rider.getBbHeight());

		return new Vec3(x, y, z);
	}
}
