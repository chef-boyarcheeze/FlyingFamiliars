package com.beesechurger.flyingfamiliars.items;

import com.beesechurger.flyingfamiliars.items.custom.SoulItems.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.items.custom.SoulItems.IModeCycleItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.BASE_ENTITY_TAGNAME;
import static com.beesechurger.flyingfamiliars.util.FFStringConstants.ITEM_MODE_TAGNAME;

public class EntityTagItemHelper
{
    public final static int MAX_ENTITIES = 3;

    public static void populateTag(ItemStack stack)
    {
        if(stack.getItem() instanceof BaseEntityTagItem item)
        {
            CompoundTag compound = new CompoundTag();

            CompoundTag entityNBT = new CompoundTag();
            ListTag tagList = entityNBT.getList(BASE_ENTITY_TAGNAME, 10);
            entityNBT.putString(BASE_ENTITY_TAGNAME, "Empty");

            for(int i = 0; i < item.getMaxEntities(); i++)
            {
                tagList.addTag(i, entityNBT);
            }

            compound.put(BASE_ENTITY_TAGNAME, tagList);

            if(stack.getItem() instanceof IModeCycleItem)
            {
                CompoundTag modeTag = new CompoundTag();
                modeTag.putInt(ITEM_MODE_TAGNAME, 0);

                compound.put(ITEM_MODE_TAGNAME, modeTag);
            }

            stack.setTag(compound);
        }
    }

    public static void ensureTagPopulated(ItemStack stack)
    {
        if(!stack.hasTag())
            EntityTagItemHelper.populateTag(stack);
    }

    public static String getSelectedEntity(ItemStack stack)
    {
        if(stack.getItem() instanceof BaseEntityTagItem item)
        {
            CompoundTag stackTag = stack.getTag();

            if(stackTag != null)
            {
                ListTag stackList = stackTag.getList(BASE_ENTITY_TAGNAME, 10);

                return stackList.getCompound(item.getMaxEntities()-1).getString(BASE_ENTITY_TAGNAME);
            }
        }

        return "Empty";
    }

    public static boolean isSelectionEmpty(ItemStack stack)
    {
        if(stack.getItem() instanceof BaseEntityTagItem item)
        {
            CompoundTag stackTag = stack.getTag();

            if(stackTag != null)
            {
                ListTag stackList = stackTag.getList(BASE_ENTITY_TAGNAME, 10);

                return stackList.get(item.getMaxEntities()-1).toString().contains("Empty");
            }
        }

        return true;
    }

    public static boolean isEmpty(ItemStack stack)
    {
        int entityCount = 0;

        if(stack.getItem() instanceof BaseEntityTagItem item)
        {
            CompoundTag stackTag = stack.getTag();

            if(stackTag != null)
            {
                ListTag stackList = stackTag.getList(BASE_ENTITY_TAGNAME, 10);

                for(int i = 0; i < item.getMaxEntities(); i++)
                {
                    // Need to use regular Tag object here, not CompoundTag
                    if(!stackList.get(i).toString().contains("Empty")) entityCount++;
                }
            }
        }

        return entityCount == 0;
    }
}
