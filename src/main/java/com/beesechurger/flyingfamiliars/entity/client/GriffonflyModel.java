package com.beesechurger.flyingfamiliars.entity.client;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.GriffonflyEntity;

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
public class GriffonflyModel extends AnimatedGeoModel<GriffonflyEntity> {

	@Override
	public ResourceLocation getAnimationFileLocation(GriffonflyEntity animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/griffonfly.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(GriffonflyEntity animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/griffonfly/griffonfly.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(GriffonflyEntity animatable)
	{
        return switch (animatable.getVariant()) {
            case "yellow" ->
                    new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/griffonfly/griffonfly_yellow.png");
            case "green" ->
                    new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/griffonfly/griffonfly_green.png");
            case "blue" ->
                    new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/griffonfly/griffonfly_blue.png");
            case "purple" ->
                    new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/griffonfly/griffonfly_purple.png");
            case "red" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/griffonfly/griffonfly_red.png");
            default ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/griffonfly/griffonfly_yellow.png");
        };
	}

	@Override
	public void setLivingAnimations(GriffonflyEntity animatable, Integer uniqueID, @Nullable AnimationEvent customPredicate)
	{
		super.setLivingAnimations(animatable, uniqueID, customPredicate);

		if(customPredicate == null)
			return;

		List<EntityModelData> extraDataOfType = customPredicate.getExtraDataOfType(EntityModelData.class);
		IBone head = this.getAnimationProcessor().getBone("head");

		float xRot = Mth.clamp(extraDataOfType.get(0).headPitch, -15.0f, 15.0f);
		float yRot = Mth.clamp(extraDataOfType.get(0).netHeadYaw, -30.0f, 30.0f);

		head.setRotationX((float) Math.toRadians(xRot));
		head.setRotationY((float) Math.toRadians(yRot));
	}
}
