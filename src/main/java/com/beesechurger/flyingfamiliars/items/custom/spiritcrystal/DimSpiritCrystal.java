package com.beesechurger.flyingfamiliars.items.custom.spiritcrystal;

import net.minecraft.world.entity.EntityType;

public class DimSpiritCrystal extends AbstractSpiritCrystal
{
	public static EntityType<?>[] DIM = {EntityType.BAT, EntityType.GLOW_SQUID, EntityType.AXOLOTL};

	public DimSpiritCrystal(Properties properties) 
	{
		super(properties);
	}
	
	@Override
	public EntityType<?> getEntityType()
	{
		return DIM[(int) Math.floor(Math.random()*DIM.length)];
	}
}
