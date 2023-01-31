package com.beesechurger.flyingfamiliars.items.custom.spiritcrystal;

import net.minecraft.world.entity.EntityType;

public class GrassySpiritCrystal extends AbstractSpiritCrystal
{
	public static EntityType<?>[] GRASSY = {EntityType.HORSE, EntityType.DONKEY, EntityType.RABBIT, EntityType.LLAMA};
	
	public GrassySpiritCrystal(Properties properties) 
	{
		super(properties);
	}
	
	@Override
	public EntityType<?> getEntityType()
	{
		return GRASSY[(int) Math.floor(Math.random()*GRASSY.length)];
	}
}
