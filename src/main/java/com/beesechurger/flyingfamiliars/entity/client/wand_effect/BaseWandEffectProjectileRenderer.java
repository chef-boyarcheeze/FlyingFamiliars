package com.beesechurger.flyingfamiliars.entity.client.wand_effect;

import com.beesechurger.flyingfamiliars.entity.common.wand_effect.BaseWandEffectProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.BakedGeoModel;
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

        stack.mulPose(Axis.XP.rotationDegrees(-renderPitch));
        stack.mulPose(Axis.YP.rotationDegrees(renderYaw));
    }
}
