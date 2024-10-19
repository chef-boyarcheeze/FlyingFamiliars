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

public abstract class BaseStorageTagItem extends Item implements IEntryManipModeItem
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
}
