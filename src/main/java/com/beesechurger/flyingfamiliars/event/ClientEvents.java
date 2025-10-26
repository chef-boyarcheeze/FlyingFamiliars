package com.beesechurger.flyingfamiliars.event;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;

import com.beesechurger.flyingfamiliars.entity.common.familiar.BaseFamiliarEntity;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = FlyingFamiliars.MOD_ID, value = Dist.CLIENT)
public class ClientEvents
{
    public static List<UUID> blockRenderList = new ArrayList<>();

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

    @SubscribeEvent
    public static void cancelRenderLiving(RenderLivingEvent event)
    {
        Entity passenger = event.getEntity();
        Entity vehicle = passenger.getVehicle();

        //System.out.println(passenger == Minecraft.getInstance().player);

        if (vehicle instanceof BaseFamiliarEntity familiar)
        {
            if (passenger == Minecraft.getInstance().player && Minecraft.getInstance().options.getCameraType().isFirstPerson()
                || blockRenderList.contains(passenger.getUUID()))
            {
                event.setCanceled(true);
            }
        }
    }
}