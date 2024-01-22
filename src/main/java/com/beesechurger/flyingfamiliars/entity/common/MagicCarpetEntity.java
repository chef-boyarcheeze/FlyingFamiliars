package com.beesechurger.flyingfamiliars.entity.common;

import com.beesechurger.flyingfamiliars.entity.ai.FamiliarBodyRotationControl;
import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarFollowOwnerGoal;
import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarWanderGoal;
import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarWanderGoalOld;
import com.beesechurger.flyingfamiliars.sound.FFSounds;
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
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class MagicCarpetEntity extends BaseFamiliarEntity implements IAnimatable
{
    public static final float MAX_HEALTH = 10.00f;
    public static final float FLYING_SPEED = 0.5f;
    public static final int VARIANTS = 16;

    private final AnimationFactory factory = new AnimationFactory(this);

    public MagicCarpetEntity(EntityType<MagicCarpetEntity> entityType, Level level)
    {
        super(entityType, level);
        selectVariant(0, false);
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
        this.goalSelector.addGoal(1, new FamiliarFollowOwnerGoal(this, 0.75d, BEGIN_FOLLOW_DISTANCE, END_FOLLOW_DISTANCE));
        this.goalSelector.addGoal(2, new FamiliarWanderGoal(this, 0.5d, 0, 0));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    @Override
    protected BodyRotationControl createBodyControl()
    {
        return new FamiliarBodyRotationControl(this, getMoveControlType(), 10, 2.0f);
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

    private <E extends IAnimatable> PlayState generalController(AnimationEvent<E> event)
    {
        if(event.getController().getCurrentAnimation() != null && prevAnim != currentAnim)
        {
            currentAnimLength = event.getController().getCurrentAnimation().animationLength;
            currentAnimTimer = 0;
            currentAnimInProgress = true;

            prevAnim = currentAnim;
        }

        boolean shouldRoll = isSitting() && !isFlying() && notCarryingPassengers() && !isOwnerNear(5);

        if(currentAnim == MagicCarpetAnimation.UNROLLING)
        {
            if(!currentAnimInProgress)
            {
                if(shouldRoll)
                {
                    currentAnim = MagicCarpetAnimation.ROLLING;
                    //return PlayState.STOP;
                }
                else
                {
                    currentAnim = MagicCarpetAnimation.FLOATING;
                }
            }
        }
        else if(currentAnim == MagicCarpetAnimation.FLOATING)
        {
            if(shouldRoll)
                currentAnim = MagicCarpetAnimation.ROLLING;

            event.getController().setAnimationSpeed(currentAnimTimer / currentAnimLength * 0.75);
        }
        else if(currentAnim == MagicCarpetAnimation.ROLLING)
        {
            if(!currentAnimInProgress)
            {
                if(!shouldRoll)
                {
                    currentAnim = MagicCarpetAnimation.UNROLLING;
                    //return PlayState.STOP;
                }
                else
                {
                    currentAnim = MagicCarpetAnimation.ROLLED;
                }
            }
        }
        else if(currentAnim == MagicCarpetAnimation.ROLLED)
        {
            if(!shouldRoll)
                currentAnim = MagicCarpetAnimation.UNROLLING;
        }

        switch(currentAnim)
        {
            case UNROLLING -> event.getController().setAnimation(new AnimationBuilder()
                    .addAnimation("animation.magic_carpet.general_unrolling", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            case FLOATING -> event.getController().setAnimation(new AnimationBuilder()
                    .addAnimation("animation.magic_carpet.general_floating", ILoopType.EDefaultLoopTypes.LOOP));
            case ROLLING -> event.getController().setAnimation(new AnimationBuilder()
                    .addAnimation("animation.magic_carpet.general_rolling", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            case ROLLED -> event.getController().setAnimation(new AnimationBuilder()
                    .addAnimation("animation.magic_carpet.general_rolled", ILoopType.EDefaultLoopTypes.LOOP));
            default -> event.getController().setAnimation(new AnimationBuilder()
                    .addAnimation("animation.magic_carpet.general_floating", ILoopType.EDefaultLoopTypes.LOOP));
        }

        if(currentAnim != MagicCarpetAnimation.FLOATING)
            event.getController().setAnimationSpeed(0.75);

        return PlayState.CONTINUE;
    }

    private static enum MagicCarpetAnimation
    {
        UNROLLING,
        FLOATING,
        ROLLING,
        ROLLED;
    }

    private MagicCarpetAnimation currentAnim = MagicCarpetAnimation.FLOATING;
    private MagicCarpetAnimation prevAnim = null;
    private double currentAnimTimer = 0;
    private double currentAnimLength = 0;
    private boolean currentAnimInProgress = false;

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

    @Override
    public boolean canWalk()
    {
        return false;
    }

// Floats:

    @Override
    protected float getDrivingSpeedMod()
    {
        return 0.25f;
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

        final InteractionResult SUCCESS = InteractionResult.sidedSuccess(this.level.isClientSide);

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

        if(currentAnimInProgress)
        {
            if(currentAnimTimer >= currentAnimLength + 5)
                currentAnimInProgress = false;

            currentAnimTimer++;
        }

        if(!level.isClientSide())
        {
            if(actionCooldown == 0 && isOwnerDoingFamiliarAction() && isFlying())
            {
                // Sand bomb type thing
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
