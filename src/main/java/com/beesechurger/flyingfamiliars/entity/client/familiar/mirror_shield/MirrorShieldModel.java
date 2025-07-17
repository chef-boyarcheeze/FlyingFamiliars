package com.beesechurger.flyingfamiliars.entity.client.familiar.mirror_shield;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.familiar.MirrorShieldEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class MirrorShieldModel extends GeoModel<MirrorShieldEntity>
{
	@Override
	public ResourceLocation getModelResource(MirrorShieldEntity MirrorShieldEntity)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/familiar/mirror_shield.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(MirrorShieldEntity MirrorShieldEntity)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/mirror_shield/mirror_shield.png");
	}

	@Override
	public ResourceLocation getAnimationResource(MirrorShieldEntity MirrorShieldEntity) {
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/familiar/mirror_shield.animation.json");
	}

	@Override
	public void setCustomAnimations(MirrorShieldEntity animatable, long instanceId, AnimationState customPredicate)
	{
		super.setCustomAnimations(animatable, instanceId, customPredicate);

		if(customPredicate == null)
			return;
	}

	@Override
	public RenderType getRenderType(MirrorShieldEntity animatable, ResourceLocation texture)
	{
		return RenderType.entityTranslucent(texture);
	}
}
