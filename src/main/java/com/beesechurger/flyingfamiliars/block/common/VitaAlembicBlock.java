package com.beesechurger.flyingfamiliars.block.common;

import com.beesechurger.flyingfamiliars.block.entity.VitaAlembicBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;

import javax.annotation.Nullable;

public class VitaAlembicBlock extends BaseEntityTagBlock
{
    public VitaAlembicBlock(Properties properties)
    {
        super(properties);
        this.SHAPE = Shapes.join(Block.box(0, 0, 0, 16, 8, 16),
                        Block.box(2, 6, 2, 14, 15, 14), BooleanOp.OR).optimize();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new VitaAlembicBE(pos, state);
    }
}
