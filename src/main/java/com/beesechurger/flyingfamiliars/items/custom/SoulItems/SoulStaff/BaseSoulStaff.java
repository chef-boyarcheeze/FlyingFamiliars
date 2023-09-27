package com.beesechurger.flyingfamiliars.items.custom.SoulItems.SoulStaff;

import com.beesechurger.flyingfamiliars.entity.custom.projectile.SoulWandProjectile;
import com.beesechurger.flyingfamiliars.items.custom.SoulItems.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.items.custom.SoulItems.IModeCycleItem;
import com.beesechurger.flyingfamiliars.items.custom.SoulItems.ISoulCycleItem;
import com.beesechurger.flyingfamiliars.keys.FFKeys;
import com.beesechurger.flyingfamiliars.sound.FFSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BaseSoulStaff extends BaseEntityTagItem implements IModeCycleItem
{
    public BaseSoulStaff(Properties properties)
    {
        super(properties);
    }
}
