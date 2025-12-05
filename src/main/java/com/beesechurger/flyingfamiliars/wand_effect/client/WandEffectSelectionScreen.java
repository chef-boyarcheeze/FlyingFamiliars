package com.beesechurger.flyingfamiliars.wand_effect.client;

import com.beesechurger.flyingfamiliars.item.common.entity_items.SoulWand.BaseSoulWand;
import com.beesechurger.flyingfamiliars.tags.WandEffectTagRef;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.joml.Vector4f;

public class WandEffectSelectionScreen implements IGuiOverlay
{
    public final static WandEffectSelectionScreen INSTANCE = new WandEffectSelectionScreen();

    private Boolean active = false;
    private int mouseSelection = -1;
    private ItemStack stack = null;
    private ListTag wandEffectList = new ListTag();

    private final Vector4f lineColor = new Vector4f(1f, 0.85f, 0.7f, 1f);
    private final Vector4f radialButtonColor = new Vector4f(.04f, .03f, .01f, .6f);
    private final Vector4f highlightColor = new Vector4f(.8f, .7f, .55f, .7f);

    private final double innerBoundary = 20;
    private double outerBoundary = 80;
    private double outerBoundaryMin = 65;
    private double outerBoundaryMax = 80;

    public void open(ItemStack incomingStack)
    {
        if (incomingStack.getItem() instanceof BaseSoulWand item)
        {
            active = true;
            mouseSelection = -1;
            stack = incomingStack;
            wandEffectList = WandEffectTagRef.INSTANCE.getEntryList(stack.getOrCreateTag());

            Minecraft.getInstance().mouseHandler.releaseMouse();

            System.out.println("open");
        }
    }

    public void close()
    {
        active = false;

        if (mouseSelection >= 0)
        {
            // select wand effect in item
        }

        stack = null;
        wandEffectList = new ListTag();

        Minecraft.getInstance().mouseHandler.grabMouse();

        System.out.println("close");
    }

    public Boolean isActive()
    {
        return active;
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight)
    {
        System.out.println("not yety rendereing");
        if (!active)
            return;

        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null || mc.screen != null || mc.mouseHandler.isMouseGrabbed() || wandEffectList.size() <= 0)
        {
            close();
            return;
        }

        System.out.println("rendereing");

        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();

        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;

        Vec2 screenCenter = new Vec2(mc.getWindow().getScreenWidth() * .5f, mc.getWindow().getScreenHeight() * .5f);
        Vec2 mousePos = new Vec2((float) mc.mouseHandler.xpos(), (float) mc.mouseHandler.ypos());

        double x1 = mc.mouseHandler.xpos(), x2 = mc.getWindow().getScreenWidth() * 0.5f;
        double y1 = mc.mouseHandler.ypos(), y2 = mc.getWindow().getScreenHeight() * 0.5f;

        double radiansPerWandEffect = Math.toRadians(360 / (float) wandEffectList.size());
        float mouseRotation = (getMouseAngle(x1, x2, y1, y2) + 1.570f + (float) radiansPerWandEffect * .5f) % 6.283f;

        mouseSelection = (int) Mth.clamp(mouseRotation / radiansPerWandEffect, 0, wandEffectList.size() - 1);
        if (Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2)) < outerBoundaryMin * outerBoundaryMin)
        {
            // reset mouse selection to currently selected wand effect
            mouseSelection = 0;
        }

        graphics.fill(0, 0, screenWidth, screenHeight, 0);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        final Tesselator tesselator = Tesselator.getInstance();
        final BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        drawRadialBackgrounds(buffer, centerX, centerY);
        drawDividingLines(buffer, centerX, centerY);

        tesselator.end();
        RenderSystem.disableBlend();

        /*//Text background
        var selectedSpell = swsm.getSpellData(mouseSelection);
        var spellLevel = selectedSpell.getSpell().getLevelFor(selectedSpell.getLevel(), player);
        var font = gui.getFont();
        var info = selectedSpell.getSpell().getUniqueInfo(spellLevel, minecraft.player);
        int textHeight = Math.max(2, info.size()) * font.lineHeight + 5;
        int textCenterMargin = 5;
        int textTitleMargin = 5;
        var title = selectedSpell.getSpell().getDisplayName(minecraft.player).withStyle(Style.EMPTY.withUnderlined(true));
        var level = Component.translatable("ui.irons_spellbooks.level", TooltipsUtils.getLevelComponenet(selectedSpell, player).withStyle(selectedSpell.getSpell().getRarity(spellLevel).getDisplayName().getStyle()));
        var mana = Component.translatable("ui.irons_spellbooks.mana_cost", selectedSpell.getSpell().getManaCost(spellLevel)).withStyle(ChatFormatting.AQUA);
//            selectedSpell.getUniqueInfo(minecraft.player).forEach((line) -> lines.add(line.withStyle(ChatFormatting.DARK_GREEN)));

        drawTextBackground(guiHelper, centerX, centerY, outerBoundary + textHeight - textTitleMargin - font.lineHeight, textCenterMargin, Math.max(2, info.size()) * font.lineHeight);
        guiHelper.drawString(font, title, (int) (centerX - font.width(title) / 2), (int) (centerY - (outerBoundary + textHeight)), 0xFFFFFF, true);
        guiHelper.drawString(font, level, (int) (centerX - font.width(level) - textCenterMargin), (int) (centerY - (outerBoundary + textHeight) + font.lineHeight + textTitleMargin), 0xFFFFFF, true);
        guiHelper.drawString(font, mana, (int) (centerX - font.width(mana) - textCenterMargin), (int) (centerY - (outerBoundary + textHeight) + font.lineHeight * 2 + textTitleMargin), 0xFFFFFF, true);

        for (int i = 0; i < info.size(); i++)
        {
            var line = info.get(i);
            guiHelper.drawString(font, line, (int) (centerX + textCenterMargin), (int) (centerY - (outerBoundaryMax + textHeight) + font.lineHeight * (i + 1) + textTitleMargin), 0x3be33b, true);
        }

        //Spell Icons
        float scale = Mth.lerp(totalSpellsAvailable / 15f, 2, 1.25f) * .65f;
        double radius = 3 / scale * (innerBoundary + innerBoundary) * .5 * (.85f + .25f * (totalSpellsAvailable / 15f));
        Vec2[] locations = new Vec2[totalSpellsAvailable];
        for (int i = 0; i < locations.length; i++)
        {
            locations[i] = new Vec2((float) (Math.sin(radiansPerWandEffect * i) * radius), (float) (-Math.cos(radiansPerWandEffect * i) * radius));
        }
        for (int i = 0; i < locations.length; i++)
        {
            var spell = swsm.getSpellData(i);
            if (spell != null)
            {
                var texture = spell.getSpell().getSpellIconResource();
                poseStack.pushPose();
                poseStack.translate(centerX, centerY, 0);
                poseStack.scale(scale, scale, scale);

                //Icon
                int iconWidth = 16 / 2;
                int borderWidth = 32 / 2;
                int cdWidth = 16 / 2;
                //blit(poseStack, centerX + (int) locations[i].x + 3, centerY + (int) locations[i].y + 3, 0, 0, 16, 16, 16, 16);
                graphics.blit(texture, (int) locations[i].x - iconWidth, (int) locations[i].y - iconWidth, 0, 0, 16, 16, 16, 16);
            *//*
            Border
             *//*
                graphics.blit(TEXTURE, (int) locations[i].x - borderWidth, (int) locations[i].y - borderWidth, swsm.getSelectionIndex() == i ? 32 : 0, 106, 32, 32);
            *//*
            Cooldown
             *//*
                float f = ClientMagicData.getCooldownPercent(spell.getSpell());
                if (f > 0)
                {
                    RenderSystem.enableBlend();
                    int pixels = (int) (16 * f + 1f);
//                    gui.blit(poseStack, centerX + (int) locations[i].x + 3, centerY + (int) locations[i].y + 19 - pixels, 47, 87, 16, pixels);
                    graphics.blit(TEXTURE, (int) locations[i].x - cdWidth, (int) locations[i].y + cdWidth - pixels, 47, 87, 16, pixels);
                }
                poseStack.popPose();
            }
        }*/

        poseStack.popPose();
    }

    private void drawDividingLines(BufferBuilder buffer, double centerX, double centerY)
    {
        if (wandEffectList.size() <= 1)
            return;

        double radiansPerSpell = 2 * Math.PI / wandEffectList.size();
        outerBoundary = Math.max(outerBoundaryMin, outerBoundaryMax);

        for (int i = 0; i < wandEffectList.size(); i++)
        {
            final double closeWidth = 8 * Mth.DEG_TO_RAD;
            final double farWidth = closeWidth / 4;
            final double beginCloseRadians = i * radiansPerSpell - (Math.PI / 2 + (radiansPerSpell / 2)) - (closeWidth / 4);
            final double endCloseRadians = beginCloseRadians + closeWidth;
            final double beginFarRadians = i * radiansPerSpell - (Math.PI / 2 + (radiansPerSpell / 2)) - (farWidth / 4);
            final double endFarRadians = beginCloseRadians + farWidth;

            final double x1m1 = Math.cos(beginCloseRadians) * innerBoundary;
            final double x2m1 = Math.cos(endCloseRadians) * innerBoundary;
            final double y1m1 = Math.sin(beginCloseRadians) * innerBoundary;
            final double y2m1 = Math.sin(endCloseRadians) * innerBoundary;

            final double x1m2 = Math.cos(beginFarRadians) * outerBoundary * 1.4;
            final double x2m2 = Math.cos(endFarRadians) * outerBoundary * 1.4;
            final double y1m2 = Math.sin(beginFarRadians) * outerBoundary * 1.4;
            final double y2m2 = Math.sin(endFarRadians) * outerBoundary * 1.4;

            Vector4f color = lineColor;
            buffer.vertex(centerX + x1m1, centerY + y1m1, 0).color(color.x(), color.y(), color.z(), color.w()).endVertex();
            buffer.vertex(centerX + x2m1, centerY + y2m1, 0).color(color.x(), color.y(), color.z(), color.w()).endVertex();
            buffer.vertex(centerX + x2m2, centerY + y2m2, 0).color(color.x(), color.y(), color.z(), 0).endVertex();
            buffer.vertex(centerX + x1m2, centerY + y1m2, 0).color(color.x(), color.y(), color.z(), 0).endVertex();
        }
    }

    private void drawRadialBackgrounds(BufferBuilder buffer, double centerX, double centerY)
    {
        int segments = wandEffectList.size() < 6 ?
                (wandEffectList.size() % 2 == 1 ? 15 : 12) :
                (wandEffectList.size() * 2);

        double radiansPerObject = 2 * Math.PI / segments;
        double radiansPerSpell = 2 * Math.PI / wandEffectList.size();

        outerBoundary = Math.max(outerBoundaryMin, outerBoundaryMax);
        for (int i = 0; i < segments; i++)
        {
            final double beginRadians = i * radiansPerObject - (Math.PI / 2 + (radiansPerSpell / 2));
            final double endRadians = (i + 1) * radiansPerObject - (Math.PI / 2 + (radiansPerSpell / 2));

            final double x1m1 = Math.cos(beginRadians) * innerBoundary;
            final double x2m1 = Math.cos(endRadians) * innerBoundary;
            final double y1m1 = Math.sin(beginRadians) * innerBoundary;
            final double y2m1 = Math.sin(endRadians) * innerBoundary;

            final double x1m2 = Math.cos(beginRadians) * outerBoundary;
            final double x2m2 = Math.cos(endRadians) * outerBoundary;
            final double y1m2 = Math.sin(beginRadians) * outerBoundary;
            final double y2m2 = Math.sin(endRadians) * outerBoundary;

            boolean isHighlighted = (i * wandEffectList.size()) / segments == mouseSelection;

            Vector4f color = radialButtonColor;
            if (isHighlighted) color = highlightColor;

            buffer.vertex(centerX + x1m1, centerY + y1m1, 0).color(color.x(), color.y(), color.z(), color.w()).endVertex();
            buffer.vertex(centerX + x2m1, centerY + y2m1, 0).color(color.x(), color.y(), color.z(), color.w()).endVertex();
            buffer.vertex(centerX + x2m2, centerY + y2m2, 0).color(color.x(), color.y(), color.z(), 0).endVertex();
            buffer.vertex(centerX + x1m2, centerY + y1m2, 0).color(color.x(), color.y(), color.z(), 0).endVertex();

            //Category line
            color = lineColor;
            double categoryLineWidth = 2;
            final double categoryLineOuterEdge = innerBoundary + categoryLineWidth;

            final double x1m3 = Math.cos(beginRadians) * categoryLineOuterEdge;
            final double x2m3 = Math.cos(endRadians) * categoryLineOuterEdge;
            final double y1m3 = Math.sin(beginRadians) * categoryLineOuterEdge;
            final double y2m3 = Math.sin(endRadians) * categoryLineOuterEdge;

            buffer.vertex(centerX + x1m1, centerY + y1m1, 0).color(color.x(), color.y(), color.z(), color.w()).endVertex();
            buffer.vertex(centerX + x2m1, centerY + y2m1, 0).color(color.x(), color.y(), color.z(), color.w()).endVertex();
            buffer.vertex(centerX + x2m3, centerY + y2m3, 0).color(color.x(), color.y(), color.z(), color.w()).endVertex();
            buffer.vertex(centerX + x1m3, centerY + y1m3, 0).color(color.x(), color.y(), color.z(), color.w()).endVertex();
        }
    }

    private static float getMouseAngle(double x1, double y1, double x2, double y2)
    {
        return (float) (Math.atan2(y2 - y1, x2- x1) + Math.PI);
    }
}
