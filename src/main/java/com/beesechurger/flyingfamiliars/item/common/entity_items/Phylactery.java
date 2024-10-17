package com.beesechurger.flyingfamiliars.item.common.entity_items;

import com.beesechurger.flyingfamiliars.item.common.ITieredItem;

public class Phylactery extends BaseEntityTagItem implements ITieredItem
{
    protected final ItemTier TIER;

    public Phylactery(Properties properties, ItemTier tier)
    {
        super(properties, tier.VALUE);

        this.TIER = tier;
    }
}
