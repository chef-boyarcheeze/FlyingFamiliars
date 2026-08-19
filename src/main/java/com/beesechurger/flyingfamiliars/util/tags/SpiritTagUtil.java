package com.beesechurger.flyingfamiliars.util.tags;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.HashMap;
import java.util.Map;

import static com.beesechurger.flyingfamiliars.util.FFConstants.*;

public class SpiritTagUtil implements IStorageTagUtil
{
    public static final SpiritTagUtil INSTANCE = new SpiritTagUtil();

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

    public boolean isEntryEmpty(CompoundTag entryTag)
    {
        if (hasTag(entryTag) && entryTag.contains(STORAGE_SPIRIT_TYPE))
        {
            return entryTag.getInt(STORAGE_SPIRIT_STORAGE) == 0;
        }

        return false;
    }

    public boolean isEntryFull(CompoundTag entryTag, int maxStorage)
    {
        if (hasTag(entryTag) && entryTag.contains(STORAGE_SPIRIT_TYPE))
        {
            return entryTag.getInt(STORAGE_SPIRIT_STORAGE) >= maxStorage;
        }

        return false;
    }

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

    public int getMaxStorage(CompoundTag storageTag)
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

/// Misc:

    public Map<String, Integer> getSpiritContents(CompoundTag storageTag)
    {
        Map<String, Integer> spiritContents = new HashMap<>();

        if (!isEmpty(storageTag))
        {
            for (Tag tag : getEntryList(storageTag))
            {
                CompoundTag entryTag = (CompoundTag) tag;

                spiritContents.put(entryTag.getString(STORAGE_SPIRIT_TYPE), entryTag.getInt(STORAGE_SPIRIT_STORAGE));
            }
        }

        return spiritContents;
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

////////////////////
/// Tag Actions: ///
////////////////////

    public boolean addSpirit(CompoundTag entryTag, int maxStorage, int addSpiritAmount)
    {
        if (!isEntryFull(entryTag, maxStorage))
        {
            int endSpiritAmount = entryTag.getInt(STORAGE_SPIRIT_STORAGE) + addSpiritAmount;

            if (endSpiritAmount <= maxStorage)
            {
                entryTag.putInt(STORAGE_SPIRIT_STORAGE, endSpiritAmount);

                return true;
            }
        }

        return false;
    }

    public boolean moveSpirit(CompoundTag sourceEntryTag, CompoundTag targetEntryTag, int targetMaxStorage)
    {
        if (!isEntryEmpty(sourceEntryTag) && !isEntryFull(targetEntryTag, targetMaxStorage))
        {
            int sourceSpirit = sourceEntryTag.getInt(STORAGE_SPIRIT_STORAGE);
            int targetSpirit = targetEntryTag.getInt(STORAGE_SPIRIT_STORAGE);
            int moveSpiritAmount = Math.min(sourceSpirit, (targetMaxStorage - targetSpirit));

            return removeSpirit(sourceEntryTag, moveSpiritAmount) && addSpirit(targetEntryTag, targetMaxStorage, moveSpiritAmount);
        }

        return false;
    }

    public boolean removeSpirit(CompoundTag entryTag, int removeSpiritAmount)
    {
        if (!isEntryEmpty(entryTag))
        {
            int endSpiritAmount = entryTag.getInt(STORAGE_SPIRIT_STORAGE) - removeSpiritAmount;

            if (endSpiritAmount >= 0)
            {
                entryTag.putInt(STORAGE_SPIRIT_STORAGE, endSpiritAmount);

                return true;
            }
        }

        return false;
    }
}