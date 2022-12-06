package com.beesechurger.flyingfamiliars.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class SoulWand extends Item{

	public SoulWand(Properties properties)
	{
		super(properties);
	}
	
	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand)
	{
		if(!player.level.isClientSide())
		{
			if (!(target instanceof Player || !target.canChangeDimensions() || !target.isAlive()) && target instanceof Mob)
			{
				if(target.hasEffect(MobEffects.WEAKNESS) && target.getHealth() <= target.getMaxHealth() * 0.5f)
				{
					CompoundTag compound = stack.getTag();
					if (compound == null)
					{
						compound = new CompoundTag();
					}

					ListTag tagList = compound.getList("entity", 10);
					
					CompoundTag entityNBT = new CompoundTag();
					
					entityNBT.putString("entity", EntityType.getKey(target.getType()).toString());
					target.saveWithoutId(entityNBT);
					tagList.addTag(tagList.size(),entityNBT);
					
					target.remove(RemovalReason.KILLED);
					
					compound.put("entity", tagList);
					stack.setTag(compound);
				}
			}
		}
		
		return InteractionResult.PASS;
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		Player player = context.getPlayer();
		Level level = context.getLevel();
		InteractionHand hand = context.getHand();
		BlockPos spawn = context.getClickedPos();
		Direction facing = context.getClickedFace();
		ItemStack stack = context.getItemInHand();
		
		CompoundTag compound = stack.getTag();
		
		if(compound != null && !level.isClientSide() && player.experienceLevel >= 15)
		{
			ListTag tagList = compound.getList("entity", 10);
			CompoundTag entityNBT = tagList.getCompound(0);
			
	        EntityType<?> type = EntityType.byString(entityNBT.getString("entity")).orElse(null);
            if (type != null)
            {
            	if(type == EntityType.PHANTOM)
            	{
            		
            	}
            	Entity entity;
		        BlockPos blockPos = spawn.relative(facing);
            	
                entity = type.create(level);
                entity.load(entityNBT);
                
                entity.absMoveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0);
				level.addFreshEntity(entity);
            }
            
			stack.setTag(null);
			player.swing(hand);
			player.setItemInHand(hand, stack);
		}
		
		return InteractionResult.FAIL;
	}
	
	@Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tipFlag)
    {    	
		CompoundTag compound = stack.getTag();
		
		if (compound != null)
		{			
			if(Screen.hasShiftDown())
			{				
				tooltip.add(new TextComponent("Collected Entity: " + getID(0, stack)).withStyle(ChatFormatting.YELLOW));
			}
			else
			{
				tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.soul_wand.left_shift").withStyle(ChatFormatting.GRAY));
			}
		}
    }
	
    public String getID(int listValue, ItemStack stack)
    {
        return stack.getTag().getList("entity", 10).getCompound(listValue).getString("entity");
    }

    
}
