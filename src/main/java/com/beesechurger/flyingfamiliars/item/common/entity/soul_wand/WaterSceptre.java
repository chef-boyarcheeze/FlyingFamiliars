package com.beesechurger.flyingfamiliars.item.common.entity.soul_wand;

import com.beesechurger.flyingfamiliars.util.FFTypes;

public class WaterSceptre extends BaseSoulWand
{
	public WaterSceptre(Properties properties)
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
		return FFTypes.FAMILIAR_TYPE_WATER;
	}
}
