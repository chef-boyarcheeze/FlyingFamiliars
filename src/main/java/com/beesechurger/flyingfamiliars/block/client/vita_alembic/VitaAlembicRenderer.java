package com.beesechurger.flyingfamiliars.block.client.vita_alembic;

import com.beesechurger.flyingfamiliars.block.entity.VitaAlembicBE;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VitaAlembicRenderer implements BlockEntityRenderer<VitaAlembicBE>
{
    public VitaAlembicRenderer(BlockEntityRendererProvider.Context ctx)
    {}

    public void render(VitaAlembicBE vitaAlembicBE, float partialTick, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay)
    {
    }
}