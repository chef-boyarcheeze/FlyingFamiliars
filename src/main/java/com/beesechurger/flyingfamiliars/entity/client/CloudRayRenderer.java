package com.beesechurger.flyingfamiliars.entity.client;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.custom.CloudRayEntity;
import com.beesechurger.flyingfamiliars.init.FFKeys;
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

public class CloudRayRenderer extends GeoEntityRenderer<CloudRayEntity> {

	private static final float MAX_PITCH_SPEED = 1;
	private static final float MAX_PITCH_ROT = 30;
	private static final float MAX_YAW_SPEED = 1;
	private static final float MAX_YAW_ROT = 30;
	
	private float pitchUp = 0;
	private float pitchDown = 0;
	private float yawTurn = 0;

	public CloudRayRenderer(Context renderManager) {
		super(renderManager, new CloudRayModel());
		this.shadowRadius = 1.5f;
	}
	
	@Override
	public ResourceLocation getTextureLocation(CloudRayEntity instance) {
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/cloud_ray/cloud_ray.png");
	}

	@Override
	public RenderType getRenderType(CloudRayEntity animatable, float partialTicks, PoseStack stack,
									MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation) {
		stack.scale(1.5f, 1.5f, 1.5f);
		return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
	}
	
	@Override
    protected void applyRotations(CloudRayEntity animatable, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(animatable, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
        float moveRotY = 0;
        float moveRotX = 0;
        LivingEntity driver = (LivingEntity) animatable.getFirstPassenger();

        if (driver != null && animatable.isFlying())
        {
        	double forwardMove = Math.min(Math.abs(driver.zza) + Math.abs(driver.xxa), 1);
        	
        	float yaw = 0;
            if (forwardMove > 0) yaw = (float) Mth.atan2(driver.zza, driver.xxa) * (180f / (float) Math.PI) - 90;
            
            switch ((int) yaw)
            {
	            case 45:
	            	yawTurn += yawTurn >= MAX_YAW_ROT / 2 ? centerOnYaw(yaw) : MAX_YAW_SPEED;
	            	break;
	            case 90:
	            	yawTurn += yawTurn >= MAX_YAW_ROT ? centerOnYaw(yaw) : MAX_YAW_SPEED;
	            	break;
	            case -225:
	            	yawTurn += yawTurn >= MAX_YAW_ROT / 2 ? centerOnYaw(yaw) : MAX_YAW_SPEED;
	            	break;
	            case -45:
	            	yawTurn += yawTurn <= -MAX_YAW_ROT / 2 ? centerOnYaw(yaw) : -MAX_YAW_SPEED;
	            	break;
	            case -90:
	            	yawTurn += yawTurn <= -MAX_YAW_ROT ? centerOnYaw(yaw) : -MAX_YAW_SPEED;
	            	break;
	            case -135:
	            	yawTurn += yawTurn <= -MAX_YAW_ROT / 2 ? centerOnYaw(yaw) : -MAX_YAW_SPEED;
	            	break;
	            case 0:
            		yawTurn += yawTurn > 0 ? -MAX_YAW_SPEED : yawTurn < 0 ? MAX_YAW_SPEED : 0;
            		break;
	            case -180:
            		yawTurn += yawTurn > 0 ? -MAX_YAW_SPEED : yawTurn < 0 ? MAX_YAW_SPEED : 0;
            		break;
            }
            
            moveRotY = -yawTurn;

            if(FFKeys.ascend.isDown() && FFKeys.descend.isDown())
            {
            	pitchUp += pitchUp == 0 ? 0 : MAX_PITCH_SPEED;
            	pitchDown -= pitchDown == 0 ? 0 : MAX_PITCH_SPEED;
            }
            
            if(FFKeys.ascend.isDown() && !FFKeys.descend.isDown()) pitchUp -= pitchUp > -MAX_PITCH_ROT ? MAX_PITCH_SPEED : 0;
            else pitchUp += pitchUp == 0 ? 0 : MAX_PITCH_SPEED;
            
            if(FFKeys.descend.isDown() && !FFKeys.ascend.isDown()) pitchDown += pitchDown < MAX_PITCH_ROT ? MAX_PITCH_SPEED : 0;
            else pitchDown -= pitchDown == 0 ? 0 : MAX_PITCH_SPEED;
            
            if(pitchUp < -MAX_PITCH_ROT) moveRotX = -MAX_PITCH_ROT;
            else if(pitchDown > MAX_PITCH_ROT) moveRotX = MAX_PITCH_ROT;
            else moveRotX = -(pitchUp + pitchDown);
        }

        matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(Mth.wrapDegrees(moveRotY)));
        matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(Mth.wrapDegrees(moveRotX)));        
    }
	
	private float centerOnYaw(float yaw)
	{
		float rotation = 0;
		
		switch ((int) yaw)
		{
			case 45:
				rotation = MAX_YAW_ROT / 2;
				break;
			case 90:
				rotation = MAX_YAW_ROT;
				break;
			case -225:
				rotation = MAX_YAW_ROT / 2;
				break;
			case -45:
				rotation = -MAX_YAW_ROT / 2;
				break;
			case -90:
				rotation = -MAX_YAW_ROT;
				break;
			case -135:
				rotation = -MAX_YAW_ROT / 2;
				break;
			default:
				rotation = 0;
				break;
		}
		
		return yawTurn > rotation ? -MAX_YAW_SPEED : yawTurn < rotation ? MAX_YAW_SPEED : 0;
	}
}
