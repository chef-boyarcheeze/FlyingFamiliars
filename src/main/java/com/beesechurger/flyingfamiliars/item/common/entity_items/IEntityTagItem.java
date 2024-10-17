/*
package com.beesechurger.flyingfamiliars.item.common.entity_items;

import com.beesechurger.flyingfamiliars.item.FFItemHandler;
import com.beesechurger.flyingfamiliars.item.common.IStorageTagItem;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static com.beesechurger.flyingfamiliars.util.FFConstants.BASE_ENTITY_EMPTY;
import static com.beesechurger.flyingfamiliars.util.FFConstants.BASE_ENTITY_TAGNAME;

*/
/*
 *  Default interface to hide all entity tag-related code from implementers
 *//*

public interface IEntityTagItem extends IStorageTagItem
{
////////////////
// Accessors: //
////////////////

// Strings:
    @Override
    default String getEntryListName()
    {
        return BASE_ENTITY_TAGNAME;
    }

    default String getEntityID(CompoundTag tag)
    {
        if (tag != null && tag.contains(getEntryListName()))
            return tag.getString(getEntryListName());

        return BASE_ENTITY_EMPTY;
    }

// Booleans:
    default Boolean isEntityTamed(CompoundTag tag)
    {
        return tag != null && tag.contains("Owner");
    }

// Misc:
    @Override
    default List<ItemStack> getStackList(Player player)
    {
        NonNullList<ItemStack> stacks = NonNullList.create();

        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = FFItemHandler.getOffHandTagItem(player);
        ItemStack curioCharm = FFItemHandler.getCurioCharmTagItem(player);

        if (mainHand != null)
            stacks.add(mainHand);
        if (offHand != null)
            stacks.add(offHand);
        if (curioCharm != null)
            stacks.add(curioCharm);

        return stacks;
    }

///////////////////
// Tag actions: //
///////////////////

// For Wand Effects:

*/
/*if(stack.getItem() instanceof IWandEffectItem)
    {
        // set default wand effect selection as capture projectile
        CompoundTag selectionTag = new CompoundTag();
        selectionTag.putString(ITEM_WAND_EFFECT_SELECTION_TAGNAME, "capture_projectile");

        stackTag.put(ITEM_WAND_EFFECT_SELECTION_TAGNAME, selectionTag);
    }*//*

}*/
