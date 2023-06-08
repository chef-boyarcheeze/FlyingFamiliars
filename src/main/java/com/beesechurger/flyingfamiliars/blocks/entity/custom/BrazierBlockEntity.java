package com.beesechurger.flyingfamiliars.blocks.entity.custom;

import com.beesechurger.flyingfamiliars.blocks.entity.FFBLockEntities;
import com.beesechurger.flyingfamiliars.networking.FFMessages;
import com.beesechurger.flyingfamiliars.networking.packet.ItemStackSyncS2CPacket;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Clearable;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

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
		if(items.get(0).isEmpty())
		{
		   this.items.set(0, stack.split(1));
		   
		   contentsChanged();
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
            
            contentsChanged();
            return true;
		}
		
		return false;
	}
	
	private void contentsChanged()
	{
		setChanged();
		
		if(!level.isClientSide())
		{
			FFMessages.sendToClients(new ItemStackSyncS2CPacket(new ItemStackHandler(items), worldPosition));
		}
	}
	
	public NonNullList<ItemStack> getItems()
	{
		return this.items;
	}
	
	public static void tick(Level level, BlockPos pos, BlockState state, BrazierBlockEntity entity)
	{
		if(level.isClientSide()) return;
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
