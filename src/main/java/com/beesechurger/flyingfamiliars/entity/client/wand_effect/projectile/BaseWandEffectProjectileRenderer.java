package com.beesechurger.flyingfamiliars.entity.client.wand_effect.projectile;

import com.beesechurger.flyingfamiliars.entity.common.wand_effect.projectile.BaseWandEffectProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BaseWandEffectProjectileRenderer<T extends BaseWandEffectProjectile & GeoAnimatable> extends GeoEntityRenderer<T>
{
    public BaseWandEffectProjectileRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model)
    {
        super(renderManager, model);
    }

    @Override
    protected void applyRotations(T animatable, PoseStack stack, float ageInTicks, float rotationYaw, float partialTicks)
    {
        super.applyRotations(animatable, stack, ageInTicks, rotationYaw, partialTicks);

        float renderPitch = (float) animatable.getPitch(partialTicks);
        float renderYaw = (float) animatable.getYaw(partialTicks);

        stack.mulPose(Axis.YP.rotationDegrees(renderYaw));
        stack.mulPose(Axis.XP.rotationDegrees(renderPitch));
    }
}
