package com.beesechurger.flyingfamiliars.item.common.entity;

import com.beesechurger.flyingfamiliars.item.FFItemHandler;
import com.beesechurger.flyingfamiliars.item.common.entity.soul_wand.BaseSoulWand;
import com.beesechurger.flyingfamiliars.tags.EntityTagUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IEntityCycleItem
{
//////////////////
/// Accessors: ///
//////////////////

/// Booleans:

    public boolean canCycle(Player player, ItemStack scrollStack, List<ItemStack> allStacks);

/////////////////////
/// Item Actions: ///
/////////////////////

    default void cycle(Player player, int scrollStackIndex, int direction, boolean singleStack)
    {
        if (player != null)
        {
            List<ItemStack> stacks = singleStack ? List.of(player.getInventory().items.get(scrollStackIndex)) : FFItemHandler.getEntityStackList(player);
            ListTag fullEntryList = new ListTag();

            for (int i = 0; i < stacks.size();)
            {
                if (stacks.get(i).getItem() instanceof BaseEntityTagItem item)
                {
                    // remove empty non-soul wand from stack list - this allows phylacteries to fill soul wand in hand
                    if (EntityTagUtil.INSTANCE.isEmpty(stacks.get(i).getOrCreateTag()) && !(stacks.get(i).getItem() instanceof BaseSoulWand))
                    {
                        stacks.remove(i);
                    }
                    else
                    {
                        for (Tag tag : EntityTagUtil.INSTANCE.getEntryList(stacks.get(i).getOrCreateTag()))
                        {
                            fullEntryList.add((CompoundTag) tag);
                        }

                        i++;
                    }
                }
                else
                {
                    stacks.remove(i);
                }
            }

            if (stacks.isEmpty())
            {
                return;
            }

            if (direction > 0)
            {
                fullEntryList.add(0, fullEntryList.remove(fullEntryList.size() - 1));
            }
            else
            {
                fullEntryList.add(fullEntryList.remove(0));
            }

            for (ItemStack stack : stacks)
            {
                if (stack.getItem() instanceof BaseEntityTagItem item)
                {
                    CompoundTag stackTag = stack.getOrCreateTag();
                    ListTag newEntryList = EntityTagUtil.INSTANCE.getEntryList(stack.getOrCreateTag());
                    newEntryList.clear();

                    for (int i = 0; !fullEntryList.isEmpty() && i < EntityTagUtil.INSTANCE.getMaxEntries(stack.getOrCreateTag()); i++)
                    {
                        newEntryList.add(fullEntryList.remove(0));
                    }
                }
            }
        }
    }
}
