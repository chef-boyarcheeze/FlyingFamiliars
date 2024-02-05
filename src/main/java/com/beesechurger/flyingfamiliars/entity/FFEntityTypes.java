package com.beesechurger.flyingfamiliars.entity;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.CloudRayEntity;
import com.beesechurger.flyingfamiliars.entity.common.CormorantEntity;
import com.beesechurger.flyingfamiliars.entity.common.GriffonflyEntity;
import com.beesechurger.flyingfamiliars.entity.common.MagicCarpetEntity;
import com.beesechurger.flyingfamiliars.entity.common.projectile.CaptureProjectile;
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
	
	public static final RegistryObject<EntityType<CloudRayEntity>> CLOUD_RAY = ENTITY_TYPES.register("cloud_ray", 
			() -> EntityType.Builder.of(CloudRayEntity::new, MobCategory.CREATURE)
					.sized(2.0f, 1.5f)
					.build(new ResourceLocation(FlyingFamiliars.MOD_ID, "cloud_ray").toString()));

	public static final RegistryObject<EntityType<CormorantEntity>> CORMORANT = ENTITY_TYPES.register("cormorant",
			() -> EntityType.Builder.of(CormorantEntity::new, MobCategory.CREATURE)
					.sized(0.4f, 1.4f)
					.build(new ResourceLocation(FlyingFamiliars.MOD_ID, "cormorant").toString()));
	
	public static final RegistryObject<EntityType<GriffonflyEntity>> GRIFFONFLY = ENTITY_TYPES.register("griffonfly", 
			() -> EntityType.Builder.of(GriffonflyEntity::new, MobCategory.CREATURE)
					.sized(1.5f, 1.5f)
					.build(new ResourceLocation(FlyingFamiliars.MOD_ID, "griffonfly").toString()));

	public static final RegistryObject<EntityType<MagicCarpetEntity>> MAGIC_CARPET = ENTITY_TYPES.register("magic_carpet",
			() -> EntityType.Builder.of(MagicCarpetEntity::new, MobCategory.CREATURE)
					.sized(1.8f, 0.5f)
					.build(new ResourceLocation(FlyingFamiliars.MOD_ID, "magic_carpet").toString()));
	
	public static final RegistryObject<EntityType<CaptureProjectile>> CAPTURE_PROJECTILE = ENTITY_TYPES.register("capture_projectile",
			() -> EntityType.Builder.<CaptureProjectile>of(CaptureProjectile::new, MobCategory.MISC)
					.sized(0.5f, 0.5f)
					.setShouldReceiveVelocityUpdates(true)
					.build("capture_projectile"));

	public static void register(IEventBus eventbus)
	{
		ENTITY_TYPES.register(eventbus);
	}
}
