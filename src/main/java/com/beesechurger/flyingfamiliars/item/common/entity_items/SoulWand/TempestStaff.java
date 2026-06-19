package com.beesechurger.flyingfamiliars.item.common.entity_items.SoulWand;

import com.beesechurger.flyingfamiliars.util.FFConstants;

public class TempestStaff extends BaseSoulWand
{
    public TempestStaff(Properties properties)
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
        return FFConstants.FAMILIAR_TYPE_AIR;
    }
}
