package com.beesechurger.flyingfamiliars.registries;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.*;
import com.beesechurger.flyingfamiliars.entity.common.projectile.CaptureProjectile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = FlyingFamiliars.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FFEntityTypes 
{
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPE_REG = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, FlyingFamiliars.MOD_ID);
	
	public static final RegistryObject<EntityType<CloudRayEntity>> CLOUD_RAY = ENTITY_TYPE_REG.register("cloud_ray",
			() -> EntityType.Builder.of(CloudRayEntity::new, MobCategory.CREATURE)
					.sized(2.0f, 1.5f)
					.build(new ResourceLocation(FlyingFamiliars.MOD_ID, "cloud_ray").toString()));

	public static final RegistryObject<EntityType<CormorantEntity>> CORMORANT = ENTITY_TYPE_REG.register("cormorant",
			() -> EntityType.Builder.of(CormorantEntity::new, MobCategory.CREATURE)
					.sized(0.4f, 1.4f)
					.build(new ResourceLocation(FlyingFamiliars.MOD_ID, "cormorant").toString()));
	
	public static final RegistryObject<EntityType<GriffonflyEntity>> GRIFFONFLY = ENTITY_TYPE_REG.register("griffonfly",
			() -> EntityType.Builder.of(GriffonflyEntity::new, MobCategory.CREATURE)
					.sized(1.5f, 1.5f)
					.build(new ResourceLocation(FlyingFamiliars.MOD_ID, "griffonfly").toString()));

	public static final RegistryObject<EntityType<MagicCarpetEntity>> MAGIC_CARPET = ENTITY_TYPE_REG.register("magic_carpet",
			() -> EntityType.Builder.of(MagicCarpetEntity::new, MobCategory.CREATURE)
					.sized(1.8f, 0.5f)
					.build(new ResourceLocation(FlyingFamiliars.MOD_ID, "magic_carpet").toString()));

	public static final RegistryObject<EntityType<PhoenixEntity>> PHOENIX = ENTITY_TYPE_REG.register("phoenix",
			() -> EntityType.Builder.of(PhoenixEntity::new, MobCategory.CREATURE)
					.sized(1.0f, 1.0f)
					.build(new ResourceLocation(FlyingFamiliars.MOD_ID, "phoenix").toString()));

	public static final RegistryObject<EntityType<VoidMothEntity>> VOID_MOTH = ENTITY_TYPE_REG.register("void_moth",
			() -> EntityType.Builder.of(VoidMothEntity::new, MobCategory.CREATURE)
					.sized(1.0f, 1.0f)
					.build(new ResourceLocation(FlyingFamiliars.MOD_ID, "void_moth").toString()));
	
	public static final RegistryObject<EntityType<CaptureProjectile>> CAPTURE_PROJECTILE = ENTITY_TYPE_REG.register("capture_projectile",
			() -> EntityType.Builder.<CaptureProjectile>of(CaptureProjectile::new, MobCategory.MISC)
					.sized(0.5f, 0.5f)
					.setShouldReceiveVelocityUpdates(true)
					.build("capture_projectile"));

	@SubscribeEvent
	public static void entityAttributeEvent(EntityAttributeCreationEvent event)
	{
		event.put(FFEntityTypes.CLOUD_RAY.get(), CloudRayEntity.setAttributes());
		event.put(FFEntityTypes.CORMORANT.get(), GriffonflyEntity.setAttributes());
		event.put(FFEntityTypes.GRIFFONFLY.get(), GriffonflyEntity.setAttributes());
		event.put(FFEntityTypes.MAGIC_CARPET.get(), MagicCarpetEntity.setAttributes());
		event.put(FFEntityTypes.PHOENIX.get(), PhoenixEntity.setAttributes());
		event.put(FFEntityTypes.VOID_MOTH.get(), VoidMothEntity.setAttributes());
	}
}
