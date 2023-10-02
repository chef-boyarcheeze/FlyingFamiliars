package com.beesechurger.flyingfamiliars.items.common.SoulItems;

import com.beesechurger.flyingfamiliars.items.EntityTagItemHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.BASE_ENTITY_TAGNAME;

public interface ISoulCycleItem
{
    default void cycleSoul(Player player)
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
                CompoundTag compound = stack.getTag();
                ListTag tempItem = compound.getList(BASE_ENTITY_TAGNAME, 10);

                for(Tag tag : tempItem)
                {
                    tempTotal.add(tag);
                }
            }
        }

        if(player.isShiftKeyDown())
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
                CompoundTag compound = stack.getTag();
                ListTag tempItem = new ListTag();

                System.out.println(stack);

                for(int i = 0; i < item.getMaxEntities(); i++)
                {
                    System.out.println(tagList.size());
                    tempItem.add(tagList.get(0));
                    tagList.remove(0);
                }

                compound.put(BASE_ENTITY_TAGNAME, tempItem);
                stack.setTag(compound);
            }
        }
    }

    default String getID(int listValue, ItemStack stack)
    {
        return stack.getTag().getList(BASE_ENTITY_TAGNAME, 10).getCompound(listValue).getString(BASE_ENTITY_TAGNAME);
    }
}
