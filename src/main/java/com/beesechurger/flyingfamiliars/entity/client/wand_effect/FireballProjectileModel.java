package com.beesechurger.flyingfamiliars.entity.client.wand_effect;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.wand_effect.FireballProjectile;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class FireballProjectileModel extends GeoModel<FireballProjectile>
{
	@Override
	public ResourceLocation getAnimationResource(FireballProjectile animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/wand_effect/fireball_projectile.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(FireballProjectile animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/wand_effect/fireball_projectile.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(FireballProjectile animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/wand_effect/fireball_projectile/fireball_projectile_animated.png");
	}
}
