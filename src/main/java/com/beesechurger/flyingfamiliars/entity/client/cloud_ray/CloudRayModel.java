package com.beesechurger.flyingfamiliars.entity.client.cloud_ray;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.CloudRayEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import java.util.List;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class CloudRayModel extends GeoModel<CloudRayEntity>
{
	@Override
	public ResourceLocation getModelResource(CloudRayEntity cloudRayEntity)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/cloud_ray/cloud_ray.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(CloudRayEntity cloudRayEntity)
	{
		return switch (cloudRayEntity.getVariant())
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

	@Override
	public ResourceLocation getAnimationResource(CloudRayEntity cloudRayEntity) {
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/cloud_ray.animation.json");
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
