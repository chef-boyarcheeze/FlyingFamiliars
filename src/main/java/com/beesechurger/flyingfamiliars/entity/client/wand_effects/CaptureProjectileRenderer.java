package com.beesechurger.flyingfamiliars.entity.client.wand_effects;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.CloudRayEntity;
import com.beesechurger.flyingfamiliars.entity.common.GriffonflyEntity;
import com.beesechurger.flyingfamiliars.entity.common.projectile.CaptureProjectile;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Option;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;
import software.bernie.geckolib3.util.EModelRenderCycle;
import software.bernie.geckolib3.util.RenderUtils;

import java.util.Collections;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class CaptureProjectileRenderer extends GeoProjectilesRenderer<CaptureProjectile>
{

	public CaptureProjectileRenderer(Context renderManager)
	{
		super(renderManager, new CaptureProjectileModel());
	}

	@Override
	public ResourceLocation getTextureLocation(CaptureProjectile animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/wand_effects/capture_projectile.png");
	}

	@Override
	public RenderType getRenderType(CaptureProjectile animatable, float partialTicks, PoseStack stack,
									MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation)
	{
		stack.scale(1.5f, 1.5f, 1.5f);
		return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
	}

	public void render(CaptureProjectile animatable, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight)
	{
		GeoModel model = this.modelProvider.getModel(this.modelProvider.getModelLocation(animatable));

		Optional<GeoBone> core = model.getBone("core");
		Optional<GeoBone> interior = model.getBone("interior");
		Optional<GeoBone> exterior = model.getBone("exterior");

		/*
		float angle = Minecraft.getInstance().level.getGameTime() + partialTick;

		core.get().setRotationX((float) Math.toRadians(angle));
		core.get().setRotationY((float) -Math.toRadians(angle));

		interior.get().setRotationX((float) -Math.toRadians(2 * angle));
		interior.get().setRotationY((float) Math.toRadians(2 * angle));

		exterior.get().setRotationX((float) Math.toRadians(4 * angle));
		exterior.get().setRotationY((float) -Math.toRadians(4 * angle));
		//*/

		this.dispatchedMat = poseStack.last().pose().copy();
		this.setCurrentModelRenderCycle(EModelRenderCycle.INITIAL);
		AnimationEvent<CaptureProjectile> predicate = new AnimationEvent(animatable, 0.0F, 0.0F, partialTick, false, Collections.singletonList(new EntityModelData()));
		this.modelProvider.setLivingAnimations(animatable, this.getInstanceId(animatable), predicate);
		RenderSystem.setShaderTexture(0, this.getTextureLocation(animatable));
		Color renderColor = this.getRenderColor(animatable, partialTick, poseStack, bufferSource, (VertexConsumer)null, packedLight);
		RenderType renderType = this.getRenderType(animatable, partialTick, poseStack, bufferSource, (VertexConsumer)null, packedLight, this.getTextureLocation(animatable));

		if (!animatable.isInvisibleTo(Minecraft.getInstance().player))
		{
			this.render(model, animatable, partialTick, renderType, poseStack, bufferSource, (VertexConsumer)null, packedLight, getPackedOverlay(animatable, 0.0F), (float)renderColor.getRed() / 255.0F, (float)renderColor.getGreen() / 255.0F, (float)renderColor.getBlue() / 255.0F, (float)renderColor.getAlpha() / 255.0F);
		}

		//super.render(animatable, yaw, partialTick, poseStack, bufferSource, packedLight);
	}
}
