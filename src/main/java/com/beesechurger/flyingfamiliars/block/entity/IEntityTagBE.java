package com.beesechurger.flyingfamiliars.block.entity;

import net.minecraft.nbt.CompoundTag;

import static com.beesechurger.flyingfamiliars.util.FFConstants.*;

public interface IEntityTagBE extends IStorageTagBE
{
////////////////
// Accessors: //
////////////////

// Strings:
    @Override
    default String getEntryListName()
    {
        return STORAGE_ENTITY_TAGNAME;
    }

    default String getEntityID(CompoundTag tag)
    {
        if (tag != null && tag.contains(getEntryListName()))
            return tag.getString(getEntryListName());

        return STORAGE_EMPTY;
    }

// Booleans:
    default Boolean isEntityTamed(CompoundTag tag)
    {
        return tag != null && tag.contains("Owner");
    }
}
