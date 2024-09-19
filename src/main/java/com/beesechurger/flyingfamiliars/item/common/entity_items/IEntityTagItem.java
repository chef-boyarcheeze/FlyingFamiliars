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
        CompoundTag stackTag = getOrCreateTag(stack);
        ListTag entityList = stackTag.getList(BASE_ENTITY_TAGNAME, 10);

        return entityList.getCompound(getMaxEntities()-1).getString(BASE_ENTITY_TAGNAME);
    }

// Booleans:
    default boolean isSelectionEmpty(ItemStack stack)
    {
        return getSelectedEntity(stack) == ENTITY_EMPTY;
    }

    default boolean isEmpty(ItemStack stack)
    {
        return getEntityCount(stack) == 0;
    }

// Integers:
    default int getEntityCount(ItemStack stack)
    {
        int entityCount = 0;

        CompoundTag stackTag = getOrCreateTag(stack);
        ListTag entityList = stackTag.getList(BASE_ENTITY_TAGNAME, 10);

        for(int i = 0; i < getMaxEntities(); i++)
        {
            // Need to use regular Tag object here, not CompoundTag
            if(!entityList.get(i).toString().contains(ENTITY_EMPTY)) entityCount++;
        }

        return entityCount;
    }

    public abstract int getMaxEntities();

// Misc:

    default CompoundTag getOrCreateTag(ItemStack stack)
    {
        ensureTagPopulated(stack);

        return stack.getTag();
    }

///////////////////
// Item actions: //
///////////////////

    default void populateTag(ItemStack stack)
    {
        CompoundTag stackTag = new CompoundTag();

        CompoundTag entityTag = new CompoundTag();
        ListTag tagList = entityTag.getList(BASE_ENTITY_TAGNAME, 10);
        entityTag.putString(BASE_ENTITY_TAGNAME, ENTITY_EMPTY);

        for(int i = 0; i < getMaxEntities(); i++)
        {
            tagList.addTag(i, entityTag);
        }

        stackTag.put(BASE_ENTITY_TAGNAME, tagList);

        if(stack.getItem() instanceof IWandEffectItem)
        {
            // set default wand effect selection as capture projectile
            CompoundTag selectionTag = new CompoundTag();
            selectionTag.putString(ITEM_WAND_EFFECT_SELECTION_TAGNAME, "capture_projectile");

            stackTag.put(ITEM_WAND_EFFECT_SELECTION_TAGNAME, selectionTag);
        }

        stack.setTag(stackTag);
    }

    default void ensureTagPopulated(ItemStack stack)
    {
        if(!stack.hasTag() || stack.getTag().getList(BASE_ENTITY_TAGNAME, 10).size() != getMaxEntities())
            populateTag(stack);
    }
}