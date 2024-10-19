package com.beesechurger.flyingfamiliars.item.common;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public interface IEntryManipModeItem
{
////////////////
// Accessors: //
////////////////

// Booleans:
    boolean getManipMode(ItemStack stack);

//////////////////
// Tag actions: //
//////////////////

    void toggleManipMode(ItemStack stack);
}
