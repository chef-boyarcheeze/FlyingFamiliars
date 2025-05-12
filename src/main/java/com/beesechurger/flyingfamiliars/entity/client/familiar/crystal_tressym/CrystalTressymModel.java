package com.beesechurger.flyingfamiliars.entity.client.familiar.crystal_tressym;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.familiar.CrystalTressymEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

@OnlyIn(Dist.CLIENT)
public class CrystalTressymModel extends GeoModel<CrystalTressymEntity>
{
	@Override
	public ResourceLocation getModelResource(CrystalTressymEntity CrystalTressymEntity)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/familiar/crystal_tressym/crystal_tressym.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(CrystalTressymEntity CrystalTressymEntity)
	{
		return switch (CrystalTressymEntity.getVariant())
		{
			case "blue" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/crystal_tressym/crystal_tressym_blue.png");
			case "green" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/crystal_tressym/crystal_tressym_green.png");
			case "purple" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/crystal_tressym/crystal_tressym_purple.png");
			default ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/crystal_tressym/crystal_tressym_green.png");
		};
	}

	@Override
	public ResourceLocation getAnimationResource(CrystalTressymEntity CrystalTressymEntity) {
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/familiar/crystal_tressym.animation.json");
	}

	@Override
	public void setCustomAnimations(CrystalTressymEntity animatable, long instanceId, AnimationState customPredicate)
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
