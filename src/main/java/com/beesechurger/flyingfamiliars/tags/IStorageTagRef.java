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
        return getEntryList(storageTag).size() == getMaxEntries() && getMaxEntries() > 0;
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

    default boolean addEntry(CompoundTag storageTag, CompoundTag entryTag)
    {
        if (!isFull(storageTag) && hasTag(entryTag))
        {
            // get current entry list, add incoming entry tag, and update item stack's tag
            ListTag entryList = getEntryList(storageTag);
            entryList.add(entryTag);

            storageTag.put(getEntryListName(), entryList);

            return true;
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

    default boolean removeEntry(CompoundTag storageTag, CompoundTag entryTag)
    {
        if (!isEmpty(storageTag) && hasTag(entryTag))
        {
            // get current entry list, remove selected entry tag, and update storedTag
            ListTag entryList = getEntryList(storageTag);
            entryList.remove(entryTag);

            storageTag.put(getEntryListName(), entryList);

            return true;
        }

        return false;
    }
}