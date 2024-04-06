package com.beesechurger.flyingfamiliars.block.entity.common;

import com.beesechurger.flyingfamiliars.entity.common.MagicCarpetEntity;
import com.beesechurger.flyingfamiliars.entity.util.FFAnimationController;
import com.beesechurger.flyingfamiliars.registries.FFBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.ANIMATION_EMPTY;
import static com.beesechurger.flyingfamiliars.util.FFStringConstants.BLOCK_PROGRESS_TAGNAME;

public class ObeliskBlockEntity extends BaseEntityTagBE
{
    public ObeliskBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(FFBlockEntities.OBELISK_BLOCK_ENTITY.get(), pos, blockState);

        this.itemCapacityMod = 0;
        this.entityCapacityMod = 0;
        this.fluidCapacityMod = 4;

        createFluid();
    }

///////////////////////////
// Additional Save Data: //
///////////////////////////

    @Override
    public void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        //tag.putInt(BLOCK_PROGRESS_TAGNAME, progress);
    }

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        //progress = tag.getInt(BLOCK_PROGRESS_TAGNAME);
    }

/////////////////////////////////
// GeckoLib animation control: //
/////////////////////////////////

    private ObeliskBodyAnimation bodyAnimation = ObeliskBodyAnimation.INACTIVE;

    private static enum ObeliskBodyAnimation
    {
        INACTIVE,
        ACTIVATING,
        ACTIVE,
        INACTIVATING;
    }

    private <E extends GeoAnimatable> PlayState bodyController(AnimationState<E> event)
    {
        FFAnimationController controller = (FFAnimationController) event.getController();

        switch(bodyAnimation)
        {
            case INACTIVE -> controller.setAnimation(RawAnimation.begin()
                    .thenPlay("animation.obelisk.body_inactive"));
            case ACTIVATING -> controller.setAnimation(RawAnimation.begin()
                    .thenLoop("animation.obelisk.body_activating"));
            case ACTIVE -> controller.setAnimation(RawAnimation.begin()
                    .thenPlay("animation.obelisk.body_active"));
            case INACTIVATING -> controller.setAnimation(RawAnimation.begin()
                    .thenLoop("animation.obelisk.body_inactivating"));
            default -> controller.setAnimation(RawAnimation.begin()
                    .thenLoop("animation.obelisk.body_inactive"));
        }

        controller.updateCurrentAnimation(bodyAnimation.name());

        if(controller.currentAnimation != ANIMATION_EMPTY && controller.hasAnimChanged())
        {
            controller.resetProgress();
            controller.updatePreviousAnimation();
        }

        boolean shouldActivate = true;

        if(bodyAnimation == ObeliskBodyAnimation.INACTIVATING)
        {
            if(!controller.isAnimInProgress())
            {
                if(shouldActivate)
                {
                    bodyAnimation = ObeliskBodyAnimation.ACTIVATING;
                }
                else
                {
                    bodyAnimation = ObeliskBodyAnimation.INACTIVE;
                }
            }
        }
        else if(bodyAnimation == ObeliskBodyAnimation.INACTIVE)
        {
            if(shouldActivate)
                bodyAnimation = ObeliskBodyAnimation.ACTIVATING;
        }
        else if(bodyAnimation == ObeliskBodyAnimation.ACTIVATING)
        {
            if(!controller.isAnimInProgress())
            {
                if(!shouldActivate)
                {
                    bodyAnimation = ObeliskBodyAnimation.INACTIVATING;
                }
                else
                {
                    bodyAnimation = ObeliskBodyAnimation.ACTIVE;
                }
            }
        }
        else if(bodyAnimation == ObeliskBodyAnimation.ACTIVE)
        {
            if(!shouldActivate)
                bodyAnimation = ObeliskBodyAnimation.INACTIVATING;
        }

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data)
    {
        FFAnimationController bodyController = new FFAnimationController<>(this, "bodyController", 0, 0, this::bodyController);

        data.add(bodyController);

        animationControllers.add(bodyController);
    }

///////////////////////////
// Block Entity methods: //
///////////////////////////

    public static void tick(Level level, BlockPos pos, BlockState state, ObeliskBlockEntity entity)
    {
        for(FFAnimationController controller : entity.animationControllers)
            controller.updateProgress();
    }
}
