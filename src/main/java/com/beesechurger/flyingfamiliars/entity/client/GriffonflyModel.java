package com.beesechurger.flyingfamiliars.entity.client;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.custom.GriffonflyEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GriffonflyModel extends AnimatedGeoModel<GriffonflyEntity> {

	@Override
	public ResourceLocation getAnimationFileLocation(GriffonflyEntity animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/griffonfly.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(GriffonflyEntity animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/griffonfly.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(GriffonflyEntity animatable)
	{
        return switch (animatable.getVariant()) {
            case "Yellow" ->
                    new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/griffonfly/griffonfly_yellow.png");
            case "Green" ->
                    new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/griffonfly/griffonfly_green.png");
            case "Blue" ->
                    new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/griffonfly/griffonfly_blue.png");
            case "Purple" ->
                    new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/griffonfly/griffonfly_purple.png");
            case "Red" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/griffonfly/griffonfly_red.png");
            default ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/griffonfly/griffonfly_yellow.png");
        };
	}
}
