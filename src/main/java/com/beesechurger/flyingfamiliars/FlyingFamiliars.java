package com.beesechurger.flyingfamiliars;

import com.beesechurger.flyingfamiliars.event.ClientEvents;
import com.beesechurger.flyingfamiliars.integration.curios.CuriosIntegration;
import com.beesechurger.flyingfamiliars.item.FFItemHandler;
import com.beesechurger.flyingfamiliars.registries.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib.GeckoLib;

import static com.beesechurger.flyingfamiliars.util.FFConstants.CURIOS_MODNAME;

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
		FFClientSetup.registerRenderers();
	}
}
