package com.beesechurger.flyingfamiliars.entity.common.familiar;

import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarFollowOwnerGoal;
import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarSitGoal;
import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarWanderGoal;
import com.beesechurger.flyingfamiliars.entity.common.type.IEarthEntity;
import com.beesechurger.flyingfamiliars.entity.common.type.IPlantEntity;
import com.beesechurger.flyingfamiliars.entity.client.FFAnimationController;
import com.beesechurger.flyingfamiliars.registries.FFSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;

import static com.beesechurger.flyingfamiliars.util.FFConstants.BASE_FLYING_SPEED;
import static com.beesechurger.flyingfamiliars.util.FFConstants.BASE_MOVEMENT_SPEED;

public class GriffonflyEntity extends BaseFamiliarEntity implements IPlantEntity, IEarthEntity
{
	protected static final float MAX_HEALTH = 20.00f;
	protected static final int FOLLOW_RANGE = 6;
	protected static final float ARMOR = 4.0f;
	protected static final int VARIANTS = 5;

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
				.add(Attributes.FLYING_SPEED, BASE_FLYING_SPEED)
				.add(Attributes.MOVEMENT_SPEED, BASE_MOVEMENT_SPEED)
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
	
	private <E extends GeoAnimatable> PlayState headController(AnimationState<E> event)
	{
		FFAnimationController controller = (FFAnimationController) event.getController();

		controller.setAnimation(RawAnimation.begin()
				.thenLoop("animation.griffonfly.head_idle"));

		return PlayState.CONTINUE;
	}
	
	private <E extends GeoAnimatable> PlayState legsController(AnimationState<E> event)
	{
		FFAnimationController controller = (FFAnimationController) event.getController();

		if(this.isCarryingMob())
			controller.setAnimation(RawAnimation.begin()
					.thenLoop("animation.griffonfly.legs_grab"));
		else if(this.isFlying())
			controller.setAnimation(RawAnimation.begin()
					.thenLoop("animation.griffonfly.legs_flying"));
		else if(!this.isFlying() && this.isMoving())
			controller.setAnimation(RawAnimation.begin()
					.thenLoop("animation.griffonfly.legs_walking"));
		else
			controller.setAnimation(RawAnimation.begin()
					.thenLoop("animation.griffonfly.legs_idle"));

		//controller.setAnimationSpeed(0.8f);
		return PlayState.CONTINUE;
	}
	
	private <E extends GeoAnimatable> PlayState wingsController(AnimationState<E> event)
	{
		FFAnimationController controller = (FFAnimationController) event.getController();

		if(isFlying())
		{
			controller.setAnimation(RawAnimation.begin()
					.thenLoop("animation.griffonfly.wings_flying"));
			controller.setAnimationSpeed(2.5f);
		}
		else if(!isFlying() && isMoving())
		{
			controller.setAnimation(RawAnimation.begin()
					.thenLoop("animation.griffonfly.wings_walking"));
			controller.setAnimationSpeed(1.0f);
		}
		else
		{
			controller.setAnimation(RawAnimation.begin()
					.thenLoop("animation.griffonfly.wings_idle"));
			controller.setAnimationSpeed(0.8f);
		}

		return PlayState.CONTINUE;
	}

	private <E extends GeoAnimatable> PlayState bodyController(AnimationState<E> event)
	{
		FFAnimationController controller = (FFAnimationController) event.getController();

		if(isFlying())
			controller.setAnimation(RawAnimation.begin()
					.thenLoop("animation.griffonfly.body_flying"));
		else if(!isFlying() && isMoving())
			controller.setAnimation(RawAnimation.begin()
					.thenLoop("animation.griffonfly.body_walking"));
		else
			controller.setAnimation(RawAnimation.begin()
					.thenLoop("animation.griffonfly.body_idle"));

		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar data)
	{
		FFAnimationController headController = new FFAnimationController<>(this, "headController", 0, 0, this::headController);
		FFAnimationController legsController = new FFAnimationController<>(this, "legsController", 5, 0, this::legsController);
		FFAnimationController wingsController = new FFAnimationController<>(this, "wingsController", 2, 0, this::wingsController);
		FFAnimationController bodyController = new FFAnimationController<>(this, "bodyController", 5, 0, this::bodyController);

		data.add(headController);
		data.add(legsController);
		data.add(wingsController);
		data.add(bodyController);

		animationControllers.add(headController);
		animationControllers.add(legsController);
		animationControllers.add(wingsController);
		animationControllers.add(bodyController);
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
			return FFSounds.GRIFFONFLY_FLAP.get();
		else
			return FFSounds.GRIFFONFLY_CHITTER.get();
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
	public FamiliarMoveTypes getMoveControlType()
	{
		return FamiliarMoveTypes.HOVER;
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

	@Override
	public double getBodyRotationAngleLimit()
	{
		return 15d;
	}

/////////////
// Mob AI: //
/////////////

	@Override
	public void tick()
	{
		super.tick();

		if(!level().isClientSide())
		{
			if(actionCooldown == 0 && isOwnerDoingFamiliarAction() && isFlying())
			{
				if(pickUpMob())
					resetActionCooldown();
			}
			if(getControllingPassenger() == null && getPassengers().size() != 0)
			{
				ejectPassengers();
				level().playSound(null, getX(), getY(), getZ(), FFSounds.GRIFFONFLY_RELEASE.get(), SoundSource.NEUTRAL, 2f + random.nextFloat(), 1.5f * FFSounds.getPitch());
			}
		}
	}

	private boolean pickUpMob()
	{
		List<Entity> list = level().getEntities(this, this.getBoundingBox().expandTowards(0, -this.getBbHeight(), 0));

		for(Entity candidate : list)
		{
			if(candidate != getOwner() && candidate != getControllingPassenger() && candidate instanceof LivingEntity)
			{
				// Pick up mob in bounding box (start riding this)
				if(getPassengers().size() <= 1)
				{
					candidate.startRiding(this);
					level().playSound(null, getX(), getY(), getZ(), FFSounds.GRIFFONFLY_GRAB.get(), SoundSource.NEUTRAL, 2f + random.nextFloat(), 2f * FFSounds.getPitch());
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
							level().playSound(null, getX(), getY(), getZ(), FFSounds.GRIFFONFLY_RELEASE.get(), SoundSource.NEUTRAL, 2f + random.nextFloat(), 2f * FFSounds.getPitch());
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	@Override
	public void positionRider(Entity rider, MoveFunction function)
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
