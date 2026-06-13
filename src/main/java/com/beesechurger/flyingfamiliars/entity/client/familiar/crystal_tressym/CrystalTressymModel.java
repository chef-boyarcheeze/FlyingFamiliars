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
	public ResourceLocation getModelResource(CrystalTressymEntity entity)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/familiar/crystal_tressym.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(CrystalTressymEntity entity)
	{
		return switch (entity.getVariant())
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
	public ResourceLocation getAnimationResource(CrystalTressymEntity entity)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/familiar/crystal_tressym.animation.json");
	}

	@Override
	public void setCustomAnimations(CrystalTressymEntity entity, long instanceId, AnimationState customPredicate)
	{
		super.setCustomAnimations(entity, instanceId, customPredicate);

		if(customPredicate == null)
			return;

		EntityModelData extraDataOfType = (EntityModelData) customPredicate.getData(DataTickets.ENTITY_MODEL_DATA);
		CoreGeoBone head = this.getAnimationProcessor().getBone("head");

        float pitchOffset = entity.isSitting() ? 40 : 30;

        float pitch = Mth.clamp(extraDataOfType.headPitch() - pitchOffset, -60, -15);
		float yaw = Mth.clamp(extraDataOfType.netHeadYaw(), -20.0f, 20.0f);

        head.setRotX((float) Math.toRadians(pitch));
		head.setRotY((float) Math.toRadians(yaw));
        head.setRotZ((float) -Math.toRadians(yaw) * 0.8f);
	}
}
