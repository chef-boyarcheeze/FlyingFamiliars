package com.beesechurger.flyingfamiliars.entity.common;

import com.beesechurger.flyingfamiliars.keys.FFKeys;
import com.beesechurger.flyingfamiliars.sound.FFSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.MOVE_CONTROL_HOVER;
import static com.beesechurger.flyingfamiliars.util.FFStringConstants.MOVE_CONTROL_NONE;

public class CormorantEntity extends BaseFamiliarEntity implements IAnimatable
{
    public static final float MAX_HEALTH = 8.00f;
    public static final float MOVEMENT_SPEED = 0.4f;

    private final AnimationFactory factory = new AnimationFactory(this);

    public CormorantEntity(EntityType<CormorantEntity> entityType, Level level)
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
        this.goalSelector.addGoal(2, new FamiliarLandGoal(this, 0.3f, 10));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    @Override
    protected BodyRotationControl createBodyControl()
    {
        return new FamiliarBodyRotationControl(this, MOVE_CONTROL_NONE, 0, 0);
    }

    private void selectVariant(int variant)
    {
        /*if(!hasVariant())
        {
            switch (variant) {
                case 0 -> setVariant("Yellow");
                case 1 -> setVariant("Green");
                case 2 -> setVariant("Blue");
                case 3 -> setVariant("Purple");
                case 4 -> setVariant("Red");
            }
        }*/
    }

    @Override
    public MobType getMobType()
    {
        return MobType.WATER;
    }

// Geckolib animation controls:

    private <E extends IAnimatable> PlayState generalController(AnimationEvent<E> event)
    {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.cormorant.wings_idle", ILoopType.EDefaultLoopTypes.LOOP));

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data)
    {
        data.addAnimationController(new AnimationController<>(this, "generalController", 0, this::generalController));
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
        return random.nextInt(2) == 0 ? FFSounds.CORMORANT_SQUAWK1.get() : FFSounds.CORMORANT_SQUAWK2.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource attack)
    {
        return SoundEvents.PARROT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.PARROT_DEATH;
    }

// Entity booleans:

    @Override
    public boolean canOwnerRide()
    {
        return false;
    }

    @Override
    public boolean isTameItem(ItemStack stack)
    {
        return stack.is(Items.COD) || stack.is(Items.SALMON) || stack.is(Items.TROPICAL_FISH);
    }

    @Override
    public boolean isFoodItem(ItemStack stack)
    {
        return isTameItem(stack);
    }

// Mob AI methods:

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
                else if(FFKeys.FAMILIAR_ASCEND.isDown())
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
    }
}
