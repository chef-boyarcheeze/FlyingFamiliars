package com.beesechurger.flyingfamiliars.items.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.BASE_ENTITY_TAGNAME;

public class SoulBatteryRenderer implements ICurioRenderer
{
    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack,
                                                                          SlotContext slotContext,
                                                                          PoseStack matrixStack,
                                                                          RenderLayerParent<T, M> renderLayerParent,
                                                                          MultiBufferSource renderTypeBuffer,
                                                                          int light, float limbSwing,
                                                                          float limbSwingAmount,
                                                                          float partialTicks,
                                                                          float ageInTicks,
                                                                          float netHeadYaw,
                                                                          float headPitch)
    {
        if(Minecraft.getInstance().level == null) return;
        float time = Minecraft.getInstance().level.getGameTime() + partialTicks;

        BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(stack, Minecraft.getInstance().level, (LivingEntity)null, 0);

        matrixStack.pushPose();

        ICurioRenderer.translateIfSneaking(matrixStack, slotContext.entity());
        ICurioRenderer.rotateIfSneaking(matrixStack, slotContext.entity());
        ICurioRenderer.followBodyRotations(slotContext.entity());

        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180));
        matrixStack.translate(-0.25f, -0.7f, 0);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(-90));
        matrixStack.scale(0.5f, 0.5f, 0.5f);

        Minecraft.getInstance().getItemRenderer().render(stack, ItemTransforms.TransformType.GROUND, false, matrixStack, renderTypeBuffer, 255, OverlayTexture.NO_OVERLAY, model);
        matrixStack.popPose();
    }
}
