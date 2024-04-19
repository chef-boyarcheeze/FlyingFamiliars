package com.beesechurger.flyingfamiliars.block.entity;

import com.beesechurger.flyingfamiliars.registries.FFBlockEntities;
import com.beesechurger.flyingfamiliars.registries.FFBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class VitaAlembicBE extends BaseEntityTagBE
{
    public VitaAlembicBE(BlockPos pos, BlockState blockState)
    {
        super(FFBlockEntities.OBELISK_BLOCK_ENTITY.get(), pos, blockState);
    }
}
