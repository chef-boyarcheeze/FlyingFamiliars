package com.beesechurger.flyingfamiliars.items.custom.SoulItems;

import com.beesechurger.flyingfamiliars.items.EntityTagItemHelper;
import com.beesechurger.flyingfamiliars.items.FFItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.ITEM_MODE_TAGNAME;

public class SoulBattery extends BaseEntityTagItem
{
    public SoulBattery(Properties properties, int capacityMod)
    {
        super(properties);
        this.capacityMod = capacityMod;
    }

    private boolean swapSoulList(Player player)
    {
        ItemStack battery = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack wand = player.getItemInHand(InteractionHand.OFF_HAND);

        if(wand.getTag() == null)
            EntityTagItemHelper.populateTag(wand);

        if(battery.getItem() instanceof SoulBattery)
        {
            if(wand.getItem() instanceof IModeCycleItem)
            {
                CompoundTag batteryTag = battery.getTag();
                CompoundTag wandTag = wand.getTag();

                int mode = wandTag.getInt(ITEM_MODE_TAGNAME);

                wandTag.remove(ITEM_MODE_TAGNAME);
                batteryTag.putInt(ITEM_MODE_TAGNAME, mode);

                battery.setTag(wandTag);
                wand.setTag(batteryTag);

                return true;
            }
            else if(wand.getItem() == FFItems.SOUL_WAND.get())
            {
                CompoundTag batteryTag = battery.getTag();
                CompoundTag wandTag = wand.getTag();

                battery.setTag(wandTag);
                wand.setTag(batteryTag);

                return true;
            }
        }

        return false;
    }
}
