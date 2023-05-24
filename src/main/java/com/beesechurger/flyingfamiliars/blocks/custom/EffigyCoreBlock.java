package com.beesechurger.flyingfamiliars.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EffigyCoreBlock extends Block
{
	private static final VoxelShape SHAPE = Shapes.join(Block.box(6, 0, 6, 10, 15.75, 10), 
												Shapes.join(Block.box(2, 8, 6, 14, 12, 10), 
													Shapes.join(Block.box(6, 8, 2, 10, 12, 14), 
															Block.box(3, 0, 3, 13, 2, 13),
														BooleanOp.OR),
													BooleanOp.OR),
												BooleanOp.OR);
	
	public EffigyCoreBlock(Properties properties)
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
}
