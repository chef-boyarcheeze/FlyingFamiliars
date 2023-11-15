package com.beesechurger.flyingfamiliars.event;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.block.client.BrazierRenderer;
import com.beesechurger.flyingfamiliars.block.entity.FFBlockEntities;
import com.beesechurger.flyingfamiliars.entity.FFEntityTypes;
import com.beesechurger.flyingfamiliars.entity.common.CloudRayEntity;
import com.beesechurger.flyingfamiliars.entity.common.GriffonflyEntity;
import com.beesechurger.flyingfamiliars.entity.common.MagicCarpetEntity;
import com.beesechurger.flyingfamiliars.recipe.BrazierRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FlyingFamiliars.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FFEventBusEvents
{
    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event)
    {
        event.put(FFEntityTypes.CLOUD_RAY.get(), CloudRayEntity.setAttributes());
        event.put(FFEntityTypes.CORMORANT.get(), GriffonflyEntity.setAttributes());
        event.put(FFEntityTypes.GRIFFONFLY.get(), GriffonflyEntity.setAttributes());
        event.put(FFEntityTypes.MAGIC_CARPET.get(), MagicCarpetEntity.setAttributes());
    }
    
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event)
    {
    	event.registerBlockEntityRenderer(FFBlockEntities.BRAZIER_BLOCK_ENTITY.get(), BrazierRenderer::new);
    }
    
    @SubscribeEvent
    public static void registerRecipeTypes(final RegistryEvent.Register<RecipeSerializer<?>> event)
    {
    	Registry.register(Registry.RECIPE_TYPE, BrazierRecipe.Type.ID, BrazierRecipe.Type.INSTANCE);
    }
}