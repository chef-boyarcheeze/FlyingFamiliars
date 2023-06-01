package com.beesechurger.flyingfamiliars.blocks.custom;

import javax.annotation.Nullable;

import com.beesechurger.flyingfamiliars.blocks.entity.FFBLockEntities;
import com.beesechurger.flyingfamiliars.blocks.entity.custom.BrazierBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BrazierBlock extends BaseEntityBlock
{
	private static final VoxelShape SHAPE = Block.box(1, 0, 1, 15, 14, 15);
	
	public BrazierBlock(Properties properties)
	{
		super(properties);
		registerDefaultState(this.stateDefinition.any());
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
			if(entity instanceof BrazierBlockEntity)
			{
				((BrazierBlockEntity) entity).drops();
			}
		}
	}
	
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
	{
		if(!level.isClientSide())
		{
			BlockEntity entity = level.getBlockEntity(pos);
			if(entity instanceof BrazierBlockEntity)
			{
				BrazierBlockEntity brazier = (BrazierBlockEntity) entity;
				ItemStack stack = player.getItemInHand(hand);
				
				if(!brazier.placeItem(stack)) brazier.removeItem(level, pos, brazier);
				
				return InteractionResult.SUCCESS;
			}
			else
			{
				throw new IllegalStateException("Flying Familiars Brazier container provider missing");
			}
		}
		
		return InteractionResult.sidedSuccess(level.isClientSide());
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new BrazierBlockEntity(pos, state);
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntity)
	{
		return createTickerHelper(blockEntity, FFBLockEntities.BRAZIER_BLOCK_ENTITY.get(), BrazierBlockEntity::tick);
	}
}
