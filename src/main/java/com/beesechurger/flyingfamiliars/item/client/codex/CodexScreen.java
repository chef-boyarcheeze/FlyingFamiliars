package com.beesechurger.flyingfamiliars.item.client.codex;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ForgeRenderTypes;

public class CodexScreen extends Screen
{
    private static final ResourceLocation CODEX_TEXTURE = new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/gui/codex/codex.png");
    private static final int CODEX_SCALE = 150;

    private static CodexModel CODEX_MODEL;

    protected ResourceLocation prevEntryJSON = null;
    protected ResourceLocation currentEntryJSON;
    protected ResourceLocation nextEntryJSON;

    private CodexEntry currentEntry;
    private CodexEntry prevEntry;
    private CodexEntry nextEntry;

    private int tickCount = 0;

    public CodexScreen()
    {
        super(Component.translatable("item.flyingfamiliars.codex"));

        CODEX_MODEL = new CodexModel(Minecraft.getInstance().getEntityModels().bakeLayer(CodexModel.LAYER_LOCATION));
        // set unlocked things progress (Progress will be stored per-item, and while in Creative mode, have all progress)
        // set current entry (where codex starts from)
        currentEntryJSON = new ResourceLocation(FlyingFamiliars.MOD_ID, "codex/root.json");
    }

    // TODO maybe true / configurable? maybe ingame (or in-book) way to change it
    @Override
    public boolean isPauseScreen()
    {
        return false;
    }

    @Override
    public void tick()
    {
        tickCount++;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        this.renderBackground(guiGraphics);

        render3DBinder(guiGraphics, this.width / 2, this.height / 2, partialTick);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private float getScale(float currentTime)
    {
        float scale = Math.min((float) Math.log(currentTime) * 60, CODEX_SCALE);

        if (CODEX_MODEL.codexOpenTime == 0 && scale >= CODEX_SCALE)
        {
            CODEX_MODEL.codexOpenTime = currentTime;
        }

        scale += CODEX_MODEL.codexOpenTime > 0 ? 1.5f * (float) Math.sin((currentTime - CODEX_MODEL.codexOpenTime) / 10) : 0;

        return scale;
    }

    private void render3DBinder(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick)
    {
        float currentTime = tickCount + Minecraft.getInstance().getPartialTick();
        float scale = getScale(currentTime);

        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        //float openGuiAmount = Math.min(ageInTicks, 5F) / 5F;
        //float invOpenGuiAmount = 1F - openGuiAmount;
        //float openBookAmount = prevOpenBookProgress + (openBookProgress - prevOpenBookProgress) * partialTick;
        //float bookScale = 221 - invOpenGuiAmount * 180F;
        //float flip = prevFlipProgress + (flipProgress - prevFlipProgress) * partialTick;
        //float pageAngle = (float) Math.PI * (incrementingPage ? 1F - flip : flip);
        //float pageFlipBump = (float) Math.sin(flip * Math.PI);
        //float pageUp = incrementingPage ? 1F - flip : 0.0F;


        //float mouseLeanX = (mouseX - i) / (float) bookScale;
        //float mouseLeanY = (mouseY - j) / (float) bookScale;
        //this.hoveringPageLeft = mouseLeanX < -MOUSE_LEAN_THRESHOLD && canGoLeft();
        //this.hoveringPageRight = mouseLeanX > MOUSE_LEAN_THRESHOLD && canGoRight();
        poseStack.pushPose();
        //poseStack.translate(i + invOpenGuiAmount * i * 0.5F, j + 6 + 15 * pageFlipBump, 100.0F);

        poseStack.translate(this.width / 2, this.height / 2, 2 * scale);

        poseStack.mulPose(Axis.YP.rotationDegrees(-90));
        poseStack.mulPose(Axis.ZP.rotationDegrees(90));

        //poseStack.mulPose(Axis.XN.rotationDegrees(90));
        //poseStack.mulPose(Axis.YN.rotationDegrees(50 * invOpenGuiAmount));

        poseStack.scale(scale, scale, scale);
        //poseStack.pushPose();
        CODEX_MODEL.setupAnim(null, 0, 0, currentTime, 0, 0);
        //CODEX_MODEL.mouseOver(mouseLeanX, mouseLeanY, ageInTicks, flip, canGoLeft(), canGoRight());

        CODEX_MODEL.renderToBuffer(poseStack, bufferSource.getBuffer(ForgeRenderTypes.getUnlitTranslucent(CODEX_TEXTURE)), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        //renderBookContents(poseStack, mouseX, mouseY, partialTick);
        guiGraphics.flush();
        poseStack.popPose();
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        /*int i = this.width / 2;
        float distFromMiddleX = (float) ((mouseX - i) / (float) 220);

        boolean prev = super.mouseClicked(mouseX, mouseY, button);*/

        return CODEX_MODEL.pageSelection(tickCount + Minecraft.getInstance().getPartialTick(), button, false);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY)
    {
        return CODEX_MODEL.pageSelection(tickCount + Minecraft.getInstance().getPartialTick(), button, true);
    }
}
