package com.beesechurger.flyingfamiliars.items.SpiritCrystal;

import java.util.List;

import javax.annotation.Nullable;

import com.beesechurger.flyingfamiliars.sound.FFSounds;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class SpiritCrystal extends Item 
{	
	public SpiritCrystal(Properties properties)
	{
		super(properties);
	}

	public InteractionResult useOn(UseOnContext context) 
	{
		if(release(context)) return InteractionResult.SUCCESS;
		
		return InteractionResult.FAIL;
	}
	
	public boolean release(UseOnContext context)
	{
		ItemStack stack = context.getItemInHand();
		BlockPos pos = context.getClickedPos();
		Direction facing = context.getClickedFace();
		Level level = context.getLevel();
		Player player = context.getPlayer();

		CompoundTag compound = stack.getTag();
		if (compound != null)
		{
			ListTag tagList = compound.getList("entity", 10);
			if (tagList.size() > 0)
			{
				CompoundTag entityNBT = tagList.getCompound(tagList.size()-1);
				tagList.remove(tagList.size()-1);
				
		        EntityType<?> type = EntityType.byString(entityNBT.getString("entity")).orElse(null);
	            if (type != null)
	            {
	            	Entity entity;
			        BlockPos blockPos = pos.relative(facing);
	            	
	                entity = type.create(level);
	                entity.load(entityNBT);
	                
	                entity.absMoveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0);
					level.addFreshEntity(entity);
					
					level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 0.5f, FFSounds.getPitch());
				    
				    for(int i = 0; i < 360; i++)
				    {
				    	level.addParticle(ParticleTypes.END_ROD, entity.getX(), entity.getY(), entity.getZ(), 0.1 * Math.cos(i * Math.PI), 0.1, 0.1 * Math.sin(i));
				    	level.addParticle(ParticleTypes.END_ROD, entity.getX(), entity.getY(), entity.getZ(), 0.1 * Math.sin(i), 0.1, 0.1 * Math.cos(i * Math.PI));
				    }
					
					return true;
	            }
			}
		}

	    return false;
	}
	
	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand)
	{
		if(capture(stack, playerIn, target, hand)) return InteractionResult.SUCCESS;
		
		return InteractionResult.FAIL;
	}
	
	public boolean capture(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand)
	{
		if (!(target instanceof Player) && target.canChangeDimensions() && target.isAlive() && target instanceof Mob)
		{
			CompoundTag compound = stack.getTag();
			if (compound == null)
			{
				compound = new CompoundTag();
			}

			ListTag tagList = compound.getList("entity", 10);
			if (tagList.size() < 1)
			{
				CompoundTag entityNBT = new CompoundTag();
				
				entityNBT.putString("entity", EntityType.getKey(target.getType()).toString());
				target.saveWithoutId(entityNBT);
				tagList.addTag(tagList.size(),entityNBT);
				
				Level level = target.getLevel();
				
				level.playSound((Player)null, target.getX(), target.getY(), target.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 0.5f, FFSounds.getPitch());
			    
			    for(int i = 0; i < 360; i++)
			    {
			    	level.addParticle(ParticleTypes.CLOUD, target.getX(), target.getY(), target.getZ(), 0.1 * Math.cos(i), 0.1, 0.1 * Math.sin(i));
			    }
				
				target.remove(RemovalReason.KILLED);
			}
			else
			{
				return false;
			}
			
			compound.put("entity", tagList);
			stack.setTag(compound);
			
			playerIn.swing(hand);
	        playerIn.setItemInHand(hand, stack);
			
			return true;
		}
		
		return false;
	}
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tipFlag)
    {
    	int entityCount = 0;
    	
		CompoundTag compound = stack.getTag();
		
		if (compound != null)
		{
			ListTag tagList = compound.getList("entity", 10);
			entityCount = tagList.size();
			
			if(entityCount != 0)
			{
				tooltip.add(new TextComponent("Entity: " + getID(0, stack)).withStyle(ChatFormatting.YELLOW));
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
    
    public String getID(int listValue, ItemStack stack)
    {
        return stack.getTag().getList("entity", 10).getCompound(listValue).getString("entity");
    }
    
    @Override
    public Component getName(ItemStack stack) 
    {
        if (getListTag(stack).size() == 0) return new TranslatableComponent(super.getDescriptionId(stack));
            
        return new TranslatableComponent(super.getDescriptionId(stack)).append(" (" + getID(0, stack) + ")");
    }
    
    public ListTag getListTag(ItemStack stack)
    {
    	CompoundTag compound = stack.getTag();
    	ListTag list = new ListTag();
    	
    	if(compound != null) list = compound.getList("entity", 10);
    	
    	return list;
    }
    
    @Override
    public boolean isFoil(ItemStack stack)
    {
    	return getListTag(stack).size() == 1;
    }
}
