package com.beesechurger.flyingfamiliars.items.custom;

import java.util.List;

import javax.annotation.Nullable;

import com.beesechurger.flyingfamiliars.sound.FFSounds;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
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

public class SpecterMote extends Item
{
	public static EntityType<?>[] FARM = {EntityType.PIG, EntityType.COW, EntityType.SHEEP, EntityType.CHICKEN};
	public static EntityType<?>[] CIVILIZED = {EntityType.VILLAGER, EntityType.CAT, EntityType.IRON_GOLEM};
	public static EntityType<?>[] DIM = {EntityType.BAT, EntityType.GLOW_SQUID, EntityType.AXOLOTL};
	public static EntityType<?>[] GRASSY = {EntityType.HORSE, EntityType.DONKEY, EntityType.RABBIT, EntityType.LLAMA};
	public static EntityType<?>[] WARM = {EntityType.PANDA, EntityType.PARROT, EntityType.OCELOT};
	public static EntityType<?>[] WATERY = {EntityType.TURTLE, EntityType.DOLPHIN, EntityType.COD, EntityType.SALMON, EntityType.TROPICAL_FISH, EntityType.PUFFERFISH, EntityType.SQUID};
	
	public SpecterMote(Properties properties)
	{
		super(properties);
	}
	
	public InteractionResult useOn(UseOnContext context)
	{
	    EntityType<?> type = getEntityType();
	    
	    if (type != null)
	    {
	    	ItemStack stack = context.getItemInHand();
			BlockPos pos = context.getClickedPos();
			Direction facing = context.getClickedFace();
			Level level = context.getLevel();
			Player player = context.getPlayer();
	    	
	        BlockPos blockPos = pos.relative(facing);
            Entity entity = type.create(level);
            
            entity.absMoveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0);
			level.addFreshEntity(entity);
			
			stack.shrink(1);
			player.awardStat(Stats.ITEM_USED.get(this));
		    
			doEffects(level, player, entity);
		    
		    return InteractionResult.SUCCESS;
	    }

	    return InteractionResult.FAIL;
	}
    
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tipFlag)
    {
    	//tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.spirit_crystal.tooltip").withStyle(ChatFormatting.GRAY));
    }
    
    @Override
    public boolean isFoil(ItemStack stack)
    {
    	return getEntityType() != null; 
    }
    
    public EntityType<?> getEntityType()
    {
    	return EntityType.EXPERIENCE_ORB;
    }
    
    public void doEffects(Level level, Player player, Entity entity)
	{
		level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), FFSounds.SOUL_WAND_THROW.get(), SoundSource.PLAYERS, 1.0f, FFSounds.getPitch());
		
		for(int i = 0; i < 360; i++)
	    {
	    	level.addParticle(ParticleTypes.END_ROD, entity.getX(), entity.getY(), entity.getZ(), 0.1 * Math.cos(i * Math.PI), 0.1, 0.1 * Math.sin(i));
	    	level.addParticle(ParticleTypes.END_ROD, entity.getX(), entity.getY(), entity.getZ(), 0.1 * Math.sin(i), 0.1, 0.1 * Math.cos(i * Math.PI));
	    }
	}
}
