package com.beesechurger.flyingfamiliars.items.custom.spiritcrystal;

import net.minecraft.world.entity.EntityType;

public class CivilizedSpiritCrystal extends AbstractSpiritCrystal
{
	public static EntityType<?>[] CIVILIZED = {EntityType.VILLAGER, EntityType.CAT, EntityType.IRON_GOLEM};
	
	public CivilizedSpiritCrystal(Properties properties) 
	{
		super(properties);
	}
	
	@Override
	public EntityType<?> getEntityType()
	{
		return CIVILIZED[(int) Math.floor(Math.random()*CIVILIZED.length)];
	}
}
