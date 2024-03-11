package com.beesechurger.flyingfamiliars.entity.client.phoenix;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.PhoenixEntity;
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
public class PhoenixModel extends GeoModel<PhoenixEntity>
{
	@Override
	public ResourceLocation getModelResource(PhoenixEntity phoenixEntity)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/phoenix/phoenix.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(PhoenixEntity phoenixEntity)
	{
		return switch (phoenixEntity.getVariant()) {
			case "red" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/phoenix/phoenix_red.png");
			case "blue" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/phoenix/phoenix_blue.png");
			default ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/phoenix/phoenix_red.png");
		};
	}

	@Override
	public ResourceLocation getAnimationResource(PhoenixEntity phoenixEntity)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/phoenix.animation.json");
	}

	@Override
	public void setCustomAnimations(PhoenixEntity phoenixEntity, long instanceId, AnimationState customPredicate)
	{
		super.setCustomAnimations(phoenixEntity, instanceId, customPredicate);

		/*if(customPredicate == null || phoenixEntity.isFlying())
			return;

		EntityModelData extraDataOfType = (EntityModelData) customPredicate.getData(DataTickets.ENTITY_MODEL_DATA);
		CoreGeoBone neck_lower = this.getAnimationProcessor().getBone("neck_lower");
		CoreGeoBone neck_lower_middle = this.getAnimationProcessor().getBone("neck_lower_middle");
		CoreGeoBone neck_upper_middle = this.getAnimationProcessor().getBone("neck_upper_middle");
		CoreGeoBone neck_upper = this.getAnimationProcessor().getBone("neck_upper");
		CoreGeoBone head = this.getAnimationProcessor().getBone("head");

		float yRot = Mth.clamp(0.2f * extraDataOfType.netHeadYaw(), -5.0f, 5.0f);

		neck_lower.setRotY((float) Math.toRadians(yRot));
		neck_lower_middle.setRotY((float) Math.toRadians(yRot));
		neck_upper_middle.setRotY((float) Math.toRadians(yRot));
		neck_upper.setRotY((float) Math.toRadians(yRot));
		head.setRotY((float) Math.toRadians(yRot * 2.0f));

		// compensation for bone rotations not being 0 initially
		neck_lower_middle.setRotZ((float) Math.toRadians(yRot));
		head.setRotZ((float) Math.toRadians(yRot));*/
	}
}
