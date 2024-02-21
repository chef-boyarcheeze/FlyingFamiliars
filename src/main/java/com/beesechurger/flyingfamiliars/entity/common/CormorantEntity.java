package com.beesechurger.flyingfamiliars.entity.common;

import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarFollowOwnerGoal;
import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarSitGoal;
import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarWanderGoal;
import com.beesechurger.flyingfamiliars.entity.util.FFAnimationController;
import com.beesechurger.flyingfamiliars.item.FFItems;
import com.beesechurger.flyingfamiliars.sound.FFSounds;
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
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import static com.beesechurger.flyingfamiliars.util.FFValueConstants.FLYING_SPEED;
import static com.beesechurger.flyingfamiliars.util.FFValueConstants.MOVEMENT_SPEED;

public class CormorantEntity extends BaseFamiliarEntity
{
    private static final EntityDataAccessor<Boolean> HAS_RING = SynchedEntityData.defineId(CormorantEntity.class, EntityDataSerializers.BOOLEAN);

    protected static final float MAX_HEALTH = 8.00f;
    protected static final int FOLLOW_RANGE = 4;
    protected static final int VARIANTS = 3;

    private final AnimationFactory factory = new AnimationFactory(this);

    public CormorantEntity(EntityType<CormorantEntity> entityType, Level level)
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
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.75d));
        this.goalSelector.addGoal(3, new FamiliarWanderGoal(this, 1.0d));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    private void selectVariant(int variant)
    {
        if(!hasVariant())
        {
            switch(variant)
            {
                case 0 -> setVariant("great_cormorant");
                case 1 -> setVariant("australian_pied_cormorant");
                case 2 -> setVariant("red_legged_cormorant");
            }
        }
    }

    @Override
    public MobType getMobType()
    {
        return MobType.WATER;
    }

///////////////////////////
// Additional save data: //
///////////////////////////

    @Override
    public void readAdditionalSaveData(CompoundTag tag)
    {
        super.readAdditionalSaveData(tag);
        setHasRing(tag.getBoolean("hasRing"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag)
    {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("hasRing", getHasRing());
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        entityData.define(HAS_RING, false);
    }

//////////////////////////////////
// Geckolib animation controls: //
//////////////////////////////////

    private <E extends IAnimatable> PlayState tailController(AnimationEvent<E> event)
    {
        FFAnimationController controller = (FFAnimationController) event.getController();

        controller.setAnimation(new AnimationBuilder()
                .addAnimation("animation.cormorant.tail_idle", ILoopType.EDefaultLoopTypes.LOOP));

        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState mouthController(AnimationEvent<E> event)
    {
        FFAnimationController controller = (FFAnimationController) event.getController();

        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState bodyController(AnimationEvent<E> event)
    {
        FFAnimationController controller = (FFAnimationController) event.getController();

        if(isFlying())
            controller.setAnimation(new AnimationBuilder()
                    .addAnimation("animation.cormorant.body_flapping", ILoopType.EDefaultLoopTypes.LOOP));
        else if(isInWater())
            controller.setAnimation(new AnimationBuilder()
                    .addAnimation("animation.cormorant.body_swimming", ILoopType.EDefaultLoopTypes.LOOP));
        else if(!isFlying() && isMoving())
            controller.setAnimation(new AnimationBuilder()
                    .addAnimation("animation.cormorant.body_walking", ILoopType.EDefaultLoopTypes.LOOP));
        else
            controller.setAnimation((new AnimationBuilder()
                    .addAnimation("animation.cormorant.body_idle", ILoopType.EDefaultLoopTypes.LOOP)));

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data)
    {
        FFAnimationController tailController = new FFAnimationController<>(this, "tailController", 4, 0, this::tailController);
        FFAnimationController mouthController = new FFAnimationController<>(this, "mouthController", 0, 0, this::mouthController);
        FFAnimationController bodyController = new FFAnimationController<>(this, "bodyController", 5, 0, this::bodyController);

        data.addAnimationController(tailController);
        data.addAnimationController(mouthController);
        data.addAnimationController(bodyController);

        animationControllers.add(tailController);
        animationControllers.add(mouthController);
        animationControllers.add(bodyController);
    }

    @Override
    public AnimationFactory getFactory()
    {
        return this.factory;
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
    public boolean getHasRing()
    {
        return entityData.get(HAS_RING);
    }

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

    @Override
    public boolean canWalk()
    {
        return true;
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

        final InteractionResult SUCCESS = InteractionResult.sidedSuccess(this.level.isClientSide);

        if(isTamedFor(player) && player.isShiftKeyDown())
        {
            if(getHasRing())
            {
                setHasRing(false);

                if(!player.isCreative())
                {
                    ItemEntity item = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), new ItemStack(FFItems.CORMORANT_RING.get()));
                    this.level.addFreshEntity(item);
                }

                return SUCCESS;
            }
            if(stack.is(FFItems.CORMORANT_RING.get()))
            {
                setHasRing(true);
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
