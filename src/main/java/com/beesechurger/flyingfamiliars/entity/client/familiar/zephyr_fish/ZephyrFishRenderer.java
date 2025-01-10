package com.beesechurger.flyingfamiliars.entity.client.familiar.zephyr_fish;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.client.familiar.BaseFamiliarRenderer;
import com.beesechurger.flyingfamiliars.entity.common.familiar.VoidMothEntity;
import com.beesechurger.flyingfamiliars.entity.common.familiar.ZephyrFishEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ZephyrFishRenderer extends BaseFamiliarRenderer<ZephyrFishEntity>
{

	public ZephyrFishRenderer(Context renderManager)
	{
		super(renderManager, new ZephyrFishModel());
		this.shadowRadius = 0.35f;
		this.withScale(1.0f);
	}

	@Override
	public ResourceLocation getTextureLocation(ZephyrFishEntity animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/zephyr_fish/zephyr_fish.png");
	}
}
