/*
package com.beesechurger.flyingfamiliars.item.common;

import com.beesechurger.flyingfamiliars.item.common.entity_items.BaseEntityTagItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

*/
/*
 *  Default interface to hide all tag-related code from implementers
 *  (and to unify all storage-related tag items)
 *//*

public interface IStorageTagItem
{
////////////////
// Accessors: //
////////////////

// Strings:
    public String getEntryListName();

// Booleans:
    default boolean isEmpty(ItemStack stack)
    {
        return getEntryList(stack).isEmpty();
    }

    default boolean isFull(ItemStack stack)
    {
        return getEntryList(stack).size() == getMaxEntries();
    }

// Integers:
    public int getMaxEntries();

    default int getEntryCount(ItemStack stack)
    {
        return getEntryList(stack).size();
    }

// Tags:
    default CompoundTag getOrCreateTag(ItemStack stack)
    {
        if(!stack.hasTag() || !stack.getTag().contains(getEntryListName()))
        {
            CompoundTag stackTag = stack.hasTag() ? stack.getTag() : new CompoundTag();

            // get (new) ListTag under "getEntryListName()" in 'stackTag', to place back into 'stackTag'
            ListTag tagList = stackTag.getList(getEntryListName(), ListTag.TAG_COMPOUND);
            stackTag.put(getEntryListName(), tagList);

            stack.setTag(stackTag);
        }

        return stack.getTag();
    }

    default ListTag getEntryList(ItemStack stack)
    {
        CompoundTag stackTag = getOrCreateTag(stack);

        return stackTag.getList(getEntryListName(), ListTag.TAG_COMPOUND);
    }

    default CompoundTag getSelectedEntry(ItemStack stack)
    {
        ListTag entryList = getEntryList(stack);

        if(!isEmpty(stack))
            return entryList.getCompound(getEntryCount(stack)-1);

        return new CompoundTag();
    }

// Misc:
    public List<ItemStack> getStackList(Player player);

//////////////////
// Tag actions: //
//////////////////

    default boolean pushEntry(Player player, CompoundTag entryTag)
    {
        if (player != null)
        {
            for (ItemStack stack : getStackList(player))
            {
                // require 'item' qualifier to make sure all list ItemStacks have 'getEntryList' method
                if (stack.getItem() instanceof BaseEntityTagItem item && !item.isFull(stack))
                {
                    // get current entry list, add incoming entry tag, and update item stack's tag
                    ListTag entryList = item.getEntryList(stack);
                    entryList.add(entryTag);
                    item.getOrCreateTag(stack).put(item.getEntryListName(), entryList);

                    return true;
                }
            }
        }

        return false;
    }

    default CompoundTag popEntry(Player player)
    {
        for (ItemStack stack : getStackList(player))
        {
            // require 'item' qualifier to make sure all list ItemStacks have 'getEntryList' method
            if (stack.getItem() instanceof BaseEntityTagItem item && !item.isEmpty(stack))
            {
                // get current entry list, remove selected entry tag, and update item stack's tag
                ListTag entryList = item.getEntryList(stack);
                CompoundTag entryTag = (CompoundTag) entryList.remove(item.getEntryCount(stack) - 1);
                item.getOrCreateTag(stack).put(item.getEntryListName(), entryList);

                return entryTag;
            }
        }

        return new CompoundTag();
    }
}
*/
