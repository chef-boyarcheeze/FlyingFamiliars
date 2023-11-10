package com.beesechurger.flyingfamiliars.item.common.SoulItems;

import com.beesechurger.flyingfamiliars.item.FFItems;
import com.beesechurger.flyingfamiliars.sound.FFSounds;
import com.beesechurger.flyingfamiliars.util.FFValueConstants;
import net.minecraft.world.item.ItemStack;

public class SoulBattery extends BaseEntityTagItem
{
    public SoulBattery(Properties properties, int capacityMod)
    {
        super(properties);
        this.capacityMod = capacityMod;
    }
}
