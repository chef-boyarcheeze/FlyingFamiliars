package com.beesechurger.flyingfamiliars.entity.client;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.CloudRayEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@OnlyIn(Dist.CLIENT)
public class CloudRayModel extends AnimatedGeoModel<CloudRayEntity> {

	@Override
	public ResourceLocation getAnimationFileLocation(CloudRayEntity animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/cloud_ray.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(CloudRayEntity animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/cloud_ray/cloud_ray.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(CloudRayEntity animatable)
	{
		return switch (animatable.getVariant())
		{
			case "white" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/cloud_ray/cloud_ray_white.png");
			case "light_gray" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/cloud_ray/cloud_ray_light_gray.png");
			case "dark_gray" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/cloud_ray/cloud_ray_dark_gray.png");
			default ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/cloud_ray/cloud_ray_white.png");
		};
	}
}
