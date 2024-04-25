package com.beesechurger.flyingfamiliars.block.client.ceremonial_font;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.block.entity.CeremonialFontBE;
import com.beesechurger.flyingfamiliars.block.entity.ObeliskBE;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class CeremonialFontCupModel extends Model
{
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(FlyingFamiliars.MOD_ID, "ceremonial_font_cup"), "main");
	private final ModelPart body;

	public CeremonialFontCupModel(ModelPart root)
	{
		super(RenderType::entitySolid);
		this.body = root.getChild("body");
	}

	public static LayerDefinition createBodyLayer()
	{
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition base = body.addOrReplaceChild("base", CubeListBuilder.create().texOffs(32, 0).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition cube_r1 = base.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(14, 7).addBox(-6.0F, -2.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));
		PartDefinition cube_r2 = base.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(14, 20).addBox(-3.0F, -2.0F, -6.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, -0.7854F, 0.0F, 0.0F));
		PartDefinition cube_r3 = base.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 28).addBox(-3.0F, -2.0F, 0.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 3.0F, 0.7854F, 0.0F, 0.0F));
		PartDefinition cube_r4 = base.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(24, 28).addBox(0.0F, -2.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition diagonal_base = base.addOrReplaceChild("diagonal_base", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
		PartDefinition cube_r5 = diagonal_base.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(13, 15).addBox(-1.5F, -0.575F, -0.225F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, -6.0F, 0.6109F, 0.0F, 0.0F));
		PartDefinition cube_r6 = diagonal_base.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(13, 0).addBox(-0.775F, -0.575F, -1.5F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.6109F));
		PartDefinition cube_r7 = diagonal_base.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(21, 15).addBox(-1.5F, -0.575F, -0.775F, 3.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 6.0F, -0.6109F, 0.0F, 0.0F));
		PartDefinition cube_r8 = diagonal_base.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(21, 0).addBox(-0.225F, -0.575F, -1.5F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.6109F));

		PartDefinition lower_wall = body.addOrReplaceChild("lower_wall", CubeListBuilder.create().texOffs(0, 13).addBox(-3.0F, -5.0F, -0.5F, 3.0F, 6.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-15.0F, -5.0F, -0.5F, 3.0F, 6.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(38, 36).addBox(-11.0F, -5.0F, -4.5F, 7.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(18, 36).addBox(-11.0F, -5.0F, 7.5F, 7.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(7.5F, -4.0F, -3.0F));

		PartDefinition cube_r9 = lower_wall.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(42, 21).addBox(-6.25F, -5.001F, 0.0F, 6.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
		PartDefinition cube_r10 = lower_wall.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(32, 9).addBox(0.0F, -5.001F, 0.25F, 3.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.5F, 0.0F, -4.5F, 0.0F, -0.7854F, 0.0F));
		PartDefinition cube_r11 = lower_wall.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(15, 45).addBox(0.25F, -5.001F, -3.0F, 6.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.0F, 0.0F, 6.0F, 0.0F, -0.7854F, 0.0F));
		PartDefinition cube_r12 = lower_wall.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 36).addBox(-3.0F, -5.001F, -6.25F, 3.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.5F, 0.0F, 10.5F, 0.0F, -0.7854F, 0.0F));

		PartDefinition ornaments = body.addOrReplaceChild("ornaments", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition blue = ornaments.addOrReplaceChild("blue", CubeListBuilder.create().texOffs(49, 45).addBox(2.25F, -8.0F, -8.25F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(48, 30).addBox(-4.25F, -8.0F, -8.25F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition red = ornaments.addOrReplaceChild("red", CubeListBuilder.create().texOffs(0, 48).addBox(6.25F, -8.0F, 2.25F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(41, 45).addBox(6.25F, -8.0F, -4.25F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition yellow = ornaments.addOrReplaceChild("yellow", CubeListBuilder.create().texOffs(33, 45).addBox(2.25F, -1.0F, 6.25F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(44, 8).addBox(-4.25F, -1.0F, 6.25F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));
		PartDefinition green = ornaments.addOrReplaceChild("green", CubeListBuilder.create().texOffs(29, 0).addBox(-0.75F, -1.0F, 2.25F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(18, 28).addBox(-0.75F, -1.0F, -4.25F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.5F, -7.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	public void render(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public static ResourceLocation getTexture(CeremonialFontBE ceremonialFontBE)
	{
		return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/block/ceremonial_font/ceremonial_font_cup.png");
	}
}