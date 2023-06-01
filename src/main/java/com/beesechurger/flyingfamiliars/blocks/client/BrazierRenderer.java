package com.beesechurger.flyingfamiliars.blocks.client;

import com.beesechurger.flyingfamiliars.blocks.entity.custom.BrazierBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BrazierRenderer implements BlockEntityRenderer<BrazierBlockEntity>
{		
	public BrazierRenderer(BlockEntityRendererProvider.Context c)
	{
	}
	
	public void render(BrazierBlockEntity brazierEntity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int combinedOverlay, int packedLight)
	{
		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		ItemStack inventory = brazierEntity.getItems().get(0);
		
		if(inventory != ItemStack.EMPTY)
		{
			int i = (int)brazierEntity.getBlockPos().asLong();
			
			stack.pushPose();
			itemRenderer.renderStatic(new ItemStack(Items.ACACIA_LOG), TransformType.FIXED, combinedOverlay, packedLight, stack, buffer, i);
			//itemRenderer.render(inventory, TransformType.FIXED, false, stack, buffer, combinedOverlay, packedLight, null);
			stack.popPose();
			
			/*stack.pushPose();
			stack.translate(0.5D, 0.44921875D, 0.5D);
            Direction direction1 = Direction.from2DDataValue((j + direction.get2DDataValue()) % 4);
            float f = -direction1.toYRot();
            stack.mulPose(Vector3f.YP.rotationDegrees(f));
            stack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            stack.translate(-0.3125D, -0.3125D, 0.0D);
            stack.scale(0.375F, 0.375F, 0.375F);
            itemRenderer.renderStatic(itemstack, ItemTransforms.TransformType.FIXED, p_112348_, p_112349_, p_112346_, p_112347_, i + j);
            stack.popPose();*/
		}
		
	}
}
