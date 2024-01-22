package com.beesechurger.flyingfamiliars.entity.client;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
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
public class GriffonflyRenderer extends GeoEntityRenderer<GriffonflyEntity>
{
	public GriffonflyRenderer(Context renderManager)
	{
		super(renderManager, new GriffonflyModel());
		this.shadowRadius = 1.2f;
	}
	
	@Override
	public ResourceLocation getTextureLocation(GriffonflyEntity animatable)
	{
        return switch (animatable.getVariant())
		{
            case "yellow" ->
                    new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/griffonfly/griffonfly_yellow.png");
            case "green" ->
                    new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/griffonfly/griffonfly_green.png");
            case "blue" ->
                    new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/griffonfly/griffonfly_blue.png");
            case "purple" ->
                    new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/griffonfly/griffonfly_purple.png");
            case "red" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/griffonfly/griffonfly_red.png");
            default ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/griffonfly/griffonfly_yellow.png");
        };
	}

	@Override
	public RenderType getRenderType(GriffonflyEntity animatable, float partialTicks, PoseStack stack,
									MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation)
	{
		stack.scale(1.5f, 1.5f, 1.5f);
		return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
	}
	
	@Override
    protected void applyRotations(GriffonflyEntity animatable, PoseStack stack, float ageInTicks, float rotationYaw, float partialTicks)
	{
        super.applyRotations(animatable, stack, ageInTicks, rotationYaw, partialTicks);
        
        float renderPitch = (float) animatable.getPitch(partialTicks);
    	float renderRoll = (float) animatable.getRoll(partialTicks);

        stack.mulPose(Vector3f.XP.rotationDegrees(-renderPitch));
        stack.mulPose(Vector3f.ZP.rotationDegrees(renderRoll));
	}
}
