package com.beesechurger.flyingfamiliars.entity.client;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.custom.PhoenixEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class PhoenixRenderer extends GeoEntityRenderer<PhoenixEntity> {

	public PhoenixRenderer(Context renderManager) {
		super(renderManager, new PhoenixModel());
		this.shadowRadius = 0.7f;
	}
	
	@Override
	public ResourceLocation getTextureLocation(PhoenixEntity instance) {
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/phoenix/phoenix.png");
	}

	@Override
	public RenderType getRenderType(PhoenixEntity animatable, float partialTicks, PoseStack stack,
									MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation) {
		stack.scale(1.8f, 1.8f, 1.8f);
		return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
	}
}