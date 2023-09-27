package com.beesechurger.flyingfamiliars.items.custom.SoulItems.SoulStaff;

import com.beesechurger.flyingfamiliars.items.EntityTagItemHelper;
import com.beesechurger.flyingfamiliars.items.custom.SoulItems.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.items.custom.SoulItems.ISoulCycleItem;
import com.beesechurger.flyingfamiliars.keys.FFKeys;
import com.beesechurger.flyingfamiliars.entity.custom.projectile.SoulWandProjectile;
import com.beesechurger.flyingfamiliars.sound.FFSounds;

import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SoulWand extends BaseEntityTagItem
{
	public SoulWand(Properties properties)
	{
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		EntityTagItemHelper.ensureTagPopulated(stack);

	    level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), FFSounds.SOUL_WAND_THROW.get(), SoundSource.NEUTRAL, 0.5F, FFSounds.getPitch());
	    
	    if (!level.isClientSide())
		{
			SoulWandProjectile capture = new SoulWandProjectile(level, player, stack, FFKeys.soul_wand_shift.isDown());
	        capture.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.2F, 1.0F);
	        level.addFreshEntity(capture);
	    }

	    player.awardStat(Stats.ITEM_USED.get(this));

	    return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}
}
