package com.beesechurger.flyingfamiliars;

import com.beesechurger.flyingfamiliars.event.ClientEvents;
import com.beesechurger.flyingfamiliars.registries.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib.GeckoLib;

@Mod("flyingfamiliars")
public class FlyingFamiliars
{
	public static final String MOD_ID = "flyingfamiliars";

	public FlyingFamiliars()
	{
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		FFItems.ITEM_REG.register(modEventBus);

		FFBlocks.BLOCK_REG.register(modEventBus);
		FFBlockEntities.BLOCK_ENTITY_REG.register(modEventBus);

		FFFluids.FLUIDS_REG.register(modEventBus);
		FFFluidTypes.FLUID_TYPES_REG.register(modEventBus);

		FFEffects.MOB_EFFECT_REG.register(modEventBus);
		FFEntityTypes.ENTITY_TYPE_REG.register(modEventBus);

		FFCreativeTabs.CREATIVE_TAB_REG.register(modEventBus);
		FFSounds.SOUND_EVENT_REG.register(modEventBus);
		FFRecipes.RECIPE_SERIALIZER_REG.register(modEventBus);

		CommonSetup.INSTANCE.register(modEventBus);
		ClientSetup.INSTANCE.register(modEventBus);
		
		MinecraftForge.EVENT_BUS.register(ClientEvents.INSTANCE);

		GeckoLib.initialize();
	}
}
