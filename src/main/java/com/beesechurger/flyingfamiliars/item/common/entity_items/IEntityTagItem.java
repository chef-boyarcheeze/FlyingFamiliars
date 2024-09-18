package com.beesechurger.flyingfamiliars.item.common.entity_items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.*;

public interface IEntityTagItem
{
    public final static int MAX_ENTITIES = 3;

////////////////
// Accessors: //
////////////////

// Strings:
    default String getSelectedEntity(ItemStack stack)
    {
        if(stack.getItem() instanceof BaseEntityTagItem item)
        {
            if(stack.hasTag())
            {
                CompoundTag stackTag = stack.getTag();
                ListTag stackList = stackTag.getList(BASE_ENTITY_TAGNAME, 10);

                return stackList.getCompound(item.getMaxEntities()-1).getString(BASE_ENTITY_TAGNAME);
            }
        }

        return ENTITY_EMPTY;
    }

// Booleans:
    default boolean isSelectionEmpty(ItemStack stack)
    {
        if(stack.getItem() instanceof BaseEntityTagItem item)
        {
            if(stack.hasTag())
            {
                CompoundTag stackTag = stack.getTag();
                ListTag stackList = stackTag.getList(BASE_ENTITY_TAGNAME, 10);

                return stackList.get(item.getMaxEntities()-1).toString().contains(ENTITY_EMPTY);
            }
        }

        return true;
    }

    default boolean isEmpty(ItemStack stack)
    {
        int entityCount = 0;

        if(stack.getItem() instanceof BaseEntityTagItem item)
        {
            if(stack.hasTag())
            {
                CompoundTag stackTag = stack.getTag();
                ListTag stackList = stackTag.getList(BASE_ENTITY_TAGNAME, 10);

                for(int i = 0; i < item.getMaxEntities(); i++)
                {
                    // Need to use regular Tag object here, not CompoundTag
                    if(!stackList.get(i).toString().contains(ENTITY_EMPTY)) entityCount++;
                }
            }
        }

        return entityCount == 0;
    }

// Integers:
    default int getEntityCount(ItemStack stack)
    {
        ensureTagPopulated(stack);

        int entityCount = 0;

        if(stack.hasTag())
        {
            CompoundTag stackTag = stack.getTag();
            ListTag tagList = stackTag.getList(BASE_ENTITY_TAGNAME, 10);

            for(int i = 0; i < getMaxEntities(); i++)
            {
                // Need to use regular Tag object here, not CompoundTag
                if(!tagList.get(i).toString().contains(ENTITY_EMPTY)) entityCount++;
            }
        }

        return entityCount;
    }

    public abstract int getMaxEntities();

///////////////////
// Item actions: //
///////////////////

    default void populateTag(ItemStack stack)
    {
        if(stack.getItem() instanceof BaseEntityTagItem tagItem)
        {
            CompoundTag stackTag = new CompoundTag();

            CompoundTag entityTag = new CompoundTag();
            ListTag tagList = entityTag.getList(BASE_ENTITY_TAGNAME, 10);
            entityTag.putString(BASE_ENTITY_TAGNAME, ENTITY_EMPTY);

            for(int i = 0; i < tagItem.getMaxEntities(); i++)
            {
                tagList.addTag(i, entityTag);
            }

            stackTag.put(BASE_ENTITY_TAGNAME, tagList);

            if(stack.getItem() instanceof IWandEffectItem effectItem)
            {
                // set default wand effect selection as capture projectile
                CompoundTag selectionTag = new CompoundTag();
                selectionTag.putString(ITEM_WAND_EFFECT_SELECTION_TAGNAME, "capture_projectile");

                stackTag.put(ITEM_WAND_EFFECT_SELECTION_TAGNAME, selectionTag);
            }

            stack.setTag(stackTag);
        }
    }

    default void ensureTagPopulated(ItemStack stack)
    {
        if(stack.getItem() instanceof BaseEntityTagItem item)
        {
            if(!stack.hasTag() || stack.getTag().getList(BASE_ENTITY_TAGNAME, 10).size() != item.getMaxEntities())
                populateTag(stack);
        }
    }
}