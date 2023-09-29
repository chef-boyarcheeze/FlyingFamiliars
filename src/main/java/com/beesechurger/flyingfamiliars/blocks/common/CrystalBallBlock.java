package com.beesechurger.flyingfamiliars.blocks.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CrystalBallBlock extends Block
{
	private static final VoxelShape SHAPE = 
			Shapes.join(
				Shapes.join(
					Shapes.join(
						Shapes.join(
							Shapes.join(
								Block.box(2, 0, 2, 14, 2, 14), Block.box(4, 0.5, 4, 12, 0.5, 12), BooleanOp.OR), 	// Lowest and highest base sections
							Block.box(6, 3, 6, 10, 11, 10), BooleanOp.OR), 											// Pillar section
						Block.box(5, 10.75, 5, 11, 12, 11), BooleanOp.OR),											// Lowest pedestal section
					Block.box(4, 11.75, 4, 12, 13, 12), BooleanOp.OR),												// Highest pedestal section
				Block.box(5, 13, 5, 11, 19, 11), BooleanOp.OR);														// Crystal ball section
	
	public CrystalBallBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return SHAPE;
	}
}
