package com.beesechurger.flyingfamiliars.item.common.entity_items;

import com.beesechurger.flyingfamiliars.item.FFItemHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static com.beesechurger.flyingfamiliars.util.FFConstants.STORAGE_ENTITY_TAGNAME;

public interface IEntityCycleItem
{
////////////////
// Accessors: //
////////////////

// Booleans:
    public boolean canCycle(ItemStack stack);

    default void cycle(Player player, int direction)
    {
        List<ItemStack> stacks = FFItemHandler.getEntityStackList(player);

        ListTag tagList = new ListTag();
        ListTag tempTotal = new ListTag();

        // Add all item tags into one ListTag
        for (ItemStack stack : stacks)
        {
            if (stack.getItem() instanceof BaseEntityTagItem item)
            {
                for (Tag tag : item.entities.getEntryList(stack.getOrCreateTag()))
                {
                    tempTotal.add(tag);
                }
            }
        }

        if (direction < 0)
        {
            for (int i = 1; i < tempTotal.size(); i++)
            {
                tagList.add(tempTotal.get(i));
            }

            tagList.add(tempTotal.get(0));
        }
        else
        {
            tagList.add(tempTotal.get(tempTotal.size() - 1));

            for (int i = 0; i < tempTotal.size() - 1; i++)
            {
                tagList.add(tempTotal.get(i));
            }
        }

        for (ItemStack stack : stacks)
        {
            if (stack.getItem() instanceof BaseEntityTagItem item)
            {
                CompoundTag stackTag = stack.getOrCreateTag();
                ListTag tempList = new ListTag();

                for (int i = 0; i < item.entities.getEntryCount(stackTag); i++)
                {
                    tempList.add(tagList.remove(0));
                }

                stackTag.put(STORAGE_ENTITY_TAGNAME, tempList);
                stack.setTag(stackTag);
            }
        }
    }
}
