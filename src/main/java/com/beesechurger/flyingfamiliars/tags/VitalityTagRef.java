package com.beesechurger.flyingfamiliars.tags;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.List;

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
    public List<Integer> getStoredVitality(CompoundTag storageTag)
    {
        ListTag entryList = getEntryList(storageTag);

        NonNullList<Integer> storage = NonNullList.withSize(getMaxEntries(), 0);

        for(Tag tag : entryList)
        {
            switch (((CompoundTag) tag).getString(STORAGE_FLUID_TYPE))
            {
                case VITALITY_BLUE -> storage.set(0, ((CompoundTag) tag).getInt(VITALITY_BLUE));
                case VITALITY_GREEN -> storage.set(1, ((CompoundTag) tag).getInt(VITALITY_GREEN));
                case VITALITY_YELLOW -> storage.set(2, ((CompoundTag) tag).getInt(VITALITY_YELLOW));
                case VITALITY_GOLD -> storage.set(3, ((CompoundTag) tag).getInt(VITALITY_GOLD));
                case VITALITY_RED -> storage.set(4, ((CompoundTag) tag).getInt(VITALITY_RED));
                case VITALITY_BLACK -> storage.set(5, ((CompoundTag) tag).getInt(VITALITY_BLACK));
                case VITALITY_WHITE -> storage.set(6, ((CompoundTag) tag).getInt(VITALITY_WHITE));
            }
        }

        return storage;
    }
}