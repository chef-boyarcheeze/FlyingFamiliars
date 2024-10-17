/*
package com.beesechurger.flyingfamiliars.item.common.fluid_items;

import com.beesechurger.flyingfamiliars.item.common.IStorageTagItem;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static com.beesechurger.flyingfamiliars.util.FFConstants.*;

*/
/*
 *  Default interface to hide all fluid tag-related code from implementers
 *//*

public interface IVitalityTagItem extends IStorageTagItem
{
    // Class to store all seven vitality storage values
    public class VitalityStorage
    {
        public int blue = 0;
        public int green = 0;
        public int yellow = 0;
        public int gold = 0;
        public int red = 0;
        public int black = 0;
        public int white = 0;
    }

////////////////
// Accessors: //
////////////////

// Integers:
    public int getMaxFluidCapacity();

// Tags:
    @Override
    default CompoundTag getOrCreateTag(ItemStack stack)
    {
        if(!stack.hasTag() || !stack.getTag().contains(getEntryListName()))
        {
            CompoundTag stackTag = stack.hasTag() ? stack.getTag() : new CompoundTag();

            // get (new) ListTag under "getEntryListName()" in 'stackTag', to place back into 'stackTag'
            NonNullList<CompoundTag> fluidTags = NonNullList.withSize(getEntryCount(stack), new CompoundTag());
            ListTag tagList = stackTag.getList(getEntryListName(), ListTag.TAG_COMPOUND);

            for(int i = 0; i < getMaxEntries(); i++)
            {
                fluidTags.get(i).putString(ITEM_FLUID_TYPE, VITALITY_TYPES.get(i));
                fluidTags.get(i).putInt(ITEM_FLUID_STORAGE, 0);

                tagList.add(fluidTags.get(i));
            }

            stackTag.put(getEntryListName(), tagList);

            stack.setTag(stackTag);
        }

        return stack.getTag();
    }



// Misc:
    @Override
    default List<ItemStack> getStackList(Player player)
    {
        NonNullList<ItemStack> stacks = NonNullList.create();

        ItemStack mainHand = player.getMainHandItem();

        if (mainHand != null)
            stacks.add(mainHand);

        return stacks;
    }

///////////////////
// Tag actions: //
///////////////////
}
*/
