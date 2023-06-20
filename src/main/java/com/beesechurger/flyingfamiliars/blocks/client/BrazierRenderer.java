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
		for(int i = 0; i < brazierEntity.itemCount(); i++)
		{
			float angle = ((i+1) * 360F / brazierEntity.itemCount());
			
			ItemStack inventory = brazierEntity.getItems().get(i);
			BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(inventory, brazierEntity.getLevel(), (LivingEntity)null, 0);
			float time = Minecraft.getInstance().level.getGameTime() + partialTicks;
			
			stack.pushPose();
			stack.translate(0.5D, 1.2D, 0.5D);
			stack.mulPose(Vector3f.YP.rotationDegrees(angle + time));
			stack.translate(0.6D, 0, 0);
			stack.mulPose(Vector3f.YP.rotationDegrees(time * 2));
			Minecraft.getInstance().getItemRenderer().render(inventory, ItemTransforms.TransformType.GROUND, false, stack, buffer, 255, OverlayTexture.NO_OVERLAY, model);
			stack.popPose();
		}
	}
}
