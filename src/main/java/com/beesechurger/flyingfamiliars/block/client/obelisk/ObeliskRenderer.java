package com.beesechurger.flyingfamiliars.block.client.obelisk;

import com.beesechurger.flyingfamiliars.block.entity.ObeliskBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.EnchantmentTableBlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ObeliskRenderer implements BlockEntityRenderer<ObeliskBlockEntity>
{
    private final ObeliskModel obeliskModel;

    private float startRotatingTime;
    private boolean rotating = false;

    public ObeliskRenderer(BlockEntityRendererProvider.Context ctx)
    {
        this.obeliskModel = new ObeliskModel(ctx.bakeLayer(ObeliskModel.LAYER_LOCATION));
    }

    public void render(ObeliskBlockEntity obeliskBlockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay)
    {
        stack.pushPose();
        stack.translate(0.5f, 1.8f, 0.5f);
        stack.mulPose(Axis.ZP.rotationDegrees(180));

        if(obeliskBlockEntity.clicked && !rotating)
        {
            startRotatingTime = (float) Minecraft.getInstance().level.getGameTime() + partialTick;
            rotating = true;
        }

        float time = (float) Minecraft.getInstance().level.getGameTime() + partialTick - startRotatingTime;

        if(!obeliskBlockEntity.clicked && rotating && time % 360 == 0)
        {
            rotating = false;
        }

        if(rotating)
        {
            stack.mulPose(Axis.YP.rotationDegrees(time));
        }

        RenderType layer = RenderType.entityTranslucent(ObeliskModel.TEXTURE);
        VertexConsumer vex = buffer.getBuffer(layer);

        obeliskModel.render(stack, vex, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        stack.popPose();
    }
}