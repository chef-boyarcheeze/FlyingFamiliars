package com.beesechurger.flyingfamiliars.block.client.vita_alembic;

import com.beesechurger.flyingfamiliars.block.entity.VitaAlembicBE;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class VitaAlembicRenderer implements BlockEntityRenderer<VitaAlembicBE>
{
    public VitaAlembicRenderer(BlockEntityRendererProvider.Context ctx)
    {
    }

    @SuppressWarnings("resource")
    @Override
    public void render(VitaAlembicBE vitaAlembicBE, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay)
    {
        if(Minecraft.getInstance().level != null)
        {
            float time = Minecraft.getInstance().level.getGameTime() + partialTicks;

            for(int i = 0; i < vitaAlembicBE.entities.getEntryCount(vitaAlembicBE.entityStorageTag); i++)
            {
                EntityType<?> type = EntityType.byString(vitaAlembicBE.getEntitiesStrings().get(i)).orElse(null);
                if(type != null)
                {
                    Entity storedEntity = type.create(vitaAlembicBE.getLevel());
                    storedEntity.load(vitaAlembicBE.entities.getEntryList(vitaAlembicBE.entityStorageTag).getCompound(i));

                    stack.pushPose();
                    stack.translate(0.5d, 1.6d, 0.5d); //.6875
                    stack.mulPose(Axis.YN.rotationDegrees(time / 2));
                    stack.scale(0.2f, 0.2f, 0.2f);
                    stack.mulPose(Axis.YP.rotationDegrees((float) (30 * Math.sin(time / 30) + 90)));
                    Minecraft.getInstance().getEntityRenderDispatcher().render(storedEntity, 0, 0, 0, 0, 0, stack, buffer, 255);
                    stack.popPose();
                }
            }
        }
    }
}