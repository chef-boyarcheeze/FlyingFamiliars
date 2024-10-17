package com.beesechurger.flyingfamiliars.item.common.fluid_items;

import com.beesechurger.flyingfamiliars.item.common.ITieredItem;

public class Multiphial extends BaseVitalityTagItem implements ITieredItem
{
    protected final ItemTier TIER;

    public Multiphial(Properties properties, ItemTier tier)
    {
        super(properties, tier.VALUE, getMaxVolume(tier));

        this.TIER = tier;
    }

////////////////
// Accessors: //
////////////////

    public static int getMaxVolume(ItemTier tier)
    {
        return switch (tier)
        {
            case BLUE -> 100;
            case GREEN -> 200;
            case YELLOW -> 300;
            case GOLD -> 450;
            case RED -> 600;
            case BLACK -> 750;
            case WHITE -> 1000;
        };
    }
}
