package com.beesechurger.flyingfamiliars.tags;

import it.unimi.dsi.fastutil.chars.Char2IntOpenCustomHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public abstract class BaseStorageTagRef implements IStorageTagRef
{
    protected final int maxEntries;

    public BaseStorageTagRef()
    {
        this.maxEntries = 1;
    }

    public BaseStorageTagRef(int maxEntries)
    {
        this.maxEntries = maxEntries;
    }

////////////////
// Accessors: //
////////////////

// Integers:
    @Override
    public int getMaxEntries()
    {
        return maxEntries;
    }
}
