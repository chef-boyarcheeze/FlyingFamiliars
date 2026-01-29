package com.beesechurger.flyingfamiliars.item.common.entity_items;

import com.beesechurger.flyingfamiliars.item.FFItemHandler;
import com.beesechurger.flyingfamiliars.item.common.entity_items.SoulWand.BaseSoulWand;
import com.beesechurger.flyingfamiliars.tags.EntityTagRef;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static com.beesechurger.flyingfamiliars.util.FFConstants.STORAGE_ENTITY_TAGNAME;
import static com.beesechurger.flyingfamiliars.util.FFConstants.STORAGE_ENTRY_LIST;

public interface IEntityCycleItem
{
////////////////
// Accessors: //
////////////////

// Booleans:
    public boolean canCycle(Player player, ItemStack stack);

    default void cycle(Player player, int direction)
    {
        if (player != null)
        {
            List<ItemStack> stacks = FFItemHandler.getEntityStackList(player);
            List<ListTag> entryLists = new ArrayList<>();

            for (int i = 0; i < stacks.size(); )
            {
                if (stacks.get(i).getItem() instanceof BaseEntityTagItem item)
                {
                    if (EntityTagRef.INSTANCE.isEmpty(stacks.get(i).getOrCreateTag()) && !(stacks.get(i).getItem() instanceof BaseSoulWand))
                    {
                        stacks.remove(i);
                    } else
                    {
                        entryLists.add(EntityTagRef.INSTANCE.getEntryList(stacks.get(i).getOrCreateTag()));

                        i++;
                    }
                }
            }

            if (direction > 0)
            {
                for (int i = 0; i < entryLists.size() - 1; i++)
                {
                    ListTag currList = entryLists.get(i);
                    ListTag nextList = entryLists.get(i + 1);

                    if (currList.size() > 0)
                    {
                        nextList.add((CompoundTag) currList.remove(0));
                    }
                }

                ListTag firstList = entryLists.get(0);
                ListTag lastList = entryLists.get(entryLists.size() - 1);

                if (lastList.size() > 0)
                {
                    firstList.add((CompoundTag) lastList.remove(0));
                }
            }
            else
            {
                for (int i = entryLists.size() - 1; i > 0; i--)
                {
                    ListTag currList = entryLists.get(i);
                    ListTag nextList = entryLists.get(i - 1);

                    if (currList.size() > 0)
                    {
                        nextList.add((CompoundTag) currList.remove(0));
                    }
                }

                ListTag firstList = entryLists.get(0);
                ListTag lastList = entryLists.get(entryLists.size() - 1);

                if (firstList.size() > 0)
                {
                    lastList.add((CompoundTag) firstList.remove(0));
                }
            }

            for (ItemStack stack : stacks)
            {
                if (stack.getItem() instanceof BaseEntityTagItem item)
                {
                    CompoundTag stackTag = stack.getOrCreateTag();

                    stackTag.getCompound(STORAGE_ENTITY_TAGNAME).put(STORAGE_ENTRY_LIST, entryLists.remove(0));
                    stack.setTag(stackTag);
                }
            }
        }
    }
}
