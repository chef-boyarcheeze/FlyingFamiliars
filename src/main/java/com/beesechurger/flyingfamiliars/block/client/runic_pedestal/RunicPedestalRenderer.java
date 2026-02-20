package com.beesechurger.flyingfamiliars.block.client.runic_pedestal;

import com.beesechurger.flyingfamiliars.block.entity.RunicPedestalBE;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RunicPedestalRenderer implements BlockEntityRenderer<RunicPedestalBE>
{
    public RunicPedestalRenderer(BlockEntityRendererProvider.Context c)
    {
    }

    @SuppressWarnings("resource")
    @Override
    public void render(RunicPedestalBE runicPedestalBE, float partialTick, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight)
    {
        if(Minecraft.getInstance().level != null)
        {
            float time = Minecraft.getInstance().level.getGameTime() + partialTick;

            for (final ItemStack storedItem : runicPedestalBE.items)
            {
                //if ( is crstal item then get different model)
                BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(storedItem, runicPedestalBE.getLevel(), (LivingEntity)null, 0);

                stack.pushPose();
                stack.translate(0.5d, 1.2d, 0.5d);
                stack.mulPose(Axis.YP.rotationDegrees(time));
                stack.mulPose(Axis.YP.rotationDegrees(time * 2));
                stack.scale(0.75f, 0.75f, 0.75f);
                Minecraft.getInstance().getItemRenderer().render(storedItem, ItemDisplayContext.GROUND, false, stack, buffer, 255, OverlayTexture.NO_OVERLAY, model);
                stack.popPose();
            }
        }
    }
}
