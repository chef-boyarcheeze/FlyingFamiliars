package com.beesechurger.flyingfamiliars.entity.client;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.GriffonflyEntity;
import com.beesechurger.flyingfamiliars.entity.common.MagicCarpetEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class MagicCarpetRenderer extends GeoEntityRenderer<MagicCarpetEntity>
{
	public MagicCarpetRenderer(Context renderManager)
	{
		super(renderManager, new MagicCarpetModel());
		this.shadowRadius = 0.6f;
		this.withScale(1.5f);
	}
	
	@Override
	public ResourceLocation getTextureLocation(MagicCarpetEntity animatable)
	{
		return switch (animatable.getVariant()) {
			case "white" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/magic_carpet/magic_carpet_white.png");
			case "light_gray" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/magic_carpet/magic_carpet_light_gray.png");
			case "gray" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/magic_carpet/magic_carpet_gray.png");
			case "black" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/magic_carpet/magic_carpet_black.png");
			case "red" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/magic_carpet/magic_carpet_red.png");
			case "orange" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/magic_carpet/magic_carpet_orange.png");
			case "yellow" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/magic_carpet/magic_carpet_yellow.png");
			case "lime" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/magic_carpet/magic_carpet_lime.png");
			case "green" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/magic_carpet/magic_carpet_green.png");
			case "cyan" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/magic_carpet/magic_carpet_cyan.png");
			case "light_blue" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/magic_carpet/magic_carpet_light_blue.png");
			case "blue" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/magic_carpet/magic_carpet_blue.png");
			case "purple" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/magic_carpet/magic_carpet_purple.png");
			case "magenta" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/magic_carpet/magic_carpet_magenta.png");
			case "pink" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/magic_carpet/magic_carpet_pink.png");
			case "brown" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/magic_carpet/magic_carpet_brown.png");
			default ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/magic_carpet/magic_carpet_white.png");
		};
	}
	
	@Override
    protected void applyRotations(MagicCarpetEntity animatable, PoseStack stack, float ageInTicks, float rotationYaw, float partialTicks)
	{
        super.applyRotations(animatable, stack, ageInTicks, rotationYaw, partialTicks);
        
        float renderPitch = (float) animatable.getPitch(partialTicks);
    	float renderRoll = (float) animatable.getRoll(partialTicks);

        stack.mulPose(Axis.XP.rotationDegrees(-renderPitch));
        stack.mulPose(Axis.ZP.rotationDegrees(renderRoll));
	}
}
