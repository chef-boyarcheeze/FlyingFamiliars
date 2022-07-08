package com.beesechurger.flyingfamiliars.init;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.items.SummoningBell;
import com.google.common.base.Supplier;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FFItems {
	public static final DeferredRegister<Item> ITEM_REG = DeferredRegister.create(ForgeRegistries.ITEMS, FlyingFamiliars.MOD_ID);
	
	// Creative mode tab item (same as the summoning bell):
	public static final RegistryObject<Item> CREATIVE_TAB_ICON = register("tab_icon", () -> new Item(new Item.Properties()));
	
	// Tools/Utility items:
	public static final RegistryObject<Item> SUMMONING_BELL = register("summoning_bell", () -> new SummoningBell(new Item.Properties().tab(FlyingFamiliars.FF_TAB).stacksTo(1).durability(3)));
	
	// General items:
	public static final RegistryObject<Item> PHOENIX_FEATHER = register("phoenix_feather", () -> new Item(new Item.Properties().tab(FlyingFamiliars.FF_TAB)));
	
	private static <T extends Item> RegistryObject<T> register(final String name, final Supplier<T> item) 
	{
		return ITEM_REG.register(name,item);
	}
}
