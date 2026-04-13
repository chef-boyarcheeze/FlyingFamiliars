package com.beesechurger.flyingfamiliars.tags;

import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.Map;

import static com.beesechurger.flyingfamiliars.util.FFConstants.*;

public class VitalityTagRef implements IStorageTagRef
{
    public static final VitalityTagRef INSTANCE = new VitalityTagRef();

//////////////////
/// Accessors: ///
//////////////////

/// Strings:

    @Override
    public String getEntryListName()
    {
        return STORAGE_FLUID_TAGNAME;
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
        return 0;
    }

    public int getMaxVolume()
    {
        return 100;
    }

/// Tags:

    @Override
    public ListTag getInitialEntryList(CompoundTag storageTag)
    {
        CompoundTag tag = new CompoundTag();

        // get (new) ListTag under "getEntryListName()" in 'tag', to place back into 'tag'
        NonNullList<CompoundTag> fluidTags = NonNullList.withSize(getMaxEntries(storageTag), new CompoundTag());
        ListTag tagList = tag.getList(getEntryListName(), ListTag.TAG_COMPOUND);

        for (int i = 0; i < getMaxEntries(storageTag); i++)
        {
            fluidTags.get(i).putString(STORAGE_FLUID_TYPE, VITALITY_TYPES.get(i));
            fluidTags.get(i).putInt(STORAGE_FLUID_STORAGE, 0);

            tagList.add(fluidTags.get(i));
        }

        return tagList;
    }

    @Override
    public CompoundTag getInitialSettingsTag(CompoundTag storageTag)
    {
        CompoundTag tag = new CompoundTag();
        tag.putInt(STORAGE_ENTRY_STORAGE_MAX, 1);

        return tag;
    }

/// Misc:

    public Map<String, Integer> getStoredVitality(CompoundTag storageTag)
    {
        ListTag entryList = getEntryList(storageTag);

        Map<String, Integer> entryMap = (Map) Util.make(Maps.newHashMap(), (map) -> {
            for(Tag tag : entryList)
            {
                String type = ((CompoundTag) tag).getString(STORAGE_FLUID_TYPE);
                Integer volume = ((CompoundTag) tag).getInt(type);

                if (volume > 0)
                {
                    map.put(type, volume);
                }
            }
        });

        return entryMap;
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