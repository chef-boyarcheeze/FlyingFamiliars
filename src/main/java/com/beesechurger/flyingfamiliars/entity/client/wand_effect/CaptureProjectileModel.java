package com.beesechurger.flyingfamiliars.entity.client.wand_effect;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.wand_effect.CaptureProjectile;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class CaptureProjectileModel extends GeoModel<CaptureProjectile>
{
	@Override
	public ResourceLocation getAnimationResource(CaptureProjectile animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/wand_effect/capture_projectile.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(CaptureProjectile animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/wand_effect/capture_projectile.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(CaptureProjectile animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/wand_effect/capture_projectile/capture_projectile.png");
	}
}
