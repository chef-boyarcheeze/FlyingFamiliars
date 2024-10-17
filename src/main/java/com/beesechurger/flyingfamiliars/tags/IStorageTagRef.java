package com.beesechurger.flyingfamiliars.tags;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public interface IStorageTagRef
{
////////////////
// Accessors: //
////////////////

// Strings:
    public String getEntryListName();

// Booleans:
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
        return getEntryList(storageTag).size() == getMaxEntries();
    }

// Integers:
    public int getMaxEntries();

    default int getEntryCount(CompoundTag storageTag)
    {
        return getEntryList(storageTag).size();
    }

// Tags:
    default CompoundTag getOrCreateTag(CompoundTag storageTag)
    {
        if(!hasTag(storageTag) || !storageTag.contains(getEntryListName()))
        {
            storageTag = storageTag != null ? storageTag : new CompoundTag();
            storageTag.put(getEntryListName(), getInitialTagList());
        }

        return storageTag;
    }

    default ListTag getInitialTagList()
    {
        CompoundTag tag = new CompoundTag();

        // get (new) ListTag under "getEntryListName()" in 'tag', to place back into 'tag'
        ListTag tagList = tag.getList(getEntryListName(), ListTag.TAG_COMPOUND);

        return tagList;
    }

    default ListTag getEntryList(CompoundTag storageTag)
    {
        CompoundTag tag = getOrCreateTag(storageTag);

        return tag.getList(getEntryListName(), ListTag.TAG_COMPOUND);
    }

    default CompoundTag getSelectedEntry(CompoundTag storageTag)
    {
        ListTag entryList = getEntryList(storageTag);

        if(!isEmpty(storageTag))
            return entryList.getCompound(getEntryCount(storageTag) - 1);

        return new CompoundTag();
    }

//////////////////
// Tag actions: //
//////////////////

    default boolean pushEntry(CompoundTag storageTag, CompoundTag entryTag)
    {
        if (!isFull(storageTag) && hasTag(entryTag))
        {
            // get current entry list, add incoming entry tag, and update item stack's tag
            ListTag entryList = getEntryList(storageTag);
            entryList.add(entryTag);

            // hopefully actually saves to 'storageTag'
            storageTag.put(getEntryListName(), entryList);

            return true;
        }

        return false;
    }

    default CompoundTag popEntry(CompoundTag storageTag)
    {
        if (!isEmpty(storageTag))
        {
            // get current entry list, remove selected entry tag, and update storedTag
            ListTag entryList = getEntryList(storageTag);
            CompoundTag entryTag = (CompoundTag) entryList.remove(getEntryCount(storageTag) - 1);

            // hopefully actually saves to 'storageTag'
            storageTag.put(getEntryListName(), entryList);

            return entryTag;
        }

        return new CompoundTag();
    }
}