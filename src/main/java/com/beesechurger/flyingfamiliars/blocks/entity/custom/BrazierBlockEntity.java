package com.beesechurger.flyingfamiliars.blocks.entity.custom;

import com.beesechurger.flyingfamiliars.blocks.entity.FFBLockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BrazierBlockEntity extends BlockEntity implements Clearable 
{
	private final static int NUM_SLOTS = 1;
	private final NonNullList<ItemStack> items = NonNullList.withSize(NUM_SLOTS, ItemStack.EMPTY);
	
	public BrazierBlockEntity(BlockPos position, BlockState state)
	{
		super(FFBLockEntities.BRAZIER_BLOCK_ENTITY.get(), position, state);
	}
	
	@Override
	public void saveAdditional(CompoundTag tag)
	{
		super.saveAdditional(tag);
		ContainerHelper.saveAllItems(tag, this.items, true);
	}
	
	@Override
	public void load(CompoundTag tag)
	{		
		super.load(tag);
	    this.items.clear();
	    ContainerHelper.loadAllItems(tag, this.items);
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
		if(items.get(0).isEmpty())
		{
		   this.items.set(0, stack.split(1));
		   return true;
		}
		
		return false;
	}
	
	public boolean removeItem(Level level, BlockPos pos, BrazierBlockEntity brazier)
	{
		if(!items.get(0).isEmpty())
		{
			ItemStack stack = items.get(0);
			
            ItemEntity drop = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, stack);
            drop.setDefaultPickUpDelay();
            level.addFreshEntity(drop);
            
            brazier.items.set(0, ItemStack.EMPTY);
            
            return true;
		}
		
		return false;
	}
	
	public static void tick(Level level, BlockPos pos, BlockState state, BrazierBlockEntity entity)
	{
		/*if(hasRecipe(entity) && hasNotReachedStackLimit(entity))
		{
			craftItem(entity);
		}*/
	}
	
	/*private static void craftItem(BrazierBlockEntity entity)
	{
		entity.itemHandler.extractItem(0, 1, false);
		entity.itemHandler.extractItem(1, 1, false);
		entity.itemHandler.getStackInSlot(2).hurt(1, new Random(), null);
		
		entity.itemHandler.setStackInSlot(3, new ItemStack(FFItems.SPECTER_MOTE.get(), entity.itemHandler.getStackInSlot(3).getCount() + 1));
	}
	
	private static boolean hasRecipe(BrazierBlockEntity entity)
	{
		return entity.itemHandler.getStackInSlot(0).getItem() == FFItems.PHOENIX_FEATHER.get();
	}
	
	private static boolean hasNotReachedStackLimit(BrazierBlockEntity entity)
	{
		return entity.itemHandler.getStackInSlot(3).getCount() < entity.itemHandler.getStackInSlot(3).getMaxStackSize();
	}*/

	@Override
	public void clearContent()
	{
		this.items.clear();
	}
}
