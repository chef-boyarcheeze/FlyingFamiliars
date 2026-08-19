package com.beesechurger.flyingfamiliars;

import com.beesechurger.flyingfamiliars.block.FFBlockEntities;
import com.beesechurger.flyingfamiliars.block.FFBlocks;
import com.beesechurger.flyingfamiliars.client.FFSounds;
import com.beesechurger.flyingfamiliars.effect.FFEffects;
import com.beesechurger.flyingfamiliars.entity.FFEntityTypes;
import com.beesechurger.flyingfamiliars.item.FFCreativeTabs;
import com.beesechurger.flyingfamiliars.item.FFItems;
import com.beesechurger.flyingfamiliars.recipe.FFRecipes;
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

		FFBlocks.BLOCKS.register(modEventBus);
		FFBlockEntities.BLOCK_ENTITIES.register(modEventBus);
		FFEffects.EFFECTS.register(modEventBus);
		FFEntityTypes.ENTITY_TYPES.register(modEventBus);
		FFCreativeTabs.CREATIVE_TABS.register(modEventBus);
		FFItems.ITEMS.register(modEventBus);
		FFRecipes.RECIPES.register(modEventBus);
		FFSounds.SOUNDS.register(modEventBus);

		ClientSetup.INSTANCE.register(modEventBus, MinecraftForge.EVENT_BUS);
		CommonSetup.INSTANCE.register(modEventBus, MinecraftForge.EVENT_BUS);
		
		MinecraftForge.EVENT_BUS.register(FFEvents.Client.INSTANCE);
		MinecraftForge.EVENT_BUS.register(FFEvents.Common.INSTANCE);

		GeckoLib.initialize();
	}
}
