package com.beesechurger.flyingfamiliars.util.tags;

import com.beesechurger.flyingfamiliars.item.FFItemHandler;
import com.beesechurger.flyingfamiliars.item.common.entity.BaseEntityTagItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static com.beesechurger.flyingfamiliars.util.FFConstants.*;

public class EntityTagUtil implements IStorageTagUtil
{
    public static final EntityTagUtil INSTANCE = new EntityTagUtil();

//////////////////
/// Accessors: ///
//////////////////

/// Strings:

    @Override
    public String getEntryListName()
    {
        return STORAGE_ENTITY_TAGNAME;
    }

    public static String getEntityID(CompoundTag tag)
    {
        if (tag != null && tag.contains(STORAGE_ENTITY_TYPE))
        {
            return tag.getString(STORAGE_ENTITY_TYPE);
        }

        return STORAGE_EMPTY;
    }

/// Booleans:

    public static Boolean isEntityTamed(CompoundTag tag)
    {
        return tag != null && tag.contains("Owner");
    }

    public boolean getManipMode(CompoundTag storageTag)
    {
        CompoundTag settingsTag = getSettingsTag(storageTag);

        if (settingsTag.contains(STORAGE_ENTRY_MANIP_MODE))
        {
            return settingsTag.getBoolean(STORAGE_ENTRY_MANIP_MODE);
        }

        return false;
    }

/// Tags:

    @Override
    public CompoundTag getInitialSettingsTag(CompoundTag storageTag)
    {
        CompoundTag tag = new CompoundTag();
        tag.putInt(STORAGE_ENTRY_STORAGE_MAX, 3);

        return tag;
    }

    public CompoundTag getPlayerFullEntityListTag(Player player)
    {
        ListTag fullEntryList = new ListTag();

        for (ItemStack stack : FFItemHandler.getEntityStackList(player))
        {
            if(stack.getItem() instanceof BaseEntityTagItem item)
            {
                for (Tag tag : EntityTagUtil.INSTANCE.getEntryList(stack.getOrCreateTag()))
                {
                    fullEntryList.add(tag);
                }
            }
        }

        CompoundTag storageTag = EntityTagUtil.INSTANCE.getOrCreateTag(new CompoundTag());
        storageTag.getCompound(STORAGE_ENTITY_TAGNAME).put(STORAGE_ENTRY_LIST, fullEntryList);

        return storageTag;
    }

/////////////////
/// Mutators: ///
/////////////////

/// Booleans:

    public void toggleManipMode(CompoundTag storageTag)
    {
        CompoundTag settingsTag = getSettingsTag(storageTag);
        settingsTag.putBoolean(STORAGE_ENTRY_MANIP_MODE, !getManipMode(storageTag));

        storageTag.put(STORAGE_SETTINGS, settingsTag);
    }
}