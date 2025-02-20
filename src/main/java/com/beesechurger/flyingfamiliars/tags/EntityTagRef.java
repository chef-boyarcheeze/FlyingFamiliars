package com.beesechurger.flyingfamiliars.tags;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import static com.beesechurger.flyingfamiliars.util.FFConstants.*;

public class EntityTagRef implements IStorageTagRef
{
    public static final EntityTagRef INSTANCE = new EntityTagRef();

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

    public boolean getManipMode(CompoundTag storageTag)
    {
        CompoundTag settingsTag = getSettingsTag(storageTag);

        if (settingsTag.contains(STORAGE_ENTRY_MANIP_MODE))
        {
            return settingsTag.getBoolean(STORAGE_ENTRY_MANIP_MODE);
        }

        return false;
    }

// Tags:
    @Override
    public CompoundTag getInitialSettingsTag(CompoundTag storageTag)
    {
        CompoundTag tag = new CompoundTag();
        tag.putInt(STORAGE_ENTRY_STORAGE_MAX, 3);

        return tag;
    }

///////////////
// Mutators: //
///////////////

// Booleans:
    public void toggleManipMode(CompoundTag storageTag)
    {
        CompoundTag settingsTag = getSettingsTag(storageTag);
        settingsTag.putBoolean(STORAGE_ENTRY_MANIP_MODE, !getManipMode(storageTag));

        storageTag.put(STORAGE_SETTINGS, settingsTag);
    }
}