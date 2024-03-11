package com.beesechurger.flyingfamiliars.entity.common;

import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarFollowOwnerGoal;
import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarSitGoal;
import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarWanderGoal;
import com.beesechurger.flyingfamiliars.entity.util.FFAnimationController;
import com.beesechurger.flyingfamiliars.registries.FFItems;
import com.beesechurger.flyingfamiliars.registries.FFSounds;
import com.beesechurger.flyingfamiliars.util.FFEnumValues;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import static com.beesechurger.flyingfamiliars.util.FFValueConstants.FLYING_SPEED;
import static com.beesechurger.flyingfamiliars.util.FFValueConstants.MOVEMENT_SPEED;

public class PhoenixEntity extends BaseFamiliarEntity
{
    private static final EntityDataAccessor<Boolean> HAS_RING = SynchedEntityData.defineId(PhoenixEntity.class, EntityDataSerializers.BOOLEAN);

    protected static final float MAX_HEALTH = 8.00f;
    protected static final int FOLLOW_RANGE = 4;
    protected static final int VARIANTS = 2;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public PhoenixEntity(EntityType<PhoenixEntity> entityType, Level level)
    {
        super(entityType, level);
        selectVariant(0);
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
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.75d));
        this.goalSelector.addGoal(3, new FamiliarWanderGoal(this, 1.0d));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    private void selectVariant(int variant)
    {
        if(!hasVariant())
        {
            switch (variant)
            {
                case 0: setVariant("red");
                case 1: setVariant("blue");
                default: setVariant("red");
            }
        }
    }

///////////////////////////
// Additional save data: //
///////////////////////////

    @Override
    public void readAdditionalSaveData(CompoundTag tag)
    {
        super.readAdditionalSaveData(tag);
        // has Molted
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag)
    {
        super.addAdditionalSaveData(tag);
        // has Molted
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        // has molted
    }

//////////////////////////////////
// Geckolib animation controls: //
//////////////////////////////////

    private <E extends GeoAnimatable> PlayState crestController(AnimationState<E> event)
    {
        FFAnimationController controller = (FFAnimationController) event.getController();

        controller.setAnimation(RawAnimation.begin()
                .thenLoop("animation.phoenix.crest_idle"));

        return PlayState.CONTINUE;
    }

    private <E extends GeoAnimatable> PlayState bodyController(AnimationState<E> event)
    {
        FFAnimationController controller = (FFAnimationController) event.getController();

        if(isFlying())
            if(isMoving())
                controller.setAnimation(RawAnimation.begin()
                        .thenLoop("animation.phoenix.body_flapping"));
            else
                controller.setAnimation(RawAnimation.begin()
                        .thenLoop("animation.phoenix.body_hovering"));
        else if(!isFlying() && isMoving())
            controller.setAnimation(RawAnimation.begin()
                    .thenLoop("animation.phoenix.body_walking"));
        else
            controller.setAnimation(RawAnimation.begin()
                    .thenLoop("animation.phoenix.body_idle"));

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data)
    {
        FFAnimationController crestController = new FFAnimationController<>(this, "crestController", 0, 0, this::crestController);
        FFAnimationController bodyController = new FFAnimationController<>(this, "bodyController", 8, 0, this::bodyController);

        data.add(crestController);
        data.add(bodyController);

        animationControllers.add(crestController);
        animationControllers.add(bodyController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache()
    {
        return cache;
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
        return SoundEvents.PARROT_AMBIENT;
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

///////////////////////
// Entity accessors: //
///////////////////////

// Enums:

    @Override
    public FFEnumValues.FamiliarMoveTypes getMoveControlType()
    {
        return FFEnumValues.FamiliarMoveTypes.NONE;
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
        return stack.is(Items.BLAZE_POWDER);
    }

    @Override
    public boolean isFoodItem(ItemStack stack)
    {
        return stack.is(Items.BAMBOO);
    }

// Doubles:

    @Override
    public double getFlySpeedMod()
    {
        return 4d;
    }

    @Override
    public double getWalkSpeedMod()
    {
        return 1.2d;
    }

//////////////////////
// Entity mutators: //
//////////////////////

    protected void setHasRing(boolean hasRing)
    {
        entityData.set(HAS_RING, hasRing);
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
            if(stack.is(Items.HEART_OF_THE_SEA))
            {
                selectVariant(1);
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
    }
}
