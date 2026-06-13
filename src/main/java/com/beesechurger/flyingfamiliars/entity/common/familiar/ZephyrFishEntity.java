package com.beesechurger.flyingfamiliars.entity.common.familiar;

import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarFollowOwnerGoal;
import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarSitGoal;
import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarWanderGoal;
import com.beesechurger.flyingfamiliars.entity.client.FFAnimationController;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class ZephyrFishEntity extends BaseFamiliarEntity
{
    protected static final float MAX_HEALTH = 8.00f;
    protected static final int FOLLOW_RANGE = 3;

    public ZephyrFishEntity(EntityType<ZephyrFishEntity> entityType, Level level)
    {
        super(entityType, level);
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

//////////////////////////////////
// Geckolib animation controls: //
//////////////////////////////////

    private <E extends GeoAnimatable> PlayState bubbleController(AnimationState<E> event)
    {
        FFAnimationController controller = (FFAnimationController) event.getController();

        controller.setAnimation(RawAnimation.begin()
                .thenLoop("animation.zephyr_fish.bubble_idle"));

        return PlayState.CONTINUE;
    }

    private <E extends GeoAnimatable> PlayState bodyController(AnimationState<E> event)
    {
        FFAnimationController controller = (FFAnimationController) event.getController();

        if(isMoving())
            controller.setAnimation(RawAnimation.begin()
                    .thenLoop("animation.zephyr_fish.body_flying"));
        else
            controller.setAnimation(RawAnimation.begin()
                    .thenLoop("animation.zephyr_fish.body_idle"));

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data)
    {
        FFAnimationController bubbleController = new FFAnimationController<>(this, "bubbleController", 0, 0, this::bubbleController);
        FFAnimationController bodyController = new FFAnimationController<>(this, "bodyController", 4, 0, this::bodyController);

        data.add(bubbleController);
        data.add(bodyController);

        animationControllers.add(bubbleController);
        animationControllers.add(bodyController);
    }

////////////////////////////////
// Sound-controlling methods: //
////////////////////////////////

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
        return SoundEvents.COD_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource attack)
    {
        return SoundEvents.COD_HURT;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.COD_DEATH;
    }

///////////////////////
// Entity accessors: //
///////////////////////

// Enums:
    @Override
    public FamiliarMoveTypes getMoveControlType()
    {
        return FamiliarMoveTypes.NONE;
    }

// Booleans:
    @Override
    public boolean canOwnerRide()
    {
        return false;
    }

    @Override
    public boolean isTameItem(ItemStack stack)
    {
        return stack.is(Items.SPONGE);
    }

    @Override
    public boolean isFoodItem(ItemStack stack)
    {
        return stack.is(Items.POPPY);
    }

// Doubles:
    @Override
    public double getFlySpeedMod()
    {
        return 2.5d;
    }

    @Override
    public double getWalkSpeedMod()
    {
        return 2d;
    }

// Misc:
    @Override
    public MobType getMobType()
    {
        return MobType.WATER;
    }

//////////////////////
// Entity mutators: //
//////////////////////

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

        return super.mobInteract(player, hand);
    }

/////////////
// Mob AI: //
/////////////

    @Override
    public void tick()
    {
        super.tick();
    }
}
