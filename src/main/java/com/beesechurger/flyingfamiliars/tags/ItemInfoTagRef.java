package com.beesechurger.flyingfamiliars.tags;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import static com.beesechurger.flyingfamiliars.util.FFConstants.STORAGE_ITEM_INFO_ENTITY_MANIP_MODE;
import static com.beesechurger.flyingfamiliars.util.FFConstants.STORAGE_ITEM_INFO_TAGNAME;

public class ItemInfoTagRef extends BaseStorageTagRef
{
    public ItemInfoTagRef()
    {
        super();
    }

////////////////
// Accessors: //
////////////////

// Strings:
    @Override
    public String getEntryListName()
    {
        return STORAGE_ITEM_INFO_TAGNAME;
    }

// Booleans:
    public boolean getManipMode(CompoundTag storageTag)
    {
        ListTag entryList = getEntryList(storageTag);

        for (Tag tag : entryList)
        {
            if (((CompoundTag) tag).contains(STORAGE_ITEM_INFO_ENTITY_MANIP_MODE))
            {
                return ((CompoundTag) tag).getBoolean(STORAGE_ITEM_INFO_ENTITY_MANIP_MODE);
            }
        }

        return false;
    }

///////////////
// Mutators: //
///////////////

// Booleans:
    public void toggleManipMode(CompoundTag storageTag)
    {
        ListTag entryList = getEntryList(storageTag);

        for (Tag tag : entryList)
        {
            if (((CompoundTag) tag).contains(STORAGE_ITEM_INFO_ENTITY_MANIP_MODE))
            {
                ((CompoundTag) tag).putBoolean(STORAGE_ITEM_INFO_ENTITY_MANIP_MODE, !getManipMode(storageTag));
                return;
            }
        }

        CompoundTag entry = new CompoundTag();
        entry.putBoolean(STORAGE_ITEM_INFO_ENTITY_MANIP_MODE, true);

        addEntry(storageTag, entry);
    }
}
