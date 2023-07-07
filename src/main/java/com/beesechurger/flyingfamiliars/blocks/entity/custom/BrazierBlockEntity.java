package com.beesechurger.flyingfamiliars.blocks.entity.custom;

import java.util.List;
import java.util.Optional;

import com.beesechurger.flyingfamiliars.blocks.entity.FFBLockEntities;
import com.beesechurger.flyingfamiliars.networking.FFMessages;
import com.beesechurger.flyingfamiliars.networking.packet.ItemStackSyncS2CPacket;
import com.beesechurger.flyingfamiliars.recipe.BrazierRecipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraftforge.items.ItemStackHandler;

public class BrazierBlockEntity extends BlockEntity implements Clearable 
{
	private final static int NUM_SLOTS = 4;
	private final NonNullList<ItemStack> items = NonNullList.withSize(NUM_SLOTS, ItemStack.EMPTY);
	private BrazierRecipe currentRecipe;
	
	protected final ContainerData data;
	private int progress = 0;
	private int maxProgress = 60;
	
	public BrazierBlockEntity(BlockPos position, BlockState state)
	{
		super(FFBLockEntities.BRAZIER_BLOCK_ENTITY.get(), position, state);
		
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
	}
	
	@Override
	public void load(CompoundTag tag)
	{		
		super.load(tag);
	    this.items.clear();
	    ContainerHelper.loadAllItems(tag, this.items);
	    progress = tag.getInt("brazier.progress");
	}
	
	public void setHandler(ItemStackHandler itemStackHandler)
	{
		for(int i = 0; i < itemStackHandler.getSlots(); i++)
		{
			items.set(i, itemStackHandler.getStackInSlot(i));
		}
	}
	
	public void drops()
	{
		SimpleContainer inventory = new SimpleContainer(items.size());
		for(int i = 0; i < items.size(); i++)
		{
			inventory.setItem(i, items.get(i));
		}
		
		Containers.dropContents(this.level, this.worldPosition, inventory);
	}
	
	public boolean placeItem(ItemStack stack)
	{		
		if(itemCount() < NUM_SLOTS)
		{
		   this.items.set(itemCount(), stack.split(1));
		   
		   contentsChanged();
		   
		   return true;
		}
		
		return false;
	}
	
	public boolean removeItem(Level level, BlockPos pos, BrazierBlockEntity brazier)
	{
		if(itemCount() > 0)
		{
			ItemStack stack = items.get(itemCount()-1);
			
            ItemEntity drop = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, stack);
            drop.setDefaultPickUpDelay();
            level.addFreshEntity(drop);
            
            brazier.items.set(itemCount()-1, ItemStack.EMPTY);
            
            contentsChanged();
            
            return true;
		}
	
		return false;
	}
	
	private void contentsChanged()
	{
		setChanged();
		findMatch();
		
		if(!level.isClientSide())
		{
			FFMessages.sendToClients(new ItemStackSyncS2CPacket(new ItemStackHandler(items), worldPosition));
		}
	}
	
	public NonNullList<ItemStack> getItems()
	{
		return this.items;
	}
	
	public int itemCount()
	{
		for(int i = 0; i < NUM_SLOTS; i++)
		{
			if(items.get(i).isEmpty()) return i;
		}
		
		return NUM_SLOTS;
	}
	
	public static void tick(Level level, BlockPos pos, BlockState state, BrazierBlockEntity entity)
	{
		if(level.isClientSide()) return;
		if(entity.currentRecipe != null)
		{
			if(entity.currentRecipe.matches(entity.items))
			{
				entity.progress++;
				setChanged(level, pos, state);
				
				if(entity.progress > entity.maxProgress)
				{
					craftItem(pos, entity);
					entity.contentsChanged();
				}
			}
			else
			{
				entity.resetProgress();
				setChanged(level, pos, state);
			}
		}
	}
	
	private void findMatch()
	{
		boolean found = false;
		for(BrazierRecipe entry : level.getRecipeManager().getAllRecipesFor(BrazierRecipe.Type.INSTANCE))
		{
			if(entry.matches(items))
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
	}
}
