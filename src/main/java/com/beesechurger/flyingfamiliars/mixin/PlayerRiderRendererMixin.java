package com.beesechurger.flyingfamiliars.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.beesechurger.flyingfamiliars.entity.custom.AbstractFamiliarEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

@Mixin(LivingEntityRenderer.class)
public class PlayerRiderRendererMixin<T extends LivingEntity>
{	
	private static float ANGLE_INTERVAL = 0.5f;
	private static float ANGLE_LIMIT = 15;
	private float pitch = 0;
	private float roll = 0;
	
    @Inject(method = "setupRotations(Lnet/minecraft/world/entity/LivingEntity;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V", at = @At("RETURN"))
    private void setupFamiliarRidingRotations(T entity, PoseStack stack, float ageInTicks, float rotationYaw, float partialTicks, CallbackInfo ci)
    {
        if (entity.getVehicle() instanceof AbstractFamiliarEntity familiar)
        {
        	updatePitchRoll(entity);
        	
        	stack.translate(0, -0.9D, 0);
        	stack.mulPose(Vector3f.XP.rotationDegrees(Mth.wrapDegrees(-pitch)));
            stack.mulPose(Vector3f.ZP.rotationDegrees(Mth.wrapDegrees(roll)));
            stack.translate(0, 0.9D, 0);
        }
    }

    @Redirect(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getVehicle()Lnet/minecraft/world/entity/Entity;", ordinal = 2))
    private Entity redirectPlayerRotation(LivingEntity entity)
    {
        if (entity.getVehicle() instanceof AbstractFamiliarEntity familiar) return null;
        else return entity.getVehicle();
    }
    
    private void updatePitchRoll(LivingEntity entity)
	{
    	AbstractFamiliarEntity familiar = (AbstractFamiliarEntity) entity.getVehicle();
    	
        int forwardMove = Math.round(entity.zza);
        int sideMove = Math.round(entity.xxa);
        
        if(familiar.isFlying())
        {
        	switch(forwardMove)
        	{
        		case 0:
        			centerPitch();
        			break;
        		case 1:
        			if(pitch < ANGLE_LIMIT) pitch += ANGLE_INTERVAL;
        			break;
        		case -1:
        			if(pitch > -ANGLE_LIMIT) pitch -= ANGLE_INTERVAL;
        	}
        }
        else
        {
        	centerPitch();
        }
    	
    	if(familiar.isFlying())
    	{
    		switch(sideMove)
        	{
        		case 0:
        			centerRoll();
        			break;
        		case 1:
        			if(roll < ANGLE_LIMIT) roll += ANGLE_INTERVAL;
        			break;
        		case -1:
        			if(roll > -ANGLE_LIMIT) roll -= ANGLE_INTERVAL;
        	}
    	}
    	else
    	{
    		centerRoll();
    	}
	}
	
	private void centerPitch()
	{
		if(pitch < 0) pitch += ANGLE_INTERVAL;
		if(pitch > 0) pitch -= ANGLE_INTERVAL;
	}
	
	private void centerRoll()
	{
		if(roll < 0) roll += ANGLE_INTERVAL;
		if(roll > 0) roll -= ANGLE_INTERVAL;
	}
}
