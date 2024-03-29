package com.beesechurger.flyingfamiliars.entity.client;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.CloudRayEntity;
import com.beesechurger.flyingfamiliars.entity.common.CormorantEntity;
import com.beesechurger.flyingfamiliars.entity.common.GriffonflyEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class CormorantRenderer extends GeoEntityRenderer<CormorantEntity>
{

	public CormorantRenderer(Context renderManager)
	{
		super(renderManager, new CormorantModel());
		this.shadowRadius = 0.35f;
	}

	@Override
	public ResourceLocation getTextureLocation(CormorantEntity animatable)
	{
		return switch (animatable.getVariant()) {
			case "great_cormorant" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/cormorant/great_cormorant.png");
			case "australian_pied_cormorant" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/cormorant/australian_pied_cormorant.png");
			case "red_legged_cormorant" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/cormorant/red_legged_cormorant.png");
			default ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/cormorant/great_cormorant.png");
		};
	}

	@Override
	public RenderType getRenderType(CormorantEntity animatable, float partialTicks, PoseStack stack,
									MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation)
	{
		stack.scale(1.4f, 1.4f, 1.4f);
		return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
	}

	@Override
	protected void applyRotations(CormorantEntity animatable, PoseStack stack, float ageInTicks, float rotationYaw, float partialTicks)
	{
		super.applyRotations(animatable, stack, ageInTicks, rotationYaw, partialTicks);

		float renderPitch = (float) animatable.getPitch(partialTicks);
		float renderRoll = (float) animatable.getRoll(partialTicks);

		stack.mulPose(Vector3f.XP.rotationDegrees(-renderPitch));
		stack.mulPose(Vector3f.ZP.rotationDegrees(renderRoll));
	}
}
