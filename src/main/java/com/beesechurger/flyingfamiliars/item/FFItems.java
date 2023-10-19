package com.beesechurger.flyingfamiliars.item;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.FFEntityTypes;
import com.beesechurger.flyingfamiliars.item.common.SoulItems.SoulBattery;
import com.beesechurger.flyingfamiliars.item.common.SoulItems.SoulWand.*;
import com.google.common.base.Supplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FFItems 
{
	public static final DeferredRegister<Item> ITEM_REG = DeferredRegister.create(ForgeRegistries.ITEMS, FlyingFamiliars.MOD_ID);
	
	// Creative tab icon and other items solely for effects:
	public static final RegistryObject<Item> CREATIVE_TAB_ICON = register("tab_icon", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> CAPTURE_PROJECTILE = register("capture_projectile", () -> new Item(new Item.Properties()));

	// Soul Wands:
	public static final RegistryObject<Item> WATER_SCEPTRE = register("water_sceptre", () -> new WaterSceptre(new Item.Properties().tab(FlyingFamiliars.FF_TAB).stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> VERDANT_BOUGH = register("verdant_bough", () -> new VerdantBough(new Item.Properties().tab(FlyingFamiliars.FF_TAB).stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> TEMPEST_STAFF = register("tempest_staff", () -> new TempestStaff(new Item.Properties().tab(FlyingFamiliars.FF_TAB).stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> FISSURE_BATON = register("fissure_baton", () -> new FissureBaton(new Item.Properties().tab(FlyingFamiliars.FF_TAB).stacksTo(1).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> FIERY_CROOK = register("fiery_crook", () -> new FieryCrook(new Item.Properties().tab(FlyingFamiliars.FF_TAB).stacksTo(1).rarity(Rarity.RARE)));
	public static final RegistryObject<Item> VOID_SHARD = register("void_shard", () -> new VoidShard(new Item.Properties().tab(FlyingFamiliars.FF_TAB).stacksTo(1).rarity(Rarity.EPIC)));
	public static final RegistryObject<Item> LIGHT_PRISM = register("light_prism", () -> new LightPrism(new Item.Properties().tab(FlyingFamiliars.FF_TAB).stacksTo(1).rarity(Rarity.EPIC)));

	// Soul Batteries:
	public static final RegistryObject<Item> SOUL_BATTERY_BLUE = register("soul_battery_blue", () -> new SoulBattery(new Item.Properties().tab(FlyingFamiliars.FF_TAB).stacksTo(1).rarity(Rarity.UNCOMMON), 1));
	public static final RegistryObject<Item> SOUL_BATTERY_GREEN = register("soul_battery_green", () -> new SoulBattery(new Item.Properties().tab(FlyingFamiliars.FF_TAB).stacksTo(1).rarity(Rarity.UNCOMMON), 2));
	public static final RegistryObject<Item> SOUL_BATTERY_YELLOW = register("soul_battery_yellow", () -> new SoulBattery(new Item.Properties().tab(FlyingFamiliars.FF_TAB).stacksTo(1).rarity(Rarity.UNCOMMON), 3));
	public static final RegistryObject<Item> SOUL_BATTERY_GOLD = register("soul_battery_gold", () -> new SoulBattery(new Item.Properties().tab(FlyingFamiliars.FF_TAB).stacksTo(1).rarity(Rarity.RARE), 4));
	public static final RegistryObject<Item> SOUL_BATTERY_RED = register("soul_battery_red", () -> new SoulBattery(new Item.Properties().tab(FlyingFamiliars.FF_TAB).stacksTo(1).rarity(Rarity.RARE), 5));
	public static final RegistryObject<Item> SOUL_BATTERY_BLACK = register("soul_battery_black", () -> new SoulBattery(new Item.Properties().tab(FlyingFamiliars.FF_TAB).stacksTo(1).rarity(Rarity.EPIC), 6));
	public static final RegistryObject<Item> SOUL_BATTERY_WHITE = register("soul_battery_white", () -> new SoulBattery(new Item.Properties().tab(FlyingFamiliars.FF_TAB).stacksTo(1).rarity(Rarity.EPIC), 7));

	// General items:
	public static final RegistryObject<Item> SOUL_WAND_ROD = register("soul_wand_rod", () -> new Item(new Item.Properties().tab(FlyingFamiliars.FF_TAB).rarity(Rarity.COMMON)));
	public static final RegistryObject<Item> PELT_OF_THE_FOREST = register("pelt_of_the_forest", () -> new Item(new Item.Properties().tab(FlyingFamiliars.FF_TAB).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> EYE_OF_THE_STORM = register("eye_of_the_storm", () -> new Item(new Item.Properties().tab(FlyingFamiliars.FF_TAB).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> BONES_OF_THE_EARTH = register("bones_of_the_earth", () -> new Item(new Item.Properties().tab(FlyingFamiliars.FF_TAB).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> HAND_OF_THE_HELLS = register("hand_of_the_hells", () -> new Item(new Item.Properties().tab(FlyingFamiliars.FF_TAB).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> PEARL_OF_THE_VOID = register("pearl_of_the_void", () -> new Item(new Item.Properties().tab(FlyingFamiliars.FF_TAB).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> STRAND_OF_THE_LIGHT = register("strand_of_the_light", () -> new Item(new Item.Properties().tab(FlyingFamiliars.FF_TAB).rarity(Rarity.UNCOMMON)));

	// Spawn eggs:
	public static final RegistryObject<ForgeSpawnEggItem> CLOUD_RAY_SPAWN_EGG = register("cloud_ray_spawn_egg", () -> new ForgeSpawnEggItem(FFEntityTypes.CLOUD_RAY, 0xDCDCDC, 0x88FFEB, new Item.Properties().tab(FlyingFamiliars.FF_TAB)));
	public static final RegistryObject<ForgeSpawnEggItem> CORMORANT_SPAWN_EGG = register("cormorant_spawn_egg", () -> new ForgeSpawnEggItem(FFEntityTypes.CORMORANT, 0x070F1C, 0x1B3B6E, new Item.Properties().tab(FlyingFamiliars.FF_TAB)));
	public static final RegistryObject<ForgeSpawnEggItem> GRIFFONFLY_SPAWN_EGG = register("griffonfly_spawn_egg", () -> new ForgeSpawnEggItem(FFEntityTypes.GRIFFONFLY, 0xCE9800, 0x7C1096, new Item.Properties().tab(FlyingFamiliars.FF_TAB)));
	
	private static <T extends Item> RegistryObject<T> register(final String name, final Supplier<T> item)
	{
		return ITEM_REG.register(name,item);
	}
}
