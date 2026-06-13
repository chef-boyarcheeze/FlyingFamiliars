package com.beesechurger.flyingfamiliars.entity.client.wand_effect.projectile.flamethrower_projectile;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.wand_effect.projectile.FlamethrowerProjectile;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class FlamethrowerProjectileModel extends GeoModel<FlamethrowerProjectile>
{
	@Override
	public ResourceLocation getAnimationResource(FlamethrowerProjectile animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/wand_effect/projectile/fireball_projectile.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(FlamethrowerProjectile animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/wand_effect/projectile/fireball_projectile.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(FlamethrowerProjectile animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/wand_effect/projectile/fireball_projectile/fireball_projectile_animated.png");
	}
}
