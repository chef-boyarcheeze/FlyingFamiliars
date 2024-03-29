package com.beesechurger.flyingfamiliars.entity.client;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.GriffonflyEntity;
import com.beesechurger.flyingfamiliars.entity.common.MagicCarpetEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class MagicCarpetModel extends AnimatedGeoModel<MagicCarpetEntity> {

	@Override
	public ResourceLocation getAnimationFileLocation(MagicCarpetEntity animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/magic_carpet.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(MagicCarpetEntity animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/magic_carpet/magic_carpet.geo.json");
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
}
