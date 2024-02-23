package com.beesechurger.flyingfamiliars.entity.client;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.CloudRayEntity;

import com.beesechurger.flyingfamiliars.entity.common.projectile.CaptureProjectile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import java.util.List;

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

	@Override
	public void setLivingAnimations(CloudRayEntity animatable, Integer uniqueID, @Nullable AnimationEvent customPredicate)
	{
		super.setLivingAnimations(animatable, uniqueID, customPredicate);

		if(customPredicate == null)
			return;

		List<EntityModelData> extraDataOfType = customPredicate.getExtraDataOfType(EntityModelData.class);
		IBone head = this.getAnimationProcessor().getBone("head");

		float yRot = Mth.clamp(extraDataOfType.get(0).netHeadYaw, -3.0f, 3.0f);
		float zRot = Mth.clamp(extraDataOfType.get(0).headPitch + 20, 5.0f, 35.0f);

		head.setRotationY((float) Math.toRadians(yRot));
		head.setRotationZ((float) Math.toRadians(zRot));
	}
}
