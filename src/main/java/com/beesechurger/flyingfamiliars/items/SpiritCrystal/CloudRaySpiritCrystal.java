package com.beesechurger.flyingfamiliars.items.SpiritCrystal;

import com.beesechurger.flyingfamiliars.entity.FFEntityTypes;

import net.minecraft.world.entity.EntityType;

public class CloudRaySpiritCrystal extends AbstractSpiritCrystal
{
	public CloudRaySpiritCrystal(Properties properties) 
	{
		super(properties);
	}
	
	@Override
	public EntityType<?> getEntityType()
	{
		return FFEntityTypes.CLOUD_RAY.get();
	}
}
