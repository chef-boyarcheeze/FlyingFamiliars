package com.beesechurger.flyingfamiliars.entity.common.familiar;

import com.beesechurger.flyingfamiliars.entity.ai.FamiliarBodyRotationControl;
import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarFollowOwnerGoal;
import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarSitGoal;
import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarWanderGoal;
import com.beesechurger.flyingfamiliars.entity.common.type.IAirEntity;
import com.beesechurger.flyingfamiliars.entity.common.type.IEarthEntity;
import com.beesechurger.flyingfamiliars.util.FFAnimationController;
import com.beesechurger.flyingfamiliars.registries.FFSounds;
import com.beesechurger.flyingfamiliars.util.FFEnumValues;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
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

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.ANIMATION_EMPTY;
import static com.beesechurger.flyingfamiliars.util.FFValueConstants.BASE_FLYING_SPEED;
import static com.beesechurger.flyingfamiliars.util.FFValueConstants.BASE_MOVEMENT_SPEED;

public class MagicCarpetEntity extends BaseFamiliarEntity implements IEarthEntity, IAirEntity
{
    protected static final float MAX_HEALTH = 10.00f;
    protected static final int FOLLOW_RANGE = 6;
    protected static final int VARIANTS = 16;

    public MagicCarpetEntity(EntityType<MagicCarpetEntity> entityType, Level level)
    {
        super(entityType, level);
        selectVariant(0, false);
    }

    public static AttributeSupplier setAttributes()
    {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, MAX_HEALTH)
                .add(Attributes.FOLLOW_RANGE, FOLLOW_RANGE)
                .add(Attributes.FLYING_SPEED, BASE_FLYING_SPEED)
                .add(Attributes.MOVEMENT_SPEED, BASE_MOVEMENT_SPEED).build();
    }

    @Override
    protected void registerGoals()
    {
        this.goalSelector.addGoal(0, new FamiliarSitGoal(this, 0.5d));
        this.goalSelector.addGoal(1, new FamiliarFollowOwnerGoal(this, 1.0d));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.75d));
        this.goalSelector.addGoal(3, new FamiliarWanderGoal(this, 1.0d));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    @Override
    protected BodyRotationControl createBodyControl()
    {
        return new FamiliarBodyRotationControl(this, getMoveControlType(), getBodyRotationAngleLimit(), getBodyRotationAngleInterval());
    }

    private void selectVariant(int variant, boolean playerSet)
    {
        if(!hasVariant() || playerSet)
        {
            switch(variant)
            {
                case 0 -> setVariant("white");
                case 1 -> setVariant("light_gray");
                case 2 -> setVariant("gray");
                case 3 -> setVariant("black");
                case 4 -> setVariant("red");
                case 5 -> setVariant("orange");
                case 6 -> setVariant("yellow");
                case 7 -> setVariant("lime");
                case 8 -> setVariant("green");
                case 9 -> setVariant("cyan");
                case 10 -> setVariant("light_blue");
                case 11 -> setVariant("blue");
                case 12 -> setVariant("purple");
                case 13 -> setVariant("magenta");
                case 14 -> setVariant("pink");
                case 15 -> setVariant("brown");
            }
        }
    }

/////////////////////////////////
// GeckoLib animation control: //
/////////////////////////////////

    private MagicCarpetBodyAnimation bodyAnimation = MagicCarpetBodyAnimation.FLOATING;

    private static enum MagicCarpetBodyAnimation
    {
        UNROLLING,
        FLOATING,
        ROLLING,
        ROLLED;
    }

    private <E extends GeoAnimatable> PlayState bodyController(AnimationState<E> event)
    {
        FFAnimationController controller = (FFAnimationController) event.getController();

        switch(bodyAnimation)
        {
            case UNROLLING -> controller.setAnimation(RawAnimation.begin()
                    .thenPlay("animation.magic_carpet.body_unrolling"));
            case FLOATING -> controller.setAnimation(RawAnimation.begin()
                    .thenLoop("animation.magic_carpet.body_floating"));
            case ROLLING -> controller.setAnimation(RawAnimation.begin()
                    .thenPlay("animation.magic_carpet.body_rolling"));
            case ROLLED -> controller.setAnimation(RawAnimation.begin()
                    .thenLoop("animation.magic_carpet.body_rolled"));
            default -> controller.setAnimation(RawAnimation.begin()
                    .thenLoop("animation.magic_carpet.body_floating"));
        }

        controller.updateCurrentAnimation(bodyAnimation.name());

        if(controller.currentAnimation != ANIMATION_EMPTY && controller.hasAnimChanged())
        {
            controller.resetProgress();
            controller.updatePreviousAnimation();
        }

        boolean shouldRoll = isSitting() && !isFlying() && notCarryingPassengers() && !isOwnerNear(5);

        if(bodyAnimation == MagicCarpetBodyAnimation.UNROLLING)
        {
            if(!controller.isAnimInProgress())
            {
                if(shouldRoll)
                {
                    bodyAnimation = MagicCarpetBodyAnimation.ROLLING;
                    //return PlayState.STOP;
                }
                else
                {
                    bodyAnimation = MagicCarpetBodyAnimation.FLOATING;
                }
            }
        }
        else if(bodyAnimation == MagicCarpetBodyAnimation.FLOATING)
        {
            if(shouldRoll)
                bodyAnimation = MagicCarpetBodyAnimation.ROLLING;

            if(controller.getCurrentAnimation() != null)
                controller.setAnimationSpeed(controller.progress / controller.getCurrentAnimation().animation().length() * 0.75);
        }
        else if(bodyAnimation == MagicCarpetBodyAnimation.ROLLING)
        {
            if(!controller.isAnimInProgress())
            {
                if(!shouldRoll)
                {
                    bodyAnimation = MagicCarpetBodyAnimation.UNROLLING;
                    //return PlayState.STOP;
                }
                else
                {
                    bodyAnimation = MagicCarpetBodyAnimation.ROLLED;
                }
            }
        }
        else if(bodyAnimation == MagicCarpetBodyAnimation.ROLLED)
        {
            if(!shouldRoll)
                bodyAnimation = MagicCarpetBodyAnimation.UNROLLING;
        }

        if(bodyAnimation != MagicCarpetBodyAnimation.FLOATING)
            controller.setAnimationSpeed(0.75);

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data)
    {
        FFAnimationController body = new FFAnimationController<>(this, "bodyController", 0, 6, this::bodyController);

        data.add(body);

        animationControllers.add(body);
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
        return FFEnumValues.FamiliarMoveTypes.FORWARD;
    }

// Booleans:

    @Override
    protected boolean canAddPassenger(Entity rider)
    {
        return this.getPassengers().size() < 1;
    }

    @Override
    public boolean isTameItem(ItemStack stack)
    {
        return stack.is(Items.RED_SAND);
    }

    @Override
    public boolean isFoodItem(ItemStack stack)
    {
        return stack.is(Items.WHITE_WOOL);
    }

// Doubles:

    @Override
    public double getFlySpeedMod()
    {
        return getControllingPassenger() == null ? 1d : 1d;
    }

    @Override
    public double getWalkSpeedMod()
    {
        return getControllingPassenger() == null ? 1d : 1d;
    }

    @Override
    public double getBodyRotationAngleLimit()
    {
        return 10d;
    }

    @Override
    public double getBodyRotationAngleInterval()
    {
        return getBodyRotationAngleLimit() / 5d;
    }

////////////////////////////////////
// Player and entity interaction: //
////////////////////////////////////

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);

        InteractionResult stackResult = stack.interactLivingEntity(player, this, hand);
        if (stackResult.consumesAction())
            return stackResult;

        final InteractionResult SUCCESS = InteractionResult.sidedSuccess(level().isClientSide);

        if(isTamedFor(player) && player.isShiftKeyDown())
        {
            if(stack.is(Items.WHITE_DYE) && getVariant() != "white")
            {
                selectVariant(0, true);
                stack.shrink(1);

                return SUCCESS;
            }
            else if(stack.is(Items.LIGHT_GRAY_DYE) && getVariant() != "light_gray")
            {
                selectVariant(1, true);
                stack.shrink(1);

                return SUCCESS;
            }
            else if(stack.is(Items.GRAY_DYE) && getVariant() != "gray")
            {
                selectVariant(2, true);
                stack.shrink(1);

                return SUCCESS;
            }
            else if(stack.is(Items.BLACK_DYE) && getVariant() != "black")
            {
                selectVariant(3, true);
                stack.shrink(1);

                return SUCCESS;
            }
            else if(stack.is(Items.RED_DYE) && getVariant() != "red")
            {
                selectVariant(4, true);
                stack.shrink(1);

                return SUCCESS;
            }
            else if(stack.is(Items.ORANGE_DYE) && getVariant() != "orange")
            {
                selectVariant(5, true);
                stack.shrink(1);

                return SUCCESS;
            }
            else if(stack.is(Items.YELLOW_DYE) && getVariant() != "yellow")
            {
                selectVariant(6, true);
                stack.shrink(1);

                return SUCCESS;
            }
            else if(stack.is(Items.LIME_DYE) && getVariant() != "lime")
            {
                selectVariant(7, true);
                stack.shrink(1);

                return SUCCESS;
            }
            else if(stack.is(Items.GREEN_DYE) && getVariant() != "green")
            {
                selectVariant(8, true);
                stack.shrink(1);

                return SUCCESS;
            }
            else if(stack.is(Items.CYAN_DYE) && getVariant() != "cyan")
            {
                selectVariant(9, true);
                stack.shrink(1);

                return SUCCESS;
            }else if(stack.is(Items.LIGHT_BLUE_DYE) && getVariant() != "light_blue")
            {
                selectVariant(10, true);
                stack.shrink(1);

                return SUCCESS;
            }else if(stack.is(Items.BLUE_DYE) && getVariant() != "blue")
            {
                selectVariant(11, true);
                stack.shrink(1);

                return SUCCESS;
            }
            else if(stack.is(Items.PURPLE_DYE) && getVariant() != "purple")
            {
                selectVariant(12, true);
                stack.shrink(1);

                return SUCCESS;
            }else if(stack.is(Items.MAGENTA_DYE) && getVariant() != "magenta")
            {
                selectVariant(13, true);
                stack.shrink(1);

                return SUCCESS;
            }else if(stack.is(Items.PINK_DYE) && getVariant() != "pink")
            {
                selectVariant(14, true);
                stack.shrink(1);

                return SUCCESS;
            }else if(stack.is(Items.BROWN_DYE) && getVariant() != "brown")
            {
                selectVariant(15, true);
                stack.shrink(1);

                return SUCCESS;
            }
        }

        return super.mobInteract(player, hand);
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
                // Sand bomb type thing
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

            if(!(rider instanceof Player))
                rider.setYHeadRot(yHeadRot);
        }
    }

    public Vec3 getRiderPosition(Entity rider)
    {
        double x = 0;
        double y = getPassengersRidingOffset() + rider.getMyRidingOffset() - 0.4;
        double z = getScale() - 1;

        return new Vec3(x, y, z);
    }
}
