package com.beesechurger.flyingfamiliars.tags;

import com.beesechurger.flyingfamiliars.wand_effect.BaseWandEffect;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.List;
import java.util.Map;

import static com.beesechurger.flyingfamiliars.util.FFConstants.*;

public class VitalityTagRef extends BaseStorageTagRef
{
    protected final int maxVolume;

    public VitalityTagRef()
    {
        super();

        maxVolume = 250;
    }

    public VitalityTagRef(int maxEntries, int maxVolume)
    {
        super(maxEntries);

        this.maxVolume = maxVolume;
    }

////////////////
// Accessors: //
////////////////

// Strings:
    @Override
    public String getEntryListName()
    {
        return STORAGE_FLUID_TAGNAME;
    }

// Integers:
    public int getMaxVolume()
    {
        return maxVolume;
    }

// Tags:
    @Override
    public ListTag getInitialTagList()
    {
        CompoundTag tag = new CompoundTag();

        // get (new) ListTag under "getEntryListName()" in 'tag', to place back into 'tag'
        NonNullList<CompoundTag> fluidTags = NonNullList.withSize(getMaxEntries(), new CompoundTag());
        ListTag tagList = tag.getList(getEntryListName(), ListTag.TAG_COMPOUND);

        for (int i = 0; i < getMaxEntries(); i++)
        {
            fluidTags.get(i).putString(STORAGE_FLUID_TYPE, VITALITY_TYPES.get(i));
            fluidTags.get(i).putInt(STORAGE_FLUID_STORAGE, 0);

            tagList.add(fluidTags.get(i));
        }

        return tagList;
    }

// Misc:
    public Map<String, Integer> getStoredVitality(CompoundTag storageTag)
    {
        ListTag entryList = getEntryList(storageTag);

        Map<String, Integer> entryMap = (Map) Util.make(Maps.newHashMap(), (map) -> {
            for(Tag tag : entryList)
            {
                String type = ((CompoundTag) tag).getString(STORAGE_FLUID_TYPE);
                Integer volume = ((CompoundTag) tag).getInt(type);

                if (volume > 0)
                {
                    map.put(type, volume);
                }
            }
        });

        return entryMap;
    }
}