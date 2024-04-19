package com.beesechurger.flyingfamiliars.block.common;

import com.beesechurger.flyingfamiliars.block.entity.BrazierBE;
import com.beesechurger.flyingfamiliars.block.entity.VitaAlembicBE;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class VitaAlembicBlock extends BaseEntityTagBlock
{
    public VitaAlembicBlock(Properties properties)
    {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new VitaAlembicBE(pos, state);
    }
}
