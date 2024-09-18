package com.beesechurger.flyingfamiliars.item.common.fluid_items;

import com.beesechurger.flyingfamiliars.item.common.entity_items.IWandEffectItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.*;

public interface IFluidTagItem
{
    default void populateTag(ItemStack stack)
    {
        if(stack.getItem() instanceof BaseFluidTagItem tagItem)
        {
            CompoundTag stackTag = new CompoundTag();

            // multiphial tier tag
            CompoundTag tierTag = new CompoundTag();
            tierTag.putInt(ITEM_FLUID_TIER, 1);

            // vitality fluid level list
            ListTag tagList = stackTag.getList(ITEM_FLUID_LEVELS, 10);

            CompoundTag blueTag = new CompoundTag();
            blueTag.putInt(VITALITY_BLUE_LEVEL, 0);
            CompoundTag greenTag = new CompoundTag();
            greenTag.putInt(VITALITY_GREEN_LEVEL, 0);
            CompoundTag yellowTag = new CompoundTag();
            yellowTag.putInt(VITALITY_YELLOW_LEVEL, 0);
            CompoundTag goldTag = new CompoundTag();
            goldTag.putInt(VITALITY_GOLD_LEVEL, 0);
            CompoundTag redTag = new CompoundTag();
            redTag.putInt(VITALITY_RED_LEVEL, 0);
            CompoundTag blackTag = new CompoundTag();
            blackTag.putInt(VITALITY_BLACK_LEVEL, 0);
            CompoundTag whiteTag = new CompoundTag();
            whiteTag.putInt(VITALITY_WHITE_LEVEL, 0);

            tagList.add(blueTag);
            tagList.add(greenTag);
            tagList.add(yellowTag);
            tagList.add(goldTag);
            tagList.add(redTag);
            tagList.add(blackTag);
            tagList.add(whiteTag);

            stackTag.put(ITEM_FLUID_LEVELS, tagList);

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
        if(stack.getItem() instanceof BaseFluidTagItem item)
        {
            if(!stack.hasTag() || stack.getTag().getList(ITEM_FLUID_LEVELS, 10).size() != tier)
                populateTag(stack);
        }
    }
}
