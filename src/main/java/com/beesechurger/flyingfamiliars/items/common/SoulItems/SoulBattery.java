package com.beesechurger.flyingfamiliars.items.common.SoulItems;

import com.beesechurger.flyingfamiliars.entity.common.projectile.SoulWand.capture.CaptureProjectile;
import com.beesechurger.flyingfamiliars.items.EntityTagItemHelper;
import com.beesechurger.flyingfamiliars.items.FFItems;
import com.beesechurger.flyingfamiliars.keys.FFKeys;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.ITEM_MODE_TAGNAME;

public class SoulBattery extends BaseEntityTagItem
{
    public SoulBattery(Properties properties, int capacityMod)
    {
        super(properties);
        this.capacityMod = capacityMod;
    }
}
