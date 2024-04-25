package com.beesechurger.flyingfamiliars.registries;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.block.client.brazier.BrazierRenderer;
import com.beesechurger.flyingfamiliars.block.client.ceremonial_font.CeremonialFontCupModel;
import com.beesechurger.flyingfamiliars.block.client.ceremonial_font.CeremonialFontRenderer;
import com.beesechurger.flyingfamiliars.block.client.obelisk.ObeliskRenderer;
import com.beesechurger.flyingfamiliars.block.client.obelisk.ObeliskPillarModel;
import com.beesechurger.flyingfamiliars.block.client.vita_alembic.VitaAlembicRenderer;
import com.beesechurger.flyingfamiliars.block.entity.BrazierBE;
import com.beesechurger.flyingfamiliars.block.entity.CeremonialFontBE;
import com.beesechurger.flyingfamiliars.block.entity.ObeliskBE;
import com.beesechurger.flyingfamiliars.block.entity.VitaAlembicBE;
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
	
	public static final RegistryObject<BlockEntityType<BrazierBE>> BRAZIER_BLOCK_ENTITY = BLOCK_ENTITY_REG.register("brazier_block_entity", () -> BlockEntityType.Builder.of(BrazierBE::new, FFBlocks.BRAZIER.get()).build(null));
	public static final RegistryObject<BlockEntityType<ObeliskBE>> OBELISK_BLOCK_ENTITY = BLOCK_ENTITY_REG.register("obelisk_block_entity", () -> BlockEntityType.Builder.of(ObeliskBE::new, FFBlocks.OBELISK.get()).build(null));
	public static final RegistryObject<BlockEntityType<VitaAlembicBE>> VITA_ALEMBIC_BLOCK_ENTITY = BLOCK_ENTITY_REG.register("vita_alembic_block_entity", () -> BlockEntityType.Builder.of(VitaAlembicBE::new, FFBlocks.VITA_ALEMBIC.get()).build(null));
	public static final RegistryObject<BlockEntityType<CeremonialFontBE>> CEREMONIAL_FONT_BLOCK_ENTITY = BLOCK_ENTITY_REG.register("ceremonial_font_block_entity", () -> BlockEntityType.Builder.of(CeremonialFontBE::new, FFBlocks.CEREMONIAL_FONT.get()).build(null));

	@SubscribeEvent
	public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerBlockEntityRenderer(FFBlockEntities.BRAZIER_BLOCK_ENTITY.get(), BrazierRenderer::new);
		event.registerBlockEntityRenderer(FFBlockEntities.OBELISK_BLOCK_ENTITY.get(), ObeliskRenderer::new);
		event.registerBlockEntityRenderer(FFBlockEntities.VITA_ALEMBIC_BLOCK_ENTITY.get(), VitaAlembicRenderer::new);
		event.registerBlockEntityRenderer(FFBlockEntities.CEREMONIAL_FONT_BLOCK_ENTITY.get(), CeremonialFontRenderer::new);
	}

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event)
	{
		event.registerLayerDefinition(ObeliskPillarModel.LAYER_LOCATION, ObeliskPillarModel::createBodyLayer);
		event.registerLayerDefinition(CeremonialFontCupModel.LAYER_LOCATION, CeremonialFontCupModel::createBodyLayer);
	}
}