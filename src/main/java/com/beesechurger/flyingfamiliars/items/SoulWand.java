package com.beesechurger.flyingfamiliars.items;

import java.util.List;

import javax.annotation.Nullable;

import com.beesechurger.flyingfamiliars.entity.FFEntityTypes;

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
import net.minecraft.world.level.block.Block;

public class SoulWand extends Item
{
	public static EntityType<?>[] PASSIVE_STANDARD = {EntityType.PIG, EntityType.COW, EntityType.SHEEP, EntityType.CHICKEN};
	public static EntityType<?>[] PASSIVE_JUNGLE = {EntityType.PANDA, EntityType.PARROT, EntityType.OCELOT};
	public static EntityType<?>[] PASSIVE_WATER = {EntityType.TURTLE, EntityType.DOLPHIN, EntityType.COD, EntityType.SALMON, EntityType.TROPICAL_FISH};
	
	public static EntityType<?>[] TRANSFORM_WHITELIST = {EntityType.PHANTOM, EntityType.PARROT, EntityType.AXOLOTL};

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
					
					player.swing(hand);
					player.setItemInHand(hand, stack);
					
					return InteractionResult.SUCCESS;
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
		Block spawnBlock = level.getBlockState(spawn).getBlock();
		
		if(compound != null && !level.isClientSide() && (player.experienceLevel >= 15 || player.getAbilities().instabuild))
		{			
			Entity entity;
	        BlockPos blockPos = spawn.relative(facing);
	        
	        EntityType<?> type = getStoredType(stack);
			
        	if(type == EntityType.PHANTOM)
        	{
        		entity = FFEntityTypes.CLOUD_RAY.get().create(level);
        	}
        	else
        	{
        		entity = PASSIVE_STANDARD[(int) Math.floor(Math.random()*4)].create(level);
        	}
        	            
            entity.absMoveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0);
			level.addFreshEntity(entity);
            
			stack.setTag(null);
			player.swing(hand);
			player.setItemInHand(hand, stack);
			
			return InteractionResult.SUCCESS;
		}
		
		return InteractionResult.FAIL;
	}
	
	@Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tipFlag)
    {			
		tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.soul_wand.tooltip").withStyle(ChatFormatting.GRAY));
    }
	
	@Override
	public boolean isFoil(ItemStack stack)
	{
		return getListTag(stack).size() == 1;
	}
	
	public ListTag getListTag(ItemStack stack)
    {
    	CompoundTag compound = stack.getTag();
    	ListTag list = new ListTag();
    	
    	if(compound != null) list = compound.getList("entity", 10);
    	
    	return list;
    }
	
	public EntityType<?> getStoredType(ItemStack stack)
	{
		ListTag list = getListTag(stack);
		
		if(list.size() != 0)
		{
			return EntityType.byString(list.getCompound(0).getString("entity")).orElse(null);
		}
		
		return null;
	}
}
