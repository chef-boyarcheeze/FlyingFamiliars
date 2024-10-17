package com.beesechurger.flyingfamiliars.tags;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

import static com.beesechurger.flyingfamiliars.util.FFConstants.*;

public class WandEffectTagRef extends BaseStorageTagRef
{
    public WandEffectTagRef()
    {
        super();
    }

    public WandEffectTagRef(int maxEntries)
    {
        super(maxEntries);
    }

////////////////
// Accessors: //
////////////////

// Strings:
    @Override
    public String getEntryListName()
    {
        return STORAGE_WAND_EFFECT_TAGNAME;
    }

    public String getSelectedWandEffect(CompoundTag storageTag)
    {
        if(!hasTag(storageTag) || isEmpty(storageTag))
            return STORAGE_EMPTY;

        // getAsString() returns selection name without quotes, which is the correct key for WandEffectItemHelper map
        return getSelectedEntry(storageTag).get(STORAGE_WAND_EFFECT_TYPE).getAsString();
    }

// Tags:
    @Override
    public ListTag getInitialTagList()
    {
        CompoundTag tag = new CompoundTag();

        // add default capture projectile wand effect entry to add to initial list
        CompoundTag captureTag = new CompoundTag();
        captureTag.putString(STORAGE_WAND_EFFECT_TYPE, "capture_projectile");

        // get (new) ListTag under "getEntryListName()" in 'tag', to place back into 'tag'
        ListTag tagList = tag.getList(getEntryListName(), ListTag.TAG_COMPOUND);

        // add capture projectile entry to list
        tagList.add(captureTag);

        return tagList;
    }
}
