package com.beesechurger.flyingfamiliars.entity.client.layer;

import com.beesechurger.flyingfamiliars.entity.common.BaseFamiliarEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class LayerEntityRider extends GeoLayerRenderer<BaseFamiliarEntity>
{
    public LayerEntityRider(IGeoRenderer<BaseFamiliarEntity> entityRendererIn)
    {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, BaseFamiliarEntity familiar, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
    {
        matrixStackIn.pushPose();

        if (!familiar.notCarryingPassengers())
        {
            //float dragonScale = dragon.getRenderSize() / 3;

            for (Entity passenger : familiar.getPassengers())
            {
                //boolean prey = dragon.getControllingPassenger() == null || dragon.getControllingPassenger().getId() != passenger.getId();

                //ClientProxy.currentDragonRiders.remove(passenger.getUUID());


                //float riderRot = passenger.yRotO + (passenger.getYRot() - passenger.yRotO) * partialTicks;

                /*
                int animationTicks = 0;

                if (dragon.getAnimation() == EntityDragonBase.ANIMATION_SHAKEPREY) {
                    animationTicks = dragon.getAnimationTick();
                }

                if (animationTicks == 0 || animationTicks >= 15) {
                    translateToBody(matrixStackIn);
                }*/

                if (prey) {
                    if (animationTicks == 0 || animationTicks >= 15 || dragon.isFlying()) {
                        translateToHead(matrixStackIn);
                        offsetPerDragonType(dragon.dragonType, matrixStackIn);
                        EntityRenderer render = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(passenger);
                        EntityModel modelBase = null;
                        if (render instanceof MobRenderer) {
                            modelBase = ((MobRenderer) render).getModel();
                        }
                        if ((passenger.getBbHeight() > passenger.getBbWidth() || modelBase instanceof HumanoidModel) && !(modelBase instanceof QuadrupedModel) && !(modelBase instanceof HorseModel)) {
                            matrixStackIn.translate(-0.15F * passenger.getBbHeight(), 0.1F * dragonScale - 0.1F * passenger.getBbHeight(), -0.1F * dragonScale - 0.1F * passenger.getBbWidth());
                            matrixStackIn.mulPose(new Quaternion(Vector3f.ZP, 90, true));
                            matrixStackIn.mulPose(new Quaternion(Vector3f.YP, 45, true));
                        } else {
                            boolean horse = modelBase instanceof HorseModel;
                            matrixStackIn.translate((horse ? -0.08F : -0.15F) * passenger.getBbWidth(), 0.1F * dragonScale - 0.15F * passenger.getBbWidth(), -0.1F * dragonScale - 0.1F * passenger.getBbWidth());
                            matrixStackIn.mulPose(new Quaternion(Vector3f.XN, 90, true));
                        }
                    } else {
                        matrixStackIn.translate(0, 0.555F * dragonScale, -0.5F * dragonScale);
                    }

                } else {
                    matrixStackIn.translate(0, -0.01F * dragonScale, -0.035F * dragonScale);
                }
                matrixStackIn.pushPose();
                matrixStackIn.mulPose(new Quaternion(Vector3f.ZP, 180, true));
                matrixStackIn.mulPose(new Quaternion(Vector3f.YP, riderRot + 180, true));
                //matrixStackIn.scale(1 / dragonScale, 1 / dragonScale, 1 / dragonScale);
                matrixStackIn.translate(0, -0.25F, 0);
                renderEntity(passenger, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, matrixStackIn, bufferIn, packedLightIn);
                matrixStackIn.popPose();
                ClientProxy.currentDragonRiders.add(passenger.getUUID());
            }
        }

        matrixStackIn.popPose();
    }

    public <E extends Entity> void renderEntity(E entityIn, double x, double y, double z, float yaw, float partialTicks, PoseStack matrixStack, MultiBufferSource bufferIn, int packedLight)
    {
        EntityRenderDispatcher renderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<? super E> render = renderDispatcher.getRenderer(entityIn);

        if (render != null)
            render.render(entityIn, 0, partialTicks, matrixStack, bufferIn, packedLight);
    }
}
