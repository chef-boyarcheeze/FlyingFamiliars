package com.beesechurger.flyingfamiliars.block.entity;

import com.beesechurger.flyingfamiliars.util.FFAnimationController;
import com.beesechurger.flyingfamiliars.registries.FFBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.ANIMATION_EMPTY;

public class ObeliskBlockEntity extends BaseEntityTagBE
{
    public boolean clicked = false;

    public ObeliskBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(FFBlockEntities.OBELISK_BLOCK_ENTITY.get(), pos, blockState);

        this.itemCapacityMod = 0;
        this.entityCapacityMod = 0;
        this.fluidCapacityMod = 4;

        createItems();
        createEntities();
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

///////////////////////////
// Block Entity methods: //
///////////////////////////

    public static void tick(Level level, BlockPos pos, BlockState state, ObeliskBlockEntity entity)
    {

    }
}
