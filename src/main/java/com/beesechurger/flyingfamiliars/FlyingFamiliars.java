package com.beesechurger.flyingfamiliars;

import com.beesechurger.flyingfamiliars.entity.FFEntityTypes;
import com.beesechurger.flyingfamiliars.entity.client.CloudRayRenderer;
import com.beesechurger.flyingfamiliars.entity.client.PhoenixRenderer;
import com.beesechurger.flyingfamiliars.init.FFItems;
import com.beesechurger.flyingfamiliars.init.FFKeys;
import com.beesechurger.flyingfamiliars.sound.FFSounds;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

@Mod("flyingfamiliars")
public class FlyingFamiliars {

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
		FFEntityTypes.register(bus);
		FFSounds.register(bus);
		
		bus.addListener(this::clientSetup);
		
		MinecraftForge.EVENT_BUS.register(this);
		
		GeckoLib.initialize();
	}
	
	private void clientSetup(final FMLClientSetupEvent event)
	{
		EntityRenderers.register(FFEntityTypes.PHOENIX.get(), PhoenixRenderer::new);
		EntityRenderers.register(FFEntityTypes.CLOUD_RAY.get(), CloudRayRenderer::new);
		EntityRenderers.register(FFEntityTypes.SOUL_WAND_PROJECTILE.get(), ThrownItemRenderer::new);
		
		FFKeys.init();
	}
}
