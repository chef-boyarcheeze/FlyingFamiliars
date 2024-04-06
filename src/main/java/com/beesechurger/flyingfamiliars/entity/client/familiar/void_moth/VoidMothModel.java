package com.beesechurger.flyingfamiliars.entity.client.familiar.void_moth;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.familiar.VoidMothEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class VoidMothModel extends GeoModel<VoidMothEntity>
{
	@Override
	public ResourceLocation getModelResource(VoidMothEntity voidMothEntity)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/familiar/void_moth/void_moth.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(VoidMothEntity voidMothEntity)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/void_moth/void_moth.png");
	}

	@Override
	public ResourceLocation getAnimationResource(VoidMothEntity voidMothEntity)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/familiar/void_moth.animation.json");
	}

	@Override
	public void setCustomAnimations(VoidMothEntity voidMothEntity, long instanceId, AnimationState customPredicate)
	{
		super.setCustomAnimations(voidMothEntity, instanceId, customPredicate);

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
