package com.beesechurger.flyingfamiliars.entity.client.familiar;

import com.beesechurger.flyingfamiliars.entity.client.familiar.void_moth.VoidMothModel;
import com.beesechurger.flyingfamiliars.entity.common.familiar.BaseFamiliarEntity;
import com.beesechurger.flyingfamiliars.entity.common.familiar.VoidMothEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class BaseFamiliarRenderer<T extends BaseFamiliarEntity & GeoAnimatable> extends GeoEntityRenderer<T>
{
    public BaseFamiliarRenderer(Context renderManager, GeoModel<T> model)
    {
        super(renderManager, model);
    }

    @Override
    protected void applyRotations(T animatable, PoseStack stack, float ageInTicks, float rotationYaw, float partialTicks)
    {
        super.applyRotations(animatable, stack, ageInTicks, rotationYaw, partialTicks);

        float renderPitch = (float) animatable.getPitch(partialTicks);
        float renderRoll = (float) animatable.getRoll(partialTicks);

        stack.mulPose(Axis.XP.rotationDegrees(-renderPitch));
        stack.mulPose(Axis.ZP.rotationDegrees(renderRoll));
    }
}
