package com.beesechurger.flyingfamiliars.block.entity;

import com.beesechurger.flyingfamiliars.registries.FFBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ObeliskBE extends BaseEntityTagBE
{
    public boolean clicked = false;

    public ObeliskBE(BlockPos pos, BlockState blockState)
    {
        super(FFBlockEntities.OBELISK_BLOCK_ENTITY.get(), pos, blockState);

        createItems();
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

    @Override
    public int getMaxItems()
    {
        return 0;
    }

    @Override
    public int getMaxEntities()
    {
        return 0;
    }

///////////////////////////
// Block Entity methods: //
///////////////////////////

    public static void tick(Level level, BlockPos pos, BlockState state, ObeliskBE entity)
    {

    }
}
