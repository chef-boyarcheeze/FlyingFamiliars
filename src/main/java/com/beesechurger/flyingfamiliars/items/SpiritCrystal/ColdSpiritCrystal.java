package com.beesechurger.flyingfamiliars.items.SpiritCrystal;

import net.minecraft.world.entity.EntityType;

public class ColdSpiritCrystal extends AbstractSpiritCrystal
{
	public static EntityType<?>[] COLD = {EntityType.WOLF, EntityType.FOX, EntityType.GOAT, EntityType.POLAR_BEAR};

	public ColdSpiritCrystal(Properties properties) 
	{
		super(properties);
	}
	
	@Override
	public EntityType<?> getEntityType()
	{
		return COLD[(int) Math.floor(Math.random()*COLD.length)];
	}
}
