package com.beesechurger.flyingfamiliars.entity.client.familiar.mirror_shield;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.client.familiar.BaseFamiliarRenderer;
import com.beesechurger.flyingfamiliars.entity.common.familiar.MirrorShieldEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MirrorShieldRenderer extends BaseFamiliarRenderer<MirrorShieldEntity>
{
	public MirrorShieldRenderer(Context renderManager)
	{
		super(renderManager, new MirrorShieldModel());
		this.shadowRadius = 0.8f;
		this.withScale(1.2f);
	}

	@Override
	public ResourceLocation getTextureLocation(MirrorShieldEntity MirrorShieldEntity)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/mirror_shield/mirror_shield.png");
	}
}
