package com.beesechurger.flyingfamiliars.entity.client.wand_effects;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.projectile.CaptureProjectile;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.util.Collections;
import java.util.Optional;

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
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/wand_effects/capture_projectile.png");
	}
}
