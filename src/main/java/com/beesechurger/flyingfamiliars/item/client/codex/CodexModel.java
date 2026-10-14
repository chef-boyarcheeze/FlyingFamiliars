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
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CodexModel extends EntityModel
{
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "codex"), "main");
    private static Random RANDOM = new Random();

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
    private final ModelPart pages_loose_1;
    private final ModelPart pages_loose_2;
    private final ModelPart pages_loose_3;
    private final ModelPart pages_loose_4;
    private final ModelPart pages_loose_5;

    private List<ModelPart> pageList;
    private PageOperation currentPageOperation = PageOperation.EMPTY;

    public float codexOpenTime = 0;
    public float pageFlipTime = 0;

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
        this.pages_loose_1 = this.pages_loose.getChild("pages_loose_1");
        this.pages_loose_2 = this.pages_loose.getChild("pages_loose_2");
        this.pages_loose_3 = this.pages_loose.getChild("pages_loose_3");
        this.pages_loose_4 = this.pages_loose.getChild("pages_loose_4");
        this.pages_loose_5 = this.pages_loose.getChild("pages_loose_5");

        this.pageList = new ArrayList<ModelPart>(List.of(
            offsetInitialPose(this.pages_loose_1, null, null, null, null, this.pages_loose_1.yRot + (float) Math.toRadians(5 * getRandomEasing(-1, 1)), null, true),
            offsetInitialPose(this.pages_loose_2, null, null, null, null, this.pages_loose_2.yRot + (float) Math.toRadians(5 * getRandomEasing(-1, 1)), null, true),
            offsetInitialPose(this.pages_loose_3, null, null, null, null, this.pages_loose_3.yRot + (float) Math.toRadians(5 * getRandomEasing(-1, 1)), null, true),
            offsetInitialPose(this.pages_loose_4, null, null, null, null, this.pages_loose_4.yRot + (float) Math.toRadians(5 * getRandomEasing(-1, 1)), null, true),
            offsetInitialPose(this.pages_loose_5, null, null, null, null, this.pages_loose_5.yRot + (float) Math.toRadians(5 * getRandomEasing(-1, 1)), null, true)
        ));
    }

    public static LayerDefinition createBodyLayer()
    {
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
        PartDefinition pages_loose = pages_stack.addOrReplaceChild("pages_loose", CubeListBuilder.create(), PartPose.offset(-6.25F, 0.0F, 0.0F));
        PartDefinition pages_loose_1 = pages_loose.addOrReplaceChild("pages_loose_1", CubeListBuilder.create().texOffs(48, 0).addBox(-14.025F, 0.0F, -12.0F, 28.0F, 0.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));
        PartDefinition pages_loose_2 = pages_loose.addOrReplaceChild("pages_loose_2", CubeListBuilder.create().texOffs(48, 26).addBox(-14.025F, 0.0F, -12.0F, 28.0F, 0.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.25F, 0.0F));
        PartDefinition pages_loose_3 = pages_loose.addOrReplaceChild("pages_loose_3", CubeListBuilder.create().texOffs(48, 52).addBox(-14.025F, 0.0F, -12.0F, 28.0F, 0.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition pages_loose_4 = pages_loose.addOrReplaceChild("pages_loose_4", CubeListBuilder.create().texOffs(48, 78).addBox(-14.025F, 0.0F, -12.0F, 28.0F, 0.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.25F, 0.0F));
        PartDefinition pages_loose_5 = pages_loose.addOrReplaceChild("pages_loose_5", CubeListBuilder.create().texOffs(48, 104).addBox(-14.025F, 0.0F, -12.0F, 28.0F, 0.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    private static enum PageOperation
    {
        EMPTY,
        BACK_TO_FRONT,
        FRONT_TO_BACK,
        SHUFFLE
    }

    public boolean pageSelection(float currentTime, int button, boolean isDragging)
    {
        if (isDragging)
        {
            // should be some way to drag the pages as matter-of-course, at any time, as long as its not clicking a link
        }
        else if (pageFlipTime == 0)
        {
            currentPageOperation = switch (button)
            {
                case GLFW.GLFW_MOUSE_BUTTON_LEFT -> PageOperation.BACK_TO_FRONT;
                case GLFW.GLFW_MOUSE_BUTTON_RIGHT -> PageOperation.FRONT_TO_BACK;
                case GLFW.GLFW_MOUSE_BUTTON_MIDDLE -> PageOperation.EMPTY;
                case GLFW.GLFW_MOUSE_BUTTON_4 -> PageOperation.EMPTY; // Back side button
                case GLFW.GLFW_MOUSE_BUTTON_5 -> PageOperation.EMPTY; // Front side button
                default -> PageOperation.EMPTY;
            };

            switch (currentPageOperation)
            {
                case BACK_TO_FRONT ->
                {
                    // extract the last page, update its rotation, insert it in front of the stack
                    var activePage = pageList.remove(pageList.size() - 1);
                    pageList.add(0, offsetInitialPose(activePage, null, null, null, null, activePage.getInitialPose().yRot + (float) Math.toRadians(5 * getRandomEasing(-1, 1)), null, false));
                }
                case FRONT_TO_BACK ->
                {
                    // extract the first page, update its rotation, insert it in back of the stack
                    var activePage = pageList.remove(0);
                    pageList.add(offsetInitialPose(activePage, null, null, null, null, activePage.getInitialPose().yRot + (float) Math.toRadians(5 * getRandomEasing(-1, 1)), null, false));
                }
                default -> {}
            }

            // start animation
            if (currentPageOperation != PageOperation.EMPTY)
            {
                pageFlipTime = currentTime;
            }
        }

        return false;
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float currentTime, float netHeadYaw, float headPitch)
    {
        playOpenCodex(currentTime);

        switch (currentPageOperation)
        {
            case BACK_TO_FRONT -> playBackPageToFront(currentTime);
            case FRONT_TO_BACK -> playFrontPageToBack(currentTime);
            default -> {}
        }
    }

    private void playOpenCodex(float currentTime)
    {
        flap_back.zRot = flap_back.getInitialPose().zRot;

        cover_back.x = cover_back.getInitialPose().x;
        cover_back.z = cover_back.getInitialPose().z;
        cover_back.xRot = cover_back.getInitialPose().xRot;
        cover_back.zRot = cover_back.getInitialPose().zRot;

        cover_spine_back.zRot = cover_spine_back.getInitialPose().zRot;

        pages_stack.zRot = pages_stack.getInitialPose().zRot;

        pages_loose.x = pages_loose.getInitialPose().x - 0.5f;
        pages_loose.y = pages_loose.getInitialPose().y;
        pages_loose.z = pages_loose.getInitialPose().z;
        pages_loose.xRot = pages_loose.getInitialPose().xRot;
        pages_loose.xScale = 0.5f;
        pages_loose.yScale = 0.5f;
        pages_loose.zScale = 0.5f;

        if (codexOpenTime != 0)
        {
            float timeStep = codexOpenTime;

            // open flap
            flap_back.zRot = getInOutEasing(currentTime, timeStep, timeStep + 10, flap_back.zRot, 0, (float) Math.toRadians(-130));
            cover_back.zRot = getInOutEasing(currentTime, timeStep, timeStep + 10, cover_back.zRot, 0, (float) Math.toRadians(-2.5f));
            cover_spine_back.zRot = getInOutEasing(currentTime, timeStep, timeStep + 10, cover_spine_back.zRot, 0, (float) Math.toRadians(5));
            pages_stack.zRot = getInOutEasing(currentTime, timeStep, timeStep + 10, pages_stack.zRot, 0, (float) Math.toRadians(2.5f));

            // move pages_loose 1
            timeStep += 5;
            pages_loose.x = getInEasing(currentTime, timeStep, timeStep + 5, pages_loose.x, 0, -12.5f);

            // move pages_loose 2
            timeStep += 5;
            pages_loose.x = getOutEasing(currentTime, timeStep, timeStep + 5, pages_loose.x, 0, -2.5f);
            pages_loose.y = getOutEasing(currentTime, timeStep, timeStep + 5, pages_loose.y, 0, -2.5f);

            // move pages_loose 3
            timeStep += 5;
            pages_loose.x = getInEasing(currentTime, timeStep, timeStep + 5, pages_loose.x, 0, 2.5f);
            pages_loose.y = getInEasing(currentTime, timeStep, timeStep + 5, pages_loose.y, 0, -2.5f);

            // move pages_loose 4
            timeStep += 5;
            pages_loose.x = getOutEasing(currentTime, timeStep, timeStep + 10, pages_loose.x, 0, 12.5f);

            timeStep += 10;
            float coverZMoveAmount = 12f;
            cover_back.z = getInOutEasing(currentTime, timeStep, timeStep + 10, cover_back.z, 0, coverZMoveAmount);
            pages_loose.z = getInOutEasing(currentTime, timeStep, timeStep + 10, pages_loose.z, 0, -2f * coverZMoveAmount + 0.8f);

            timeStep += 5;
            float coverXMoveAmount = 1.0f;
            float coverXRotAmount = 30f + 2f * (float) Math.sin((currentTime - codexOpenTime) / 10);
            cover_back.x = getInOutEasing(currentTime, timeStep, timeStep + 5, cover_back.x, 0, coverXMoveAmount);
            cover_back.xRot = getInOutEasing(currentTime, timeStep, timeStep + 5, cover_back.xRot, 0, (float) Math.toRadians(coverXRotAmount));
            pages_loose.x = getInOutEasing(currentTime, timeStep, timeStep + 5, pages_loose.x, 0, -coverXMoveAmount / 2f);
            pages_loose.xRot = getInOutEasing(currentTime, timeStep, timeStep + 5, pages_loose.xRot, 0, (float) Math.toRadians(-coverXRotAmount));
            pages_loose.xScale = getInOutEasing(currentTime, timeStep, timeStep + 5, pages_loose.xScale, 0, 0.3f);
            pages_loose.yScale = getInOutEasing(currentTime, timeStep, timeStep + 5, pages_loose.yScale, 0, 0.3f);
            pages_loose.zScale = getInOutEasing(currentTime, timeStep, timeStep + 5, pages_loose.zScale, 0, 0.3f);

            timeStep += 5;
        }
    }

    private void playBackPageToFront(float currentTime)
    {
        var activePage = pageList.get(0);

        activePage.x = activePage.getInitialPose().x;
        activePage.z = activePage.getInitialPose().z;

        for (int i = 0; i < pageList.size(); i++)
        {
            pageList.get(i).y = pageList.get(i).getInitialPose().y;
        }

        if (pageFlipTime != 0)
        {
            float timeStep = pageFlipTime;

            // incrementally step random Y rotation of loose pages
            activePage.yRot = getInOutEasing(currentTime, timeStep, timeStep + 10, activePage.yRot, 0, activePage.getInitialPose().yRot - activePage.yRot);

            // move first (previous last) pages_loose_# 1
            pages_loose.xScale = getInEasing(currentTime, timeStep, timeStep + 5, pages_loose.xScale, 0, -0.2f);
            pages_loose.yScale = getInEasing(currentTime, timeStep, timeStep + 5, pages_loose.yScale, 0, -0.2f);
            pages_loose.zScale = getInEasing(currentTime, timeStep, timeStep + 5, pages_loose.zScale, 0, -0.2f);
            activePage.x = getInEasing(currentTime, timeStep, timeStep + 7.5f, activePage.x, 0, (float) Math.cos(activePage.yRot) * -30f);
            activePage.z = getInEasing(currentTime, timeStep, timeStep + 7.5f, activePage.z, 0, (float) Math.sin(activePage.yRot) * 30f);
            timeStep += 5;

            activePage.x = getOutEasing(currentTime, timeStep, timeStep + 5, activePage.x, 0, (float) Math.cos(activePage.yRot) * -5f);
            activePage.y = getOutEasing(currentTime, timeStep, timeStep + 5, activePage.y, 0, -0.5f);
            activePage.z = getOutEasing(currentTime, timeStep, timeStep + 5, activePage.z, 0, (float) Math.sin(activePage.yRot) * 5f);
            timeStep += 5;

            for (int i = 1; i < pageList.size(); i++)
            {
                pageList.get(i).y = getInOutEasing(currentTime, timeStep, timeStep + 5, pageList.get(i).y, 0, 0.25f);
            }

            activePage.x = getInEasing(currentTime, timeStep, timeStep + 5, activePage.x, 0, (float) Math.cos(activePage.yRot) * 5f);
            activePage.y = getInEasing(currentTime, timeStep, timeStep + 5, activePage.y, 0, -0.5f);
            activePage.z = getInEasing(currentTime, timeStep, timeStep + 5, activePage.z, 0, (float) Math.sin(activePage.yRot) * -5f);
            timeStep += 5;

            activePage.x = getOutEasing(currentTime, timeStep, timeStep + 7.5f, activePage.x, 0, (float) Math.cos(activePage.yRot) * 30f);
            activePage.z = getOutEasing(currentTime, timeStep, timeStep + 7.5f, activePage.z, 0, (float) Math.sin(activePage.yRot) * -30f);
            timeStep += 5;

            pages_loose.xScale = getOutEasing(currentTime, timeStep, timeStep + 5, pages_loose.xScale, 0, 0.2f);
            pages_loose.yScale = getOutEasing(currentTime, timeStep, timeStep + 5, pages_loose.yScale, 0, 0.2f);
            pages_loose.zScale = getOutEasing(currentTime, timeStep, timeStep + 5, pages_loose.zScale, 0, 0.2f);
            timeStep += 5;

            // complete animation
            if (getProgress(currentTime, pageFlipTime, timeStep) >= 1)
            {
                // update Y positions of pages
                for (int i = 0; i < pageList.size(); i++)
                {
                    offsetInitialPose(pageList.get(i), null, pageList.get(i).y, null, null, null, null, true);
                }

                currentPageOperation = PageOperation.EMPTY;
                pageFlipTime = 0;
            }
        }
    }

    private void playFrontPageToBack(float currentTime)
    {
        var activePage = pageList.get(pageList.size() - 1);

        activePage.x = activePage.getInitialPose().x;
        activePage.z = activePage.getInitialPose().z;

        for (int i = 0; i < pageList.size(); i++)
        {
            pageList.get(i).y = pageList.get(i).getInitialPose().y;
        }

        if (pageFlipTime != 0)
        {
            float timeStep = pageFlipTime;

            // incrementally step random Y rotation of loose pages
            activePage.yRot = getInOutEasing(currentTime, timeStep, timeStep + 10, activePage.yRot, 0, activePage.getInitialPose().yRot - activePage.yRot);

            // move first (previous last) pages_loose_# 1
            pages_loose.xScale = getInEasing(currentTime, timeStep, timeStep + 5, pages_loose.xScale, 0, -0.2f);
            pages_loose.yScale = getInEasing(currentTime, timeStep, timeStep + 5, pages_loose.yScale, 0, -0.2f);
            pages_loose.zScale = getInEasing(currentTime, timeStep, timeStep + 5, pages_loose.zScale, 0, -0.2f);
            activePage.x = getInEasing(currentTime, timeStep, timeStep + 7.5f, activePage.x, 0, (float) Math.cos(activePage.yRot) * -30f);
            activePage.z = getInEasing(currentTime, timeStep, timeStep + 7.5f, activePage.z, 0, (float) Math.sin(activePage.yRot) * 30f);
            timeStep += 5;

            activePage.x = getOutEasing(currentTime, timeStep, timeStep + 5, activePage.x, 0, (float) Math.cos(activePage.yRot) * -5f);
            activePage.y = getOutEasing(currentTime, timeStep, timeStep + 5, activePage.y, 0, 0.5f);
            activePage.z = getOutEasing(currentTime, timeStep, timeStep + 5, activePage.z, 0, (float) Math.sin(activePage.yRot) * 5f);
            timeStep += 5;

            for (int i = 0; i < pageList.size() - 1; i++)
            {
                pageList.get(i).y = getInOutEasing(currentTime, timeStep, timeStep + 5, pageList.get(i).y, 0, -0.25f);
            }

            activePage.x = getInEasing(currentTime, timeStep, timeStep + 5, activePage.x, 0, (float) Math.cos(activePage.yRot) * 5f);
            activePage.y = getInEasing(currentTime, timeStep, timeStep + 5, activePage.y, 0, 0.5f);
            activePage.z = getInEasing(currentTime, timeStep, timeStep + 5, activePage.z, 0, (float) Math.sin(activePage.yRot) * -5f);
            timeStep += 5;

            activePage.x = getOutEasing(currentTime, timeStep, timeStep + 7.5f, activePage.x, 0, (float) Math.cos(activePage.yRot) * 30f);
            activePage.z = getOutEasing(currentTime, timeStep, timeStep + 7.5f, activePage.z, 0, (float) Math.sin(activePage.yRot) * -30f);
            timeStep += 5;

            pages_loose.xScale = getOutEasing(currentTime, timeStep, timeStep + 5, pages_loose.xScale, 0, 0.2f);
            pages_loose.yScale = getOutEasing(currentTime, timeStep, timeStep + 5, pages_loose.yScale, 0, 0.2f);
            pages_loose.zScale = getOutEasing(currentTime, timeStep, timeStep + 5, pages_loose.zScale, 0, 0.2f);
            timeStep += 5;

            // complete animation
            if (getProgress(currentTime, pageFlipTime, timeStep) >= 1)
            {
                // update Y positions of pages
                for (int i = 0; i < pageList.size(); i++)
                {
                    offsetInitialPose(pageList.get(i), null, pageList.get(i).y, null, null, null, null, true);
                }

                currentPageOperation = PageOperation.EMPTY;
                pageFlipTime = 0;
            }
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

///////////////////////////////////////

    public ModelPart offsetInitialPose(ModelPart part, Float x, Float y, Float z, Float xRot, Float yRot, Float zRot, boolean shouldReset)
    {
        part.setInitialPose(PartPose.offsetAndRotation(
            x == null ? part.getInitialPose().x : x,
            y == null ? part.getInitialPose().y : y,
            z == null ? part.getInitialPose().z : z,
            xRot == null ? part.getInitialPose().xRot : xRot,
            yRot == null ? part.getInitialPose().yRot : yRot,
            zRot == null ? part.getInitialPose().zRot : zRot
        ));

        if (shouldReset)
        {
            part.resetPose();
        }

        return part;
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

    public float getRandomEasing(float min, float max)
    {
        return ((max - min) * RANDOM.nextFloat()) + min;
    }
}