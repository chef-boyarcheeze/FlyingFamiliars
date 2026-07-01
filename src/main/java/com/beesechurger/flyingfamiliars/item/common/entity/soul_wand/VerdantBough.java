package com.beesechurger.flyingfamiliars.item.common.entity.soul_wand;

import com.beesechurger.flyingfamiliars.util.FFTypes;

public class VerdantBough extends BaseSoulWand
{
    public VerdantBough(Properties properties)
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
        return FFTypes.FAMILIAR_TYPE_LIFE;
    }
}
