package com.beesechurger.flyingfamiliars.item.common.entity.soul_wand;

import com.beesechurger.flyingfamiliars.util.FFTypes;

public class FissureBaton extends BaseSoulWand
{
    public FissureBaton(Properties properties)
    {
        super(properties);
    }

//////////////////
/// Accessors: ///
//////////////////

/// Integers:

    @Override
    protected int getColor()
    {
        return FFTypes.FAMILIAR_TYPE_EARTH.color;
    }
}
