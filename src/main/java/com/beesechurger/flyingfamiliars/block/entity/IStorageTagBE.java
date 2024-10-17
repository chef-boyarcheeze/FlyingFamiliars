package com.beesechurger.flyingfamiliars.block.entity;

import com.beesechurger.flyingfamiliars.item.common.entity_items.BaseEntityTagItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/*
 *  Default interface to hide all tag-related code from implementers
 *  (and to unify all storage-related tag block entities)
 */
public interface IStorageTagBE
{
////////////////
// Accessors: //
////////////////

// Strings:
    public String getEntryListName();

// Booleans:
    default boolean isEmpty()
    {
        return getEntryList().isEmpty();
    }

    default boolean isFull()
    {
        return getEntryList().size() == getMaxEntries();
    }

// Integers:
    public int getMaxEntries();

    default int getEntryCount()
    {
        return getEntryList().size();
    }

// Tags:
    public CompoundTag getStorageTag();

    default CompoundTag getOrCreateTag()
    {
        CompoundTag blockTag = getStorageTag();

        blockTag = blockTag == null ? new CompoundTag() : blockTag;

        if(!blockTag.contains(getEntryListName()))
        {
            // get (new) ListTag under "getEntryListName()" in 'stackTag', to place back into 'stackTag'
            ListTag tagList = blockTag.getList(getEntryListName(), ListTag.TAG_COMPOUND);
            blockTag.put(getEntryListName(), tagList);
        }

        return blockTag;
    }

    default ListTag getEntryList()
    {
        CompoundTag blockTag = getOrCreateTag();

        return blockTag.getList(getEntryListName(), ListTag.TAG_COMPOUND);
    }

//////////////////
// Tag actions: //
//////////////////

    default boolean pushEntry(CompoundTag entryTag)
    {
        // require 'item' qualifier to make sure all list ItemStacks have 'getEntryList' method
        if (!isFull())
        {
            // get current entry list, add incoming entry tag, and update item stack's tag
            ListTag entryList = getEntryList();
            entryList.add(entryTag);
            getOrCreateTag().put(getEntryListName(), entryList);

            return true;
        }

        return false;
    }

    default CompoundTag popEntry()
    {
        // require 'item' qualifier to make sure all list ItemStacks have 'getEntryList' method
        if (!isEmpty())
        {
            // get current entry list, remove selected entry tag, and update item stack's tag
            ListTag entryList = getEntryList();
            CompoundTag entryTag = (CompoundTag) entryList.remove(getEntryCount() - 1);
            getOrCreateTag().put(getEntryListName(), entryList);

            return entryTag;
        }

        return new CompoundTag();
    }
}
