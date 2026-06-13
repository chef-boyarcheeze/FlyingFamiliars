package com.beesechurger.flyingfamiliars.entity.client.wand_effect.charm.crystal_spike_charm;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.wand_effect.charm.CrystalSpikeCharm;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class CrystalSpikeCharmRenderer extends GeoEntityRenderer<CrystalSpikeCharm>
{
	public CrystalSpikeCharmRenderer(Context renderManager)
	{
		super(renderManager, new CrystalSpikeCharmModel());
		this.withScale(1.0f);
	}

	@Override
	public ResourceLocation getTextureLocation(CrystalSpikeCharm animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/wand_effect/charm/crystal_spike_charm/crystal_spike_charm.png");
	}
}
