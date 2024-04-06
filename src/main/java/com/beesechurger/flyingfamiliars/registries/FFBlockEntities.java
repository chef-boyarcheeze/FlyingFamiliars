package com.beesechurger.flyingfamiliars.registries;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.block.client.brazier.BrazierRenderer;
import com.beesechurger.flyingfamiliars.block.entity.BrazierBlockEntity;
import com.beesechurger.flyingfamiliars.block.entity.ObeliskBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = FlyingFamiliars.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FFBlockEntities
{
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_REG = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, FlyingFamiliars.MOD_ID);
	
	public static final RegistryObject<BlockEntityType<BrazierBlockEntity>> BRAZIER_BLOCK_ENTITY = BLOCK_ENTITY_REG.register("brazier_block_entity", () -> BlockEntityType.Builder.of(BrazierBlockEntity::new, FFBlocks.BRAZIER.get()).build(null));
	public static final RegistryObject<BlockEntityType<ObeliskBlockEntity>> OBELISK_BLOCK_ENTITY = BLOCK_ENTITY_REG.register("obelisk_block_entity", () -> BlockEntityType.Builder.of(ObeliskBlockEntity::new, FFBlocks.OBELISK.get()).build(null));

	@SubscribeEvent
	public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerBlockEntityRenderer(FFBlockEntities.BRAZIER_BLOCK_ENTITY.get(), BrazierRenderer::new);
		//event.registerBlockEntityRenderer(FFBlockEntities.OBELISK_BLOCK_ENTITY.get(), ObeliskRenderer::new);
	}
}
