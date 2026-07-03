package com.beesechurger.flyingfamiliars.integration.curios;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class CuriosIntegration
{
    public static final String CURIOS_MODNAME = "curios";

    public static void register()
    {
    }

    public Container getAllWornItems(LivingEntity living)
    {
        return CuriosApi.getCuriosInventory(living)
                .map(ICuriosItemHandler::getEquippedCurios)
                .<Container>map(RecipeWrapper::new)
                .orElseGet(() -> new SimpleContainer(0));
    }
}
