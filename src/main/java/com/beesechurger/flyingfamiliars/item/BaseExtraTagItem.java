package com.beesechurger.flyingfamiliars.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BaseExtraTagItem extends Item
{
    public BaseExtraTagItem(Properties properties)
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
