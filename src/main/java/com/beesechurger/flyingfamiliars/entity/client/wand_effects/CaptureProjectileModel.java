package com.beesechurger.flyingfamiliars.entity.client.wand_effects;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.CloudRayEntity;
import com.beesechurger.flyingfamiliars.entity.common.GriffonflyEntity;
import com.beesechurger.flyingfamiliars.entity.common.projectile.CaptureProjectile;
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
public class CaptureProjectileModel extends AnimatedGeoModel<CaptureProjectile>
{
	@Override
	public ResourceLocation getAnimationFileLocation(CaptureProjectile animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/wand_effects/capture_projectile.animation.json");
	}

	@Override
	public ResourceLocation getModelLocation(CaptureProjectile animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/wand_effects/capture_projectile.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation(CaptureProjectile animatable)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/entity/wand_effects/capture_projectile.png");
	}

	/*@Override
	public void setLivingAnimations(CaptureProjectile animatable, Integer uniqueID, @Nullable AnimationEvent customPredicate)
	{
		super.setLivingAnimations(animatable, uniqueID, customPredicate);

		if(customPredicate == null)
			return;

		int angle = animatable.tickCount;

		IBone core = this.getAnimationProcessor().getBone("core");
		IBone interior = this.getAnimationProcessor().getBone("interior");
		IBone exterior = this.getAnimationProcessor().getBone("exterior");

		core.setRotationX(angle);
		core.setRotationY(-angle);

		interior.setRotationX(-angle / 2);
		interior.setRotationY(angle / 2);

		exterior.setRotationX(angle / 4);
		exterior.setRotationY(angle / 4);
	}*/
}
