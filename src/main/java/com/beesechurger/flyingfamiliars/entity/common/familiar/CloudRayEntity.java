package com.beesechurger.flyingfamiliars.entity.common.familiar;

import com.beesechurger.flyingfamiliars.entity.common.type.IAirEntity;
import com.beesechurger.flyingfamiliars.entity.common.type.IWaterEntity;
import com.beesechurger.flyingfamiliars.registries.FFEffects;
import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarFollowOwnerGoal;
import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarSitGoal;
import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarWanderGoal;
import com.beesechurger.flyingfamiliars.util.FFAnimationController;
import com.beesechurger.flyingfamiliars.registries.FFSounds;
import com.beesechurger.flyingfamiliars.util.FFEnumValues;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
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

import static com.beesechurger.flyingfamiliars.util.FFValueConstants.FLYING_SPEED;
import static com.beesechurger.flyingfamiliars.util.FFValueConstants.MOVEMENT_SPEED;

public class CloudRayEntity extends BaseFamiliarEntity implements IWaterEntity, IAirEntity
{
	protected static final float MAX_HEALTH = 40.0f;
	protected static final int FOLLOW_RANGE = 8;
	protected static final int VARIANTS = 3;

	public CloudRayEntity(EntityType<CloudRayEntity> entityType, Level level)
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
				.add(Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED).build();
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
				case 0 -> setVariant("white");
				case 1 -> setVariant("light_gray");
				case 2 -> setVariant("dark_gray");
			}
		}
	}

	@Override
	public MobType getMobType()
	{
		return MobType.WATER;
	}

/////////////////////////////////
// GeckoLib animation control: //
/////////////////////////////////

	private <E extends GeoAnimatable> PlayState finsController(AnimationState<E> event)
	{
		FFAnimationController controller = (FFAnimationController) event.getController();

		controller.setAnimation(RawAnimation.begin().thenLoop("animation.cloud_ray.fins_idle"));

		return PlayState.CONTINUE;
	}

	private <E extends GeoAnimatable> PlayState mouthController(AnimationState<E> event)
	{
		FFAnimationController controller = (FFAnimationController) event.getController();

		controller.setAnimation(RawAnimation.begin().thenLoop("animation.cloud_ray.mouth_idle"));

		return PlayState.CONTINUE;
	}

	private <E extends GeoAnimatable> PlayState bodyController(AnimationState<E> event)
	{
		FFAnimationController controller = (FFAnimationController) event.getController();

		if(isFlying())
			event.getController().setAnimation(RawAnimation.begin()
					.thenLoop("animation.cloud_ray.body_flapping"));
		else if(!isFlying() && isMoving())
			event.getController().setAnimation(RawAnimation.begin()
					.thenLoop("animation.cloud_ray.body_walking"));
		else
			event.getController().setAnimation(RawAnimation.begin()
					.thenLoop("animation.cloud_ray.body_idle"));

		controller.setAnimationSpeed(0.6d);

		return PlayState.CONTINUE;
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar data)
	{
		FFAnimationController finsController = new FFAnimationController<>(this, "finsController", 0, 0, this::finsController);
		FFAnimationController mouthController = new FFAnimationController<>(this, "mouthController", 0, 0, this::mouthController);
		FFAnimationController bodyController = new FFAnimationController<>(this, "bodyController", 5, 0, this::bodyController);

		data.add(finsController);
		data.add(mouthController);
		data.add(bodyController);

		animationControllers.add(finsController);
		animationControllers.add(mouthController);
		animationControllers.add(bodyController);
	}

////////////////////
// Sound control: //
////////////////////

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
		if(onGround())
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

///////////////////////
// Entity accessors: //
///////////////////////

// Strings:

	@Override
	public FFEnumValues.FamiliarMoveTypes getMoveControlType()
	{
		return FFEnumValues.FamiliarMoveTypes.FORWARD;
	}

// Booleans:

	@Override
	public boolean dismountsUnderwater()
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

	@Override
	public boolean isInvulnerableTo(DamageSource source)
	{
		if(!level().isClientSide() && source.getEntity() instanceof Player player && player.isAutoSpinAttack())
			return true;

		return super.isInvulnerableTo(source);
	}

	@Override
	public boolean isTameItem(ItemStack stack)
	{
		return stack.is(Items.GLOW_BERRIES);
	}

	@Override
	public boolean isFoodItem(ItemStack stack)
	{
		return isTameItem(stack) || stack.is(Items.SWEET_BERRIES) || stack.is(Items.APPLE) || stack.is(Items.GOLDEN_APPLE) || stack.is(Items.MELON_SLICE);
	}

// Integers:

	@Override
	public int getActionCooldownMax()
	{
		return 400;
	}

// Doubles:

	@Override
	public double getFlySpeedMod()
	{
		return getControllingPassenger() == null ? 3d : 1d;
	}

	@Override
	public double getWalkSpeedMod()
	{
		return getControllingPassenger() == null ? 3.5d : 0.4d;
	}

	@Override
	public double getBodyRotationAngleLimit()
	{
		return 30d;
	}

	@Override
	public double getBodyRotationAngleInterval()
	{
		return getBodyRotationAngleLimit() / 15d;
	}

/////////////
// Mob AI: //
/////////////

	@Override
	public void tick()
	{
		super.tick();

		if(!level().isClientSide() && !isSitting() && getOwner() != null && actionCooldown == 0)
			if(distanceToSqr(getOwner()) < FOLLOW_RANGE * FOLLOW_RANGE)
			{
				if(getOwner().getEffect(FFEffects.DOUSED.get()) == null)
				{
					MobEffectInstance doused = new MobEffectInstance(FFEffects.DOUSED.get(), 1200);
					getOwner().addEffect(doused);

					resetActionCooldown();
					level().playSound(null, getX(), getY(), getZ(), FFSounds.CLOUD_RAY_APPLY_DOUSED.get(), SoundSource.NEUTRAL, 0.5f + random.nextFloat(), 1.5f * FFSounds.getPitch());
				}
			}
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
