package com.beesechurger.flyingfamiliars.items.common.SoulItems;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.BASE_ENTITY_TAGNAME;

public interface ISoulCycleItem
{
    default void cycleSoul(ItemStack stack, boolean shiftDown)
    {
        if(stack.getItem() instanceof BaseEntityTagItem item)
        {
            CompoundTag compound = stack.getTag();

            ListTag temp = compound.getList(BASE_ENTITY_TAGNAME, 10);
            ListTag tagList = new ListTag();

            if(shiftDown)
            {
                for(int i = 1; i < item.getMaxEntities(); i++)
                {
                    tagList.add(temp.get(i));
                }

                tagList.add(temp.get(0));
            }
            else
            {
                tagList.add(temp.get(item.getMaxEntities()-1));

                for(int i = 0; i < item.getMaxEntities()-1; i++)
                {
                    tagList.add(temp.get(i));
                }
            }

            compound.put(BASE_ENTITY_TAGNAME, tagList);
            stack.setTag(compound);
        }
    }

    default String getID(int listValue, ItemStack stack)
    {
        return stack.getTag().getList(BASE_ENTITY_TAGNAME, 10).getCompound(listValue).getString(BASE_ENTITY_TAGNAME);
    }
}
