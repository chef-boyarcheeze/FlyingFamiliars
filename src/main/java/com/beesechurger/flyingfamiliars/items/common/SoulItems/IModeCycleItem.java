package com.beesechurger.flyingfamiliars.items.common.SoulItems;

import com.beesechurger.flyingfamiliars.items.common.SoulItems.SoulWand.VoidShard;
import net.minecraft.world.item.ItemStack;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.ITEM_MODE_TAGNAME;

public interface IModeCycleItem
{
    default int getMode(ItemStack stack)
    {
        if(!stack.hasTag())
            return 0;

        return stack.getTag().getInt(ITEM_MODE_TAGNAME);
    }

    default void cycleMode(ItemStack stack, boolean shiftDown)
    {
        int mode = getMode(stack);

        if(modeCount(stack) <= 1)
            return;
        mode += shiftDown ? -1 : 1;

        if(mode == modeCount(stack))
            mode = 0;
        else if(mode < 0)
            mode = modeCount(stack) - 1;

        stack.getTag().putInt(ITEM_MODE_TAGNAME, mode);
    }

    private int modeCount(ItemStack stack)
    {
        return 3;
    }
}
