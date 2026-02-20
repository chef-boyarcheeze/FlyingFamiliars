package com.beesechurger.flyingfamiliars.block.entity;

import com.beesechurger.flyingfamiliars.packet.BEProgressS2CPacket;
import com.beesechurger.flyingfamiliars.recipe.BrazierRecipe;
import com.beesechurger.flyingfamiliars.registries.FFBlockEntities;
import com.beesechurger.flyingfamiliars.registries.FFPackets;
import com.beesechurger.flyingfamiliars.registries.FFSounds;
import com.beesechurger.flyingfamiliars.tags.EntityTagRef;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static com.beesechurger.flyingfamiliars.util.FFConstants.BLOCK_PROGRESS_TAGNAME;
import static com.beesechurger.flyingfamiliars.util.FFConstants.STORAGE_ENTITY_TYPE;

public class FragmentPedestalBE extends BaseEntityTagBE
{
	public FragmentPedestalBE(BlockPos position, BlockState state)
	{
		super(FFBlockEntities.FRAGMENT_PEDESTAL_BLOCK_ENTITY.get(), position, state);

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

	public static void tick(Level level, BlockPos pos, BlockState state, FragmentPedestalBE entity)
	{
		if(level.isClientSide())
		{
			return;
		}
	}
}
