package com.beesechurger.flyingfamiliars.blocks.client;

import com.beesechurger.flyingfamiliars.blocks.entity.custom.BrazierBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;


public class BrazierRenderer implements BlockEntityRenderer<BrazierBlockEntity>
{	
	public BrazierRenderer(BlockEntityRendererProvider.Context c)
	{
	}
	
	@SuppressWarnings("resource")
	@Override
	public void render(BrazierBlockEntity brazierEntity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight)
	{
		float time = Minecraft.getInstance().level.getGameTime() + partialTicks;
		
		for(int i = 0; i < brazierEntity.getItemCount(); i++)
		{
			float angle = ((i+1) * 360F / brazierEntity.getItemCount());
			
			ItemStack storedItem = brazierEntity.getItems().get(i);
			BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(storedItem, brazierEntity.getLevel(), (LivingEntity)null, 0);

			double craftingOffset = 0.25 * ((double) brazierEntity.getProgress() / (double) brazierEntity.getMaxProgress());
			double craftingShift = Math.cos((angle + time) / 2) * craftingOffset;
			
			stack.pushPose();
			stack.translate(0.5D, 1.2D, 0.5D);
			stack.mulPose(Vector3f.YP.rotationDegrees(angle + time));
			stack.translate(0.6D, craftingShift, 0);
			stack.mulPose(Vector3f.YP.rotationDegrees(time * 2));
			Minecraft.getInstance().getItemRenderer().render(storedItem, ItemTransforms.TransformType.GROUND, false, stack, buffer, 255, OverlayTexture.NO_OVERLAY, model);
			stack.popPose();
		}
		
		for(int i = 0; i < brazierEntity.getEntityCount(); i++)
		{
			float angle = ((i+1) * 360F / brazierEntity.getEntityCount());
			
			EntityType<?> type = EntityType.byString(brazierEntity.getEntities().get(i)).orElse(null);
			if(type != null)
			{
				Entity storedEntity = type.create(brazierEntity.getLevel());
				
				double craftingOffset = 0.25 * ((double) brazierEntity.getProgress() / (double) brazierEntity.getMaxProgress());
				double craftingShift = Math.cos((angle + time) / 2) * craftingOffset;
				double centerShift = brazierEntity.getEntityCount() == 1 ? 0 : 2.5D;
				
				stack.pushPose();
				stack.translate(0.5D, 1.6D, 0.5D);
				stack.mulPose(Vector3f.YN.rotationDegrees(angle + time / 2));
				stack.scale(0.2f, 0.2f, 0.2f);
				stack.translate(centerShift, craftingShift, 0);
				stack.mulPose(Vector3f.YP.rotationDegrees((float) (30 * Math.sin(time / 30) + 90)));
				Minecraft.getInstance().getEntityRenderDispatcher().render(storedEntity, 0, 0, 0, 0, 0, stack, buffer, 255);
				stack.popPose();
			}			
		}
	}
}
