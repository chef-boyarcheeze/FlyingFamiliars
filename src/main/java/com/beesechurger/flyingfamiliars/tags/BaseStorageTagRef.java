package com.beesechurger.flyingfamiliars.tags;

import it.unimi.dsi.fastutil.chars.Char2IntOpenCustomHashMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public abstract class BaseStorageTagRef implements IStorageTagRef
{
    protected int maxEntries;

    public BaseStorageTagRef()
    {
        // by default infinite storage space
        setMaxEntries(0);
    }

    public BaseStorageTagRef(int maxEntries)
    {
        setMaxEntries(maxEntries);
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

///////////////
// Mutators: //
///////////////

// Integers:
    public void setMaxEntries(int maxEntries)
    {
        this.maxEntries = maxEntries;
    }
}
