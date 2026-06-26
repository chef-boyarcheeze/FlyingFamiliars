package com.beesechurger.flyingfamiliars.item.common.entity.SoulWand;

import com.beesechurger.flyingfamiliars.util.FFColors;

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
        return FFColors.FAMILIAR_TYPE_LIFE;
    }
}
