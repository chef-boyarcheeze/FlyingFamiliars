package com.beesechurger.flyingfamiliars.block.entity.common;

import java.util.Random;

import com.beesechurger.flyingfamiliars.registries.FFBlockEntities;
import com.beesechurger.flyingfamiliars.networking.FFMessages;
import com.beesechurger.flyingfamiliars.networking.packet.BEProgressS2CPacket;
import com.beesechurger.flyingfamiliars.recipe.BrazierRecipe;
import com.beesechurger.flyingfamiliars.registries.FFSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.*;

public class BrazierBlockEntity extends BaseEntityTagBE implements Clearable
{
	private BrazierRecipe currentRecipe;
	
	protected final ContainerData data;
	private int progress = 0;
	private int maxProgress = 60;
	
	public BrazierBlockEntity(BlockPos position, BlockState state)
	{
		super(FFBlockEntities.BRAZIER_BLOCK_ENTITY.get(), position, state);
		
		this.data = new ContainerData() {
			public int get(int index)
			{
				return switch(index)
				{
					case 0 -> BrazierBlockEntity.this.progress;
					case 1 -> BrazierBlockEntity.this.maxProgress;
					default -> 0;
				};
			}
			
			public void set(int index, int value)
			{
				switch(index)
				{
					case 0 -> BrazierBlockEntity.this.progress = value;
					case 1 -> BrazierBlockEntity.this.maxProgress = value;
				}
			}
			
			public int getCount()
			{
				return 2;
			}
		};

		this.itemCapacityMod = 5;
		this.entityCapacityMod = 3;
		this.items = NonNullList.withSize(getMaxItems(), ItemStack.EMPTY);
	}
	
	@Override
	public void saveAdditional(CompoundTag tag)
	{
		super.saveAdditional(tag);
		tag.putInt(BLOCK_PROGRESS_TAGNAME, progress);
	}
	
	@Override
	public void load(CompoundTag tag)
	{		
		super.load(tag);
	    progress = tag.getInt(BLOCK_PROGRESS_TAGNAME);
	}

	@Override
	public boolean placeItem(ItemStack stack)
	{
		if(super.placeItem(stack))
		{
			findMatch();
			playSound(1);

			return true;
		}

		return false;
	}

	@Override
	public boolean removeItem(Level level, BlockPos pos)
	{
		if(super.removeItem(level, pos))
		{
			findMatch();
			playSound(2);

			return true;
		}

		return false;
	}
	
	@Override
	public boolean placeEntity(ItemStack stack)
	{
		if(super.placeEntity(stack))
		{
			findMatch();
			playSound(3);

			return true;
		}

		return false;
	}

	@Override
	public boolean removeEntity(ItemStack stack)
	{
		if(super.removeEntity(stack))
		{
			findMatch();
			playSound(4);

			return true;
		}

		return false;
	}
	
	public int getProgress()
	{
		return progress;
	}
	
	public void setProgress(int pr)
	{
		progress = pr;
	}
	
	public int getMaxProgress()
	{
		return maxProgress;
	}
	
	public static void tick(Level level, BlockPos pos, BlockState state, BrazierBlockEntity entity)
	{		
		if(level.isClientSide())
		{
			if(entity.progress >= entity.maxProgress)
			{
				for(int i = 0; i < 360; i++)
				{
					if(i % 4 == 0) entity.level.addParticle(ParticleTypes.CLOUD, entity.getBlockPos().getX() + 0.5F, entity.getBlockPos().getY() + 1.2F, entity.getBlockPos().getZ() + 0.5F, 0.1 * Math.cos(i), 0, 0.1 * Math.sin(i));
					entity.level.addParticle(ParticleTypes.SMOKE, entity.getBlockPos().getX() + 0.5F, entity.getBlockPos().getY() + 1.2F, entity.getBlockPos().getZ() + 0.5F, 0.12 * Math.cos(i), -0.05F, 0.12 * Math.sin(i));
				}
			}
			
			return;
		}
		if(entity.currentRecipe != null)
		{
			if(entity.currentRecipe.itemsMatch(entity.items))
			{
				if(entity.progress == 0) entity.playSound(5);
				
				entity.progress++;
				setChanged(level, pos, state);
				
				if(entity.progress > entity.maxProgress)
				{
					entity.level.playSound(null, pos, FFSounds.BRAZIER_RESULT.get(), SoundSource.BLOCKS, 0.8F, 0.5F);
					
					craft(pos, entity);
					entity.contentsChanged();
					entity.findMatch();
				}
			}
		}
		else
		{
			entity.resetProgress();
			setChanged(level, pos, state);
		}
		
		FFMessages.sendToClients(new BEProgressS2CPacket(entity.progress, entity.worldPosition));
	}
	
	private void playSound(int source)
	{
		Random random = new Random();
		switch (source)
		{
			case 1:
				level.playSound(null, getBlockPos(), FFSounds.BRAZIER_ADD_ITEM.get(), SoundSource.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.4F);
				break;
			case 2:
				level.playSound(null, getBlockPos(), FFSounds.BRAZIER_REMOVE_ITEM.get(), SoundSource.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.4F);
				break;
			case 3:
				level.playSound(null, getBlockPos(), FFSounds.BRAZIER_ADD_ENTITY.get(), SoundSource.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.4F);
				break;
			case 4:
				level.playSound(null, getBlockPos(), FFSounds.BRAZIER_REMOVE_ENTITY.get(), SoundSource.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.4F);
				break;
			case 5:
				level.playSound(null, getBlockPos(), FFSounds.BRAZIER_CRAFT.get(), SoundSource.BLOCKS, 0.7F, 1.0F);
				break;
			default:
				break;
		}
	}
	
	private void findMatch()
	{
		boolean found = false;
		for(BrazierRecipe entry : level.getRecipeManager().getAllRecipesFor(BrazierRecipe.Type.INSTANCE))
		{
			if(entry.itemsMatch(items) && entry.entitiesMatch(getEntitiesStrings()))
			{
				currentRecipe = entry;
				found = true;
				break;
			}
		}
		if(!found) currentRecipe = null;
	}
	
	private static void craft(BlockPos pos, BrazierBlockEntity entity)
	{		
		// Clear all fields
		entity.clearContent();
		entity.resetProgress();
		
		// Spawn result item drop
		ItemEntity drop = new ItemEntity(entity.level, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, entity.currentRecipe.getOutputItem());
        drop.setDefaultPickUpDelay();
        entity.level.addFreshEntity(drop);
        
        // Add result entity to entity list
        entity.addResultEntity(entity.currentRecipe.getOutputEntity());
	}
	
	public void addResultEntity(String resultEntity)
	{
		if(resultEntity == null) return;
		
    	EntityType<?> type = EntityType.byString(resultEntity).orElse(null);
    	if(type != null)
    	{
    		CompoundTag entityNBT = new CompoundTag();
    		ListTag tagList = entities.getList(BASE_ENTITY_TAGNAME, 10);
    		Entity entity = type.create(level);
    		
    		entityNBT.putString(BASE_ENTITY_TAGNAME, EntityType.getKey(entity.getType()).toString());
    		entity.saveWithoutId(entityNBT);
    		
    		tagList.set(0, entityNBT);
    		entities.put(BASE_ENTITY_TAGNAME, tagList);
    	}
	}
	
	private void resetProgress()
	{
		this.progress = 0;
	}
}
