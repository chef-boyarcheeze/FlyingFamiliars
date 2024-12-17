package com.beesechurger.flyingfamiliars.entity.client.familiar.cloud_ray;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.familiar.CloudRayEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

@OnlyIn(Dist.CLIENT)
public class CloudRayModel extends GeoModel<CloudRayEntity>
{
	@Override
	public ResourceLocation getModelResource(CloudRayEntity cloudRayEntity)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/familiar/cloud_ray/cloud_ray.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(CloudRayEntity cloudRayEntity)
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
	public ResourceLocation getAnimationResource(CloudRayEntity cloudRayEntity) {
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/familiar/cloud_ray.animation.json");
	}

	@Override
	public void setCustomAnimations(CloudRayEntity animatable, long instanceId, AnimationState customPredicate)
	{
		super.setCustomAnimations(animatable, instanceId, customPredicate);

		if(customPredicate == null)
			return;

		EntityModelData extraDataOfType = (EntityModelData) customPredicate.getData(DataTickets.ENTITY_MODEL_DATA);
		CoreGeoBone head = this.getAnimationProcessor().getBone("head");

		float yRot = Mth.clamp(extraDataOfType.netHeadYaw(), -3.0f, 3.0f);
		float zRot = Mth.clamp(extraDataOfType.headPitch() + 20, 5.0f, 35.0f);

		head.setRotY((float) Math.toRadians(yRot));
		head.setRotZ((float) Math.toRadians(zRot));
	}
}
