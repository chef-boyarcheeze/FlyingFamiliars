package com.beesechurger.flyingfamiliars.util.tags;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import static com.beesechurger.flyingfamiliars.util.FFConstants.*;

public interface IStorageTagUtil
{
//////////////////
/// Accessors: ///
//////////////////

/// Strings:

    public String getEntryListName();

/// Booleans:

    default boolean hasTag(CompoundTag tag)
    {
        return tag != null && tag != new CompoundTag();
    }

    default boolean isEmpty(CompoundTag storageTag)
    {
        return getEntryList(storageTag).isEmpty();
    }

    default boolean isFull(CompoundTag storageTag)
    {
        return getEntryList(storageTag).size() == getMaxEntries(storageTag) && getMaxEntries(storageTag) > 0;
    }

/// Integers:

    default int getMaxEntries(CompoundTag storageTag)
    {
        return getSettingsTag(storageTag).getInt(STORAGE_ENTRY_STORAGE_MAX);
    }

    default int getEntryCount(CompoundTag storageTag)
    {
        return getEntryList(storageTag).size();
    }

/// Tags:

    default CompoundTag getStorageNameTag(CompoundTag storageTag)
    {
        return getOrCreateTag(storageTag).getCompound(getEntryListName());
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/// Initial tag setup functions:

    default CompoundTag getOrCreateTag(CompoundTag storageTag)
    {
        if(!hasTag(storageTag) || !storageTag.contains(getEntryListName()))
        {
            storageTag = storageTag != null ? storageTag : new CompoundTag();

            CompoundTag tag = new CompoundTag();
            tag.put(STORAGE_ENTRY_LIST, getInitialEntryList(storageTag));
            tag.put(STORAGE_SETTINGS, getInitialSettingsTag(storageTag));

            storageTag.put(getEntryListName(), tag);
        }

        return storageTag;
    }

    default ListTag getInitialEntryList(CompoundTag storageTag)
    {
        return storageTag.getCompound(getEntryListName()).getList(STORAGE_ENTRY_LIST, ListTag.TAG_COMPOUND);
    }

    public CompoundTag getInitialSettingsTag(CompoundTag storageTag);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    default ListTag getEntryList(CompoundTag storageTag)
    {
        return getStorageNameTag(storageTag).getList(STORAGE_ENTRY_LIST, ListTag.TAG_COMPOUND);
    }

    default CompoundTag getSettingsTag(CompoundTag storageTag)
    {
        return getStorageNameTag(storageTag).getCompound(STORAGE_SETTINGS);
    }

    default CompoundTag getSelectedEntry(CompoundTag storageTag)
    {
        ListTag entryList = getEntryList(storageTag);

        if(!isEmpty(storageTag))
        {
            return entryList.getCompound(getEntryCount(storageTag) - 1);
        }

        return new CompoundTag();
    }

/////////////////
/// Mutators: ///
/////////////////

/// Integers:

    default void setMaxEntries(CompoundTag storageTag, int newSize)
    {
        CompoundTag settingsTag = getSettingsTag(storageTag);
        settingsTag.putInt(STORAGE_ENTRY_STORAGE_MAX, newSize);

        getStorageNameTag(storageTag).put(STORAGE_SETTINGS, settingsTag);
    }

////////////////////
/// Tag Actions: ///
////////////////////

    default boolean addEntry(CompoundTag storageTag, CompoundTag entryTag)
    {
        if (!isFull(storageTag) && hasTag(entryTag))
        {
            return getEntryList(storageTag).add(entryTag);
        }

        return false;
    }

    default boolean moveEntry(CompoundTag sourceTag, CompoundTag targetTag)
    {
        if (!isEmpty(sourceTag) && !isFull(targetTag))
        {
            CompoundTag entryTag = getSelectedEntry(sourceTag);

            if (hasTag(entryTag))
            {
                return removeEntry(sourceTag, entryTag) && addEntry(targetTag, entryTag);
            }
        }

        return false;
    }

    default boolean moveEntry(CompoundTag sourceTag, CompoundTag targetTag, CompoundTag entryTag)
    {
        if (!isEmpty(sourceTag) && !isFull(targetTag) && hasTag(entryTag))
        {
            return removeEntry(sourceTag, entryTag) && addEntry(targetTag, entryTag);
        }

        return false;
    }

    default boolean removeEntry(CompoundTag storageTag, CompoundTag entryTag)
    {
        if (!isEmpty(storageTag) && hasTag(entryTag))
        {
            return getEntryList(storageTag).remove(entryTag);
        }

        return false;
    }
}