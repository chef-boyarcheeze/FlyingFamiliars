package com.beesechurger.flyingfamiliars.block.common;

import java.util.Random;

import javax.annotation.Nullable;

import com.beesechurger.flyingfamiliars.block.entity.FFBlockEntities;
import com.beesechurger.flyingfamiliars.block.entity.common.BrazierBlockEntity;
import com.beesechurger.flyingfamiliars.sound.FFSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BrazierBlock extends BaseEntityTagBlock
{
	public BrazierBlock(Properties properties)
	{
		super(properties);
		registerDefaultState(this.stateDefinition.any());
		this.SHAPE = Block.box(1, 0, 1, 15, 14, 15);
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
		return createTickerHelper(blockEntity, FFBlockEntities.BRAZIER_BLOCK_ENTITY.get(), BrazierBlockEntity::tick);
	}
	
	public void animateTick(BlockState state, Level level, BlockPos pos, Random random)
	{
		if (random.nextInt(20) == 0)
		{
			level.playLocalSound((double) pos.getX() + 0.5D,(double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D,
					FFSounds.BRAZIER_AMBIENT.get(), SoundSource.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.4F, false);
		}
		
		if (random.nextInt(3) == 0)
		{
		   for(int i = 0; i < random.nextInt(1) + 1; ++i)
		   {
			   level.addParticle(ParticleTypes.SOUL, (double) pos.getX() + 0.5D,
					   								 (double) pos.getY() + 0.5D,
					   								 (double) pos.getZ() + 0.5D,
					   								 (double)(random.nextFloat() / 20.0F), 0.1D, (double)(random.nextFloat() / 20.0F));
		   }
		}
		
      	level.addAlwaysVisibleParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, true, (double) pos.getX() + 0.5D + random.nextDouble() / 3.0D * (double) (random.nextBoolean() ? 1 : -1),
      																			(double) pos.getY() + 1.2D,
      																			(double) pos.getZ() + 0.5D + random.nextDouble() / 3.0D * (double) (random.nextBoolean() ? 1 : -1),
      																			0.0D, 0.07D, 0.0D);
      	
      	level.addParticle(ParticleTypes.SMOKE, (double) pos.getX() + 0.5D + random.nextDouble() / 4.0D * (double) (random.nextBoolean() ? 1 : -1),
      										   (double) pos.getY() + 0.4D,
      										   (double) pos.getZ() + 0.5D + random.nextDouble() / 4.0D * (double) (random.nextBoolean() ? 1 : -1),
      										   0.0D, 0.005D, 0.0D);
    }

	@Override
	protected SoundEvent getPlaceEntitySound()
	{
		return FFSounds.BRAZIER_ADD_ENTITY.get();
	}

	@Override
	protected SoundEvent getRemoveEntitySound()
	{
		return FFSounds.BRAZIER_REMOVE_ENTITY.get();
	}
}
