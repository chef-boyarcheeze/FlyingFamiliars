package com.beesechurger.flyingfamiliars.entity.client.familiar.shadewyrm;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.familiar.ShadewyrmEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class ShadewyrmModel extends GeoModel<ShadewyrmEntity>
{
	@Override
	public ResourceLocation getModelResource(ShadewyrmEntity entity)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/familiar/shadewyrm.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(ShadewyrmEntity entity)
	{
		return switch (entity.getVariant())
		{
			case "onyx" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/shadewyrm/shadewyrm_onyx.png");
			case "alabaster" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/shadewyrm/shadewyrm_alabaster.png");
			case "olivine" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/shadewyrm/shadewyrm_olivine.png");
			default ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/shadewyrm/shadewyrm_onyx.png");
		};
	}

	@Override
	public ResourceLocation getAnimationResource(ShadewyrmEntity entity)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/familiar/shadewyrm.animation.json");
	}

	@Override
	public void setCustomAnimations(ShadewyrmEntity entity, long instanceId, AnimationState customPredicate)
	{
		super.setCustomAnimations(entity, instanceId, customPredicate);

		if(customPredicate == null)
			return;

		/*EntityModelData extraDataOfType = (EntityModelData) customPredicate.getData(DataTickets.ENTITY_MODEL_DATA);
		CoreGeoBone head = this.getAnimationProcessor().getBone("head");

		float yRot = Mth.clamp(extraDataOfType.netHeadYaw(), -3.0f, 3.0f);
		float zRot = Mth.clamp(extraDataOfType.headPitch() + 20, 5.0f, 35.0f);

		head.setRotY((float) Math.toRadians(yRot));
		head.setRotZ((float) Math.toRadians(zRot));*/
	}
}
