package com.beesechurger.flyingfamiliars.event;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.blocks.client.BrazierRenderer;
import com.beesechurger.flyingfamiliars.blocks.entity.FFBlockEntities;
import com.beesechurger.flyingfamiliars.entity.FFEntityTypes;
import com.beesechurger.flyingfamiliars.entity.custom.CloudRayEntity;
import com.beesechurger.flyingfamiliars.entity.custom.GriffonflyEntity;
import com.beesechurger.flyingfamiliars.entity.custom.PhoenixEntity;
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
        event.put(FFEntityTypes.PHOENIX.get(), PhoenixEntity.setAttributes());
        event.put(FFEntityTypes.GRIFFONFLY.get(), GriffonflyEntity.setAttributes());
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
    
    /*@OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        Player player = Minecraft.getInstance().player;
        if (player.getVehicle() != null) {
            if (player.getVehicle() instanceof AbstractFamiliarEntity) {
                int currentView = 2;
                float scale = 1;
                if (Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_BACK ||
                    Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_FRONT) {
                    if (currentView == 1) {
                        event.getCamera().move(-event.getCamera().getMaxZoom(scale * 1.2F), 0F, 0);
                    } else if (currentView == 2) {
                        event.getCamera().move(-event.getCamera().getMaxZoom(scale * 3F), 0F, 0);
                    } else if (currentView == 3) {
                        event.getCamera().move(-event.getCamera().getMaxZoom(scale * 5F), 0F, 0);
                    }
                }
            }
        }
    }*/
}