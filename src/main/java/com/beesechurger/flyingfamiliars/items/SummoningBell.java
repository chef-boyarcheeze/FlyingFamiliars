package com.beesechurger.flyingfamiliars.items;

import java.util.List;

import javax.annotation.Nullable;

import com.beesechurger.flyingfamiliars.entity.custom.projectile.SummoningBellProjectile;
import com.beesechurger.flyingfamiliars.init.FFItems;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class SummoningBell extends Item
{
	private final float[] scale = {0.5f, 0.56f, 0.64f, 0.68f, 0.76f, 0.85f, 0.95f, 1.0f};
	private boolean action;
	
	public SummoningBell(Properties properties)
	{
		super(properties);
		action = false;
	}

	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) 
	{
		ItemStack stack = player.getItemInHand(hand);
		
		if(player.isShiftKeyDown())
		{
			if(level.isClientSide()) toggleInteractType(player);
			return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
		}
		
	    level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.NOTE_BLOCK_BELL, SoundSource.NEUTRAL, 0.5F, scale[(int) Math.floor(Math.random()*scale.length)]);
	    
	    if (!level.isClientSide)
	    {
	       SummoningBellProjectile ding = new SummoningBellProjectile(level, player, stack, action);
	       
	       int note_type = (int) Math.floor(Math.random()*3);
	       switch(note_type)
	       {
	       		case 0:
	       			ding.setItem(new ItemStack(FFItems.MUSIC_NOTE_1.get()));
	       			break;
	       		case 1:
	       			ding.setItem(new ItemStack(FFItems.MUSIC_NOTE_2.get()));
	       			break;
	       		case 2:
	       			ding.setItem(new ItemStack(FFItems.MUSIC_NOTE_3.get()));
	       			break;
	       }
	       
	       ding.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.2F, 1.0F);
	       level.addFreshEntity(ding);
	    }

	    //player.awardStat(Stats.ITEM_USED.get(this));

	    return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}
	
    public String getID(int listValue, ItemStack stack)
    {
        return stack.getTag().getList("entity", 10).getCompound(listValue).getString("entity");
    }
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tipFlag)
    {
    	int entityCount = 0;
    	
		CompoundTag compound = stack.getTag();
		
		if(!action) tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.summoning_tool.tooltip.capture"));
		else tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.summoning_tool.tooltip.release"));
		
		if (compound != null)
		{
			ListTag tagList = compound.getList("entity", 10);
			entityCount = tagList.size();
			
			if(Screen.hasShiftDown())
			{				
				if(entityCount != 0)
				{
					for (int i = 0; i < entityCount; i++)
					{
						tooltip.add(new TextComponent("Entity " + (i+1) + ": " + getID(i, stack)).withStyle(ChatFormatting.YELLOW));
					}
				}
				else
				{
					tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.summoning_bell.tooltip.empty").withStyle(ChatFormatting.GRAY));
				}
			}
			else
			{
				switch(entityCount)
				{
					case 0: tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.summoning_bell.tooltip.stored_0").withStyle(ChatFormatting.GRAY));
						break;
						
					case 1: tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.summoning_bell.tooltip.stored_1").withStyle(ChatFormatting.GRAY));
						break;
						
					case 2: tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.summoning_bell.tooltip.stored_2").withStyle(ChatFormatting.GRAY));
						break;
						
					case 3: tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.summoning_bell.tooltip.stored_3").withStyle(ChatFormatting.GRAY));
						break;
				}
				
				if(entityCount != 0)
				{
					tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.summoning_bell.tooltip").withStyle(ChatFormatting.GRAY));
				}
			}
		}
		else
		{
			if(Screen.hasShiftDown())
			{
				tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.summoning_bell.tooltip.stored_0").withStyle(ChatFormatting.GRAY));
			}
			else
			{
				tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.summoning_bell.tooltip.empty").withStyle(ChatFormatting.GRAY));
			}
		}
    }
    
    public void toggleInteractType(Player player)
    {
    	action = !action;
    	
    	if(!action) player.displayClientMessage(new TranslatableComponent("message.flyingfamiliars.summoning_tool.toggle_capture"), true);
    	else player.displayClientMessage(new TranslatableComponent("message.flyingfamiliars.summoning_tool.toggle_release"), true);
    }
}
