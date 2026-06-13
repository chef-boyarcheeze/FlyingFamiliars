package com.beesechurger.flyingfamiliars.tags;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import static com.beesechurger.flyingfamiliars.util.FFConstants.*;
import static com.beesechurger.flyingfamiliars.wand_effect.common.WandEffectItemHelper.*;

public class WandEffectTagRef implements IStorageTagRef
{
    public static final WandEffectTagRef INSTANCE = new WandEffectTagRef();

//////////////////
/// Accessors: ///
//////////////////

/// Strings:

    @Override
    public String getEntryListName()
    {
        return STORAGE_WAND_EFFECT_TAGNAME;
    }

    public String getSelectedWandEffect(CompoundTag storageTag)
    {
        if(!hasTag(storageTag) || isEmpty(storageTag))
            return STORAGE_EMPTY;

        CompoundTag entry = getSelectedEntry(storageTag);

        // getAsString() returns selection name without quotes, which is the correct key for WandEffectItemHelper map
        return hasTag(entry) ? entry.get(STORAGE_WAND_EFFECT_TYPE).getAsString() : "";
    }

/// Tags:

    @Override
    public ListTag getInitialEntryList(CompoundTag storageTag)
    {
        CompoundTag tag = new CompoundTag();

        // add default capture projectile wand effect entry to add to initial list
        CompoundTag captureTag = new CompoundTag();
        captureTag.putString(STORAGE_WAND_EFFECT_TYPE, WAND_EFFECT_CAPTURE);

        CompoundTag fireballTag = new CompoundTag();
        fireballTag.putString(STORAGE_WAND_EFFECT_TYPE, WAND_EFFECT_FIREBALL);

        CompoundTag flamethrowerTag = new CompoundTag();
        flamethrowerTag.putString(STORAGE_WAND_EFFECT_TYPE, WAND_EFFECT_FLAMETHROWER);

        CompoundTag cloudCallTag = new CompoundTag();
        cloudCallTag.putString(STORAGE_WAND_EFFECT_TYPE, WAND_EFFECT_CLOUD_CALL);

        CompoundTag tectonicSunderTag = new CompoundTag();
        tectonicSunderTag.putString(STORAGE_WAND_EFFECT_TYPE, WAND_EFFECT_TECTONIC_SUNDER);

        CompoundTag timberCleaveTag = new CompoundTag();
        timberCleaveTag.putString(STORAGE_WAND_EFFECT_TYPE, WAND_EFFECT_TIMBER_CLEAVE);

        CompoundTag zephyreanGustTag = new CompoundTag();
        zephyreanGustTag.putString(STORAGE_WAND_EFFECT_TYPE, WAND_EFFECT_ZEPHYREAN_GUST);

        // get (new) ListTag under "getEntryListName()" in 'tag', to place back into 'tag'
        ListTag tagList = tag.getList(getEntryListName(), ListTag.TAG_COMPOUND);

        // add capture projectile entry to list
        tagList.add(captureTag);
        tagList.add(fireballTag);
        tagList.add(flamethrowerTag);
        tagList.add(cloudCallTag);
        tagList.add(tectonicSunderTag);
        tagList.add(timberCleaveTag);
        tagList.add(zephyreanGustTag);

        return tagList;
    }

    @Override
    public CompoundTag getInitialSettingsTag(CompoundTag storageTag)
    {
        CompoundTag tag = new CompoundTag();
        tag.putInt(STORAGE_ENTRY_STORAGE_MAX, 10);

        return tag;
    }

    @Override
    public CompoundTag getSelectedEntry(CompoundTag storageTag)
    {
        ListTag entryList = getEntryList(storageTag);

        for (Tag tag : entryList)
        {
            CompoundTag entry = (CompoundTag) tag;

            if (entry.contains(STORAGE_WAND_EFFECT_SELECTION))
            {
                return entry;
            }
        }

        // no wand effect is selected, select either first or none
        if (entryList.size() > 0)
        {
            CompoundTag entry = entryList.getCompound(0);
            entry.put(STORAGE_WAND_EFFECT_SELECTION, new CompoundTag());

            return entry;
        }

        return new CompoundTag();
    }
}
