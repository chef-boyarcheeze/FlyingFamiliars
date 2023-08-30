package com.beesechurger.flyingfamiliars.event;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.custom.AbstractFamiliarEntity;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = FlyingFamiliars.MOD_ID, value = Dist.CLIENT)
public class ClientEvents
{
    @SubscribeEvent
    public static void onCameraSetup(EntityViewRenderEvent.CameraSetup event)
    {
    	Minecraft mc = Minecraft.getInstance();
    	Player player = mc.player;
        
    	if(player.getVehicle() != null)
    	{
    		if(player.getVehicle() instanceof AbstractFamiliarEntity familiar)
            {
    			double cameraZoom = mc.options.getCameraType() == CameraType.FIRST_PERSON ? 0.25 : 0.5;
    			
    			float renderPitch = (float) (mc.options.getCameraType() == CameraType.THIRD_PERSON_FRONT ? 
   					 -familiar.getPitch(event.getPartialTicks()) : familiar.getPitch(event.getPartialTicks()));
    			float renderRoll = (float) (mc.options.getCameraType() == CameraType.THIRD_PERSON_FRONT ? 
    					 familiar.getRoll(event.getPartialTicks()) : -familiar.getRoll(event.getPartialTicks()));
    			
    			event.setPitch(player.getViewXRot(renderRoll) + renderPitch);
    			event.setRoll(renderRoll);
    			
    			event.getCamera().move(-event.getCamera().getMaxZoom(cameraZoom), 0, 0);
            }
    	}
    }
}