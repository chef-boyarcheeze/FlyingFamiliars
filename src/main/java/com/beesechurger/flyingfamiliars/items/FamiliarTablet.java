package com.beesechurger.flyingfamiliars.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class FamiliarTablet extends Item
{
	public FamiliarTablet(Properties properties)
	{
		super(properties);
	}
	
	@Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand)
	{
		Level level = player.level;
		
        if(target.getType() == EntityType.PHANTOM && !level.isClientSide())
        {
    		CompoundTag compound = getCompoundTag(stack);

			ListTag tagList = compound.getList("entity", 10);
			if (!hasEntity(stack))
			{
				CompoundTag entityNBT = new CompoundTag();
				
				entityNBT.putString("entity", EntityType.getKey(target.getType()).toString());
				target.saveWithoutId(entityNBT);
				tagList.addTag(tagList.size(),entityNBT);
				
			}
			
			compound.put("entity", tagList);
			stack.setTag(compound);
			
			player.swing(hand);
			player.setItemInHand(hand, stack);
        }
        
        return InteractionResult.SUCCESS;
    }
	
	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		Player player = context.getPlayer();
		InteractionHand hand = context.getHand();
		ItemStack stack = player.getItemInHand(hand);
		
		CompoundTag compound = new CompoundTag();
		
		if(player.isShiftKeyDown() && hasEntity(stack))
		{
			stack.setTag(compound);
			
			player.swing(hand);
			player.setItemInHand(hand, stack);
		}
		
		return InteractionResult.PASS;
	}
	
	@Override
	public boolean isFoil(ItemStack stack)
	{
		return super.isFoil(stack) || hasEntity(stack);
	}
	
	public boolean hasEntity(ItemStack stack)
	{
		ListTag tagList = getCompoundTag(stack).getList("entity", 10);
		
		return tagList.size() > 0;
	}
	
	public CompoundTag getCompoundTag(ItemStack stack)
	{
		CompoundTag tag = stack.getTag();
		if (tag == null)
		{
			tag = new CompoundTag();
		}
		
		return tag;
	}
	
	public String getID(ItemStack stack)
    {
        return stack.getTag().getList("entity", 10).getCompound(0).getString("entity");
    }
	
	@Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tipFlag)
    {
    	if(hasEntity(stack))
    	{
    		if(Screen.hasShiftDown())
    		{
    			tooltip.add(new TextComponent("Collected Entity: " + getID(stack)).withStyle(ChatFormatting.YELLOW));
    		}
    		else
    		{
    			tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.familiar_tablet.tooltip.left_shift").withStyle(ChatFormatting.GRAY));
    		}
    	}
    }
}
