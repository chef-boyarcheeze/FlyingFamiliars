package com.beesechurger.flyingfamiliars.tags;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import static com.beesechurger.flyingfamiliars.util.FFConstants.*;

public class EntityTagRef extends BaseStorageTagRef
{
    public EntityTagRef()
    {
        super();
    }

    public EntityTagRef(int maxEntries)
    {
        super(maxEntries);
    }

////////////////
// Accessors: //
////////////////

// Strings:
    @Override
    public String getEntryListName()
    {
        return STORAGE_ENTITY_TAGNAME;
    }

    public static String getEntityID(CompoundTag tag)
    {
        if (tag != null && tag.contains(STORAGE_ENTITY_TYPE))
            return tag.getString(STORAGE_ENTITY_TYPE);

        return STORAGE_EMPTY;
    }

// Booleans:
    public static Boolean isEntityTamed(CompoundTag tag)
    {
        return tag != null && tag.contains("Owner");
    }
}