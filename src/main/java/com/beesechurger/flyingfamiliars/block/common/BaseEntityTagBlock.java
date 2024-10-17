package com.beesechurger.flyingfamiliars.block.common;

import com.beesechurger.flyingfamiliars.block.entity.BaseEntityTagBE;
import com.beesechurger.flyingfamiliars.item.common.entity_items.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.item.common.entity_items.Phylactery;
import com.beesechurger.flyingfamiliars.item.common.fluid_items.BaseVitalityTagItem;
import com.beesechurger.flyingfamiliars.registries.FFSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class BaseEntityTagBlock extends BaseEntityBlock
{
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    protected VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16,16);

    protected BaseEntityTagBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if(state.getBlock() != newState.getBlock())
        {
            BlockEntity entity = level.getBlockEntity(pos);
            if(entity instanceof BaseEntityTagBE)
            {
                ((BaseEntityTagBE) entity).drops();
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof BaseEntityTagBE baseEntity)
        {
            if (!level.isClientSide())
            {
                ItemStack stack = player.getItemInHand(hand);

                if (stack.getItem() instanceof BaseEntityTagItem)
                {
                    if (!player.isShiftKeyDown())
                        return InteractionResult.sidedSuccess(baseEntity.placeEntity(player, hand));
                    else
                        return InteractionResult.sidedSuccess(baseEntity.removeEntity(player, hand));
                }
                else if (stack.getItem() instanceof BaseVitalityTagItem) // fluid item
                {
                    // if server
                    return InteractionResult.SUCCESS;

                    // consume if client
                }
                else
                {
                    if (!player.isShiftKeyDown())
                        return InteractionResult.sidedSuccess(baseEntity.placeItem(stack));
                    else
                        return InteractionResult.sidedSuccess(baseEntity.removeItem(level, pos));
                }
            }

            return InteractionResult.SUCCESS;
        }
        else
        {
            throw new IllegalStateException("Flying Familiars BaseEntityTagBE container provider missing");
        }
    }

    protected SoundEvent getPlaceEntitySound()
    {
        return FFSounds.TAG_BLOCK_ADD_ENTITY.get();
    }

    protected SoundEvent getRemoveEntitySound()
    {
        return FFSounds.TAG_BLOCK_REMOVE_ENTITY.get();
    }

////////////////////////////
// BlockState parameters: //
////////////////////////////

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx)
    {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override public BlockState rotate(BlockState state, Rotation rotation)
    {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror)
    {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }
}
