package com.beesechurger.flyingfamiliars.wand_effect.client;

import com.beesechurger.flyingfamiliars.item.FFItemHandler;
import com.beesechurger.flyingfamiliars.item.common.entity.soul_wand.BaseSoulWand;
import com.beesechurger.flyingfamiliars.packet.WandEffectSelectionC2SPacket;
import com.beesechurger.flyingfamiliars.registries.FFPackets;
import com.beesechurger.flyingfamiliars.tags.SpiritTagUtil;
import com.beesechurger.flyingfamiliars.tags.WandEffectTagUtil;
import com.beesechurger.flyingfamiliars.util.FFTypes;
import com.beesechurger.flyingfamiliars.wand_effect.common.BaseWandEffect;
import com.beesechurger.flyingfamiliars.wand_effect.common.WandEffectItemHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import static com.beesechurger.flyingfamiliars.util.FFConstants.*;

public class WandEffectSelectionScreen implements IGuiOverlay
{
    public static final WandEffectSelectionScreen INSTANCE = new WandEffectSelectionScreen();
    public static final String WAND_EFFECT_WHEEL = "wand_effect_wheel";

    private static final FFTypes.ColorType LINE_COLOR = FFTypes.getTypeColorRGBA(0xFF484848);
    private static final FFTypes.ColorType RADIAL_BUTTON_COLOR = FFTypes.getTypeColorRGBA(0x990A0702);
    private static final FFTypes.ColorType HIGHLIGHT_COLOR = FFTypes.getTypeColorRGBA(0xB2CCB28C);

    private static final float BOUNDARY_0 = 16.0f;
    private static final float BOUNDARY_1 = BOUNDARY_0 + 2;
    private static final float BOUNDARY_2 = 24.0f;
    private static final float BOUNDARY_3 = BOUNDARY_2 + 2;
    private static final float BOUNDARY_4 = 90.0f;
    private static final float BOUNDARY_5 = BOUNDARY_4 + 2;

    private Boolean active = false;
    private ItemStack stack = null;
    private ListTag wandEffectList = null;
    private int newSelectionIndex = -1;
    private int currentSelectionIndex = -1;

    public void open(ItemStack incomingStack)
    {
        if (incomingStack.getItem() instanceof BaseSoulWand item)
        {
            stack = incomingStack;

            if (!WandEffectTagUtil.INSTANCE.isEmpty(stack.getOrCreateTag()))
            {
                active = true;
                wandEffectList = WandEffectTagUtil.INSTANCE.getEntryList(stack.getOrCreateTag());
                newSelectionIndex = -1;

                CompoundTag selection = WandEffectTagUtil.INSTANCE.getSelectedEntry(stack.getOrCreateTag());

                for (int i = 0; i < wandEffectList.size(); ++i)
                {
                    String selectionString = selection.get(STORAGE_WAND_EFFECT_TYPE).toString();
                    String comparisonString = wandEffectList.getCompound(i).get(STORAGE_WAND_EFFECT_TYPE).toString();

                    if (selectionString.equals(comparisonString))
                    {
                        currentSelectionIndex = i;
                        break;
                    }
                }

                Minecraft.getInstance().mouseHandler.releaseMouse();
            }
        }
    }

    public void close()
    {
        active = false;

        if (newSelectionIndex >= 0 && newSelectionIndex != currentSelectionIndex)
        {
            FFPackets.sendToServer(new WandEffectSelectionC2SPacket(newSelectionIndex, currentSelectionIndex));
        }

        stack = null;
        wandEffectList = null;
        newSelectionIndex = -1;
        currentSelectionIndex = -1;

        Minecraft.getInstance().mouseHandler.grabMouse();
    }

    public Boolean isActive()
    {
        return active;
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight)
    {
        if (active)
        {
            Minecraft mc = Minecraft.getInstance();

            if (mc.player != null && !mc.player.isSpectator() && !mc.options.hideGui && mc.screen == null && !mc.mouseHandler.isMouseGrabbed() && wandEffectList.size() > 0)
            {
                PoseStack poseStack = guiGraphics.pose();
                poseStack.pushPose();

                Vec2 screenCenter = new Vec2(mc.getWindow().getScreenWidth() * 0.5f, mc.getWindow().getScreenHeight() * 0.5f);
                Vec2 mousePos = new Vec2((float) mc.mouseHandler.xpos(), (float) mc.mouseHandler.ypos());

                double screenWidthCenter = mc.getWindow().getScreenWidth() * 0.5f;
                double screenHeightCenter = mc.getWindow().getScreenHeight() * 0.5f;
                double guiScale = mc.getWindow().getGuiScale();

                double radiansPerWandEffect = Math.toRadians(360 / (float) wandEffectList.size());
                double mouseRotation = ((float) (Math.atan2(screenHeightCenter - mousePos.y, screenWidthCenter - mousePos.x) + Math.PI) + 1.570f + (float) radiansPerWandEffect * .5f) % (2 * Math.PI);

                newSelectionIndex = (int) Mth.clamp(mouseRotation / radiansPerWandEffect, 0, wandEffectList.size() - 1);

                if (mousePos.distanceToSqr(screenCenter) < BOUNDARY_2 * guiScale * BOUNDARY_2 * guiScale)
                {
                    // reset new selection when not selecting any wand effect
                    newSelectionIndex = currentSelectionIndex;
                }

                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.setShader(GameRenderer::getPositionColorShader);

                final BufferBuilder buffer = Tesselator.getInstance().getBuilder();
                final int centerX = screenWidth / 2;
                final int centerY = screenHeight / 2;

                buffer.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
                drawSpiritRing(buffer, centerX, centerY);
                BufferUploader.drawWithShader(buffer.end());

                buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
                drawRadialBackgrounds(buffer, centerX, centerY);
                drawDividingLines(buffer, centerX, centerY);
                BufferUploader.drawWithShader(buffer.end());

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
                selectedSpell.getUniqueInfo(minecraft.player).forEach((line) -> lines.add(line.withStyle(ChatFormatting.DARK_GREEN)));

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

                    //Border

                        graphics.blit(TEXTURE, (int) locations[i].x - borderWidth, (int) locations[i].y - borderWidth, swsm.getSelectionIndex() == i ? 32 : 0, 106, 32, 32);

                    //Cooldown

                        float f = ClientMagicData.getCooldownPercent(spell.getSpell());
                        if (f > 0)
                        {
                            RenderSystem.enableBlend();
                            int pixels = (int) (16 * f + 1f);
                            gui.blit(poseStack, centerX + (int) locations[i].x + 3, centerY + (int) locations[i].y + 19 - pixels, 47, 87, 16, pixels);
                            graphics.blit(TEXTURE, (int) locations[i].x - cdWidth, (int) locations[i].y + cdWidth - pixels, 47, 87, 16, pixels);
                        }
                        poseStack.popPose();
                    }
                }*/

                poseStack.popPose();
            }
            else
            {
                close();
            }
        }
    }

    private void drawSpiritRing(BufferBuilder buffer, float centerX, float centerY)
    {
        Player player = Minecraft.getInstance().player;
        ItemStack phylacteryStack = FFItemHandler.getPhylacteryCharm(player);

        if (!phylacteryStack.isEmpty() && (player.getMainHandItem().getItem() instanceof BaseSoulWand || player.getOffhandItem().getItem() instanceof BaseSoulWand))
        {
            ListTag entryList = SpiritTagUtil.INSTANCE.getEntryList(phylacteryStack.getOrCreateTag());

            if (!entryList.isEmpty())
            {
                float maxSpirit = SpiritTagUtil.INSTANCE.getMaxStorage(phylacteryStack.getOrCreateTag());
                float totalSpirit = maxSpirit * entryList.size();

                // start drawing from the bottom, 90 degrees apparently
                float currentAngle = 90.0f;
                float separationArcLength = 0.0f;

                for (Tag tag : entryList)
                {
                    CompoundTag entryTag = (CompoundTag) tag;
                    FFTypes.ColorType color = FFTypes.getTypeColorRGBA(FFTypes.getTypeColorInt(entryTag.getString(STORAGE_SPIRIT_TYPE)));

                    float arcLength = ((maxSpirit / totalSpirit) * 360.0f) - separationArcLength;
                    float fillPercent = Math.max(0.0f, Math.min(1.0f, entryTag.getInt(STORAGE_SPIRIT_STORAGE) / maxSpirit));
                    float filledArcLength = ((maxSpirit / totalSpirit) * 360.0f) * fillPercent;

                    // draw filled portion
                    if (filledArcLength > 0)
                    {
                        drawArc(
                            buffer,
                            centerX,
                            centerY,
                            BOUNDARY_1,
                            BOUNDARY_2,
                            currentAngle + (separationArcLength * 0.5f),
                            currentAngle + filledArcLength,
                            color.red,
                            color.green,
                            color.blue,
                            color.alpha
                        );
                    }

                    // draw empty portion
                    float emptyArcSize = arcLength - filledArcLength;
                    if (emptyArcSize > 0)
                    {
                        drawArc(
                            buffer,
                            centerX,
                            centerY,
                            BOUNDARY_1,
                            BOUNDARY_2,
                            currentAngle + filledArcLength,
                            currentAngle + arcLength,
                            0.15f,
                            0.15f,
                            0.15f,
                            0.6f
                        );
                    }

                    // advance to next entry
                    currentAngle += arcLength + separationArcLength;
                }
            }
        }


    }

    private void drawArc(BufferBuilder buffer, float cx, float cy,
                                float innerR, float outerR, float startAngle, float endAngle,
                                float r, float g, float b, float a)
    {
        // Step size in degrees. Lower = smoother/more circular, but uses more GPU vertices.
        float step = 4.0f;
        float angle = startAngle;

        while (angle < endAngle)
        {
            addArcVertices(buffer, cx, cy, innerR, outerR, angle, r, g, b, a);
            angle += step;
        }

        addArcVertices(buffer, cx, cy, innerR, outerR, endAngle, r, g, b, a);
    }

    private void addArcVertices(BufferBuilder buffer, float cx, float cy,
                                       float innerR, float outerR, float angleDegrees,
                                       float r, float g, float b, float a)
    {
        float radians = (float) Math.toRadians(angleDegrees);
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.sin(radians);

        // Outer vertex
        float ox = cx + outerR * cos;
        float oy = cy + outerR * sin;
        buffer.vertex(ox, oy, 0).color(r, g, b, a).endVertex();

        // Inner vertex
        float ix = cx + innerR * cos;
        float iy = cy + innerR * sin;
        buffer.vertex(ix, iy, 0).color(r, g, b, a).endVertex();
    }

    private void drawDividingLines(BufferBuilder buffer, float centerX, float centerY)
    {
        if (wandEffectList.size() > 1)
        {
            double radiansPerSpell = 2 * Math.PI / wandEffectList.size();

            for (int i = 0; i < wandEffectList.size(); i++)
            {
                final double closeWidth = 8 * Mth.DEG_TO_RAD;
                final double farWidth = closeWidth / 4;
                final double beginCloseRadians = i * radiansPerSpell - (Math.PI / 2 + (radiansPerSpell / 2)) - (closeWidth / 4);
                final double endCloseRadians = beginCloseRadians + closeWidth;
                final double beginFarRadians = i * radiansPerSpell - (Math.PI / 2 + (radiansPerSpell / 2)) - (farWidth / 4);
                final double endFarRadians = beginCloseRadians + farWidth;

                final double x1m1 = Math.cos(beginCloseRadians) * BOUNDARY_2;
                final double x2m1 = Math.cos(endCloseRadians) * BOUNDARY_2;
                final double y1m1 = Math.sin(beginCloseRadians) * BOUNDARY_2;
                final double y2m1 = Math.sin(endCloseRadians) * BOUNDARY_2;

                final double x1m2 = Math.cos(beginFarRadians) * BOUNDARY_4 * 1.1;
                final double x2m2 = Math.cos(endFarRadians) * BOUNDARY_4 * 1.1;
                final double y1m2 = Math.sin(beginFarRadians) * BOUNDARY_4 * 1.1;
                final double y2m2 = Math.sin(endFarRadians) * BOUNDARY_4 * 1.1;

                FFTypes.ColorType color = LINE_COLOR;
                buffer.vertex(centerX + x1m1, centerY + y1m1, 0).color(color.red, color.green, color.blue, color.alpha).endVertex();
                buffer.vertex(centerX + x2m1, centerY + y2m1, 0).color(color.red, color.green, color.blue, color.alpha).endVertex();
                buffer.vertex(centerX + x2m2, centerY + y2m2, 0).color(color.red, color.green, color.blue, color.alpha).endVertex();
                buffer.vertex(centerX + x1m2, centerY + y1m2, 0).color(color.red, color.green, color.blue, color.alpha).endVertex();
            }
        }
    }

    private void drawRadialBackgrounds(BufferBuilder buffer, float centerX, float centerY)
    {
        Player player = Minecraft.getInstance().player;
        ItemStack phylacteryStack = FFItemHandler.getPhylacteryCharm(player);

        final int rayCount = 360;
        final double radiansPerRay = 2 * Math.PI / rayCount;
        final double radiansPerSpell = 2 * Math.PI / wandEffectList.size();

        for (int i = 0; i < rayCount; i++)
        {
            final double beginRadians = i * radiansPerRay - (Math.PI / 2 + (radiansPerSpell / 2));
            final double endRadians = (i + 1) * radiansPerRay - (Math.PI / 2 + (radiansPerSpell / 2));

            final BaseWandEffect wandEffect = WandEffectItemHelper.getSelectedWandEffect(((CompoundTag) wandEffectList.get((i * wandEffectList.size()) / rayCount)).getString(STORAGE_WAND_EFFECT_TYPE));

            FFTypes.ColorType color = LINE_COLOR;

            if (!phylacteryStack.isEmpty() && (player.getMainHandItem().getItem() instanceof BaseSoulWand || player.getOffhandItem().getItem() instanceof BaseSoulWand))
            {
                // inner ring
                final double x1m0 = Math.cos(beginRadians) * BOUNDARY_0;
                final double x2m0 = Math.cos(endRadians) * BOUNDARY_0;
                final double y1m0 = Math.sin(beginRadians) * BOUNDARY_0;
                final double y2m0 = Math.sin(endRadians) * BOUNDARY_0;

                final double x1m1 = Math.cos(beginRadians) * BOUNDARY_1;
                final double x2m1 = Math.cos(endRadians) * BOUNDARY_1;
                final double y1m1 = Math.sin(beginRadians) * BOUNDARY_1;
                final double y2m1 = Math.sin(endRadians) * BOUNDARY_1;

                buffer.vertex(centerX + x1m0, centerY + y1m0, 0).color(color.red, color.green, color.blue, color.alpha).endVertex();
                buffer.vertex(centerX + x2m0, centerY + y2m0, 0).color(color.red, color.green, color.blue, color.alpha).endVertex();
                buffer.vertex(centerX + x2m1, centerY + y2m1, 0).color(color.red, color.green, color.blue, color.alpha).endVertex();
                buffer.vertex(centerX + x1m1, centerY + y1m1, 0).color(color.red, color.green, color.blue, color.alpha).endVertex();
            }

            // middle ring
            color = LINE_COLOR;
            double categoryLineWidth = 2;
            final double categoryLineOuterEdge = BOUNDARY_2 + categoryLineWidth;

            final double x1m2 = Math.cos(beginRadians) * BOUNDARY_2;
            final double x2m2 = Math.cos(endRadians) * BOUNDARY_2;
            final double y1m2 = Math.sin(beginRadians) * BOUNDARY_2;
            final double y2m2 = Math.sin(endRadians) * BOUNDARY_2;

            final double x1m3 = Math.cos(beginRadians) * BOUNDARY_3;
            final double x2m3 = Math.cos(endRadians) * BOUNDARY_3;
            final double y1m3 = Math.sin(beginRadians) * BOUNDARY_3;
            final double y2m3 = Math.sin(endRadians) * BOUNDARY_3;

            buffer.vertex(centerX + x1m2, centerY + y1m2, 0).color(LINE_COLOR.red, LINE_COLOR.green, LINE_COLOR.blue, LINE_COLOR.alpha).endVertex();
            buffer.vertex(centerX + x2m2, centerY + y2m2, 0).color(LINE_COLOR.red, LINE_COLOR.green, LINE_COLOR.blue, LINE_COLOR.alpha).endVertex();
            buffer.vertex(centerX + x2m3, centerY + y2m3, 0).color(LINE_COLOR.red, LINE_COLOR.green, LINE_COLOR.blue, LINE_COLOR.alpha).endVertex();
            buffer.vertex(centerX + x1m3, centerY + y1m3, 0).color(LINE_COLOR.red, LINE_COLOR.green, LINE_COLOR.blue, LINE_COLOR.alpha).endVertex();

            // wand effect sections
            if (wandEffect != null)
            {
                color = FFTypes.getTypeColorRGBA(wandEffect.getColor());
            }

            final double x1m4 = Math.cos(beginRadians) * BOUNDARY_4;
            final double x2m4 = Math.cos(endRadians) * BOUNDARY_4;
            final double y1m4 = Math.sin(beginRadians) * BOUNDARY_4;
            final double y2m4 = Math.sin(endRadians) * BOUNDARY_4;

            float alphaModifier = 1; //(float) Math.cos(Math.PI / 180.0f * ((i + (rayCount / wandEffectList.size())) % (rayCount / wandEffectList.size())));

            if ((i * wandEffectList.size() / rayCount) == newSelectionIndex) // section highlighted
            {
                buffer.vertex(centerX + x1m3, centerY + y1m3, 0).color(color.red, color.green, color.blue, color.alpha * alphaModifier).endVertex();
                buffer.vertex(centerX + x2m3, centerY + y2m3, 0).color(color.red, color.green, color.blue, color.alpha * alphaModifier).endVertex();
                buffer.vertex(centerX + x2m4, centerY + y2m4, 0).color(color.red, color.green, color.blue, 0).endVertex();
                buffer.vertex(centerX + x1m4, centerY + y1m4, 0).color(color.red, color.green, color.blue, 0).endVertex();
            }
            else // section not highlighted
            {
                buffer.vertex(centerX + x1m3, centerY + y1m3, 0).color(color.red * 0.7f, color.green * 0.7f, color.blue * 0.7f, color.alpha * 0.6f).endVertex();
                buffer.vertex(centerX + x2m3, centerY + y2m3, 0).color(color.red * 0.7f, color.green * 0.7f, color.blue * 0.7f, color.alpha * 0.6f).endVertex();
                buffer.vertex(centerX + x2m4, centerY + y2m4, 0).color(color.red * 0.7f, color.green * 0.7f, color.blue * 0.7f, 0).endVertex();
                buffer.vertex(centerX + x1m4, centerY + y1m4, 0).color(color.red * 0.7f, color.green * 0.7f, color.blue * 0.7f, 0).endVertex();
            }

            final double x1m5 = Math.cos(beginRadians) * BOUNDARY_5;
            final double x2m5 = Math.cos(endRadians) * BOUNDARY_5;
            final double y1m5 = Math.sin(beginRadians) * BOUNDARY_5;
            final double y2m5 = Math.sin(endRadians) * BOUNDARY_5;

            buffer.vertex(centerX + x1m4, centerY + y1m4, 0).color(LINE_COLOR.red, LINE_COLOR.green, LINE_COLOR.blue, LINE_COLOR.alpha).endVertex();
            buffer.vertex(centerX + x2m4, centerY + y2m4, 0).color(LINE_COLOR.red, LINE_COLOR.green, LINE_COLOR.blue, LINE_COLOR.alpha).endVertex();
            buffer.vertex(centerX + x2m5, centerY + y2m5, 0).color(LINE_COLOR.red, LINE_COLOR.green, LINE_COLOR.blue, LINE_COLOR.alpha).endVertex();
            buffer.vertex(centerX + x1m5, centerY + y1m5, 0).color(LINE_COLOR.red, LINE_COLOR.green, LINE_COLOR.blue, LINE_COLOR.alpha).endVertex();
        }
    }
}
