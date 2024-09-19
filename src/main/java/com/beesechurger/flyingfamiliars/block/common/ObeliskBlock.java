package com.beesechurger.flyingfamiliars.block.common;

import com.beesechurger.flyingfamiliars.block.entity.ObeliskBE;
import com.beesechurger.flyingfamiliars.registries.FFBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.Nullable;

public class ObeliskBlock extends BaseEntityTagBlock
{
    public ObeliskBlock(Properties properties)
    {
        super(properties);
        registerDefaultState(this.stateDefinition.any());
        this.SHAPE = Shapes.join(Block.box(0, 0, 0, 16, 3, 16),
                Shapes.join(Block.box(2, 0, 2, 14, 5, 14),
                        Block.box(4, 0, 4, 12, 36, 12), BooleanOp.OR), BooleanOp.OR).optimize();
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState)
    {
        return new ObeliskBE(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntity)
    {
        return createTickerHelper(blockEntity, FFBlockEntities.OBELISK_BLOCK_ENTITY.get(), ObeliskBE::tick);
    }
}
