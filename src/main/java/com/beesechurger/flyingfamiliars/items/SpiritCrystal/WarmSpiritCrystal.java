package com.beesechurger.flyingfamiliars.items.SpiritCrystal;

import net.minecraft.world.entity.EntityType;

public class WarmSpiritCrystal extends AbstractSpiritCrystal
{
	public static EntityType<?>[] WARM = {EntityType.PANDA, EntityType.PARROT, EntityType.OCELOT};

	public WarmSpiritCrystal(Properties properties) 
	{
		super(properties);
	}
	
	@Override
	public EntityType<?> getEntityType()
	{
		return WARM[(int) Math.floor(Math.random()*WARM.length)];
	}
}
