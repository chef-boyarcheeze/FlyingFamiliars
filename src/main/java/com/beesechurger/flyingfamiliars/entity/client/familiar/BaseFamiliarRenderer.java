package com.beesechurger.flyingfamiliars.entity.client.familiar;

import com.beesechurger.flyingfamiliars.entity.common.familiar.BaseFamiliarEntity;
import com.beesechurger.flyingfamiliars.event.ClientEvents;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.cache.object.BakedGeoModel;
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
    public void actuallyRender(PoseStack poseStack, T entity, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        if (!(entity.isPassenger() && ClientEvents.blockRenderList.contains(entity.getUUID())))
        {
            super.actuallyRender(poseStack, entity, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }

    @Override
    protected void applyRotations(T entity, PoseStack stack, float ageInTicks, float rotationYaw, float partialTicks)
    {
        super.applyRotations(entity, stack, ageInTicks, rotationYaw, partialTicks);

        float renderPitch = (float) entity.getPitch(partialTicks);
        float renderRoll = (float) entity.getRoll(partialTicks);

        stack.mulPose(Axis.XP.rotationDegrees(-renderPitch));
        stack.mulPose(Axis.ZP.rotationDegrees(renderRoll));
    }
}
