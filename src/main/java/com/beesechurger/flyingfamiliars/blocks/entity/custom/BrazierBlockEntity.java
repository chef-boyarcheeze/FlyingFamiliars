package com.beesechurger.flyingfamiliars.blocks.entity.custom;

import java.util.Random;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.beesechurger.flyingfamiliars.blocks.entity.FFBLockEntities;
import com.beesechurger.flyingfamiliars.items.FFItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class BrazierBlockEntity extends BlockEntity implements MenuProvider 
{
	private final ItemStackHandler itemHandler = new ItemStackHandler(1)
	{
		@Override
		protected void onContentsChanged(int slot)
		{
			setChanged();
		}
	};
	
	private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
	
	public BrazierBlockEntity(BlockPos position, BlockState state)
	{
		super(FFBLockEntities.BRAZIER_BLOCK_ENTITY.get(), position, state);
	}

	/*@Override
	public Component getDisplayName()
	{
		return new TranslatableComponent("Brazier");
	}
	
	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_)
	{
		return null;
	}*/
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side)
	{
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			return lazyItemHandler.cast();
		}
		
		return super.getCapability(cap, side);
	}
	
	@Override
	public void onLoad()
	{
		super.onLoad();
		lazyItemHandler = LazyOptional.of(() -> itemHandler);
	}
	
	@Override
	public void invalidateCaps()
	{
		super.invalidateCaps();
		lazyItemHandler.invalidate();
	}
	
	@Override
	public void saveAdditional(@NotNull CompoundTag tag)
	{
		tag.put("inventory", itemHandler.serializeNBT());
		super.saveAdditional(tag);
	}
	
	@Override
	public void load(CompoundTag tag)
	{
		super.load(tag);
		itemHandler.deserializeNBT(tag.getCompound("inventory"));
	}
	
	public void drops()
	{
		SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
		for(int i = 0; i < itemHandler.getSlots(); i++)
		{
			inventory.setItem(i, itemHandler.getStackInSlot(i));
		}
		
		Containers.dropContents(this.level, this.worldPosition, inventory);
	}
	
	public void placeItem()
	{
		
	}
	
	public static void tick(Level level, BlockPos pos, BlockState state, BrazierBlockEntity entity)
	{
		if(hasRecipe(entity) && hasNotReachedStackLimit(entity))
		{
			craftItem(entity);
		}
	}
	
	private static void craftItem(BrazierBlockEntity entity)
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
	}
}
