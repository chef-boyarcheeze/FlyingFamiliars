package com.beesechurger.flyingfamiliars.entity.client.familiar.layer.rider;

import com.beesechurger.flyingfamiliars.FFEvents;
import com.beesechurger.flyingfamiliars.entity.FFEntityTypes;
import com.beesechurger.flyingfamiliars.entity.common.familiar.BaseFamiliarEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public abstract class BaseFamiliarRiderLayer<T extends BaseFamiliarEntity & GeoEntity> extends GeoRenderLayer<T>
{
    private final GeoEntityRenderer<T> familiarRenderer;

    public BaseFamiliarRiderLayer(GeoEntityRenderer<T> renderIn)
    {
        super(renderIn);
        this.familiarRenderer = renderIn;
    }

    @Override
    public void render(PoseStack poseStack, T entity, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTicks, int packedLight, int packedOverlay)
    {
        if (entity.isVehicle())
        {
            var familiarModel = getRenderer().getGeoModel();

            if (!familiarModel.getBone(getSeatBone()).isEmpty())
            {
                float vehicleRenderSize = FFEntityTypes.ENTITY_RENDER_SIZE_MAP.get(entity.getType().toShortString());

                var pitch = (float) entity.getPitch(partialTicks);
                var yaw = (float) entity.getYaw(partialTicks);
                var roll = (float) entity.getRoll(partialTicks);

                var bone = familiarModel.getBone(getSeatBone()).get();
                Vec3 pivot = new Vec3(bone.getPivotX(), bone.getPivotY(), bone.getPivotZ());

                for (Entity passenger : entity.getPassengers())
                {
                    if (passenger != Minecraft.getInstance().player || Minecraft.getInstance().options.getCameraType() != CameraType.FIRST_PERSON)
                    {
                        EntityRenderer<? super Entity> passengerRenderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(passenger);

                        if (passengerRenderer != null)
                        {
                            FFEvents.Client.BLOCK_RENDER_LIST.remove(passenger.getUUID());
                            poseStack.pushPose();

                            // scale for familiar's model size
                            poseStack.scale(1 / vehicleRenderSize, 1 / vehicleRenderSize, 1 / vehicleRenderSize);

                            // rotate into current familiar yaw frame of reference
                            poseStack.mulPose(Axis.YN.rotationDegrees(yaw + 180));

                                // Rotate to mount reference
                                var modelRot = bone.getModelRotationMatrix();
                                poseStack.mulPoseMatrix(modelRot);

                                // translate to bone pivot point
                                poseStack.translate(-pivot.x * 0.0625f, -pivot.y * 0.0625f, -pivot.z * 0.0625f);

                                    // rotate for current familiar pitch
                                    poseStack.mulPose(Axis.XN.rotationDegrees(pitch));

                                    // rotate for current familiar roll
                                    poseStack.mulPose(Axis.ZP.rotationDegrees(roll));

                                // translate back from bone pivot point
                                poseStack.translate(pivot.x * 0.0625f, pivot.y * 0.0625f, pivot.z * 0.0625f);

                                // translate to final render location
                                Vec3 pos = entity.getRiderPosition(passenger);
                                Vec3 renderOffset = getRenderOffset(entity, passenger);
                                poseStack.translate(-pos.x, pos.y - renderOffset.y, -pos.z);

                            // rotate out of current familiar yaw frame of reference
                            poseStack.mulPose(Axis.YP.rotationDegrees(yaw + 180));

                            passengerRenderer.render(passenger,  0, partialTicks, poseStack, bufferSource, packedLight);
                            poseStack.popPose();
                            FFEvents.Client.BLOCK_RENDER_LIST.add(passenger.getUUID());
                        }
                    }
                }
            }
        }
    }

    protected abstract String getSeatBone();

    protected abstract Vec3 getRenderOffset(T entity, Entity passenger);
}
