package com.beesechurger.flyingfamiliars.items;

import java.util.List;

import javax.annotation.Nullable;

import com.beesechurger.flyingfamiliars.entity.custom.projectile.SummoningBellProjectile;

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
	
	/*@SuppressWarnings("resource")
	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand)
	{
		if (target.getCommandSenderWorld().isClientSide) return InteractionResult.FAIL;
		if (!(target instanceof Player || !target.canChangeDimensions() || !target.isAlive()))
		{
			CompoundTag compound = stack.getTag();
			if (compound == null)
			{
				compound = new CompoundTag();
			}

			ListTag tagList = compound.getList("entity", 10);
			if (tagList.size() < 3)
			{
				CompoundTag entityNBT = new CompoundTag();
				
				entityNBT.putString("entity", EntityType.getKey(target.getType()).toString());
				target.saveWithoutId(entityNBT);
				tagList.addTag(tagList.size(),entityNBT);
				
				target.remove(RemovalReason.KILLED);
				//entity.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 0.5f, 1.5F);
			}
			else
			{
				//entity.world.playSound(null, player.getPosition(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 0.5f, 1.5F);
			}

			compound.put("entity", tagList);
			stack.setTag(compound);

			playerIn.swing(hand);
	        playerIn.setItemInHand(hand, stack);
			
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.SUCCESS;
	}*/
	
	/*@SuppressWarnings("resource")
	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getClickedFace();
        Level worldIn = context.getLevel();
        ItemStack stack = context.getItemInHand();
		
        if (player.getCommandSenderWorld().isClientSide) return InteractionResult.FAIL;

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
	            	
	                entity = type.create(worldIn);
	                entity.load(entityNBT);
	                
	                entity.absMoveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0);
					worldIn.addFreshEntity(entity);
	            }

				//playerIn.world.playSound(null, playerIn.getPosition(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 0.5f, 0.5F);
			}
			else
			{
				//playerIn.world.playSound(null, playerIn.getPosition(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 0.5f, 0.2F);
			}
		}
		
		return InteractionResult.SUCCESS;
	}

    public boolean containsEntity(ItemStack stack)
    {
        return !stack.isEmpty() && stack.hasTag() && stack.getTag().contains("entity");
    }

    @Nullable
    public Entity getEntityFromStack(ItemStack stack, Level world, boolean withInfo)
    {
        if (stack.hasTag()) 
        {
            EntityType<?> type = EntityType.byString(stack.getTag().getString("entity")).orElse(null);
            if (type != null) 
            {
                Entity entity = type.create(world);
                if (withInfo) entity.load(stack.getTag());
                return entity;
            }
        }
        return null;
    }*/

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
	       //ding.setItem(new ItemStack(Items.MUSIC_DISC_PIGSTEP));
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
