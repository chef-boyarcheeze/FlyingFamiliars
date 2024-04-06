package com.beesechurger.flyingfamiliars.registries;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.block.common.BrazierBlock;
import com.beesechurger.flyingfamiliars.block.common.ObeliskBlock;
import com.google.common.base.Supplier;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FFBlocks
{
	public static final DeferredRegister<Block> BLOCK_REG = DeferredRegister.create(ForgeRegistries.BLOCKS, FlyingFamiliars.MOD_ID);

	public static final RegistryObject<Block> BRAZIER = registerBlock("brazier", () -> new BrazierBlock(BlockBehaviour.Properties.of().strength(3.0f, 6.0f).requiresCorrectToolForDrops().lightLevel((state) -> 15).sound(SoundType.METAL)));
	public static final RegistryObject<Block> OBELISK = registerBlock("obelisk", () -> new ObeliskBlock(BlockBehaviour.Properties.of().strength(2.0f, 4.0f).requiresCorrectToolForDrops().lightLevel((state) -> 15).sound(SoundType.STONE)));

	private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block)
	{
		RegistryObject<T> object = BLOCK_REG.register(name, block);
		registerBlockItem(name, object);
		return object;
	}
	
	private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block)
	{
		return FFItems.ITEM_REG.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
	}
}
