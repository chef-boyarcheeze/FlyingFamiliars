package com.beesechurger.flyingfamiliars.items.custom;

import java.util.List;

import javax.annotation.Nullable;

import com.beesechurger.flyingfamiliars.entity.custom.projectile.SoulWandProjectile;
import com.beesechurger.flyingfamiliars.sound.FFSounds;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
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
		CompoundTag compound = stack.getTag();
		
		if(compound == null)
		{
			compound = new CompoundTag();
			
			CompoundTag entityNBT = new CompoundTag();
			ListTag tagList = entityNBT.getList("entity", 10);
			entityNBT.putString("entity", "Empty");
			
			for(int i = 0; i < MAX_ENTITIES; i++)
			{
				tagList.addTag(i, entityNBT);
			}
			
			compound.put("entity", tagList);
			stack.setTag(compound);
		}
		
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
	
    public String getID(int listValue, ItemStack stack)
    {
        return stack.getTag().getList("entity", 10).getCompound(listValue).getString("entity");
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
						tooltip.add(new TextComponent("Slot " + (i+1) + ": " + getID(i, stack)).withStyle(ChatFormatting.YELLOW));
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

					tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.soul_wand.left_shift").withStyle(ChatFormatting.GRAY));
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
    
    public ListTag getListTag(ItemStack stack)
    {
    	CompoundTag compound = stack.getTag();
    	ListTag list = new ListTag();
    	
    	if(compound != null) list = compound.getList("entity", 10);
    	
    	return list;
    }
    
    public int getEntityCount(ItemStack stack)
    {
    	int entityCount = 0;
    	
    	for(int i = 0; i < MAX_ENTITIES; i++)
    	{
    		if(getListTag(stack).getCompound(i).getString("entity") != "Empty") entityCount++;
    	}
    	
    	return entityCount;
    }
    
    @Override
    public boolean isFoil(ItemStack stack)
    {   	
    	return getEntityCount(stack) == MAX_ENTITIES && stack.getTag() != null;
    }
    
    @Override
    public boolean isBarVisible(ItemStack stack)
    {
    	return getEntityCount(stack) < MAX_ENTITIES && getEntityCount(stack) > 0 && stack.getTag() != null;
    }
    
    @Override
    public int getBarWidth(ItemStack stack)
    {
    	return Math.round((float) getEntityCount(stack) * 13.0f / (float) MAX_ENTITIES);
    }

    @Override
    public int getBarColor(ItemStack stack)
    {
    	return 32767;
    }
}
