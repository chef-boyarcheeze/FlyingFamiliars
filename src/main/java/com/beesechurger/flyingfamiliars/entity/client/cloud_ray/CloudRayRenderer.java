package com.beesechurger.flyingfamiliars.entity.client.cloud_ray;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.CloudRayEntity;
import com.mojang.blaze3d.vertex.PoseStack;

import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class CloudRayRenderer extends GeoEntityRenderer<CloudRayEntity>
{

	public CloudRayRenderer(Context renderManager)
	{
		super(renderManager, new CloudRayModel());
		this.shadowRadius = 1.5f;
		this.withScale(1.0f);
	}

	@Override
	public ResourceLocation getTextureLocation(CloudRayEntity cloudRayEntity)
	{
		return switch (cloudRayEntity.getVariant())
		{
			case "white" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/cloud_ray/cloud_ray_white.png");
			case "light_gray" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/cloud_ray/cloud_ray_light_gray.png");
			case "dark_gray" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/cloud_ray/cloud_ray_dark_gray.png");
			default ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/cloud_ray/cloud_ray_white.png");
		};
	}

	@Override
	protected void applyRotations(CloudRayEntity cloudRayEntity, PoseStack stack, float ageInTicks, float rotationYaw, float partialTicks)
	{
		super.applyRotations(cloudRayEntity, stack, ageInTicks, rotationYaw, partialTicks);

		float renderPitch = (float) cloudRayEntity.getPitch(partialTicks);
		float renderRoll = (float) cloudRayEntity.getRoll(partialTicks);

		stack.mulPose(Axis.XP.rotationDegrees(-renderPitch));
		stack.mulPose(Axis.ZP.rotationDegrees(renderRoll));
	}
}
