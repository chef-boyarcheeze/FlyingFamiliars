package com.beesechurger.flyingfamiliars.blocks.entity.custom;

import com.beesechurger.flyingfamiliars.blocks.entity.FFBlockEntities;
import com.beesechurger.flyingfamiliars.items.custom.SoulWand;
import com.beesechurger.flyingfamiliars.networking.FFMessages;
import com.beesechurger.flyingfamiliars.networking.packet.BEItemStackS2CPacket;
import com.beesechurger.flyingfamiliars.networking.packet.BEProgressS2CPacket;
import com.beesechurger.flyingfamiliars.networking.packet.EntityListS2CPacket;
import com.beesechurger.flyingfamiliars.recipe.BrazierRecipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Clearable;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BrazierBlockEntity extends BlockEntity implements Clearable 
{
	private final static int MAX_ITEMS = 4;
	private final static int MAX_ENTITIES = 3;
	private final NonNullList<ItemStack> items = NonNullList.withSize(MAX_ITEMS, ItemStack.EMPTY);
	private CompoundTag entities = new CompoundTag();
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
				switch(index)
				{
					case 0: return BrazierBlockEntity.this.progress;
					case 1: return BrazierBlockEntity.this.maxProgress;
					default: return 0;
				}
			}
			
			public void set(int index, int value)
			{
				switch(index)
				{
					case 0: BrazierBlockEntity.this.progress = value; break;
					case 1: BrazierBlockEntity.this.maxProgress = value; break;
				}
			}
			
			public int getCount()
			{
				return 2;
			}
		};
	}
	
	@Override
	public void saveAdditional(CompoundTag tag)
	{
		super.saveAdditional(tag);
		ContainerHelper.saveAllItems(tag, this.items, true);
		tag.putInt("brazier.progress", progress);
		tag.put("brazier.entities", entities);
	}
	
	@Override
	public void load(CompoundTag tag)
	{		
		super.load(tag);
	    this.items.clear();
	    ContainerHelper.loadAllItems(tag, this.items);
	    progress = tag.getInt("brazier.progress");
	    entities = tag.getCompound("brazier.entities");
	}
	
	public void drops()
	{
		SimpleContainer inventory = new SimpleContainer(items.size());
		for(int i = 0; i < items.size(); i++)
		{
			inventory.setItem(i, items.get(i));
		}
		
		Containers.dropContents(this.level, this.worldPosition, inventory);
		entities = new CompoundTag();
	}
	
	public void setClientItems(NonNullList<ItemStack> list)
	{
		for(int i = 0; i < list.size(); i++)
		{
			items.set(i, list.get(i));
		}
	}
	
	public void setClientEntities(NonNullList<String> list)
	{
		populateTag(this);
		
		ListTag clientList = entities.getList("flyingfamiliars.entity", 10);
		
		for(int i = 0; i < list.size(); i++)
		{
			CompoundTag entityNBT = new CompoundTag();
			entityNBT.putString("flyingfamiliars.entity", list.get(i));
			clientList.set(i, entityNBT);
		}
		
		entities.put("flyingfamiliars.entity", clientList);
	}
	
	public boolean placeItem(ItemStack stack)
	{		
		if(getItemCount() < MAX_ITEMS)
		{
			items.set(getItemCount(), stack.split(1));
		    contentsChanged();
		    
		    return true;
		}
		
		return false;
	}
	
	public boolean removeItem(Level level, BlockPos pos)
	{
		if(getItemCount() > 0)
		{
			ItemStack stack = items.get(getItemCount()-1);
			
            ItemEntity drop = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, stack);
            drop.setDefaultPickUpDelay();
            level.addFreshEntity(drop);
            
            items.set(getItemCount()-1, ItemStack.EMPTY);
            contentsChanged();
            
            return true;
		}
	
		return false;
	}
	
	public boolean placeSoul(ItemStack stack)
	{
		CompoundTag wandTag = stack.getTag();

		if(entities.getList("flyingfamiliars.entity", 10).size() != MAX_ENTITIES) populateTag(this);
		
		ListTag wandList = wandTag.getList("flyingfamiliars.entity", 10);
		ListTag blockList = entities.getList("flyingfamiliars.entity", 10);

		for(int i = 0; i < MAX_ENTITIES; i++)
		{
			// Need to use regular Tag object for "Empty" compare here, not CompoundTag
			if(blockList.get(i).toString().contains("Empty"))
			{
				CompoundTag entityNBT = wandList.getCompound(SoulWand.MAX_ENTITIES-1);

				blockList.set(i, entityNBT);
				entities.put("flyingfamiliars.entity", blockList);
				
				CompoundTag empty = new CompoundTag();
				empty.putString("flyingfamiliars.entity", "Empty");
				wandList.set(SoulWand.MAX_ENTITIES-1, empty);
				
				wandTag.put("flyingfamiliars.entity", wandList);
				stack.setTag(wandTag);
				contentsChanged();
				
				return true;
			}
		}
		
		return false;
	}
	
	public boolean removeSoul(ItemStack stack)
	{
		CompoundTag wandTag = stack.getTag();
		
		if(entities.getList("flyingfamiliars.entity", 10).size() != MAX_ENTITIES)
		{
			populateTag(this);
			return false;
		}
		
		ListTag wandList = wandTag.getList("flyingfamiliars.entity", 10);
		ListTag blockList = entities.getList("flyingfamiliars.entity", 10);
		
		for(int i = MAX_ENTITIES; i > 0; i--)
		{
			if(!blockList.get(i-1).toString().contains("Empty"))
			{
				CompoundTag entityNBT = blockList.getCompound(i-1);
				
				CompoundTag empty = new CompoundTag();
				empty.putString("flyingfamiliars.entity", "Empty");
				blockList.set(i-1, empty);
				entities.put("flyingfamiliars.entity", blockList);
				
				wandList.set(SoulWand.MAX_ENTITIES-1, entityNBT);
				
				wandTag.put("flyingfamiliars.entity", wandList);
				stack.setTag(wandTag);
				contentsChanged();
				
				return true;
			}
		}

		return false;
	}
	
	private void contentsChanged()
	{
		setChanged();
		findMatch();
		
		if(!level.isClientSide())
		{
			FFMessages.sendToClients(new BEItemStackS2CPacket(getItems(), worldPosition));
			FFMessages.sendToClients(new EntityListS2CPacket(getEntities(), worldPosition));
		}
	}
	
	public NonNullList<ItemStack> getItems()
	{
		return items;
	}
	
	public int getItemCount()
	{
		for(int i = 0; i < MAX_ITEMS; i++)
		{
			if(items.get(i).isEmpty()) return i;
		}
		
		return MAX_ITEMS;
	}
	
	public NonNullList<String> getEntities()
	{
		if(entities.getList("flyingfamiliars.entity", 10).size() != MAX_ENTITIES) populateTag(this);
		
		ListTag blockList = entities.getList("flyingfamiliars.entity", 10);
		NonNullList<String> entityStrings = NonNullList.withSize(MAX_ENTITIES, "");
		
		for(int i = 0; i < MAX_ENTITIES; i++)
		{
			entityStrings.set(i, blockList.getCompound(i).getString("flyingfamiliars.entity"));
		}
		
		return entityStrings;
	}
	
	public int getEntityCount()
	{	
		int entityCount = 0;
		
		if(entities.getList("flyingfamiliars.entity", 10).size() != MAX_ENTITIES) populateTag(this);
    	
		ListTag tagList = entities.getList("flyingfamiliars.entity", 10);
		
		for(int i = 0; i < MAX_ENTITIES; i++)
    	{
			// Need to use regular Tag object here, not CompoundTag
    		if(!tagList.get(i).toString().contains("Empty")) entityCount++;
    	}

    	return entityCount;
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
		if(level.isClientSide()) return;
		if(entity.currentRecipe != null)
		{
			if(entity.currentRecipe.itemsMatch(entity.items))
			{
				entity.progress++;
				setChanged(level, pos, state);
				
				//System.out.println(entity.currentRecipe.getEntities().get(0));
				
				if(entity.progress > entity.maxProgress)
				{
					craftItem(pos, entity);
					entity.contentsChanged();
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
	
	private void findMatch()
	{
		boolean found = false;
		for(BrazierRecipe entry : level.getRecipeManager().getAllRecipesFor(BrazierRecipe.Type.INSTANCE))
		{
			if(entry.itemsMatch(getItems()) && entry.entitiesMatch(getEntities()))
			{
				currentRecipe = entry;
				found = true;
				break;
			}
		}
		if(!found) currentRecipe = null;
	}
	
	private static void craftItem(BlockPos pos, BrazierBlockEntity entity)
	{	
		ItemEntity drop = new ItemEntity(entity.level, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, entity.currentRecipe.getResultItem());
        drop.setDefaultPickUpDelay();
        entity.level.addFreshEntity(drop);
        
        entity.clearContent();
		entity.resetProgress();
	}
	
	public static void populateTag(BrazierBlockEntity entity)
	{
		entity.entities = new CompoundTag();
		
		CompoundTag entityNBT = new CompoundTag();
		ListTag tagList = entityNBT.getList("flyingfamiliars.entity", 10);
		entityNBT.putString("flyingfamiliars.entity", "Empty");
		
		for(int i = 0; i < MAX_ENTITIES; i++)
		{
			tagList.addTag(i, entityNBT);
		}
		
		entity.entities.put("flyingfamiliars.entity", tagList);
	}
	
	private void resetProgress()
	{
		this.progress = 0;
	}
	
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
	    return ClientboundBlockEntityDataPacket.create(this);
	}
	
	public CompoundTag getUpdateTag()
	{
	    return this.saveWithoutMetadata();
	}

	@Override
	public void clearContent()
	{
		this.items.clear();
		populateTag(this);
	}
}
