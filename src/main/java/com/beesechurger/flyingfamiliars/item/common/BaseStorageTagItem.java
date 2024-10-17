package com.beesechurger.flyingfamiliars.item.common;

import com.beesechurger.flyingfamiliars.registries.FFItems;
import com.beesechurger.flyingfamiliars.tags.IStorageTagRef;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static com.beesechurger.flyingfamiliars.util.FFConstants.STORAGE_WAND_EFFECT_TAGNAME;

public abstract class BaseStorageTagItem extends Item
{
    public BaseStorageTagItem(Properties properties)
    {
        super(properties);
    }

////////////////
// Accessors: //
////////////////

// Booleans:
    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player)
    {
        return !player.isCreative();
    }

///////////////////
// Item actions: //
///////////////////

/*    public static void updateStorageTagRef(ItemStack stack, IStorageTagRef ref)
    {
        CompoundTag stackTag = stack.hasTag() ? stack.getTag() : new CompoundTag();
        ListTag stackList = stackTag.getList(ref.getEntryListName(), ListTag.TAG_COMPOUND);

        if (!stack.hasTag() || !stackTag.contains(ref.getEntryListName()))
        {
            System.out.println(stack.getItem());

            stackTag.put(ref.getEntryListName(), ref.getInitialTag());
            stack.setTag(stackTag);

            System.out.println(stackTag);
        }

        if (ref.getEntryList() != stackList)
        {
            CompoundTag entriesTag = ref.getOrCreateTag();

            entriesTag.put(ref.getEntryListName(), stackList);
            ref.setTag(entriesTag);
        }
    }

    public static void updateStackTagEntries(ItemStack stack, IStorageTagRef ref)
    {
        CompoundTag stackTag = stack.hasTag() ? stack.getTag() : new CompoundTag();
        ListTag stackList = stackTag.getList(ref.getEntryListName(), ListTag.TAG_COMPOUND);

        if (stackList != ref.getEntryList())
        {
            stackTag.put(ref.getEntryListName(), ref.getEntryList());
            stack.setTag(stackTag);
        }
    }*/
}
