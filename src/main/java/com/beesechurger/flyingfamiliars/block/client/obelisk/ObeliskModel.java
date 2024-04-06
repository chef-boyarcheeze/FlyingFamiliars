package com.beesechurger.flyingfamiliars.block.client.obelisk;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class ObeliskModel extends Model
{
    public ObeliskModel(Function<ResourceLocation, RenderType> function)
    {
        super(function);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int i1, float v, float v1, float v2, float v3)
    {

    }
}
