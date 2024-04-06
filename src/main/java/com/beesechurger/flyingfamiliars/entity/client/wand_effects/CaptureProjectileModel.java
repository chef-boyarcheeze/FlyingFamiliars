package com.beesechurger.flyingfamiliars.entity.client.wand_effects;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.projectile.CaptureProjectile;
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
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/familiar/wand_effects/capture_projectile.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(CaptureProjectile animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/familiar/wand_effects/capture_projectile.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(CaptureProjectile animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/wand_effects/capture_projectile.png");
	}
}
