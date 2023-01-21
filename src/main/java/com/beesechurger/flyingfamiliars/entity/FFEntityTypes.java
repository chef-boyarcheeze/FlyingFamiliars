package com.beesechurger.flyingfamiliars.entity;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.custom.CloudRayEntity;
import com.beesechurger.flyingfamiliars.entity.custom.PhoenixEntity;
import com.beesechurger.flyingfamiliars.entity.custom.projectile.SoulWandProjectile;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FFEntityTypes 
{
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, FlyingFamiliars.MOD_ID);

	public static final RegistryObject<EntityType<PhoenixEntity>> PHOENIX = ENTITY_TYPES.register("phoenix", 
			() -> EntityType.Builder.of(PhoenixEntity::new, MobCategory.CREATURE)
					.sized(0.8f, 0.6f)
					.build(new ResourceLocation(FlyingFamiliars.MOD_ID, "phoenix").toString()));
	
	public static final RegistryObject<EntityType<CloudRayEntity>> CLOUD_RAY = ENTITY_TYPES.register("cloud_ray", 
			() -> EntityType.Builder.of(CloudRayEntity::new, MobCategory.CREATURE)
					.sized(2.0f, 1.5f)
					.build(new ResourceLocation(FlyingFamiliars.MOD_ID, "cloud_ray").toString()));
	
	public static final RegistryObject<EntityType<SoulWandProjectile>> SOUL_WAND_PROJECTILE = ENTITY_TYPES.register("soul_wand_projectile",
			() -> EntityType.Builder.<SoulWandProjectile>of(SoulWandProjectile::new, MobCategory.MISC)
					.sized(0.5f, 0.5f)
					.setShouldReceiveVelocityUpdates(true)
					.build("soul_wand_projectile"));

	public static void register(IEventBus eventbus)
	{
		ENTITY_TYPES.register(eventbus);
	}
}
