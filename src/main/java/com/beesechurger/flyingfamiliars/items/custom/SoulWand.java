package com.beesechurger.flyingfamiliars.items.custom;

import java.util.List;

import javax.annotation.Nullable;

import com.beesechurger.flyingfamiliars.entity.custom.projectile.SoulWandProjectile;
import com.beesechurger.flyingfamiliars.sound.FFSounds;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SoulWand extends Item
{
	public final static int MAX_ENTITIES = 3;
	
	public SoulWand(Properties properties)
	{
		super(properties);
	}

	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) 
	{
		ItemStack stack = player.getItemInHand(hand);
		
		if(stack.getTag() == null) populateTag(stack);
		
	    level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), FFSounds.SOUL_WAND_THROW.get(), SoundSource.NEUTRAL, 0.5F, FFSounds.getPitch());
	    
	    if (!level.isClientSide())
	    {
	    	SoulWandProjectile capture;
	    	
	    	if(!player.isShiftKeyDown()) capture = new SoulWandProjectile(level, player, stack, false);
	    	else capture = new SoulWandProjectile(level, player, stack, true);
	       
	        capture.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.2F, 1.0F);
	        level.addFreshEntity(capture);
	    }

	    player.awardStat(Stats.ITEM_USED.get(this));

	    return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}
	
	@Override
	public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player)
	{
		return !player.isCreative();
	}
	
    public String getID(int listValue, ItemStack stack)
    {
        return stack.getTag().getList("flyingfamiliars.entity", 10).getCompound(listValue).getString("flyingfamiliars.entity");
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tipFlag)
    {
    	int entityCount = getEntityCount(stack);
		CompoundTag compound = stack.getTag();
		
		if (compound != null)
		{
			if(entityCount != 0)
			{
				if(Screen.hasShiftDown())
				{				
					for (int i = 0; i < MAX_ENTITIES; i++)
					{
						tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.soul_wand.tooltip.slot").withStyle(ChatFormatting.YELLOW).append(" " + (i+1) + ": " + getID(i, stack)));
					}
				}
				else
				{
					switch(entityCount)
					{		
						case 1: tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.soul_wand.tooltip.stored_1").withStyle(ChatFormatting.GRAY));
							break;
							
						case 2: tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.soul_wand.tooltip.stored_2").withStyle(ChatFormatting.GRAY));
							break;
							
						case 3: tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.soul_wand.tooltip.stored_3").withStyle(ChatFormatting.GRAY));
							break;
					}
					
					tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.soul_wand.tooltip.left_shift").withStyle(ChatFormatting.GRAY));
				}
			}
			else
			{
				tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.soul_wand.tooltip.empty").withStyle(ChatFormatting.GRAY));
			}
		}
		else
		{
			tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.soul_wand.tooltip.empty").withStyle(ChatFormatting.GRAY));
		}
    }
    
    public int getEntityCount(ItemStack stack)
    {
    	int entityCount = 0;
    	CompoundTag compound = stack.getTag();
    	
    	if(compound != null)
    	{
    		ListTag wandList = compound.getList("flyingfamiliars.entity", 10);
    		
    		for(int i = 0; i < MAX_ENTITIES; i++)
        	{
    			// Need to use regular Tag object here, not CompoundTag
        		if(!wandList.get(i).toString().contains("Empty")) entityCount++;
        	}
    	}
    	
    	return entityCount;
    }
    
    public static String getSelectedEntity(ItemStack stack)
    {
    	CompoundTag compound = stack.getTag();
    	
    	if(compound != null)
    	{
    		ListTag wandList = compound.getList("flyingfamiliars.entity", 10);
    		
    		return wandList.getCompound(MAX_ENTITIES-1).getString("flyingfamiliars.entity");
    	}
    	
    	return "Empty";
    }
    
    public static boolean isSelectionEmpty(ItemStack stack)
    {
    	CompoundTag compound = stack.getTag();
    	
    	if(compound != null)
    	{
    		ListTag wandList = compound.getList("flyingfamiliars.entity", 10);
    		
    		return wandList.get(MAX_ENTITIES-1).toString().contains("Empty");
    	}
    	
    	return true;
    }
    
    public static void populateTag(ItemStack stack)
	{
		CompoundTag compound = new CompoundTag();
		
		CompoundTag entityNBT = new CompoundTag();
		ListTag wandList = entityNBT.getList("flyingfamiliars.entity", 10);
		entityNBT.putString("flyingfamiliars.entity", "Empty");
		
		for(int i = 0; i < MAX_ENTITIES; i++)
		{
			wandList.addTag(i, entityNBT);
		}
		
		compound.put("flyingfamiliars.entity", wandList);
		stack.setTag(compound);
	}
    
    @Override
    public boolean isFoil(ItemStack stack)
    {
    	return getEntityCount(stack) == MAX_ENTITIES && stack.getTag() != null;
    }
    
    @Override
    public boolean isBarVisible(ItemStack stack)
    {
    	return getEntityCount(stack) > 0 && stack.getTag() != null;
    }
    
    @Override
    public int getBarWidth(ItemStack stack)
    {
    	return Math.round((float) getEntityCount(stack) * 13.0f / (float) MAX_ENTITIES);
    }

    @Override
    public int getBarColor(ItemStack stack)
    {
    	// 0,128,255
    	return 32767;
    }
}
