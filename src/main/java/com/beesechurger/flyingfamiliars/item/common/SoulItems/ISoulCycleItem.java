package com.beesechurger.flyingfamiliars.item.common.SoulItems;

import com.beesechurger.flyingfamiliars.item.EntityTagItemHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.BASE_ENTITY_TAGNAME;

public interface ISoulCycleItem
{
    default void cycleSoul(Player player, int direction)
    {
        NonNullList<ItemStack> stacks = NonNullList.create();

        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = EntityTagItemHelper.getOffHandBattery(player);
        ItemStack curioCharm = EntityTagItemHelper.getCurioCharmBattery(player);

        if(mainHand != null)
            stacks.add(mainHand);
        if(offHand != null)
            stacks.add(offHand);
        if(curioCharm != null)
            stacks.add(curioCharm);

        ListTag tagList = new ListTag();
        ListTag tempTotal = new ListTag();

        // Add all item tags into one ListTag
        for(ItemStack stack : stacks)
        {
            if(stack.getItem() instanceof BaseEntityTagItem item)
            {
                EntityTagItemHelper.ensureTagPopulated(stack);
                CompoundTag stackTag = stack.getTag();
                ListTag tempItem = stackTag.getList(BASE_ENTITY_TAGNAME, 10);

                for(Tag tag : tempItem)
                {
                    tempTotal.add(tag);
                }
            }
        }

        if(direction < 0)
        {
            for(int i = 1; i < tempTotal.size(); i++)
            {
                tagList.add(tempTotal.get(i));
            }

            tagList.add(tempTotal.get(0));
        }
        else
        {
            tagList.add(tempTotal.get(tempTotal.size()-1));

            for(int i = 0; i < tempTotal.size()-1; i++)
            {
                tagList.add(tempTotal.get(i));
            }
        }

        for(ItemStack stack : stacks)
        {
            if(stack.getItem() instanceof BaseEntityTagItem item)
            {
                CompoundTag stackTag = stack.getTag();
                ListTag tempItem = new ListTag();

                for(int i = 0; i < item.getMaxEntities(); i++)
                {
                    tempItem.add(tagList.get(0));
                    tagList.remove(0);
                }

                stackTag.put(BASE_ENTITY_TAGNAME, tempItem);
                stack.setTag(stackTag);
            }
        }
    }

    default String getID(int listValue, ItemStack stack)
    {
        return stack.getTag().getList(BASE_ENTITY_TAGNAME, 10).getCompound(listValue).getString(BASE_ENTITY_TAGNAME);
    }
}
