package com.beesechurger.flyingfamiliars.items;

import java.util.List;

import javax.annotation.Nullable;

import com.beesechurger.flyingfamiliars.entity.FFEntityTypes;
import com.beesechurger.flyingfamiliars.sound.FFSounds;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
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
		ItemStack stack = context.getItemInHand();
		BlockPos pos = context.getClickedPos();
		Direction facing = context.getClickedFace();
		Level level = context.getLevel();
		Player player = context.getPlayer();
	    
	    EntityType<?> type = getEntityType();
	    
	    if (type != null)
	    {
	        BlockPos blockPos = pos.relative(facing);
            Entity entity = type.create(level);
            
            entity.absMoveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0);
			level.addFreshEntity(entity);
			
			stack.shrink(1);
			player.awardStat(Stats.ITEM_USED.get(this));
			
			level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), FFSounds.SPIRIT_CRYSTAL_THROW.get(), SoundSource.NEUTRAL, 0.5f, FFSounds.getPitch());
		    
		    for(int i = 0; i < 360; i++)
		    {
		    	level.addParticle(ParticleTypes.END_ROD, entity.getX(), entity.getY(), entity.getZ(), 0.1 * Math.cos(i * Math.PI), 0.1, 0.1 * Math.sin(i));
		    	level.addParticle(ParticleTypes.END_ROD, entity.getX(), entity.getY(), entity.getZ(), 0.1 * Math.sin(i), 0.1, 0.1 * Math.cos(i * Math.PI));
		    }
	    }

	    return InteractionResult.SUCCESS;
	}
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tipFlag)
    {
    	tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.spirit_crystal.tooltip").withStyle(ChatFormatting.GRAY));
    }
    
    public EntityType<?> getEntityType()
    {
    	return FFEntityTypes.getRandomEntityType();
    }
}
