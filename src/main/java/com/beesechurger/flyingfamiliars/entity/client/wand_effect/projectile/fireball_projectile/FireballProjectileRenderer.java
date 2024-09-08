package com.beesechurger.flyingfamiliars.entity.client.wand_effect.projectile.fireball_projectile;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.client.wand_effect.projectile.BaseWandEffectProjectileRenderer;
import com.beesechurger.flyingfamiliars.entity.common.wand_effect.projectile.FireballProjectile;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FireballProjectileRenderer extends BaseWandEffectProjectileRenderer<FireballProjectile>
{
	public FireballProjectileRenderer(Context renderManager)
	{
		super(renderManager, new FireballProjectileModel());
		this.withScale(1.0f);
	}

	@Override
	public ResourceLocation getTextureLocation(FireballProjectile animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/wand_effect/projectile/fireball_projectile/fireball_projectile_animated.png");
	}
}
