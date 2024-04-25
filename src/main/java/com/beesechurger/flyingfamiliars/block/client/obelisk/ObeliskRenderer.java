package com.beesechurger.flyingfamiliars.block.client.obelisk;

import com.beesechurger.flyingfamiliars.block.entity.ObeliskBE;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ObeliskRenderer implements BlockEntityRenderer<ObeliskBE>
{
    private final ObeliskPillarModel obeliskPillarModel;

    private float startRotatingTime;
    private boolean rotating = false;

    public ObeliskRenderer(BlockEntityRendererProvider.Context ctx)
    {
        this.obeliskPillarModel = new ObeliskPillarModel(ctx.bakeLayer(ObeliskPillarModel.LAYER_LOCATION));
    }

    public void render(ObeliskBE obeliskBE, float partialTick, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay)
    {
        stack.pushPose();
        stack.translate(0.5f, 1.8f, 0.5f);
        stack.mulPose(Axis.ZP.rotationDegrees(180));

        if(obeliskBE.clicked && !rotating)
        {
            startRotatingTime = (float) Minecraft.getInstance().level.getGameTime() + partialTick;
            rotating = true;
        }

        float time = (float) Minecraft.getInstance().level.getGameTime() + partialTick - startRotatingTime;

        if(!obeliskBE.clicked && rotating && time % 360 == 0)
        {
            rotating = false;
        }

        if(rotating)
        {
            stack.mulPose(Axis.YP.rotationDegrees(time));
        }

        RenderType layer = RenderType.entityTranslucent(ObeliskPillarModel.getTexture(obeliskBE));
        VertexConsumer vex = buffer.getBuffer(layer);

        obeliskPillarModel.render(stack, vex, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        stack.popPose();
    }
}