package com.beesechurger.flyingfamiliars.item.common.fluid_items;

import com.beesechurger.flyingfamiliars.item.BaseExtraTagItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class BaseFluidTagItem extends BaseExtraTagItem implements IFluidTagItem
{
    public BaseFluidTagItem(Properties properties)
    {
        super(properties);
    }

    // 7 different fluids at max
    // start at 1, upgrade up to 7?
    // also increase volume of each fluid per upgrade?
    // store each fluid as tags

////////////////
// Accessors: //
////////////////

// Booleans:
    @Override
    public boolean isFoil(ItemStack stack)
    {
        return false;
    }

    @Override
    public boolean isBarVisible(ItemStack stack)
    {
        return false;
    }

// Integers:
    public abstract int getMaxLiquids(int tier);

    public abstract int getMaxLiquidCapacity(int tier);

///////////////////
// Item actions: //
///////////////////

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        ensureTagPopulated(stack);

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
