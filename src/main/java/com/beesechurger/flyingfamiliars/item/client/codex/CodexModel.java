package com.beesechurger.flyingfamiliars.item.client.codex;

// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.joml.Vector3f;

public class CodexModel extends EntityModel
{
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "codex"), "main");
    private final ModelPart body;
    private final ModelPart cover_back;
    private final ModelPart cover_spine_back;
    private final ModelPart cover_spine_front;
    private final ModelPart cover_front;
    private final ModelPart cover_jewels;
    private final ModelPart flap_back;
    private final ModelPart flap_front;
    private final ModelPart flap_clasp_leather;
    private final ModelPart flap_clasp_metal;
    private final ModelPart pages_stack;
    private final ModelPart pages_loose;

    public float codexOpenTime = 0;

    public CodexModel(ModelPart root)
    {
        this.body = root.getChild("body");
        this.cover_back = this.body.getChild("cover_back");
        this.cover_spine_back = this.cover_back.getChild("cover_spine_back");
        this.cover_spine_front = this.cover_spine_back.getChild("cover_spine_front");
        this.cover_front = this.cover_spine_front.getChild("cover_front");
        this.cover_jewels = this.cover_front.getChild("cover_jewels");
        this.flap_back = this.cover_back.getChild("flap_back");
        this.flap_front = this.flap_back.getChild("flap_front");
        this.flap_clasp_leather = this.flap_front.getChild("flap_clasp_leather");
        this.flap_clasp_metal = this.flap_clasp_leather.getChild("flap_clasp_metal");
        this.pages_stack = this.cover_back.getChild("pages_stack");
        this.pages_loose = this.pages_stack.getChild("pages_loose");
    }

    public static LayerDefinition createBodyLayer()
    {
        /*MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(-0.475F, 24.0F, 0.0F));

        PartDefinition cover_back = body.addOrReplaceChild("cover_back", CubeListBuilder.create().texOffs(0, 19).addBox(-14.25F, -1.0F, -8.5F, 15.0F, 2.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.25F, -1.0F, 0.0F, 0.0F, 0.0F, 0.0436F));

        PartDefinition cover_spine_back = cover_back.addOrReplaceChild("cover_spine_back", CubeListBuilder.create().texOffs(64, 0).addBox(-2.0F, -3.0F, -7.5F, 2.0F, 3.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.75F, 0.75F, 0.0F, 0.0F, 0.0F, 0.1309F));

        PartDefinition cover_spine_front = cover_spine_back.addOrReplaceChild("cover_spine_front", CubeListBuilder.create().texOffs(46, 54).addBox(-2.0F, -3.0F, -8.0F, 2.0F, 3.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition cover_front = cover_spine_front.addOrReplaceChild("cover_front", CubeListBuilder.create().texOffs(0, 0).addBox(-14.25F, -1.0F, -8.5F, 15.0F, 2.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.75F, -2.25F, 0.0F, 0.0F, 0.0F, 0.2182F));

        PartDefinition cover_jewels = cover_front.addOrReplaceChild("cover_jewels", CubeListBuilder.create().texOffs(0, 78).mirror().addBox(1.25F, -1.0F, -3.7143F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
            .texOffs(9, 78).mirror().addBox(2.25F, -1.0F, -1.4643F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
            .texOffs(18, 78).mirror().addBox(1.25F, -1.0F, 0.7857F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
            .texOffs(27, 78).mirror().addBox(-1.0F, -1.0F, 1.7857F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
            .texOffs(36, 78).mirror().addBox(-3.25F, -1.0F, 0.7857F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
            .texOffs(45, 78).mirror().addBox(-4.25F, -1.0F, -1.4643F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
            .texOffs(54, 78).mirror().addBox(-3.25F, -1.0F, -3.7143F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.251F, -0.5436F, -0.0357F, 0.0F, -1.5708F, 0.0F));

        PartDefinition flap_back = cover_back.addOrReplaceChild("flap_back", CubeListBuilder.create().texOffs(64, 18).addBox(-0.5F, -2.5F, -7.5F, 1.0F, 3.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, 0.25F, 0.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition flap_front = flap_back.addOrReplaceChild("flap_front", CubeListBuilder.create().texOffs(56, 38).addBox(-0.5F, -3.0F, -6.0F, 1.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.25F, 0.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition flap_clasp_leather = flap_front.addOrReplaceChild("flap_clasp_leather", CubeListBuilder.create().texOffs(0, 66).addBox(-0.25F, -1.75F, -4.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.15F, -2.75F, 0.0F, 0.0F, 0.0F, 1.3963F));

        PartDefinition flap_clasp_metal = flap_clasp_leather.addOrReplaceChild("flap_clasp_metal", CubeListBuilder.create().texOffs(18, 66).addBox(-0.5F, -1.75F, -1.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.375F, -1.25F, -0.5F));

        PartDefinition pages_stack = cover_back.addOrReplaceChild("pages_stack", CubeListBuilder.create().texOffs(0, 84).addBox(-13.5F, -1.5F, -7.0F, 14.0F, 3.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.25F, -2.075F, 0.0F, 0.0F, 0.0F, -0.0436F));

        PartDefinition pages_loose = pages_stack.addOrReplaceChild("pages_loose", CubeListBuilder.create().texOffs(-13, 41).addBox(-7.5F, -0.5F, -6.5F, 15.0F, 0.0F, 13.0F, new CubeDeformation(0.0F))
            .texOffs(-13, 41).addBox(-7.5F, -0.25F, -6.5F, 15.0F, 0.0F, 13.0F, new CubeDeformation(0.0F))
            .texOffs(-13, 41).addBox(-7.5F, 0.0F, -6.5F, 15.0F, 0.0F, 13.0F, new CubeDeformation(0.0F))
            .texOffs(-13, 41).addBox(-7.5F, 0.25F, -6.5F, 15.0F, 0.0F, 13.0F, new CubeDeformation(0.0F))
            .texOffs(-13, 41).addBox(-7.5F, 0.5F, -6.5F, 15.0F, 0.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.5F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);*/

        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(-0.475F, 24.0F, 0.0F));

        PartDefinition cover_back = body.addOrReplaceChild("cover_back", CubeListBuilder.create().texOffs(0, 20).addBox(-14.25F, -1.0F, -8.5F, 15.0F, 2.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.25F, -1.0F, 0.0F, 0.0F, 0.0F, 0.0436F));

        PartDefinition cover_spine_back = cover_back.addOrReplaceChild("cover_spine_back", CubeListBuilder.create().texOffs(0, 69).addBox(-2.0F, -3.0F, -7.5F, 2.0F, 3.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.75F, 0.75F, 0.0F, 0.0F, 0.0F, 0.1309F));

        PartDefinition cover_spine_front = cover_spine_back.addOrReplaceChild("cover_spine_front", CubeListBuilder.create().texOffs(33, 103).addBox(-2.0F, -3.0F, -8.0F, 2.0F, 3.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition cover_front = cover_spine_front.addOrReplaceChild("cover_front", CubeListBuilder.create().texOffs(0, 0).addBox(-14.25F, -1.0F, -8.5F, 15.0F, 2.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.75F, -2.25F, 0.0F, 0.0F, 0.0F, 0.2182F));

        PartDefinition cover_jewels = cover_front.addOrReplaceChild("cover_jewels", CubeListBuilder.create().texOffs(27, 60).addBox(1.25F, -1.0F, -3.7143F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(35, 60).addBox(2.25F, -1.0F, -1.4643F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(43, 60).addBox(1.25F, -1.0F, 0.7857F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(19, 64).addBox(-1.0F, -1.0F, 1.7857F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(27, 64).addBox(-3.25F, -1.0F, 0.7857F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(35, 64).addBox(-4.25F, -1.0F, -1.4643F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
            .texOffs(43, 64).addBox(-3.25F, -1.0F, -3.7143F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.251F, -0.5436F, -0.0357F, 0.0F, -1.5708F, 0.0F));

        PartDefinition flap_back = cover_back.addOrReplaceChild("flap_back", CubeListBuilder.create().texOffs(0, 88).addBox(-0.5F, -2.5F, -7.5F, 1.0F, 3.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, 0.25F, 0.0F, 0.0F, 0.0F, -0.4363F));

        PartDefinition flap_front = flap_back.addOrReplaceChild("flap_front", CubeListBuilder.create().texOffs(0, 107).addBox(-0.5F, -3.0F, -6.0F, 1.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.25F, 0.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition flap_clasp_leather = flap_front.addOrReplaceChild("flap_clasp_leather", CubeListBuilder.create().texOffs(0, 58).addBox(-0.25F, -1.75F, -4.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.15F, -2.75F, 0.0F, 0.0F, 0.0F, 1.3963F));

        PartDefinition flap_clasp_metal = flap_clasp_leather.addOrReplaceChild("flap_clasp_metal", CubeListBuilder.create().texOffs(15, 58).addBox(-0.5F, -1.75F, -1.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.375F, -1.25F, -0.5F));

        PartDefinition pages_stack = cover_back.addOrReplaceChild("pages_stack", CubeListBuilder.create().texOffs(0, 40).addBox(-13.5F, -1.5F, -7.0F, 14.0F, 3.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.25F, -2.075F, 0.0F, 0.0F, 0.0F, -0.0436F));

        PartDefinition pages_loose = pages_stack.addOrReplaceChild("pages_loose", CubeListBuilder.create().texOffs(48, 0).addBox(-21.025F, -0.5F, -18.0F, 28.0F, 0.0F, 24.0F, new CubeDeformation(0.0F))
            .texOffs(48, 26).addBox(-21.025F, -0.25F, -18.0F, 28.0F, 0.0F, 24.0F, new CubeDeformation(0.0F))
            .texOffs(48, 52).addBox(-21.025F, 0.0F, -18.0F, 28.0F, 0.0F, 24.0F, new CubeDeformation(0.0F))
            .texOffs(48, 78).addBox(-21.025F, 0.25F, -18.0F, 28.0F, 0.0F, 24.0F, new CubeDeformation(0.0F))
            .texOffs(48, 104).addBox(-21.025F, 0.5F, -18.0F, 28.0F, 0.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offset(0.75F, 0.0F, 6.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    public float getProgress(float currentTime, float startTime, float endTime)
    {
        return Mth.clamp((currentTime - startTime) / (endTime - startTime), 0, 1);
    }

    public float getInEasing(float currentTime, float startTime, float endTime, float currentValue, float startValueOffset, float endValueOffset)
    {
        return currentValue + Mth.lerp((float) (1 - Math.cos(Math.PI / 2 * getProgress(currentTime, startTime, endTime))), startValueOffset, endValueOffset);
    }

    public float getOutEasing(float currentTime, float startTime, float endTime, float currentValue, float startValueOffset, float endValueOffset)
    {
        return currentValue + Mth.lerp((float) Math.sin(Math.PI / 2 * getProgress(currentTime, startTime, endTime)), startValueOffset, endValueOffset);
    }

    public float getInOutEasing(float currentTime, float startTime, float endTime, float currentValue, float startValueOffset, float endValueOffset)
    {
        return currentValue + Mth.lerp((float) -(Math.cos(Math.PI * getProgress(currentTime, startTime, endTime)) - 1) / 2, startValueOffset, endValueOffset);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float currentTime, float netHeadYaw, float headPitch)
    {
        float timeStep = codexOpenTime;

        var flapBackZRot = flap_back.getInitialPose().zRot;

        var coverBackX = cover_back.getInitialPose().x;
        var coverBackZ = cover_back.getInitialPose().z;
        var coverBackXRot = cover_back.getInitialPose().xRot;
        var coverBackZRot = cover_back.getInitialPose().zRot;

        var coverSpineBackZRot = cover_spine_back.getInitialPose().zRot;

        var pagesStackZRot = pages_stack.getInitialPose().zRot;

        var pagesLooseX = pages_loose.getInitialPose().x - 3.5f;
        var pagesLooseY = pages_loose.getInitialPose().y;
        var pagesLooseZ = pages_loose.getInitialPose().z - 3f;
        var pagesLooseXRot = pages_loose.getInitialPose().xRot;
        var pagesLooseScale = 0.5f;

        if (codexOpenTime > 0)
        {
            // open flap
            flapBackZRot = getInOutEasing(currentTime, timeStep, timeStep + 10, flapBackZRot, 0, (float) Math.toRadians(-130));
            coverBackZRot = getInOutEasing(currentTime, timeStep, timeStep + 10, coverBackZRot, 0, (float) Math.toRadians(-2.5f));
            coverSpineBackZRot = getInOutEasing(currentTime, timeStep, timeStep + 10, coverSpineBackZRot, 0, (float) Math.toRadians(5));
            pagesStackZRot = getInOutEasing(currentTime, timeStep, timeStep + 10, pagesStackZRot, 0, (float) Math.toRadians(2.5f));

            // move pages_loose 1
            timeStep += 5;
            pagesLooseX = getInEasing(currentTime, timeStep, timeStep + 5, pagesLooseX, 0, -12.5f);

            // move pages_loose 2
            timeStep += 5;
            pagesLooseX = getOutEasing(currentTime, timeStep, timeStep + 5, pagesLooseX, 0, -2.5f);
            pagesLooseY = getOutEasing(currentTime, timeStep, timeStep + 5, pagesLooseY, 0, -2.5f);

            // move pages_loose 3
            timeStep += 5;
            pagesLooseX = getInEasing(currentTime, timeStep, timeStep + 5, pagesLooseX, 0, 2.5f);
            pagesLooseY = getInEasing(currentTime, timeStep, timeStep + 5, pagesLooseY, 0, -2.5f);

            // move pages_loose 4
            timeStep += 5;
            pagesLooseX = getOutEasing(currentTime, timeStep, timeStep + 5, pagesLooseX, 0, 12.5f);

            timeStep += 5;
            float coverZMoveAmount = 12f;
            coverBackZ = getInOutEasing(currentTime, timeStep, timeStep + 10, coverBackZ, 0, coverZMoveAmount);
            pagesLooseZ = getInOutEasing(currentTime, timeStep, timeStep + 10, pagesLooseZ, 0, -2f * coverZMoveAmount + 3.5f);

            timeStep += 5;
            float coverXRotAmount = 30f + 2f * (float) Math.sin((currentTime - codexOpenTime) / 10);
            coverBackX = getInOutEasing(currentTime, timeStep, timeStep + 5, coverBackX, 0, 0.5f);
            coverBackXRot = getInOutEasing(currentTime, timeStep, timeStep + 5, coverBackXRot, 0, (float) Math.toRadians(coverXRotAmount));
            pagesLooseX = getInOutEasing(currentTime, timeStep, timeStep + 5, pagesLooseX, 0, 1.5f);
            pagesLooseXRot = getInOutEasing(currentTime, timeStep, timeStep + 5, pagesLooseXRot, 0, (float) Math.toRadians(-coverXRotAmount));
            pagesLooseScale = getInOutEasing(currentTime, timeStep, timeStep + 5, pagesLooseScale, 0, 0.3f);
        }

        this.flap_back.zRot = flapBackZRot;

        this.cover_back.x = coverBackX;
        this.cover_back.z = coverBackZ;
        this.cover_back.xRot = coverBackXRot;
        this.cover_back.zRot = coverBackZRot;

        this.cover_spine_back.zRot = coverSpineBackZRot;

        this.pages_stack.zRot = pagesStackZRot;

        this.pages_loose.x = pagesLooseX;
        this.pages_loose.y = pagesLooseY;
        this.pages_loose.z = pagesLooseZ;
        this.pages_loose.xRot = pagesLooseXRot;
        this.pages_stack.zRot = pagesStackZRot;
        this.pages_loose.xScale = pagesLooseScale;
        this.pages_loose.yScale = pagesLooseScale;
        this.pages_loose.zScale = pagesLooseScale;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}