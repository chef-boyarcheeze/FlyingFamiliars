package com.beesechurger.flyingfamiliars.entity.client.griffonfly;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.GriffonflyEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GriffonflyModel extends GeoModel<GriffonflyEntity>
{
	@Override
	public ResourceLocation getAnimationResource(GriffonflyEntity griffonflyEntity)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/familiar/griffonfly.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(GriffonflyEntity griffonflyEntity)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/familiar/griffonfly/griffonfly.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(GriffonflyEntity griffonflyEntity)
	{
        return switch (griffonflyEntity.getVariant()) {
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

	@Override
	public void setCustomAnimations(GriffonflyEntity griffonflyEntity, long instanceId, AnimationState customPredicate)
	{
		super.setCustomAnimations(griffonflyEntity, instanceId, customPredicate);

		if(customPredicate == null)
			return;

		EntityModelData extraDataOfType = (EntityModelData) customPredicate.getData(DataTickets.ENTITY_MODEL_DATA);
		CoreGeoBone head = this.getAnimationProcessor().getBone("head");

		float xRot = Mth.clamp(extraDataOfType.headPitch(), -15.0f, 15.0f);
		float yRot = Mth.clamp(extraDataOfType.netHeadYaw(), -30.0f, 30.0f);

		head.setRotX((float) Math.toRadians(xRot));
		head.setRotY((float) Math.toRadians(yRot));
	}
}
