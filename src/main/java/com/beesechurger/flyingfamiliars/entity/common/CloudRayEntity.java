package com.beesechurger.flyingfamiliars.entity.common;

import com.beesechurger.flyingfamiliars.effect.FFEffects;
import com.beesechurger.flyingfamiliars.entity.ai.FamiliarBodyRotationControl;
import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarFollowOwnerGoal;
import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarLandGoal;
import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarWanderGoal;
import com.beesechurger.flyingfamiliars.keys.FFKeys;
import com.beesechurger.flyingfamiliars.sound.FFSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
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

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.MOVE_CONTROL_FORWARD;

public class CloudRayEntity extends BaseFamiliarEntity implements IAnimatable
{
	public static final float MAX_HEALTH = 40.0f;
	public static final float FLYING_SPEED = 0.3f;

	private final AnimationFactory factory = new AnimationFactory(this);

	public CloudRayEntity(EntityType<CloudRayEntity> entityType, Level level)
	{
		super(entityType, level);
		resetActionTimerAmount = 400;
	}

	public static AttributeSupplier setAttributes()
	{
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, MAX_HEALTH)
				.add(Attributes.FLYING_SPEED, FLYING_SPEED).build();
	}

	@Override
	protected void registerGoals()
	{
		this.goalSelector.addGoal(0, new SitWhenOrderedToGoal(this));
		this.goalSelector.addGoal(1, new FamiliarFollowOwnerGoal(this, 0.75f, BEGIN_FOLLOW_DISTANCE, END_FOLLOW_DISTANCE));
		this.goalSelector.addGoal(2, new FamiliarLandGoal(this, 0.3f, 10));
		this.goalSelector.addGoal(3, new FamiliarWanderGoal(this, 0.5f));
		this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0f));
		this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
	}

	@Override
	protected BodyRotationControl createBodyControl()
	{
		return new FamiliarBodyRotationControl(this, getMoveControlType(), 30, 3.0f);
	}

	@Override
	public MobType getMobType()
	{
		return MobType.WATER;
	}

/////////////////////////////////
// GeckoLib animation control: //
/////////////////////////////////

	private <E extends IAnimatable> PlayState predicateGeneral(AnimationEvent<E> event)
	{
		if(this.isFlying())
		{
			if(FFKeys.FAMILIAR_ASCEND.isDown() && !FFKeys.FAMILIAR_DESCEND.isDown())
			{
				event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.cloud_ray.flying_ascend"));
			}
			else if(!FFKeys.FAMILIAR_ASCEND.isDown() && FFKeys.FAMILIAR_DESCEND.isDown())
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
		if(this.isOnGround())
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
	public String getMoveControlType()
	{
		return MOVE_CONTROL_FORWARD;
	}

// Booleans:

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

	@Override
	public boolean isInvulnerableTo(DamageSource source)
	{
		if(!level.isClientSide() && source.getEntity() instanceof Player player && player.isAutoSpinAttack())
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

	@Override
	public boolean canWalk()
	{
		return true;
	}

// Floats:

	@Override
	protected float getDrivingSpeedMod()
	{
		return 0.33f;
	}

/////////////
// Mob AI: //
/////////////

	@Override
	public void tick()
	{
		super.tick();

		if(!level.isClientSide() && !isSitting() && getOwner() != null && actionTimer == 0)
			if(distanceToSqr(getOwner()) < BEGIN_FOLLOW_DISTANCE * BEGIN_FOLLOW_DISTANCE)
			{
				if(getOwner().getEffect(FFEffects.DOUSED.get()) == null)
				{
					MobEffectInstance doused = new MobEffectInstance(FFEffects.DOUSED.get(), 1200);
					getOwner().addEffect(doused);

					resetActionTimer();
					level.playSound(null, getX(), getY(), getZ(), FFSounds.CLOUD_RAY_APPLY_DOUSED.get(), SoundSource.NEUTRAL, 0.5f + random.nextFloat(), 1.5f * FFSounds.getPitch());
				}
			}
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
