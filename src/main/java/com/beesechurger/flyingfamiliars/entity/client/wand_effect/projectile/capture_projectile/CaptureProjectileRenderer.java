package com.beesechurger.flyingfamiliars.entity.client.wand_effect.projectile.capture_projectile;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.client.wand_effect.projectile.BaseWandEffectProjectileRenderer;
import com.beesechurger.flyingfamiliars.entity.common.wand_effect.projectile.CaptureProjectile;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CaptureProjectileRenderer extends BaseWandEffectProjectileRenderer<CaptureProjectile>
{
	public CaptureProjectileRenderer(Context renderManager)
	{
		super(renderManager, new CaptureProjectileModel());
		this.withScale(1.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(CaptureProjectile animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/wand_effect/projectile/capture_projectile/capture_projectile.png");
	}
}
