package com.beesechurger.flyingfamiliars.block.client.obelisk;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class ObeliskModel extends Model
{
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(FlyingFamiliars.MOD_ID, "obelisk"), "main");
    public static final ResourceLocation TEXTURE = new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/block/obelisk/obelisk_pillar.png");
    private final ModelPart body;

    public ObeliskModel(ModelPart root)
    {
        super(RenderType::entitySolid);
        this.body = root.getChild("body");
    }

    public static LayerDefinition createBodyLayer()
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition pillar = body.addOrReplaceChild("pillar", CubeListBuilder.create().texOffs(22, 0).addBox(-2.0F, -30.0F, -2.0F, 4.0F, 30.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition bottom = pillar.addOrReplaceChild("bottom", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition cube_r1 = bottom.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(40, 0).addBox(-2.0F, -6.0F, -4.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.1F, 2.0F, -0.829F, 0.0F, 0.0F));
        PartDefinition cube_r2 = bottom.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(40, 0).addBox(-2.0F, -6.0F, 0.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.1F, -2.0F, 0.829F, 0.0F, 0.0F));
        PartDefinition cube_r3 = bottom.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(40, 0).addBox(-4.0F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -0.1F, 0.0F, 0.0F, 0.0F, 0.829F));
        PartDefinition cube_r4 = bottom.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(40, 0).addBox(0.0F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -0.1F, 0.0F, 0.0F, 0.0F, -0.829F));

        PartDefinition top = pillar.addOrReplaceChild("top", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition cube_r5 = top.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -26.0F, -2.5F, 5.0F, 26.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.55F, -4.0F, 0.0F, 0.0F, 0.0F, -0.1396F));
        PartDefinition cube_r6 = top.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -26.0F, 0.0F, 5.0F, 26.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, -6.55F, -0.1396F, 0.0F, 0.0F));
        PartDefinition cube_r7 = top.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -26.0F, -5.0F, 5.0F, 26.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 6.55F, 0.1396F, 0.0F, 0.0F));
        PartDefinition cube_r8 = top.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -26.0F, -2.5F, 5.0F, 26.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.55F, -4.0F, 0.0F, 0.0F, 0.0F, 0.1396F));

        return LayerDefinition.create(meshdefinition, 128, 128);
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
}