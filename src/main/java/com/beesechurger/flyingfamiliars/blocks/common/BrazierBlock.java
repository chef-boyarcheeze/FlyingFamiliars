package com.beesechurger.flyingfamiliars.blocks.common;

import java.util.Random;

import javax.annotation.Nullable;

import com.beesechurger.flyingfamiliars.blocks.entity.FFBlockEntities;
import com.beesechurger.flyingfamiliars.blocks.entity.common.BrazierBlockEntity;
import com.beesechurger.flyingfamiliars.items.EntityTagItemHelper;
import com.beesechurger.flyingfamiliars.items.common.SoulItems.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.sound.FFSounds;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
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
				BrazierBlockEntity brazierEntity = (BrazierBlockEntity) entity;
				ItemStack stack = player.getItemInHand(hand);
				Random random = new Random();
				
				if(stack.getItem() instanceof BaseEntityTagItem item)
				{
					EntityTagItemHelper.ensureTagPopulated(stack);

					if(!EntityTagItemHelper.isSelectionEmpty(stack))
					{
						String selectedEntity = EntityTagItemHelper.getSelectedEntity(stack);
						
						if(brazierEntity.placeEntity(stack))
						{
							player.displayClientMessage(new TranslatableComponent("message.flyingfamiliars.entity_tag.place_entity")
																.withStyle(ChatFormatting.YELLOW)
																.append(": " + selectedEntity), true);

							level.playLocalSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 
									FFSounds.BRAZIER_ADD_ENTITY.get(), SoundSource.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.4F, false);
						}
					}
					else
					{
						if(brazierEntity.removeEntity(stack))
						{
							String selectedEntity = EntityTagItemHelper.getSelectedEntity(stack);
							
							player.displayClientMessage(new TranslatableComponent("message.flyingfamiliars.entity_tag.remove_entity")
																.withStyle(ChatFormatting.YELLOW)
																.append(": " + selectedEntity), true);
							
							level.playLocalSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, 
									FFSounds.BRAZIER_REMOVE_ENTITY.get(), SoundSource.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.4F, false);
						}
					}
				}
				else if(!player.isShiftKeyDown())
					brazierEntity.placeItem(stack);
				else
					brazierEntity.removeItem(level, pos);
				
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
}
