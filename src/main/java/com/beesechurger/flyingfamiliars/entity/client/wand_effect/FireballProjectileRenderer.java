package com.beesechurger.flyingfamiliars.entity.client.wand_effect;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.wand_effect.FireballProjectile;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class FireballProjectileRenderer extends GeoEntityRenderer<FireballProjectile>
{
	public FireballProjectileRenderer(Context renderManager)
	{
		super(renderManager, new FireballProjectileModel());
		this.withScale(1.0f);
	}

	@Override
	public ResourceLocation getTextureLocation(FireballProjectile animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/wand_effect/fireball_projectile/fireball_projectile_animated.png");
	}
}
