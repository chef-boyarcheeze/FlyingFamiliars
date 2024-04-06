package com.beesechurger.flyingfamiliars.entity.client.familiar.cormorant;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.familiar.CormorantEntity;
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
public class CormorantModel extends GeoModel<CormorantEntity>
{
	@Override
	public ResourceLocation getModelResource(CormorantEntity cormorantEntity)
	{
		if(cormorantEntity.getHasRing())
			return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/familiar/cormorant/cormorant_ring.geo.json");

		return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/familiar/cormorant/cormorant_no_ring.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(CormorantEntity cormorantEntity)
	{
		return switch (cormorantEntity.getVariant()) {
			case "great_cormorant" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/cormorant/great_cormorant.png");
			case "australian_pied_cormorant" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/cormorant/australian_pied_cormorant.png");
			case "red_legged_cormorant" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/cormorant/red_legged_cormorant.png");
			default ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/cormorant/great_cormorant.png");
		};
	}

	@Override
	public ResourceLocation getAnimationResource(CormorantEntity cormorantEntity)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/familiar/cormorant.animation.json");
	}

	@Override
	public void setCustomAnimations(CormorantEntity cormorantEntity, long instanceId, AnimationState customPredicate)
	{
		super.setCustomAnimations(cormorantEntity, instanceId, customPredicate);

		if(customPredicate == null || cormorantEntity.isFlying())
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
		head.setRotZ((float) Math.toRadians(yRot));
	}
}
