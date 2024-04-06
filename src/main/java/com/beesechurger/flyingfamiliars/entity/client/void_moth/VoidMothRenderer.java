package com.beesechurger.flyingfamiliars.entity.client.void_moth;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.PhoenixEntity;
import com.beesechurger.flyingfamiliars.entity.common.VoidMothEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class VoidMothRenderer extends GeoEntityRenderer<VoidMothEntity>
{

	public VoidMothRenderer(Context renderManager)
	{
		super(renderManager, new VoidMothModel());
		this.shadowRadius = 0.35f;
		this.withScale(1.0f);
	}

	@Override
	public ResourceLocation getTextureLocation(VoidMothEntity voidMothEntity)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/void_moth/void_moth.png");
	}

	@Override
	protected void applyRotations(VoidMothEntity voidMothEntity, PoseStack stack, float ageInTicks, float rotationYaw, float partialTicks)
	{
		super.applyRotations(voidMothEntity, stack, ageInTicks, rotationYaw, partialTicks);

		float renderPitch = (float) voidMothEntity.getPitch(partialTicks);
		float renderRoll = (float) voidMothEntity.getRoll(partialTicks);

		stack.mulPose(Axis.XP.rotationDegrees(-renderPitch));
		stack.mulPose(Axis.ZP.rotationDegrees(renderRoll));
	}
}
