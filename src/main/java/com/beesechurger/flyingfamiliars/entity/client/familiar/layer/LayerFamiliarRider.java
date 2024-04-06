package com.beesechurger.flyingfamiliars.entity.client.familiar.layer;

import com.beesechurger.flyingfamiliars.entity.common.familiar.BaseFamiliarEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class LayerFamiliarRider<T extends Entity & GeoEntity> extends GeoRenderLayer<T>
{
    public LayerFamiliarRider(GeoEntityRenderer<T> entityRendererIn)
    {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay)
    {
        BaseFamiliarEntity familiar = (BaseFamiliarEntity) animatable;

        poseStack.pushPose();

        if (!familiar.notCarryingPassengers())
        {
            //float dragonScale = dragon.getRenderSize() / 3;

            for (Entity passenger : familiar.getPassengers())
            {
                //boolean prey = dragon.getControllingPassenger() == null || dragon.getControllingPassenger().getId() != passenger.getId();

                //ClientProxy.currentDragonRiders.remove(passenger.getUUID());


                float riderRot = passenger.yRotO + (passenger.getYRot() - passenger.yRotO) * partialTick;

                /*
                int animationTicks = 0;

                if (dragon.getAnimation() == EntityDragonBase.ANIMATION_SHAKEPREY) {
                    animationTicks = dragon.getAnimationTick();
                }

                if (animationTicks == 0 || animationTicks >= 15) {
                    translateToBody(stack);
                }*/

                /*if (prey) {
                    if (animationTicks == 0 || animationTicks >= 15 || dragon.isFlying()) {
                        translateToHead(stack);
                        offsetPerDragonType(dragon.dragonType, stack);
                        EntityRenderer render = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(passenger);
                        EntityModel modelBase = null;
                        if (render instanceof MobRenderer) {
                            modelBase = ((MobRenderer) render).getModel();
                        }
                        if ((passenger.getBbHeight() > passenger.getBbWidth() || modelBase instanceof HumanoidModel) && !(modelBase instanceof QuadrupedModel) && !(modelBase instanceof HorseModel)) {
                            stack.translate(-0.15F * passenger.getBbHeight(), 0.1F * dragonScale - 0.1F * passenger.getBbHeight(), -0.1F * dragonScale - 0.1F * passenger.getBbWidth());
                            stack.mulPose(new Quaternion(Vector3f.ZP, 90, true));
                            stack.mulPose(new Quaternion(Vector3f.YP, 45, true));
                        } else {
                            boolean horse = modelBase instanceof HorseModel;
                            stack.translate((horse ? -0.08F : -0.15F) * passenger.getBbWidth(), 0.1F * dragonScale - 0.15F * passenger.getBbWidth(), -0.1F * dragonScale - 0.1F * passenger.getBbWidth());
                            stack.mulPose(new Quaternion(Vector3f.XN, 90, true));
                        }
                    } else {
                        stack.translate(0, 0.555F * dragonScale, -0.5F * dragonScale);
                    }

                } else {
                    stack.translate(0, -0.01F * dragonScale, -0.035F * dragonScale);
                }
                 */

                poseStack.pushPose();
                poseStack.mulPose(Axis.ZP.rotationDegrees(0));
                poseStack.mulPose(Axis.YP.rotationDegrees(riderRot + 180));
                //stack.scale(1 / dragonScale, 1 / dragonScale, 1 / dragonScale);
                poseStack.translate(0, -0.25F, 0);
                Minecraft.getInstance().getEntityRenderDispatcher().render(passenger, 0, 0, 0, 0, partialTick, poseStack, bufferSource, packedLight); // packedLightIn 255
                poseStack.popPose();
            }
        }

        poseStack.popPose();
    }
}
