package com.beesechurger.flyingfamiliars.block.client.runic_pedestal;

import com.beesechurger.flyingfamiliars.block.entity.RunicPedestalBE;
import com.beesechurger.flyingfamiliars.entity.common.wand_effect.projectile.RunicCubeProjectile;
import com.beesechurger.flyingfamiliars.registries.FFEntityTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

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
                if (runicPedestalBE.containsRunicCubeItem(storedItem)) // TODO: make this actual crystal item
                {
                    if (runicPedestalBE.runicCubeProjectile == null)
                    {
                        //System.out.println("cube null");
                        break;
                    }

                    stack.pushPose();
                    stack.translate(0.5d, 1.25d + 0.1d * Math.sin(time / 10), 0.5d);
                    stack.mulPose(Axis.YN.rotationDegrees(time));
                    Minecraft.getInstance().getEntityRenderDispatcher().render(runicPedestalBE.runicCubeProjectile, 0, 0, 0, 0, partialTick, stack, buffer, 255);
                    stack.popPose();
                }
                else
                {
                    BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(storedItem, runicPedestalBE.getLevel(), (LivingEntity)null, 0);

                    stack.pushPose();
                    stack.translate(0.5d, 1.2d, 0.5d);
                    stack.mulPose(Axis.YP.rotationDegrees(time * 2));
                    Minecraft.getInstance().getItemRenderer().render(storedItem, ItemDisplayContext.GROUND, false, stack, buffer, 255, OverlayTexture.NO_OVERLAY, model);
                    stack.popPose();
                }
            }
        }
    }
}
