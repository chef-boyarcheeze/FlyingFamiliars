package com.beesechurger.flyingfamiliars.entity.client.layer;

import com.beesechurger.flyingfamiliars.entity.common.BaseFamiliarEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class LayerFamiliarRider<T extends Entity & IAnimatable> extends GeoLayerRenderer<T>
{
    public LayerFamiliarRider(IGeoRenderer<T> entityRendererIn)
    {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
    {
        BaseFamiliarEntity familiar = (BaseFamiliarEntity) entity;

        stack.pushPose();

        if (!familiar.notCarryingPassengers())
        {
            //float dragonScale = dragon.getRenderSize() / 3;

            for (Entity passenger : familiar.getPassengers())
            {
                //boolean prey = dragon.getControllingPassenger() == null || dragon.getControllingPassenger().getId() != passenger.getId();

                //ClientProxy.currentDragonRiders.remove(passenger.getUUID());


                float riderRot = passenger.yRotO + (passenger.getYRot() - passenger.yRotO) * partialTicks;

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

                stack.pushPose();
                stack.mulPose(new Quaternion(Vector3f.ZP, 0, true));
                stack.mulPose(new Quaternion(Vector3f.YP, riderRot + 180, true));
                //stack.scale(1 / dragonScale, 1 / dragonScale, 1 / dragonScale);
                stack.translate(0, -0.25F, 0);
                Minecraft.getInstance().getEntityRenderDispatcher().render(passenger, 0, 0, 0, 0, partialTicks, stack, bufferIn, packedLightIn); // packedLightIn 255
                stack.popPose();
            }
        }

        stack.popPose();
    }
}
