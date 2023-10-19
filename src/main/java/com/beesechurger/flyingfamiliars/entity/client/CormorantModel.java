package com.beesechurger.flyingfamiliars.entity.client;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.CloudRayEntity;
import com.beesechurger.flyingfamiliars.entity.common.CormorantEntity;
import com.beesechurger.flyingfamiliars.entity.common.GriffonflyEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import java.util.List;

public class CormorantModel extends AnimatedGeoModel<CormorantEntity> {

	@Override
	public ResourceLocation getAnimationFileLocation(CormorantEntity animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/cormorant.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(CormorantEntity animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/cormorant.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(CormorantEntity animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/cormorant/cormorant.png");
	}

	@Override
	public void setLivingAnimations(CormorantEntity animatable, Integer uniqueID, @Nullable AnimationEvent customPredicate)
	{
		super.setLivingAnimations(animatable, uniqueID, customPredicate);

		if(customPredicate == null)
			return;

		List<EntityModelData> extraDataOfType = customPredicate.getExtraDataOfType(EntityModelData.class);
		IBone neck_lower = this.getAnimationProcessor().getBone("neck_lower");
		IBone neck_lower_middle = this.getAnimationProcessor().getBone("neck_lower_middle");
		IBone neck_upper_middle = this.getAnimationProcessor().getBone("neck_upper_middle");
		IBone neck_upper = this.getAnimationProcessor().getBone("neck_upper");
		IBone head = this.getAnimationProcessor().getBone("head");

		float yRot = Mth.clamp(0.2f * extraDataOfType.get(0).netHeadYaw, -5.0f, 5.0f);

		neck_lower.setRotationY((float) Math.toRadians(yRot));
		neck_lower_middle.setRotationY((float) Math.toRadians(yRot));
		neck_upper_middle.setRotationY((float) Math.toRadians(yRot));
		neck_upper.setRotationY((float) Math.toRadians(yRot));
		head.setRotationY((float) Math.toRadians(yRot * 2.0f));

		// compensation for bone rotations not being 0 initially
		neck_lower_middle.setRotationZ((float) Math.toRadians(yRot));
		head.setRotationZ((float) Math.toRadians(yRot * 2.0f));
	}
}
