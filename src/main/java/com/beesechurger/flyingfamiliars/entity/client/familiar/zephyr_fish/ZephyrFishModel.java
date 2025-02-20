package com.beesechurger.flyingfamiliars.entity.client.familiar.zephyr_fish;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.familiar.VoidMothEntity;
import com.beesechurger.flyingfamiliars.entity.common.familiar.ZephyrFishEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class ZephyrFishModel extends GeoModel<ZephyrFishEntity>
{
	@Override
	public ResourceLocation getModelResource(ZephyrFishEntity animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/familiar/zephyr_fish/zephyr_fish.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(ZephyrFishEntity animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/zephyr_fish/zephyr_fish.png");
	}

	@Override
	public ResourceLocation getAnimationResource(ZephyrFishEntity animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/familiar/zephyr_fish.animation.json");
	}

	@Override
	public void setCustomAnimations(ZephyrFishEntity animatable, long instanceId, AnimationState customPredicate)
	{
		super.setCustomAnimations(animatable, instanceId, customPredicate);

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
