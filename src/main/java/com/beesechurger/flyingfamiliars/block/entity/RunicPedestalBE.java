package com.beesechurger.flyingfamiliars.block.entity;

import com.beesechurger.flyingfamiliars.registries.FFBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class RunicPedestalBE extends BaseEntityTagBE
{
	public RunicPedestalBE(BlockPos position, BlockState state)
	{
		super(FFBlockEntities.RUNIC_PEDESTAL_BLOCK_ENTITY.get(), position, state);

		createItems();
		createFluid();
	}
	
	@Override
	public void saveAdditional(CompoundTag tag)
	{
		super.saveAdditional(tag);
	}
	
	@Override
	public void load(CompoundTag tag)
	{		
		super.load(tag);
	}

//////////////////
/// Accessors: ///
//////////////////

/// Integers:

	@Override
	public int getMaxItems()
	{
		return 1;
	}

	@Override
	public int getMaxEntities()
	{
		return 0;
	}

/////////////////////////////
/// Block Entity methods: ///
/////////////////////////////

	public static void tick(Level level, BlockPos pos, BlockState state, RunicPedestalBE entity)
	{
		if(level.isClientSide())
		{
			return;
		}
	}
}
