package com.beesechurger.flyingfamiliars.blocks.entity;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.blocks.FFBlocks;
import com.beesechurger.flyingfamiliars.blocks.entity.custom.BrazierBlockEntity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FFBlockEntities
{
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, FlyingFamiliars.MOD_ID);
	
	public static final RegistryObject<BlockEntityType<BrazierBlockEntity>> BRAZIER_BLOCK_ENTITY = BLOCK_ENTITIES.register("brazier_block_entity", () -> BlockEntityType.Builder.of(BrazierBlockEntity::new, FFBlocks.BRAZIER.get()).build(null));
	
	public static void register(IEventBus eventBus)
	{
		BLOCK_ENTITIES.register(eventBus);
	}
}
