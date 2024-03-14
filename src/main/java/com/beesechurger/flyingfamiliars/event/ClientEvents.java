package com.beesechurger.flyingfamiliars.event;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;

import com.beesechurger.flyingfamiliars.entity.common.BaseFamiliarEntity;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = FlyingFamiliars.MOD_ID, value = Dist.CLIENT)
public class ClientEvents
{
    @SubscribeEvent
    public static void onCameraSetup(ViewportEvent.ComputeCameraAngles event)
    {
    	Minecraft mc = Minecraft.getInstance();
    	Player player = mc.player;
        
    	if(player.getVehicle() != null)
    	{
    		if(player.getVehicle() instanceof BaseFamiliarEntity familiar)
            {
    			double cameraZoom = mc.options.getCameraType() == CameraType.FIRST_PERSON ? 0.5 : 1.25;
    			double cameraRotMod = 0.5f;
    			
    			float renderPitch = (float) (cameraRotMod * familiar.getPitch(event.getPartialTick()) +
    					(mc.options.getCameraType() == CameraType.THIRD_PERSON_FRONT ?
    							-player.getViewXRot((float) event.getPartialTick()) :
    								player.getViewXRot((float) event.getPartialTick())));
    			
    			float renderRoll = (float) (cameraRotMod * (mc.options.getCameraType() == CameraType.THIRD_PERSON_FRONT ?
    					familiar.getRoll(event.getPartialTick()) :
    						-familiar.getRoll(event.getPartialTick())));
    			
    			event.setPitch(renderPitch);
				event.setRoll(renderRoll);
    			
    			event.getCamera().move(-event.getCamera().getMaxZoom(cameraZoom), 0, 0);
            }
    	}
    }
}