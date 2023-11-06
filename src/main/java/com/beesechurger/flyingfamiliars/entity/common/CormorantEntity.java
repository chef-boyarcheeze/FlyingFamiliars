package com.beesechurger.flyingfamiliars.entity.common;

import com.beesechurger.flyingfamiliars.entity.ai.FamiliarBodyRotationControl;
import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarFollowOwnerGoal;
import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarLandGoal;
import com.beesechurger.flyingfamiliars.entity.ai.goals.FamiliarWanderGoal;
import com.beesechurger.flyingfamiliars.item.FFItems;
import com.beesechurger.flyingfamiliars.sound.FFSounds;
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
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.item.ItemEntity;
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

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.MOVE_CONTROL_NONE;

public class CormorantEntity extends BaseFamiliarEntity implements IAnimatable
{
    private static final EntityDataAccessor<Boolean> HAS_RING = SynchedEntityData.defineId(CormorantEntity.class, EntityDataSerializers.BOOLEAN);

    public static final float MAX_HEALTH = 8.00f;
    public static final float FLYING_SPEED = 0.35f;
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
                .add(Attributes.FLYING_SPEED, FLYING_SPEED).build();
    }

    @Override
    protected void registerGoals()
    {
        this.goalSelector.addGoal(0, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(1, new FamiliarFollowOwnerGoal(this, 0.75f, BEGIN_FOLLOW_DISTANCE, END_FOLLOW_DISTANCE));
        this.goalSelector.addGoal(2, new FamiliarLandGoal(this, 0.3f, 10));
        this.goalSelector.addGoal(3, new FamiliarWanderGoal(this, 0.25f));
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
        setSitting(tag.getBoolean("hasRing"));
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

    private <E extends IAnimatable> PlayState generalController(AnimationEvent<E> event)
    {
        if(isFlying())
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.cormorant.general_flying", ILoopType.EDefaultLoopTypes.LOOP));
        else if(isInWater())
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.cormorant.general_swimming", ILoopType.EDefaultLoopTypes.LOOP));
        else
            event.getController().setAnimation((new AnimationBuilder().addAnimation("animation.cormorant.general_idle", ILoopType.EDefaultLoopTypes.LOOP)));

        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState headController(AnimationEvent<E> event)
    {
        if(isFlying())
        {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.cormorant.head_flying", ILoopType.EDefaultLoopTypes.LOOP));
            event.getController().setAnimationSpeed(1.0f);
        }
        else if(isInWater())
        {
            event.getController().setAnimation((new AnimationBuilder().addAnimation("animation.cormorant.head_swimming", ILoopType.EDefaultLoopTypes.LOOP)));
            event.getController().setAnimationSpeed(0.25f);
        }
        else
        {
            event.getController().setAnimation((new AnimationBuilder().addAnimation("animation.cormorant.head_idle", ILoopType.EDefaultLoopTypes.LOOP)));
            event.getController().setAnimationSpeed(0.25f);
        }

        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState mouthController(AnimationEvent<E> event)
    {
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState tailController(AnimationEvent<E> event)
    {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.cormorant.tail_idle", ILoopType.EDefaultLoopTypes.LOOP));

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data)
    {
        data.addAnimationController(new AnimationController<>(this, "generalController", 4, this::generalController));
        data.addAnimationController(new AnimationController<>(this, "headController", 2, this::headController));
        data.addAnimationController(new AnimationController<>(this, "mouthController", 0, this::mouthController));
        data.addAnimationController(new AnimationController<>(this, "tailController", 4, this::tailController));
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

// Strings:

    @Override
    public String getMoveControlType()
    {
        return MOVE_CONTROL_NONE;
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

//////////////////////
// Entity mutators: //
//////////////////////

    protected void setHasRing(boolean hasRing)
    {
        entityData.set(HAS_RING, hasRing);
    }

/////////////////////
// Mob AI methods: //
/////////////////////

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

    @Override
    public void tick()
    {
        super.tick();
    }
}
