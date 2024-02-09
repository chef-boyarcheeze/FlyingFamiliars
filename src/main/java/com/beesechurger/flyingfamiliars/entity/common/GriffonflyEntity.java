package com.beesechurger.flyingfamiliars.entity.common;

import com.beesechurger.flyingfamiliars.entity.ai.FamiliarBodyRotationControl;
import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarFollowOwnerGoal;
import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarSitGoal;
import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarWanderGoal;
import com.beesechurger.flyingfamiliars.sound.FFSounds;
import com.beesechurger.flyingfamiliars.util.FFEnumValues;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
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

import static com.beesechurger.flyingfamiliars.util.FFValueConstants.FLYING_SPEED;
import static com.beesechurger.flyingfamiliars.util.FFValueConstants.MOVEMENT_SPEED;

public class GriffonflyEntity extends BaseFamiliarEntity
{
	protected static final float MAX_HEALTH = 20.00f;
	protected static final int FOLLOW_RANGE = 6;
	protected static final float ARMOR = 4.0f;
	protected static final int VARIANTS = 5;

	private final AnimationFactory factory = new AnimationFactory(this);

	public GriffonflyEntity(EntityType<GriffonflyEntity> entityType, Level level)
	{
		super(entityType, level);
		selectVariant(this.random.nextInt(VARIANTS));
	}

	public static AttributeSupplier setAttributes()
	{
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, MAX_HEALTH)
				.add(Attributes.FOLLOW_RANGE, FOLLOW_RANGE)
				.add(Attributes.FLYING_SPEED, FLYING_SPEED)
				.add(Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED)
				.add(Attributes.ARMOR, ARMOR).build();
	}

	@Override
	protected void registerGoals()
	{
		this.goalSelector.addGoal(0, new FamiliarSitGoal(this, 0.5d));
		this.goalSelector.addGoal(1, new FamiliarFollowOwnerGoal(this, 1.0d));
		this.goalSelector.addGoal(2, new FamiliarWanderGoal(this, 0.75d));
		this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0f));
		this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
	}

	@Override
	protected BodyRotationControl createBodyControl()
	{
		return new FamiliarBodyRotationControl(this, getMoveControlType(), 15, 1.5f);
	}

	private void selectVariant(int variant)
	{
		if(!hasVariant())
		{
            switch(variant)
			{
                case 0 -> setVariant("yellow");
                case 1 -> setVariant("green");
                case 2 -> setVariant("blue");
                case 3 -> setVariant("purple");
                case 4 -> setVariant("red");
            }
		}
	}
	
	@Override
	public MobType getMobType()
	{
		return MobType.ARTHROPOD;
	}

/////////////////////////////////
// GeckoLib animation control: //
/////////////////////////////////
	
	private <E extends IAnimatable> PlayState headController(AnimationEvent<E> event)
	{
		event.getController().setAnimation(new AnimationBuilder()
				.addAnimation("animation.griffonfly.head_idle", EDefaultLoopTypes.LOOP));

		return PlayState.CONTINUE;
	}
	
	private <E extends IAnimatable> PlayState legsController(AnimationEvent<E> event)
	{
		if(this.isCarryingMob())
			event.getController().setAnimation(new AnimationBuilder()
					.addAnimation("animation.griffonfly.legs_grab", EDefaultLoopTypes.LOOP));
		else if(this.isFlying())
			event.getController().setAnimation(new AnimationBuilder()
					.addAnimation("animation.griffonfly.legs_flying", EDefaultLoopTypes.LOOP));
		else if(!this.isFlying() && this.isMoving())
			event.getController().setAnimation(new AnimationBuilder()
					.addAnimation("animation.griffonfly.legs_walking", EDefaultLoopTypes.LOOP));
		else
			event.getController().setAnimation(new AnimationBuilder()
					.addAnimation("animation.griffonfly.legs_idle", EDefaultLoopTypes.LOOP));

		//event.getController().setAnimationSpeed(0.8f);
		return PlayState.CONTINUE;
	}
	
	private <E extends IAnimatable> PlayState wingsController(AnimationEvent<E> event)
	{
		if(this.isFlying())
		{
			event.getController().setAnimation(new AnimationBuilder()
					.addAnimation("animation.griffonfly.wings_flying", EDefaultLoopTypes.LOOP));
			event.getController().setAnimationSpeed(2.5f);
		}
		else if(!this.isFlying() && this.isMoving())
		{
			event.getController().setAnimation(new AnimationBuilder()
					.addAnimation("animation.griffonfly.wings_walking", EDefaultLoopTypes.LOOP));
			event.getController().setAnimationSpeed(1.0f);
		}
		else
		{
			event.getController().setAnimation(new AnimationBuilder()
					.addAnimation("animation.griffonfly.wings_idle", EDefaultLoopTypes.LOOP));
			event.getController().setAnimationSpeed(0.8f);
		}

		return PlayState.CONTINUE;
	}

	private <E extends IAnimatable> PlayState bodyController(AnimationEvent<E> event)
	{
		if(this.isFlying())
			event.getController().setAnimation(new AnimationBuilder()
					.addAnimation("animation.griffonfly.body_flying", EDefaultLoopTypes.LOOP));
		else if(!this.isFlying() && this.isMoving())
			event.getController().setAnimation(new AnimationBuilder()
					.addAnimation("animation.griffonfly.body_walking", EDefaultLoopTypes.LOOP));
		else
			event.getController().setAnimation(new AnimationBuilder()
					.addAnimation("animation.griffonfly.body_idle", EDefaultLoopTypes.LOOP));

		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimationData data)
	{
		data.addAnimationController(new AnimationController<>(this, "headController", 0, this::headController));
		data.addAnimationController(new AnimationController<>(this, "legsController", 5, this::legsController));
		data.addAnimationController(new AnimationController<>(this, "wingsController", 2, this::wingsController));
		data.addAnimationController(new AnimationController<>(this, "bodyController", 5, this::bodyController));
	}

	@Override
	public AnimationFactory getFactory()
	{
		return this.factory;
	}

////////////////////
// Sound control: //
////////////////////

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

///////////////////////
// Entity accessors: //
///////////////////////

// Enums:

	@Override
	public FFEnumValues.FamiliarMoveTypes getMoveControlType()
	{
		return FFEnumValues.FamiliarMoveTypes.HOVER;
	}

// Booleans:

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

	@Override
	public boolean isTameItem(ItemStack stack)
	{
		return stack.is(Items.FERMENTED_SPIDER_EYE);
	}

	@Override
	public boolean isFoodItem(ItemStack stack)
	{
		return stack.is(Items.SPIDER_EYE);
	}

	@Override
	public boolean canWalk()
	{
		return false;
	}

// Doubles:

	@Override
	public double getFlySpeedMod()
	{
		return getControllingPassenger() == null ? 5d : 1.5d;
	}

	@Override
	public double getWalkSpeedMod()
	{
		return getControllingPassenger() == null ? 3d : 0.3d;
	}

/////////////
// Mob AI: //
/////////////

	@Override
	public void tick()
	{
		super.tick();

		if(!level.isClientSide())
		{
			if(actionCooldown == 0 && isOwnerDoingFamiliarAction() && isFlying())
			{
				if(pickUpMob())
					resetActionCooldown();
			}
			if(getControllingPassenger() == null && getPassengers().size() != 0)
			{
				this.ejectPassengers();
				level.playSound(null, getX(), getY(), getZ(), FFSounds.GRIFFONFLY_RELEASE.get(), SoundSource.NEUTRAL, 0.5F + random.nextFloat(), 1.5f * FFSounds.getPitch());
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
					level.playSound(null, getX(), getY(), getZ(), FFSounds.GRIFFONFLY_GRAB.get(), SoundSource.NEUTRAL, 0.5F + random.nextFloat(), 1.5f * FFSounds.getPitch());
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
							level.playSound(null, getX(), getY(), getZ(), FFSounds.GRIFFONFLY_RELEASE.get(), SoundSource.NEUTRAL, 0.5F + random.nextFloat(), 1.5f * FFSounds.getPitch());
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

			// Set griffonfly carried mob's look direction to griffonfly's look direction
			if(!(rider instanceof Player))
				rider.setYHeadRot(yHeadRot);
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
