package com.beesechurger.flyingfamiliars.entity.client.familiar.shrubling;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.familiar.ShrublingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class ShrublingModel extends GeoModel<ShrublingEntity>
{
	@Override
	public ResourceLocation getModelResource(ShrublingEntity ShrublingEntity)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/familiar/shrubling/shrubling.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(ShrublingEntity ShrublingEntity)
	{
		return switch (ShrublingEntity.getVariant())
		{
			case "oak" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/shrubling/shrubling_oak.png");
			case "birch" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/shrubling/shrubling_birch.png");
			case "spruce" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/shrubling/shrubling_spruce.png");
			case "jungle" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/shrubling/shrubling_jungle.png");
			case "dark_oak" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/shrubling/shrubling_dark_oak.png");
			case "acacia" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/shrubling/shrubling_acacia.png");
			case "mangrove" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/shrubling/shrubling_mangrove.png");
			case "cherry" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/shrubling/shrubling_cherry.png");
			default ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/shrubling/shrubling_oak.png");
		};
	}

	@Override
	public ResourceLocation getAnimationResource(ShrublingEntity ShrublingEntity) {
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/familiar/shrubling.animation.json");
	}

	@Override
	public void setCustomAnimations(ShrublingEntity animatable, long instanceId, AnimationState customPredicate)
	{
		super.setCustomAnimations(animatable, instanceId, customPredicate);

		if(customPredicate == null)
			return;

		/*EntityModelData extraDataOfType = (EntityModelData) customPredicate.getData(DataTickets.ENTITY_MODEL_DATA);
		CoreGeoBone head = this.getAnimationProcessor().getBone("head");

		float yRot = Mth.clamp(extraDataOfType.netHeadYaw(), -3.0f, 3.0f);
		float zRot = Mth.clamp(extraDataOfType.headPitch() + 20, 5.0f, 35.0f);

		head.setRotY((float) Math.toRadians(yRot));
		head.setRotZ((float) Math.toRadians(zRot));*/
	}
}
