package com.beesechurger.flyingfamiliars.registries;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.block.common.BrazierBlock;
import com.beesechurger.flyingfamiliars.block.common.CeremonialFontBlock;
import com.beesechurger.flyingfamiliars.block.common.ObeliskBlock;
import com.beesechurger.flyingfamiliars.block.common.VitaAlembicBlock;
import com.google.common.base.Supplier;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FFBlocks
{
	public static final DeferredRegister<Block> BLOCK_REG = DeferredRegister.create(ForgeRegistries.BLOCKS, FlyingFamiliars.MOD_ID);

	// Normal Blocks
	public static final RegistryObject<Block> RUNIC_BRICKS = registerBlock("runic_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICKS).strength(2.0f).requiresCorrectToolForDrops().sound(FFSounds.RUNIC_BRICKS).instrument(NoteBlockInstrument.HAT)));
	public static final RegistryObject<Block> RUNIC_BRICK_SLAB = registerBlock("runic_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(RUNIC_BRICKS.get())));
	public static final RegistryObject<Block> RUNIC_BRICK_STAIRS = registerBlock("runic_brick_stairs", () -> new StairBlock(RUNIC_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(RUNIC_BRICKS.get())));
	public static final RegistryObject<Block> RUNIC_BRICK_WALL = registerBlock("runic_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(RUNIC_BRICKS.get())));

	public static final RegistryObject<Block> INSCRIBED_RUNIC_BRICKS = registerBlock("inscribed_runic_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICKS).strength(2.0f).requiresCorrectToolForDrops().sound(FFSounds.RUNIC_BRICKS).instrument(NoteBlockInstrument.HAT)));
	public static final RegistryObject<Block> INSCRIBED_RUNIC_BRICK_SLAB = registerBlock("inscribed_runic_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(INSCRIBED_RUNIC_BRICKS.get())));
	public static final RegistryObject<Block> INSCRIBED_RUNIC_BRICK_STAIRS = registerBlock("inscribed_runic_brick_stairs", () -> new StairBlock(INSCRIBED_RUNIC_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(INSCRIBED_RUNIC_BRICKS.get())));
	public static final RegistryObject<Block> INSCRIBED_RUNIC_BRICK_WALL = registerBlock("inscribed_runic_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(INSCRIBED_RUNIC_BRICKS.get())));

	// Block Entities
	public static final RegistryObject<Block> BRAZIER = registerBlock("brazier", () -> new BrazierBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK).lightLevel((state) -> 15)));
	public static final RegistryObject<Block> OBELISK = registerBlock("obelisk", () -> new ObeliskBlock(BlockBehaviour.Properties.copy(FFBlocks.RUNIC_BRICKS.get())));
	public static final RegistryObject<Block> VITA_ALEMBIC = registerBlock("vita_alembic", () -> new VitaAlembicBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICKS).noOcclusion()));
	public static final RegistryObject<Block> CEREMONIAL_FONT = registerBlock("ceremonial_font", () -> new CeremonialFontBlock(BlockBehaviour.Properties.copy(Blocks.SMOOTH_STONE).noOcclusion()));

	// Fluids
	public static final RegistryObject<LiquidBlock> BLUE_VITALITY_BLOCK = BLOCK_REG.register("blue_vitality_block", () -> new LiquidBlock(FFFluids.SOURCE_BLUE_VITALITY, BlockBehaviour.Properties.copy(Blocks.WATER)));
	public static final RegistryObject<LiquidBlock> GREEN_VITALITY_BLOCK = BLOCK_REG.register("green_vitality_block", () -> new LiquidBlock(FFFluids.SOURCE_GREEN_VITALITY, BlockBehaviour.Properties.copy(Blocks.WATER)));
	public static final RegistryObject<LiquidBlock> YELLOW_VITALITY_BLOCK = BLOCK_REG.register("yellow_vitality_block", () -> new LiquidBlock(FFFluids.SOURCE_YELLOW_VITALITY, BlockBehaviour.Properties.copy(Blocks.WATER)));
	public static final RegistryObject<LiquidBlock> GOLD_VITALITY_BLOCK = BLOCK_REG.register("gold_vitality_block", () -> new LiquidBlock(FFFluids.SOURCE_GOLD_VITALITY, BlockBehaviour.Properties.copy(Blocks.WATER)));
	public static final RegistryObject<LiquidBlock> RED_VITALITY_BLOCK = BLOCK_REG.register("red_vitality_block", () -> new LiquidBlock(FFFluids.SOURCE_RED_VITALITY, BlockBehaviour.Properties.copy(Blocks.WATER)));
	public static final RegistryObject<LiquidBlock> BLACK_VITALITY_BLOCK = BLOCK_REG.register("black_vitality_block", () -> new LiquidBlock(FFFluids.SOURCE_BLACK_VITALITY, BlockBehaviour.Properties.copy(Blocks.WATER)));
	public static final RegistryObject<LiquidBlock> WHITE_VITALITY_BLOCK = BLOCK_REG.register("white_vitality_block", () -> new LiquidBlock(FFFluids.SOURCE_WHITE_VITALITY, BlockBehaviour.Properties.copy(Blocks.WATER)));

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
