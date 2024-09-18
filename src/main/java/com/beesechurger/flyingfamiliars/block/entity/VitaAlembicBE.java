package com.beesechurger.flyingfamiliars.block.entity;

import com.beesechurger.flyingfamiliars.registries.FFBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class VitaAlembicBE extends BaseExtraTagBE implements IRecipeBE
{
    public VitaAlembicBE(BlockPos pos, BlockState blockState)
    {
        super(FFBlockEntities.OBELISK_BLOCK_ENTITY.get(), pos, blockState);

        this.itemCapacityMod = 0;
        this.entityCapacityMod = 1;
        this.fluidCapacityMod = 3;

        createItems();
        createEntities();
        createFluid();
    }

    @Override
    public void findMatch()
    {

    }
}
