package com.beesechurger.flyingfamiliars.block.client.ceremonial_font;

import com.beesechurger.flyingfamiliars.block.client.obelisk.ObeliskPillarModel;
import com.beesechurger.flyingfamiliars.block.entity.CeremonialFontBE;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.BASE_ENTITY_TAGNAME;

public class CeremonialFontRenderer implements BlockEntityRenderer<CeremonialFontBE>
{
    private final CeremonialFontCupModel ceremonialFontCupModel;

    public CeremonialFontRenderer(BlockEntityRendererProvider.Context ctx)
    {
        this.ceremonialFontCupModel = new CeremonialFontCupModel(ctx.bakeLayer(CeremonialFontCupModel.LAYER_LOCATION));
    }

    @Override
    public void render(CeremonialFontBE ceremonialFontBE, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay)
    {
        if(Minecraft.getInstance().level != null)
        {
            float time = Minecraft.getInstance().level.getGameTime() + partialTicks;

            stack.pushPose();
            stack.translate(0.5f, 2.2f, 0.5f);
            stack.mulPose(Axis.ZP.rotationDegrees(180));

            stack.translate(0, 0.1f * Math.sin(time / 10), 0);
            stack.mulPose(Axis.YN.rotationDegrees(time * 2));

            RenderType layer = RenderType.entityTranslucent(CeremonialFontCupModel.getTexture(ceremonialFontBE));
            VertexConsumer vex = buffer.getBuffer(layer);

            ceremonialFontCupModel.render(stack, vex, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
            stack.popPose();

            for(int i = 0; i < ceremonialFontBE.getItemCount(); i++)
            {
                float angle = ((i+1) * 360f / ceremonialFontBE.getItemCount());

                ItemStack storedItem = ceremonialFontBE.items.get(i);
                BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(storedItem, ceremonialFontBE.getLevel(), (LivingEntity)null, 0);

                //double craftingOffset = 0.25 * ((double) ceremonialFontBE.getProgress() / (double) ceremonialFontBE.getMaxProgress());
                //double craftingShift = Math.cos((angle + time) / 2) * craftingShift;

                stack.pushPose();
                stack.translate(0.5f, 1.1f, 0.5f);
                stack.mulPose(Axis.YP.rotationDegrees(angle + time * 2));
                stack.translate(0.6f, -0.1f * Math.sin(time / 10), 0);
                stack.mulPose(Axis.YP.rotationDegrees(time * 2));
                stack.scale(0.75f, 0.75f, 0.75f);
                Minecraft.getInstance().getItemRenderer().render(storedItem, ItemDisplayContext.GROUND, false, stack, buffer, 255, OverlayTexture.NO_OVERLAY, model);
                stack.popPose();
            }

            for(int i = 0; i < ceremonialFontBE.getEntityCount(); i++)
            {
                float angle = ((i+1) * 360f / ceremonialFontBE.getEntityCount());

                EntityType<?> type = EntityType.byString(ceremonialFontBE.getEntitiesStrings().get(i)).orElse(null);
                if(type != null)
                {
                    Entity storedEntity = type.create(ceremonialFontBE.getLevel());
                    storedEntity.load(ceremonialFontBE.entities.getList(BASE_ENTITY_TAGNAME, 10).getCompound(i));

                    //double craftingOffset = 0.25d * ((double) ceremonialFontBE.getProgress() / (double) ceremonialFontBE.getMaxProgress());
                    //double craftingShift = Math.cos((angle + time) / 2) * craftingOffset;
                    double centerShift = 0.6d * (ceremonialFontBE.getEntityCount() - 1);

                    stack.pushPose();
                    stack.translate(0.5d, 1.4d, 0.5d);
                    stack.mulPose(Axis.YN.rotationDegrees(angle + time / 2));
                    stack.translate(0, -0.1f * Math.sin(time / 10), 0);
                    stack.scale(0.2f, 0.2f, 0.2f);
                    //stack.translate(centerShift, craftingShift, 0);
                    stack.mulPose(Axis.YP.rotationDegrees((float) (30 * Math.sin(time / 30) + 90)));
                    Minecraft.getInstance().getEntityRenderDispatcher().render(storedEntity, 0, 0, 0, 0, 0, stack, buffer, 255);
                    stack.popPose();
                }
            }
        }
    }
}
