package com.beesechurger.flyingfamiliars.entity.client.familiar.cormorant;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.familiar.CormorantEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class CormorantRenderer extends GeoEntityRenderer<CormorantEntity>
{

	public CormorantRenderer(Context renderManager)
	{
		super(renderManager, new CormorantModel());
		this.shadowRadius = 0.35f;
		this.withScale(1.5f);
	}

	@Override
	public ResourceLocation getTextureLocation(CormorantEntity cormorantEntity)
	{
		return switch (cormorantEntity.getVariant()) {
			case "great_cormorant" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/cormorant/great_cormorant.png");
			case "australian_pied_cormorant" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/cormorant/australian_pied_cormorant.png");
			case "red_legged_cormorant" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/cormorant/red_legged_cormorant.png");
			default ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/cormorant/great_cormorant.png");
		};
	}

	@Override
	protected void applyRotations(CormorantEntity cormorantEntity, PoseStack stack, float ageInTicks, float rotationYaw, float partialTicks)
	{
		super.applyRotations(cormorantEntity, stack, ageInTicks, rotationYaw, partialTicks);

		float renderPitch = (float) cormorantEntity.getPitch(partialTicks);
		float renderRoll = (float) cormorantEntity.getRoll(partialTicks);

		stack.mulPose(Axis.XP.rotationDegrees(-renderPitch));
		stack.mulPose(Axis.ZP.rotationDegrees(renderRoll));
	}
}
