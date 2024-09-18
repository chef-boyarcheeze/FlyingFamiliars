package com.beesechurger.flyingfamiliars.item.common.entity_items;

import net.minecraft.world.item.ItemStack;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.ITEM_WAND_EFFECT_SELECTION_TAGNAME;
import static com.beesechurger.flyingfamiliars.util.FFStringConstants.WAND_EFFECT_EMPTY;

public interface IWandEffectItem
{
    default String getSelection(ItemStack stack)
    {
        if(!stack.hasTag())
            return WAND_EFFECT_EMPTY;

        // getAsString() returns selection name without quotes, which is the correct key for WandEffectItemHelper map
        return stack.getTag().getCompound(ITEM_WAND_EFFECT_SELECTION_TAGNAME).get(ITEM_WAND_EFFECT_SELECTION_TAGNAME).getAsString();
    }
}