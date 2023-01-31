package com.beesechurger.flyingfamiliars.items;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.FFEntityTypes;
import com.beesechurger.flyingfamiliars.items.custom.SoulWand;
import com.beesechurger.flyingfamiliars.items.custom.spiritcrystal.CivilizedSpiritCrystal;
import com.beesechurger.flyingfamiliars.items.custom.spiritcrystal.CloudRaySpiritCrystal;
import com.beesechurger.flyingfamiliars.items.custom.spiritcrystal.ColdSpiritCrystal;
import com.beesechurger.flyingfamiliars.items.custom.spiritcrystal.DimSpiritCrystal;
import com.beesechurger.flyingfamiliars.items.custom.spiritcrystal.GrassySpiritCrystal;
import com.beesechurger.flyingfamiliars.items.custom.spiritcrystal.PastureSpiritCrystal;
import com.beesechurger.flyingfamiliars.items.custom.spiritcrystal.SpiritCrystal;
import com.beesechurger.flyingfamiliars.items.custom.spiritcrystal.WarmSpiritCrystal;
import com.beesechurger.flyingfamiliars.items.custom.spiritcrystal.WaterySpiritCrystal;
import com.google.common.base.Supplier;

import net.minecraft.world.item.Item;
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
	public static final RegistryObject<Item> SOUL_WAND = register("soul_wand", () -> new SoulWand(new Item.Properties().tab(FlyingFamiliars.FF_TAB).stacksTo(1)));
	public static final RegistryObject<Item> SPIRIT_CRYSTAL = register("spirit_crystal", () -> new SpiritCrystal(new Item.Properties().tab(FlyingFamiliars.FF_TAB).stacksTo(1)));
	public static final RegistryObject<Item> WARM_SPIRIT_CRYSTAL = register("warm_spirit_crystal", () -> new WarmSpiritCrystal(new Item.Properties().tab(FlyingFamiliars.FF_TAB).stacksTo(16)));
	public static final RegistryObject<Item> WATERY_SPIRIT_CRYSTAL = register("watery_spirit_crystal", () -> new WaterySpiritCrystal(new Item.Properties().tab(FlyingFamiliars.FF_TAB).stacksTo(16)));
	public static final RegistryObject<Item> DIM_SPIRIT_CRYSTAL = register("dim_spirit_crystal", () -> new DimSpiritCrystal(new Item.Properties().tab(FlyingFamiliars.FF_TAB).stacksTo(16)));
	public static final RegistryObject<Item> COLD_SPIRIT_CRYSTAL = register("cold_spirit_crystal", () -> new ColdSpiritCrystal(new Item.Properties().tab(FlyingFamiliars.FF_TAB).stacksTo(16)));
	public static final RegistryObject<Item> CIVILIZED_SPIRIT_CRYSTAL = register("civilized_spirit_crystal", () -> new CivilizedSpiritCrystal(new Item.Properties().tab(FlyingFamiliars.FF_TAB).stacksTo(16)));
	public static final RegistryObject<Item> GRASSY_SPIRIT_CRYSTAL = register("grassy_spirit_crystal", () -> new GrassySpiritCrystal(new Item.Properties().tab(FlyingFamiliars.FF_TAB).stacksTo(16)));
	public static final RegistryObject<Item> PASTURE_SPIRIT_CRYSTAL = register("pasture_spirit_crystal", () -> new PastureSpiritCrystal(new Item.Properties().tab(FlyingFamiliars.FF_TAB).stacksTo(16)));
	public static final RegistryObject<Item> CLOUD_RAY_CRYSTAL = register("cloud_ray_spirit_crystal", () -> new CloudRaySpiritCrystal(new Item.Properties().tab(FlyingFamiliars.FF_TAB).stacksTo(16)));
	
	// General items:
	public static final RegistryObject<Item> PHOENIX_FEATHER = register("phoenix_feather", () -> new Item(new Item.Properties().tab(FlyingFamiliars.FF_TAB)));
	
	// Spawn eggs:
	public static final RegistryObject<ForgeSpawnEggItem> PHOENIX_SPAWN_EGG = register("phoenix_spawn_egg", () -> new ForgeSpawnEggItem(FFEntityTypes.PHOENIX, 0xF52B00, 0xEFAD28, new Item.Properties().tab(FlyingFamiliars.FF_TAB)));
	public static final RegistryObject<ForgeSpawnEggItem> CLOUD_RAY_SPAWN_EGG = register("cloud_ray_spawn_egg", () -> new ForgeSpawnEggItem(FFEntityTypes.CLOUD_RAY, 0xDCDCDC, 0x88FFEB, new Item.Properties().tab(FlyingFamiliars.FF_TAB)));
	
	private static <T extends Item> RegistryObject<T> register(final String name, final Supplier<T> item) 
	{
		return ITEM_REG.register(name,item);
	}
}
