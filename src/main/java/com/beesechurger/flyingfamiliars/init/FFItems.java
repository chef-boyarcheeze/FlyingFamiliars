package com.beesechurger.flyingfamiliars.init;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.ModEntityTypes;
import com.beesechurger.flyingfamiliars.items.CloudRayTablet;
import com.beesechurger.flyingfamiliars.items.FamiliarTablet;
import com.beesechurger.flyingfamiliars.items.SummoningBell;
import com.google.common.base.Supplier;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FFItems {
	public static final DeferredRegister<Item> ITEM_REG = DeferredRegister.create(ForgeRegistries.ITEMS, FlyingFamiliars.MOD_ID);
	
	// Creative mode tab item (same as the phoenix feather):
	public static final RegistryObject<Item> CREATIVE_TAB_ICON = register("tab_icon", () -> new Item(new Item.Properties()));
	
	// Music note items (for summoning bell projectile, based on vanilla minecraft notes):
	public static final RegistryObject<Item> MUSIC_NOTE_1 = register("music_note_1", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> MUSIC_NOTE_2 = register("music_note_2", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> MUSIC_NOTE_3 = register("music_note_3", () -> new Item(new Item.Properties()));
	
	// Tools/Utility items:
	public static final RegistryObject<Item> SUMMONING_BELL = register("summoning_bell", () -> new SummoningBell(new Item.Properties().tab(FlyingFamiliars.FF_TAB).stacksTo(1).durability(3)));
	public static final RegistryObject<Item> FAMILIAR_TABLET = register("familiar_tablet", () -> new FamiliarTablet(new Item.Properties().tab(FlyingFamiliars.FF_TAB)));
	public static final RegistryObject<Item> CLOUD_RAY_TABLET = register("cloud_ray_tablet", () -> new CloudRayTablet(new Item.Properties().tab(FlyingFamiliars.FF_TAB)));
	
	// General items:
	public static final RegistryObject<Item> PHOENIX_FEATHER = register("phoenix_feather", () -> new Item(new Item.Properties().tab(FlyingFamiliars.FF_TAB)));
	
	// Spawn eggs:
	public static final RegistryObject<ForgeSpawnEggItem> PHOENIX_SPAWN_EGG = register("phoenix_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.PHOENIX, 0xF52B00, 0xEFAD28, new Item.Properties().tab(FlyingFamiliars.FF_TAB)));
	public static final RegistryObject<ForgeSpawnEggItem> CLOUD_RAY_SPAWN_EGG = register("cloud_ray_spawn_egg", () -> new ForgeSpawnEggItem(ModEntityTypes.CLOUD_RAY, 0xDCDCDC, 0x88FFEB, new Item.Properties().tab(FlyingFamiliars.FF_TAB)));
	
	private static <T extends Item> RegistryObject<T> register(final String name, final Supplier<T> item) 
	{
		return ITEM_REG.register(name,item);
	}
}
