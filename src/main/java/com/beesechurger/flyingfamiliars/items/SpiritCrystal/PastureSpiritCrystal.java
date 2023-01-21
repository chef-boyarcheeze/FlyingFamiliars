package com.beesechurger.flyingfamiliars.items.SpiritCrystal;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class PastureSpiritCrystal extends Item 
{
	public static EntityType<?>[] FARM = {EntityType.PIG, EntityType.COW, EntityType.SHEEP, EntityType.CHICKEN};
	
	public PastureSpiritCrystal(Properties properties)
	{
		super(properties);
	}
	
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tipFlag)
    {
    	tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.spirit_crystal.tooltip").withStyle(ChatFormatting.GRAY));
    }
    
    public EntityType<?> getEntityType()
    {
    	return FARM[(int) Math.floor(Math.random()*FARM.length)];
    }
}
