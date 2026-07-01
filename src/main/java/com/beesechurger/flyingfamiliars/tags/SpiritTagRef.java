package com.beesechurger.flyingfamiliars.tags;

import net.minecraft.nbt.CompoundTag;

import static com.beesechurger.flyingfamiliars.util.FFConstants.*;

public class SpiritTagRef implements IStorageTagRef
{
    public static final SpiritTagRef INSTANCE = new SpiritTagRef();

//////////////////
/// Accessors: ///
//////////////////

/// Strings:

    @Override
    public String getEntryListName()
    {
        return STORAGE_SPIRIT_TAGNAME;
    }

/// Booleans:

    public boolean getManipMode(CompoundTag storageTag)
    {
        CompoundTag settingsTag = getSettingsTag(storageTag);

        if (settingsTag.contains(STORAGE_ENTRY_MANIP_MODE))
        {
            return (settingsTag.getBoolean(STORAGE_ENTRY_MANIP_MODE));
        }

        return false;
    }

/// Integers:

    @Override
    public int getMaxEntries(CompoundTag storageTag)
    {
        CompoundTag settingsTag = getSettingsTag(storageTag);

        if (settingsTag.contains(STORAGE_ENTRY_STORAGE_MAX))
        {
            return settingsTag.getInt(STORAGE_ENTRY_STORAGE_MAX);
        }

        return 0;
    }

    public int getMaxVolume(CompoundTag storageTag)
    {
        CompoundTag settingsTag = getSettingsTag(storageTag);

        if (settingsTag.contains(STORAGE_SPIRIT_STORAGE_MAX))
        {
            return settingsTag.getInt(STORAGE_SPIRIT_STORAGE_MAX);
        }

        return 0;
    }

/// Tags:

    @Override
    public CompoundTag getInitialSettingsTag(CompoundTag storageTag)
    {
        CompoundTag tag = new CompoundTag();
        tag.putInt(STORAGE_ENTRY_STORAGE_MAX, 3);
        tag.putInt(STORAGE_SPIRIT_STORAGE_MAX, 100);

        return tag;
    }

/////////////////
/// Mutators: ///
/////////////////

/// Booleans:

    public void toggleManipMode(CompoundTag storageTag)
    {
        CompoundTag settingsTag = getSettingsTag(storageTag);
        settingsTag.putBoolean(STORAGE_ENTRY_MANIP_MODE, !getManipMode(storageTag));

        storageTag.put(STORAGE_SETTINGS, settingsTag);
    }
}