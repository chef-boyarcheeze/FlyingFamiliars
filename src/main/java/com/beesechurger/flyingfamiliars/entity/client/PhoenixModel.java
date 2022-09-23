package com.beesechurger.flyingfamiliars.entity.client;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.custom.PhoenixEntity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PhoenixModel extends AnimatedGeoModel<PhoenixEntity> {

	@Override
	public ResourceLocation getAnimationFileLocation(PhoenixEntity animatable) {
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/phoenix.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(PhoenixEntity object) {
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/phoenix.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(PhoenixEntity object) {
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/phoenix/phoenix.png");
	}
}
