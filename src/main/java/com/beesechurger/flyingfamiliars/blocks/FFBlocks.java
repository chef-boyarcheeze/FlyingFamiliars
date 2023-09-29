package com.beesechurger.flyingfamiliars.blocks;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.blocks.common.BrazierBlock;
import com.beesechurger.flyingfamiliars.blocks.common.CrystalBallBlock;
import com.beesechurger.flyingfamiliars.blocks.common.EffigyCoreBlock;
import com.beesechurger.flyingfamiliars.items.FFItems;
import com.google.common.base.Supplier;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FFBlocks
{
	public static final DeferredRegister<Block> BLOCK_REG = DeferredRegister.create(ForgeRegistries.BLOCKS, FlyingFamiliars.MOD_ID);
	
	public static final RegistryObject<Block> CRYSTAL_BALL = registerBlock("crystal_ball", () -> new CrystalBallBlock(BlockBehaviour.Properties.of(Material.STONE).strength(3f).requiresCorrectToolForDrops()), FlyingFamiliars.FF_TAB);
	public static final RegistryObject<Block> BRAZIER = registerBlock("brazier", () -> new BrazierBlock(BlockBehaviour.Properties.of(Material.METAL).strength(3f).requiresCorrectToolForDrops().lightLevel((state) -> 15).sound(SoundType.METAL)), FlyingFamiliars.FF_TAB);
	public static final RegistryObject<Block> EFFIGY_CORE = registerBlock("effigy_core", () -> new EffigyCoreBlock(BlockBehaviour.Properties.of(Material.VEGETABLE).strength(1f).sound(SoundType.CROP)), FlyingFamiliars.FF_TAB);
	
	private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab)
	{
		RegistryObject<T> object = BLOCK_REG.register(name, block);
		registerBlockItem(name, object, tab);
		return object;
	}
	
	private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab)
	{
		return FFItems.ITEM_REG.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
	}
	
	public static void register(IEventBus bus)
	{
		BLOCK_REG.register(bus);
	}
}
