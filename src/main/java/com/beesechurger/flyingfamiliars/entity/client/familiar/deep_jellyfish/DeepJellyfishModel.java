package com.beesechurger.flyingfamiliars.entity.client.familiar.deep_jellyfish;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.familiar.DeepJellyfishEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class DeepJellyfishModel extends GeoModel<DeepJellyfishEntity>
{
	@Override
	public ResourceLocation getModelResource(DeepJellyfishEntity entity)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/familiar/deep_jellyfish.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(DeepJellyfishEntity entity)
	{
		return switch (entity.getVariant())
		{
			case "blue" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/deep_jellyfish/deep_jellyfish_blue.png");
			case "green" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/deep_jellyfish/deep_jellyfish_green.png");
			case "purple" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/deep_jellyfish/deep_jellyfish_purple.png");
			default ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/deep_jellyfish/deep_jellyfish_blue.png");
		};
	}

	@Override
	public ResourceLocation getAnimationResource(DeepJellyfishEntity entity)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/familiar/deep_jellyfish.animation.json");
	}

	@Override
	public void setCustomAnimations(DeepJellyfishEntity entity, long instanceId, AnimationState customPredicate)
	{
		super.setCustomAnimations(entity, instanceId, customPredicate);

		if(customPredicate == null)
			return;
	}

	@Override
	public RenderType getRenderType(DeepJellyfishEntity entity, ResourceLocation texture)
	{
		return RenderType.entityTranslucent(texture);
	}
}
