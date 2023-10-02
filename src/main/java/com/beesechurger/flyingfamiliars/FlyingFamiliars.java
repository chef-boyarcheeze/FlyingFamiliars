package com.beesechurger.flyingfamiliars;

import com.beesechurger.flyingfamiliars.block.FFBlocks;
import com.beesechurger.flyingfamiliars.block.entity.FFBlockEntities;
import com.beesechurger.flyingfamiliars.entity.FFEntityTypes;
import com.beesechurger.flyingfamiliars.entity.client.CloudRayRenderer;
import com.beesechurger.flyingfamiliars.entity.client.GriffonflyRenderer;
import com.beesechurger.flyingfamiliars.event.ClientEvents;
import com.beesechurger.flyingfamiliars.integration.curios.CuriosIntegration;
import com.beesechurger.flyingfamiliars.item.FFItemHandler;
import com.beesechurger.flyingfamiliars.item.FFItems;
import com.beesechurger.flyingfamiliars.item.client.SoulBatteryRenderer;
import com.beesechurger.flyingfamiliars.keys.FFKeys;
import com.beesechurger.flyingfamiliars.networking.FFMessages;
import com.beesechurger.flyingfamiliars.recipe.FFRecipes;
import com.beesechurger.flyingfamiliars.sound.FFSounds;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.CURIOS_MODNAME;

@Mod("flyingfamiliars")
public class FlyingFamiliars
{
	public static final String MOD_ID = "flyingfamiliars";

	public static final CreativeModeTab FF_TAB = new CreativeModeTab(MOD_ID) 
	{
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack makeIcon() 
		{
			return new ItemStack(FFItems.CREATIVE_TAB_ICON.get());
		}
	};
	
	public FlyingFamiliars()
	{
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		FFItems.ITEM_REG.register(bus);
		FFBlocks.BLOCK_REG.register(bus);
		FFBlockEntities.BLOCK_ENTITIES.register(bus);
		FFEntityTypes.ENTITY_TYPES.register(bus);
		FFSounds.SOUND_EVENTS.register(bus);
		FFRecipes.SERIALIZERS.register(bus);
		
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
		FFMessages.register();
	}
	
	private void clientSetup(final FMLClientSetupEvent event)
	{
		EntityRenderers.register(FFEntityTypes.CLOUD_RAY.get(), CloudRayRenderer::new);
		EntityRenderers.register(FFEntityTypes.GRIFFONFLY.get(), GriffonflyRenderer::new);
		EntityRenderers.register(FFEntityTypes.CAPTURE_PROJECTILE.get(), ThrownItemRenderer::new);
		
		ItemBlockRenderTypes.setRenderLayer(FFBlocks.BRAZIER.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(FFBlocks.CRYSTAL_BALL.get(), RenderType.translucent());

		CuriosRendererRegistry.register(FFItems.SOUL_BATTERY_BLUE.get(), () -> new SoulBatteryRenderer());
		CuriosRendererRegistry.register(FFItems.SOUL_BATTERY_GREEN.get(), () -> new SoulBatteryRenderer());
		CuriosRendererRegistry.register(FFItems.SOUL_BATTERY_YELLOW.get(), () -> new SoulBatteryRenderer());
		CuriosRendererRegistry.register(FFItems.SOUL_BATTERY_GOLD.get(), () -> new SoulBatteryRenderer());
		CuriosRendererRegistry.register(FFItems.SOUL_BATTERY_RED.get(), () -> new SoulBatteryRenderer());
		CuriosRendererRegistry.register(FFItems.SOUL_BATTERY_BLACK.get(), () -> new SoulBatteryRenderer());
		//CuriosRendererRegistry.register(FFItems.SOUL_BATTERY_WHITE.get(), () -> new SoulBatteryRenderer());
		
		FFKeys.init();
	}
}
