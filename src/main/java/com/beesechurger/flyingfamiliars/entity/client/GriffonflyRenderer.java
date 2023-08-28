package com.beesechurger.flyingfamiliars.entity.client;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.custom.GriffonflyEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class GriffonflyRenderer extends GeoEntityRenderer<GriffonflyEntity>
{
	private static float ANGLE_INTERVAL = 0.5f;
	private static float ANGLE_LIMIT = 15;
	private float pitch = 0;
	private float roll = 0;
	
	public GriffonflyRenderer(Context renderManager)
	{
		super(renderManager, new GriffonflyModel());
		this.shadowRadius = 1.5f;
	}
	
	@Override
	public ResourceLocation getTextureLocation(GriffonflyEntity animatable)
	{		
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/griffonfly/griffonfly_purple.png");
	}

	@Override
	public RenderType getRenderType(GriffonflyEntity animatable, float partialTicks, PoseStack stack,
									MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation)
	{
		stack.scale(1.5f, 1.5f, 1.5f);
		return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
	}
	
	@Override
    protected void applyRotations(GriffonflyEntity griffonfly, PoseStack stack, float ageInTicks, float rotationYaw, float partialTicks)
	{
        super.applyRotations(griffonfly, stack, ageInTicks, rotationYaw, partialTicks);
        updatePitchRoll(griffonfly);
        
        stack.mulPose(Vector3f.XP.rotationDegrees(Mth.wrapDegrees(-pitch)));
        stack.mulPose(Vector3f.ZP.rotationDegrees(Mth.wrapDegrees(roll)));
	}    
	
	private void updatePitchRoll(GriffonflyEntity griffonfly)
	{
		LivingEntity driver = (LivingEntity) griffonfly.getFirstPassenger();
		
        int forwardMove = Math.round(driver != null ? driver.zza : griffonfly.zza);
        int sideMove = Math.round(driver != null ? driver.xxa : griffonfly.xxa);
        
        if(griffonfly.isFlying())
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
    	
    	if(griffonfly.isFlying())
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
