package com.beesechurger.flyingfamiliars;

import com.beesechurger.flyingfamiliars.init.FFItems;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
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
		
		MinecraftForge.EVENT_BUS.register(this);
		
		GeckoLib.initialize();
	}
}
