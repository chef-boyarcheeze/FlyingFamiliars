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
	public static EntityType<?>[] FARM = {EntityType.PIG, EntityType.COW, EntityType.SHEEP, EntityType.CHICKEN};
	public static EntityType<?>[] WARM = {EntityType.PANDA, EntityType.PARROT, EntityType.OCELOT};
	public static EntityType<?>[] WATERY = {EntityType.TURTLE, EntityType.DOLPHIN, EntityType.COD, EntityType.SALMON, EntityType.TROPICAL_FISH, EntityType.PUFFERFISH, EntityType.SQUID};
	public static EntityType<?>[] COLD = {EntityType.WOLF, EntityType.FOX, EntityType.GOAT, EntityType.POLAR_BEAR};
	public static EntityType<?>[] WINDY = {EntityType.HORSE, EntityType.DONKEY, EntityType.RABBIT, EntityType.LLAMA};
	public static EntityType<?>[] DIM = {EntityType.BAT, EntityType.GLOW_SQUID, EntityType.AXOLOTL};
	public static EntityType<?>[] CIVILIZED = {EntityType.VILLAGER, EntityType.CAT, EntityType.IRON_GOLEM};
	
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
	
	public static EntityType<?> getRandomEntityType()
	{
		return EntityType.PIG;
	}
}
