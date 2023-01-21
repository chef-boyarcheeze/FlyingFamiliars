package com.beesechurger.flyingfamiliars.items.SpiritCrystal;

import net.minecraft.world.entity.EntityType;

public class WaterySpiritCrystal extends AbstractSpiritCrystal
{
	public static EntityType<?>[] WATERY = {EntityType.TURTLE, EntityType.DOLPHIN, EntityType.COD, EntityType.SALMON, EntityType.TROPICAL_FISH, EntityType.PUFFERFISH, EntityType.SQUID};

	public WaterySpiritCrystal(Properties properties) 
	{
		super(properties);
	}
	
	@Override
	public EntityType<?> getEntityType()
	{
		return WATERY[(int) Math.floor(Math.random()*WATERY.length)];
	}
}
