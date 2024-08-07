package com.beesechurger.flyingfamiliars.entity.client.wand_effect;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.wand_effect.CaptureProjectile;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class CaptureProjectileRenderer extends GeoEntityRenderer<CaptureProjectile>
{
	public CaptureProjectileRenderer(Context renderManager)
	{
		super(renderManager, new CaptureProjectileModel());
		this.withScale(1.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(CaptureProjectile animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/wand_effect/capture_projectile/capture_projectile.png");
	}
}
