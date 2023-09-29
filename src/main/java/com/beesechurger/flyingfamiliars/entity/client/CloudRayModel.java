package com.beesechurger.flyingfamiliars.entity.client;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.CloudRayEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CloudRayModel extends AnimatedGeoModel<CloudRayEntity> {

	@Override
	public ResourceLocation getAnimationFileLocation(CloudRayEntity animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/cloud_ray.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(CloudRayEntity animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/cloud_ray.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(CloudRayEntity animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/cloud_ray/cloud_ray.png");
	}
}
