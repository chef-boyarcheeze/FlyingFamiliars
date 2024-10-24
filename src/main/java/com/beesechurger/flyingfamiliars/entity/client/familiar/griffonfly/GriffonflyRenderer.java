package com.beesechurger.flyingfamiliars.entity.client.familiar.griffonfly;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.client.familiar.BaseFamiliarRenderer;
import com.beesechurger.flyingfamiliars.entity.common.familiar.GriffonflyEntity;
import com.mojang.blaze3d.vertex.PoseStack;

import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class GriffonflyRenderer extends BaseFamiliarRenderer<GriffonflyEntity>
{
	public GriffonflyRenderer(Context renderManager)
	{
		super(renderManager, new GriffonflyModel());
		this.shadowRadius = 1.2f;
		this.withScale(1.5f);
	}
	
	@Override
	public ResourceLocation getTextureLocation(GriffonflyEntity griffonflyEntity)
	{
        return switch (griffonflyEntity.getVariant())
		{
            case "yellow" ->
                    new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/griffonfly/griffonfly_yellow.png");
            case "green" ->
                    new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/griffonfly/griffonfly_green.png");
            case "blue" ->
                    new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/griffonfly/griffonfly_blue.png");
            case "purple" ->
                    new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/griffonfly/griffonfly_purple.png");
            case "red" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/griffonfly/griffonfly_red.png");
            default ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/griffonfly/griffonfly_yellow.png");
        };
	}
}
