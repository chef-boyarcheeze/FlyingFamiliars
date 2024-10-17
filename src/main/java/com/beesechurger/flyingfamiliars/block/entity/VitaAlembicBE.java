package com.beesechurger.flyingfamiliars.block.entity;

import com.beesechurger.flyingfamiliars.registries.FFBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class VitaAlembicBE extends BaseEntityTagBE implements IRecipeBE
{
    public VitaAlembicBE(BlockPos pos, BlockState blockState)
    {
        super(FFBlockEntities.OBELISK_BLOCK_ENTITY.get(), pos, blockState);

        createItems();
        createFluid();
    }

    @Override
    public void findMatch()
    {

    }

    @Override
    public int getMaxItems()
    {
        return 0;
    }

    @Override
    public int getMaxEntities()
    {
        return 1;
    }
}
