package com.beesechurger.flyingfamiliars.block.entity;

import com.beesechurger.flyingfamiliars.registries.FFBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CeremonialFontBE extends BaseEntityTagBE implements IRecipeBE
{
    public CeremonialFontBE(BlockPos pos, BlockState blockState)
    {
        super(FFBlockEntities.CEREMONIAL_FONT_BLOCK_ENTITY.get(), pos, blockState);

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
        return 3;
    }

    @Override
    public int getMaxEntities()
    {
        return 1;
    }
}
