package com.beesechurger.flyingfamiliars;

import com.beesechurger.flyingfamiliars.entity.client.familiar.phoenix.PhoenixRenderer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.void_moth.VoidMothRenderer;
import com.beesechurger.flyingfamiliars.registries.*;
import com.beesechurger.flyingfamiliars.entity.client.familiar.cloud_ray.CloudRayRenderer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.cormorant.CormorantRenderer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.griffonfly.GriffonflyRenderer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.magic_carpet.MagicCarpetRenderer;
import com.beesechurger.flyingfamiliars.entity.client.wand_effect.CaptureProjectileRenderer;
import com.beesechurger.flyingfamiliars.event.ClientEvents;
import com.beesechurger.flyingfamiliars.integration.curios.CuriosIntegration;
import com.beesechurger.flyingfamiliars.item.FFItemHandler;
import com.beesechurger.flyingfamiliars.item.client.PhylacteryRenderer;
import com.beesechurger.flyingfamiliars.registries.FFPackets;
import com.beesechurger.flyingfamiliars.registries.FFRecipes;
import com.beesechurger.flyingfamiliars.registries.FFSounds;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib.GeckoLib;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.CURIOS_MODNAME;

@Mod("flyingfamiliars")
public class FlyingFamiliars
{
	public static final String MOD_ID = "flyingfamiliars";

	public FlyingFamiliars()
	{
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		FFItems.ITEM_REG.register(bus);

		FFBlocks.BLOCK_REG.register(bus);
		FFBlockEntities.BLOCK_ENTITY_REG.register(bus);

		FFFluids.FLUIDS_REG.register(bus);
		FFFluidTypes.FLUID_TYPES_REG.register(bus);

		FFEffects.MOB_EFFECT_REG.register(bus);
		FFEntityTypes.ENTITY_TYPE_REG.register(bus);

		FFCreativeTabs.CREATIVE_TAB_REG.register(bus);
		FFSounds.SOUND_EVENT_REG.register(bus);
		FFRecipes.RECIPE_SERIALIZER_REG.register(bus);
		
		bus.addListener(this::commonSetup);
		bus.addListener(this::clientSetup);
		
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(FFItemHandler.INSTANCE);
		MinecraftForge.EVENT_BUS.addListener(ClientEvents::onCameraSetup);

		if(ModList.get().isLoaded(CURIOS_MODNAME))
			CuriosIntegration.register();

		GeckoLib.initialize();
	}
	
	private void commonSetup(final FMLCommonSetupEvent event)
	{
		FFPackets.register();
	}
	
	private void clientSetup(final FMLClientSetupEvent event)
	{
		EntityRenderers.register(FFEntityTypes.CLOUD_RAY.get(), CloudRayRenderer::new);
		EntityRenderers.register(FFEntityTypes.CORMORANT.get(), CormorantRenderer::new);
		EntityRenderers.register(FFEntityTypes.GRIFFONFLY.get(), GriffonflyRenderer::new);
		EntityRenderers.register(FFEntityTypes.MAGIC_CARPET.get(), MagicCarpetRenderer::new);
		EntityRenderers.register(FFEntityTypes.CAPTURE_PROJECTILE.get(), CaptureProjectileRenderer::new);
		EntityRenderers.register(FFEntityTypes.PHOENIX.get(), PhoenixRenderer::new);
		EntityRenderers.register(FFEntityTypes.VOID_MOTH.get(), VoidMothRenderer::new);

		ItemBlockRenderTypes.setRenderLayer(FFBlocks.BRAZIER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(FFBlocks.VITA_ALEMBIC.get(), RenderType.translucent());

		ItemBlockRenderTypes.setRenderLayer(FFFluids.SOURCE_BLUE_VITALITY.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FFFluids.FLOWING_BLUE_VITALITY.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FFFluids.SOURCE_GREEN_VITALITY.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FFFluids.FLOWING_GREEN_VITALITY.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FFFluids.SOURCE_YELLOW_VITALITY.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FFFluids.FLOWING_YELLOW_VITALITY.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FFFluids.SOURCE_GOLD_VITALITY.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FFFluids.FLOWING_GOLD_VITALITY.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FFFluids.SOURCE_RED_VITALITY.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FFFluids.FLOWING_RED_VITALITY.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FFFluids.SOURCE_BLACK_VITALITY.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FFFluids.FLOWING_BLACK_VITALITY.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FFFluids.SOURCE_WHITE_VITALITY.get(), RenderType.translucent());
		ItemBlockRenderTypes.setRenderLayer(FFFluids.FLOWING_WHITE_VITALITY.get(), RenderType.translucent());

		CuriosRendererRegistry.register(FFItems.PHYLACTERY_BLUE.get(), PhylacteryRenderer::new);
		CuriosRendererRegistry.register(FFItems.PHYLACTERY_GREEN.get(), PhylacteryRenderer::new);
		CuriosRendererRegistry.register(FFItems.PHYLACTERY_YELLOW.get(), PhylacteryRenderer::new);
		CuriosRendererRegistry.register(FFItems.PHYLACTERY_GOLD.get(), PhylacteryRenderer::new);
		CuriosRendererRegistry.register(FFItems.PHYLACTERY_RED.get(), PhylacteryRenderer::new);
		CuriosRendererRegistry.register(FFItems.PHYLACTERY_BLACK.get(), PhylacteryRenderer::new);
		CuriosRendererRegistry.register(FFItems.PHYLACTERY_WHITE.get(), PhylacteryRenderer::new);
	}
}
