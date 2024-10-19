package com.beesechurger.flyingfamiliars.block.entity;

import com.beesechurger.flyingfamiliars.packet.BEProgressS2CPacket;
import com.beesechurger.flyingfamiliars.recipe.BrazierRecipe;
import com.beesechurger.flyingfamiliars.registries.FFBlockEntities;
import com.beesechurger.flyingfamiliars.registries.FFPackets;
import com.beesechurger.flyingfamiliars.registries.FFSounds;
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

public class BrazierBE extends BaseEntityTagBE implements IRecipeBE
{
	private BrazierRecipe currentRecipe;
	
	protected final ContainerData data;
	private int progress = 0;
	private int maxProgress = 60;
	
	public BrazierBE(BlockPos position, BlockState state)
	{
		super(FFBlockEntities.BRAZIER_BLOCK_ENTITY.get(), position, state);

		this.data = new ContainerData() {
			public int get(int index)
			{
				return switch(index)
				{
					case 0 -> BrazierBE.this.progress;
					case 1 -> BrazierBE.this.maxProgress;
					default -> 0;
				};
			}
			
			public void set(int index, int value)
			{
				switch(index)
				{
					case 0 -> BrazierBE.this.progress = value;
					case 1 -> BrazierBE.this.maxProgress = value;
				}
			}
			
			public int getCount()
			{
				return 2;
			}
		};

		createItems();
		createFluid();
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

////////////////
// Accessors: //
////////////////

// Integers:

	@Override
	public int getMaxItems()
	{
		return 5;
	}

	@Override
	public int getMaxEntities()
	{
		return 3;
	}

	public int getProgress()
	{
		return progress;
	}

	public int getMaxProgress()
	{
		return maxProgress;
	}

///////////////
// Mutators: //
///////////////

// Integers:

	public void setProgress(int pr)
	{
		progress = pr;
	}

///////////////////////
// Crafting methods: //
///////////////////////

	@Override
	public void findMatch()
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
		if(!found)
			currentRecipe = null;
	}

	private static void craft(BlockPos pos, BrazierBE entity)
	{
		// Clear all fields
		entity.clearContent();
		entity.resetProgress();

		// Spawn result item drop
		ItemEntity drop = new ItemEntity(entity.level, pos.getX() + 0.5f, pos.getY() + 1, pos.getZ() + 0.5f, entity.currentRecipe.getOutputItem());
		drop.setDefaultPickUpDelay();
		entity.level.addFreshEntity(drop);

		// Add result entity to entity list
		entity.addResultEntity(entity.currentRecipe.getOutputEntity());
	}

	public void addResultEntity(String resultEntity)
	{
		if (resultEntity == null)
			return;

		EntityType<?> type = EntityType.byString(resultEntity).orElse(null);
		if (type != null)
		{
			CompoundTag result = new CompoundTag();
			Entity entity = type.create(level);

			result.putString(STORAGE_ENTITY_TYPE, EntityType.getKey(entity.getType()).toString());
			entity.saveWithoutId(result);

			entities.addEntry(entityStorageTag, result);
		}
	}

///////////////////////////
// Block Entity methods: //
///////////////////////////

	public static void tick(Level level, BlockPos pos, BlockState state, BrazierBE entity)
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
				if(entity.progress == 0)
					level.playSound(null, entity.getBlockPos(), FFSounds.BRAZIER_CRAFT.get(), SoundSource.BLOCKS, 0.7f + entity.random.nextFloat(), 0.4f + 0.7f * entity.random.nextFloat());
				
				entity.progress++;
				setChanged(level, pos, state);
				
				if(entity.progress > entity.maxProgress)
				{
					level.playSound(null, pos, FFSounds.BRAZIER_RESULT.get(), SoundSource.BLOCKS, 0.7f + entity.random.nextFloat(), 0.4f + 0.7f * entity.random.nextFloat());
					
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
		
		FFPackets.sendToClients(new BEProgressS2CPacket(entity.progress, entity.worldPosition));
	}
	
	private void resetProgress()
	{
		this.progress = 0;
	}
}
