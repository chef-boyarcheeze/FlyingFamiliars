package com.beesechurger.flyingfamiliars.entity.client.wand_effect.charm.crystal_spike_charm;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.wand_effect.charm.CrystalSpikeCharm;
import com.beesechurger.flyingfamiliars.entity.common.wand_effect.projectile.CaptureProjectile;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class CrystalSpikeCharmModel extends GeoModel<CrystalSpikeCharm>
{
	@Override
	public ResourceLocation getAnimationResource(CrystalSpikeCharm animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/wand_effect/charm/crystal_spike_charm.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(CrystalSpikeCharm animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/wand_effect/charm/crystal_spike_charm.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(CrystalSpikeCharm animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/wand_effect/charm/crystal_spike_charm/capture_projectile.png");
	}
}
