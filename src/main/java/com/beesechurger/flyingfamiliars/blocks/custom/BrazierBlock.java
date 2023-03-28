package com.beesechurger.flyingfamiliars.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BrazierBlock extends Block
{
	public static IntegerProperty FILL_LEVEL = IntegerProperty.create("fill_level", 0, 2);
	public static BooleanProperty LIT = BooleanProperty.create("lit");
	//public static final FireProperty
	private static final VoxelShape SHAPE = Shapes.join(Block.box(6, 1.5, 6, 10, 7.5, 10), Block.box(1, 11.25, 1, 15, 14, 15), BooleanOp.OR);
	
	public BrazierBlock(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
	{
		if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND)
		{
			// if FILL_LEVEL = 3 and player main hand item is flint and steel or fire
		}
		
		return InteractionResult.SUCCESS;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder)
	{
		pBuilder.add(FILL_LEVEL);
		pBuilder.add(LIT);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return SHAPE;
	}
	
	public boolean addFluidLevel()
	{
		return true;
	}
	
	public boolean removeFluidLevel()
	{
		
		return true;
	}
	
	public static boolean canLight(BlockState state)
	{
		return !state.getValue(LIT);
	}
}
