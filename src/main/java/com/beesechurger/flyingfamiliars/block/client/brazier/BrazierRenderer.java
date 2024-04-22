package com.beesechurger.flyingfamiliars.block.client.brazier;

import com.beesechurger.flyingfamiliars.block.entity.BrazierBE;
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

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.BASE_ENTITY_TAGNAME;

public class BrazierRenderer implements BlockEntityRenderer<BrazierBE>
{	
	public BrazierRenderer(BlockEntityRendererProvider.Context c)
	{
	}
	
	@SuppressWarnings("resource")
	@Override
	public void render(BrazierBE brazierBE, float partialTicks, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight)
	{
		if(Minecraft.getInstance().level != null)
		{
			float time = Minecraft.getInstance().level.getGameTime() + partialTicks;

			for(int i = 0; i < brazierBE.getItemCount(); i++)
			{
				float angle = ((i+1) * 360f / brazierBE.getItemCount());

				ItemStack storedItem = brazierBE.items.get(i);
				BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(storedItem, brazierBE.getLevel(), (LivingEntity)null, 0);

				double craftingOffset = 0.25 * ((double) brazierBE.getProgress() / (double) brazierBE.getMaxProgress());
				double craftingShift = Math.cos((angle + time) / 2) * craftingOffset;

				stack.pushPose();
				stack.translate(0.5d, 1.2d, 0.5d);
				stack.mulPose(Axis.YP.rotationDegrees(angle + time));
				stack.translate(0.6d, craftingShift, 0);
				stack.mulPose(Axis.YP.rotationDegrees(time * 2));
				Minecraft.getInstance().getItemRenderer().render(storedItem, ItemDisplayContext.GROUND, false, stack, buffer, 255, OverlayTexture.NO_OVERLAY, model);
				stack.popPose();
			}

			for(int i = 0; i < brazierBE.getEntityCount(); i++)
			{
				float angle = ((i+1) * 360f / brazierBE.getEntityCount());

				EntityType<?> type = EntityType.byString(brazierBE.getEntitiesStrings().get(i)).orElse(null);
				if(type != null)
				{
					Entity storedEntity = type.create(brazierBE.getLevel());
					storedEntity.load(brazierBE.entities.getList(BASE_ENTITY_TAGNAME, 10).getCompound(i));

					double craftingOffset = 0.25d * ((double) brazierBE.getProgress() / (double) brazierBE.getMaxProgress());
					double craftingShift = Math.cos((angle + time) / 2) * craftingOffset;
					double centerShift = 0.6d * (brazierBE.getEntityCount() - 1);

					stack.pushPose();
					stack.translate(0.5d, 1.6d, 0.5d);
					stack.mulPose(Axis.YN.rotationDegrees(angle + time / 2));
					stack.scale(0.2f, 0.2f, 0.2f);
					stack.translate(centerShift, craftingShift, 0);
					stack.mulPose(Axis.YP.rotationDegrees((float) (30 * Math.sin(time / 30) + 90)));
					Minecraft.getInstance().getEntityRenderDispatcher().render(storedEntity, 0, 0, 0, 0, 0, stack, buffer, 255);
					stack.popPose();
				}
			}
		}
	}
}
