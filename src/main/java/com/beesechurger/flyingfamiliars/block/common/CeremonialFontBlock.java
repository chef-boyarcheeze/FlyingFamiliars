package com.beesechurger.flyingfamiliars.block.common;

import com.beesechurger.flyingfamiliars.block.entity.CeremonialFontBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.Nullable;

public class CeremonialFontBlock extends BaseExtraTagBlock
{
    public CeremonialFontBlock(Properties properties)
    {
        super(properties);
        this.SHAPE = Shapes.join(Block.box(0, 0, 0, 16, 4, 16),
                Block.box(1, 4, 1, 15, 10, 15), BooleanOp.OR).optimize();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new CeremonialFontBE(pos, state);
    }
}
