package com.beesechurger.flyingfamiliars.entity.client.familiar.shadewyrm;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.client.familiar.BaseFamiliarRenderer;
import com.beesechurger.flyingfamiliars.entity.common.familiar.ShadewyrmEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShadewyrmRenderer extends BaseFamiliarRenderer<ShadewyrmEntity>
{
	public ShadewyrmRenderer(Context renderManager)
	{
		super(renderManager, new ShadewyrmModel());
		this.shadowRadius = 1.5f;
		this.withScale(1.0f);
	}

	@Override
	public ResourceLocation getTextureLocation(ShadewyrmEntity animatable)
	{
		return switch (animatable.getVariant())
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
}
