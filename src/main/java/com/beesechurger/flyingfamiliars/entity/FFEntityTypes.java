package com.beesechurger.flyingfamiliars.entity;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.familiar.*;
import com.beesechurger.flyingfamiliars.entity.common.wand_effect.charm.CrystalSpikeCharm;
import com.beesechurger.flyingfamiliars.entity.common.wand_effect.projectile.CaptureProjectile;
import com.beesechurger.flyingfamiliars.entity.common.wand_effect.projectile.FireballProjectile;
import com.beesechurger.flyingfamiliars.entity.common.wand_effect.projectile.FlamethrowerProjectile;
import com.beesechurger.flyingfamiliars.entity.common.wand_effect.projectile.RunicCubeProjectile;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

@Mod.EventBusSubscriber(modid = FlyingFamiliars.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FFEntityTypes
{
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, FlyingFamiliars.MOD_ID);

//////////////////
/// Familiars: ///
//////////////////

	// Cloud Ray
	public static final RegistryObject<EntityType<CloudRayEntity>> CLOUD_RAY = ENTITY_TYPES.register("cloud_ray",
			() -> EntityType.Builder.of(CloudRayEntity::new, MobCategory.CREATURE)
					.sized(4.5f, 2.0f)
					.build(new ResourceLocation(FlyingFamiliars.MOD_ID, "cloud_ray").toString()));

	// Griffonfly
	public static final RegistryObject<EntityType<GriffonflyEntity>> GRIFFONFLY = ENTITY_TYPES.register("griffonfly",
			() -> EntityType.Builder.of(GriffonflyEntity::new, MobCategory.CREATURE)
					.sized(1.8f, 1.8f)
					.build(new ResourceLocation(FlyingFamiliars.MOD_ID, "griffonfly").toString()));

	// Thunderbird

	// Magic Carpet
	public static final RegistryObject<EntityType<MagicCarpetEntity>> MAGIC_CARPET = ENTITY_TYPES.register("magic_carpet",
			() -> EntityType.Builder.of(MagicCarpetEntity::new, MobCategory.CREATURE)
					.sized(1.8f, 0.5f)
					.build(new ResourceLocation(FlyingFamiliars.MOD_ID, "magic_carpet").toString()));

	// Dragon

	// Shadewyrm
	public static final RegistryObject<EntityType<ShadewyrmEntity>> SHADEWYRM = ENTITY_TYPES.register("shadewyrm",
			() -> EntityType.Builder.of(ShadewyrmEntity::new, MobCategory.CREATURE)
					.sized(3.0f, 2.0f)
					.build(new ResourceLocation(FlyingFamiliars.MOD_ID, "shadewyrm").toString()));

	// Sundog

	// Deep Jellyfish
	public static final RegistryObject<EntityType<DeepJellyfishEntity>> DEEP_JELLYFISH = ENTITY_TYPES.register("deep_jellyfish",
			() -> EntityType.Builder.of(DeepJellyfishEntity::new, MobCategory.CREATURE)
					.sized(1.0f, 2.5f)
					.build(new ResourceLocation(FlyingFamiliars.MOD_ID, "deep_jellyfish").toString()));

	// Shrubling
	public static final RegistryObject<EntityType<ShrublingEntity>> SHRUBLING = ENTITY_TYPES.register("shrubling",
			() -> EntityType.Builder.of(ShrublingEntity::new, MobCategory.CREATURE)
					.sized(1.0f, 1.5f)
					.build(new ResourceLocation(FlyingFamiliars.MOD_ID, "shrubling").toString()));

	// Zephyr Fish
	public static final RegistryObject<EntityType<ZephyrFishEntity>> ZEPHYR_FISH = ENTITY_TYPES.register("zephyr_fish",
			() -> EntityType.Builder.of(ZephyrFishEntity::new, MobCategory.CREATURE)
					.sized(1.2f, 1.2f)
					.build(new ResourceLocation(FlyingFamiliars.MOD_ID, "zephyr_fish").toString()));

	// Crystal Tressym
	public static final RegistryObject<EntityType<CrystalTressymEntity>> CRYSTAL_TRESSYM = ENTITY_TYPES.register("crystal_tressym",
			() -> EntityType.Builder.of(CrystalTressymEntity::new, MobCategory.CREATURE)
					.sized(1.2f, 1.0f)
					.build(new ResourceLocation(FlyingFamiliars.MOD_ID, "crystal_tressym").toString()));

	// Phoenix
	public static final RegistryObject<EntityType<PhoenixEntity>> PHOENIX = ENTITY_TYPES.register("phoenix",
			() -> EntityType.Builder.of(PhoenixEntity::new, MobCategory.CREATURE)
					.sized(0.8f, 1.4f)
					.build(new ResourceLocation(FlyingFamiliars.MOD_ID, "phoenix").toString()));

	// Void Moth
	public static final RegistryObject<EntityType<VoidMothEntity>> VOID_MOTH = ENTITY_TYPES.register("void_moth",
			() -> EntityType.Builder.of(VoidMothEntity::new, MobCategory.CREATURE)
					.sized(1.0f, 1.0f)
					.build(new ResourceLocation(FlyingFamiliars.MOD_ID, "void_moth").toString()));

	// Mirror Shield
	public static final RegistryObject<EntityType<MirrorShieldEntity>> MIRROR_SHIELD = ENTITY_TYPES.register("mirror_shield",
			() -> EntityType.Builder.of(MirrorShieldEntity::new, MobCategory.CREATURE)
					.sized(1.0f, 1.5f)
					.build(new ResourceLocation(FlyingFamiliars.MOD_ID, "mirror_shield").toString()));

/// Familiar render sizes:

	public static final Map<String, Float> ENTITY_RENDER_SIZE_MAP = (Map) Util.make(Maps.newHashMap(), (map) -> {
		map.put("cloud_ray", 1.0f);
		map.put("griffonfly", 1.6f);
		//map.put("thunderbird", 1.0f);
		map.put("magic_carpet", 1.5f);
		//map.put("dragon", 1.0f);
		map.put("shadewyrm", 1.0f);
		//map.put("sundog", 1.0f);

		map.put("deep_jellyfish", 1.2f);
		map.put("shrubling", 1.2f);
		map.put("zephyr_fish", 1.0f);
		map.put("crystal_tressym", 1.2f);
		map.put("phoenix", 1.2f);
		map.put("void_moth", 1.0f);
		map.put("mirror_shield", 1.2f);
	});

/////////////////////
/// Wand Effects: ///
/////////////////////

/// Charms:

	// Crystal Spike
	public static final RegistryObject<EntityType<CrystalSpikeCharm>> CRYSTAL_SPIKE_CHARM = ENTITY_TYPES.register("crystal_spike_charm",
			() -> EntityType.Builder.<CrystalSpikeCharm>of(CrystalSpikeCharm::new, MobCategory.MISC)
					.sized(1.0f, 1.0f)
					.build("crystal_spike_charm"));

/// Projectiles:

	// Capture
	public static final RegistryObject<EntityType<CaptureProjectile>> CAPTURE_PROJECTILE = ENTITY_TYPES.register("capture_projectile",
			() -> EntityType.Builder.<CaptureProjectile>of(CaptureProjectile::new, MobCategory.MISC)
					.sized(0.5f, 0.5f)
					.setShouldReceiveVelocityUpdates(true)
					.build("capture_projectile"));

	// Fireball
	public static final RegistryObject<EntityType<FireballProjectile>> FIREBALL_PROJECTILE = ENTITY_TYPES.register("fireball_projectile",
			() -> EntityType.Builder.<FireballProjectile>of(FireballProjectile::new, MobCategory.MISC)
					.sized(0.8f, 0.8f)
					.setShouldReceiveVelocityUpdates(true)
					.build("fireball_projectile"));

	// Flamethrower
	public static final RegistryObject<EntityType<FlamethrowerProjectile>> FLAMETHROWER_PROJECTILE = ENTITY_TYPES.register("flamethrower_projectile",
			() -> EntityType.Builder.<FlamethrowerProjectile>of(FlamethrowerProjectile::new, MobCategory.MISC)
					.sized(0.8f, 0.8f)
					.setShouldReceiveVelocityUpdates(true)
					.build("flamethrower_projectile"));

	// Runic Pedestal cube
	public static final RegistryObject<EntityType<RunicCubeProjectile>> RUNIC_CUBE_PROJECTILE = ENTITY_TYPES.register("runic_cube_projectile",
			() -> EntityType.Builder.<RunicCubeProjectile>of(RunicCubeProjectile::new, MobCategory.MISC)
					.sized(0.8f, 0.8f)
					.setShouldReceiveVelocityUpdates(true)
					.build("runic_cube_projectile"));

/// Sentries:

	@SubscribeEvent
	public static void entityAttributeEvent(EntityAttributeCreationEvent event)
	{
		event.put(FFEntityTypes.CLOUD_RAY.get(), CloudRayEntity.setAttributes());
		event.put(FFEntityTypes.GRIFFONFLY.get(), GriffonflyEntity.setAttributes());

		event.put(FFEntityTypes.MAGIC_CARPET.get(), MagicCarpetEntity.setAttributes());

		event.put(FFEntityTypes.SHADEWYRM.get(), ShadewyrmEntity.setAttributes());


		event.put(FFEntityTypes.DEEP_JELLYFISH.get(), DeepJellyfishEntity.setAttributes());
		event.put(FFEntityTypes.SHRUBLING.get(), ShrublingEntity.setAttributes());
		event.put(FFEntityTypes.ZEPHYR_FISH.get(), ZephyrFishEntity.setAttributes());
		event.put(FFEntityTypes.CRYSTAL_TRESSYM.get(), CrystalTressymEntity.setAttributes());
		event.put(FFEntityTypes.PHOENIX.get(), PhoenixEntity.setAttributes());
		event.put(FFEntityTypes.VOID_MOTH.get(), VoidMothEntity.setAttributes());
		event.put(FFEntityTypes.MIRROR_SHIELD.get(), MirrorShieldEntity.setAttributes());
	}
}
