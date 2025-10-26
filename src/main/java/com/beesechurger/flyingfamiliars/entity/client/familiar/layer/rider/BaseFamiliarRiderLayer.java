package com.beesechurger.flyingfamiliars.entity.client.familiar.layer.rider;

import com.beesechurger.flyingfamiliars.entity.common.familiar.BaseFamiliarEntity;
import com.beesechurger.flyingfamiliars.entity.common.familiar.GriffonflyEntity;
import com.beesechurger.flyingfamiliars.entity.common.familiar.MagicCarpetEntity;
import com.beesechurger.flyingfamiliars.event.ClientEvents;
import com.beesechurger.flyingfamiliars.registries.FFEntityTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public abstract class BaseFamiliarRiderLayer<T extends BaseFamiliarEntity & GeoEntity> extends GeoRenderLayer<T>
{
    private final GeoEntityRenderer<T> entityRenderer;

    public BaseFamiliarRiderLayer(GeoEntityRenderer<T> renderIn)
    {
        super(renderIn);
        this.entityRenderer = renderIn;
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTicks, int packedLight, int packedOverlay)
    {
        BaseFamiliarEntity familiar = animatable;
        float vehicleRenderSize = FFEntityTypes.ENTITY_RENDER_SIZE_MAP.get(familiar.getType().toShortString());

        if (familiar.isVehicle())
        {
            var pitch = (float) familiar.getPitch(partialTicks);
            var yaw = (float) familiar.getYaw(partialTicks);
            var roll = (float) familiar.getRoll(partialTicks);

            for (Entity passenger : familiar.getPassengers())
            {
                double renderOffset = familiar.getPassengersRidingOffset() + passenger.getMyRidingOffset();

                if(passenger.getVehicle() instanceof GriffonflyEntity griffonfly)
                    renderOffset = griffonfly.getRiderPosition(passenger).y;
                else if(passenger.getVehicle() instanceof MagicCarpetEntity magicCarpet)
                    renderOffset = magicCarpet.getRiderPosition(passenger).y;

                var type = familiar.getType().toShortString();

                System.out.println(type);

                String bone = switch (type)
                {
                    case "cloud_ray" -> "torso";
                    case "griffonfly" -> "thorax";
                    default -> "";
                };

                var rotZTorso = Math.toDegrees(this.entityRenderer.getGeoModel().getBone(bone).get().getRotZ());

                System.out.println(rotZTorso);

                pitch += rotZTorso;

                ClientEvents.blockRenderList.remove(passenger.getUUID());
                poseStack.pushPose();
                poseStack.scale(1 / vehicleRenderSize, 1 / vehicleRenderSize, 1 / vehicleRenderSize);

                poseStack.mulPose(Axis.XP.rotationDegrees(pitch * Mth.cos((float) Math.toRadians(yaw))));
                poseStack.mulPose(Axis.ZP.rotationDegrees(pitch * Mth.sin((float) Math.toRadians(yaw))));

                poseStack.mulPose(Axis.XP.rotationDegrees(roll * Mth.sin((float) Math.toRadians(yaw))));
                poseStack.mulPose(Axis.ZN.rotationDegrees(roll * Mth.cos((float) Math.toRadians(yaw))));

                poseStack.translate(0, renderOffset, 0);

                Minecraft.getInstance().getEntityRenderDispatcher().render(passenger, 0, 0, 0, 0, partialTicks, poseStack, bufferSource, packedLight); // packedLightIn 255
                poseStack.popPose();
                ClientEvents.blockRenderList.add(passenger.getUUID());
            }
        }
    }

    protected abstract String getBone();
}
