package com.beesechurger.flyingfamiliars.entity.client.phoenix;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.client.cormorant.CormorantModel;
import com.beesechurger.flyingfamiliars.entity.common.PhoenixEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class PhoenixRenderer extends GeoEntityRenderer<PhoenixEntity>
{

	public PhoenixRenderer(Context renderManager)
	{
		super(renderManager, new PhoenixModel());
		this.shadowRadius = 0.35f;
		this.withScale(1.0f);
	}

	@Override
	public ResourceLocation getTextureLocation(PhoenixEntity phoenixEntity)
	{
		return switch (phoenixEntity.getVariant()) {
			case "red" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/phoenix/phoenix_red.png");
			case "blue" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/phoenix/phoenix_blue.png");
			default ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/phoenix/phoenix_red.png");
		};
	}

	@Override
	protected void applyRotations(PhoenixEntity animatable, PoseStack stack, float ageInTicks, float rotationYaw, float partialTicks)
	{
		super.applyRotations(animatable, stack, ageInTicks, rotationYaw, partialTicks);

		float renderPitch = (float) animatable.getPitch(partialTicks);
		float renderRoll = (float) animatable.getRoll(partialTicks);

		stack.mulPose(Axis.XP.rotationDegrees(-renderPitch));
		stack.mulPose(Axis.ZP.rotationDegrees(renderRoll));
	}
}
