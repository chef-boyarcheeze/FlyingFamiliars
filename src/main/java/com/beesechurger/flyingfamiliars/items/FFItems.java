package com.beesechurger.flyingfamiliars.items;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.FFEntityTypes;
import com.beesechurger.flyingfamiliars.items.custom.SpecterMote;
import com.beesechurger.flyingfamiliars.items.custom.SoulWand;
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
	public static final RegistryObject<Item> SOUL_WAND_PROJECTILE = register("soul_wand_projectile", () -> new Item(new Item.Properties()));
	
	// Utility tools:
	public static final RegistryObject<Item> SOUL_WAND = register("soul_wand", () -> new SoulWand(new Item.Properties().tab(FlyingFamiliars.FF_TAB).stacksTo(1).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> SPECTER_MOTE = register("specter_mote", () -> new SpecterMote(new Item.Properties().tab(FlyingFamiliars.FF_TAB).stacksTo(1)));
	
	// General items:
	public static final RegistryObject<Item> BONES_OF_THE_EARTH = register("bones_of_the_earth", () -> new Item(new Item.Properties().tab(FlyingFamiliars.FF_TAB).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> HAND_OF_THE_HELLS = register("hand_of_the_hells", () -> new Item(new Item.Properties().tab(FlyingFamiliars.FF_TAB).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> EYE_OF_THE_STORM = register("eye_of_the_storm", () -> new Item(new Item.Properties().tab(FlyingFamiliars.FF_TAB).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> PHOENIX_FEATHER = register("phoenix_feather", () -> new Item(new Item.Properties().tab(FlyingFamiliars.FF_TAB)));
	
	// Spawn eggs:
	public static final RegistryObject<ForgeSpawnEggItem> PHOENIX_SPAWN_EGG = register("phoenix_spawn_egg", () -> new ForgeSpawnEggItem(FFEntityTypes.PHOENIX, 0xF52B00, 0xEFAD28, new Item.Properties().tab(FlyingFamiliars.FF_TAB)));
	public static final RegistryObject<ForgeSpawnEggItem> CLOUD_RAY_SPAWN_EGG = register("cloud_ray_spawn_egg", () -> new ForgeSpawnEggItem(FFEntityTypes.CLOUD_RAY, 0xDCDCDC, 0x88FFEB, new Item.Properties().tab(FlyingFamiliars.FF_TAB)));
	
	private static <T extends Item> RegistryObject<T> register(final String name, final Supplier<T> item) 
	{
		return ITEM_REG.register(name,item);
	}
}
