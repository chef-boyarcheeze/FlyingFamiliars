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

    private static final float TOP_ANGLE = -90.0f;
    private static final float BOTTOM_ANGLE = 90.0f;
    private static final float SEPARATION_ARC_LENGTH = 3.0f;

    private Boolean active = false;
    private ItemStack stack = null;
    private ListTag wandEffectList = null;
    private int newSelectionIndex = -1;
    private int currentSelectionIndex = -1;

    private int centerX = 0;
    private int centerY = 0;

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

                centerX = screenWidth / 2;
                centerY = screenHeight / 2;

                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.setShader(GameRenderer::getPositionColorShader);

                drawSpiritRing(Tesselator.getInstance().getBuilder());

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

    private void drawSpiritRing(BufferBuilder buffer)
    {
        Player player = Minecraft.getInstance().player;
        ItemStack phylacteryStack = FFItemHandler.getPhylacteryCharm(player);

        float currentAngle = BOTTOM_ANGLE + (SEPARATION_ARC_LENGTH * 0.5f);

        if (!phylacteryStack.isEmpty() && (player.getMainHandItem().getItem() instanceof BaseSoulWand || player.getOffhandItem().getItem() instanceof BaseSoulWand))
        {
            ListTag spiritList = SpiritTagUtil.INSTANCE.getEntryList(phylacteryStack.getOrCreateTag());

            if (!spiritList.isEmpty())
            {
                // draw inner ring
                drawArc(
                        buffer,
                        BOUNDARY_0,
                        BOUNDARY_1,
                        0,
                        360.0f,
                        LINE_COLOR,
                        LINE_COLOR
                );

                float maxSpirit = SpiritTagUtil.INSTANCE.getMaxStorage(phylacteryStack.getOrCreateTag());
                float totalSpirit = maxSpirit * spiritList.size();

                // draw spirit ring
                for (Tag tag : spiritList)
                {
                    CompoundTag spiritTag = (CompoundTag) tag;

                    float arcLength = ((maxSpirit / totalSpirit) * 360.0f) - SEPARATION_ARC_LENGTH;
                    float fillPercent = Math.max(0.0f, Math.min(1.0f, spiritTag.getInt(STORAGE_SPIRIT_STORAGE) / maxSpirit));
                    float filledArcLength = ((maxSpirit / totalSpirit) * 360.0f) * fillPercent;

                    // draw empty portion
                    float emptyArcSize = arcLength - filledArcLength;
                    if (emptyArcSize > 0)
                    {
                        FFTypes.ColorType emptyColor = FFTypes.getTypeColorRGBA(0x88040404);

                        drawArc(
                                buffer,
                                BOUNDARY_1,
                                BOUNDARY_2,
                                currentAngle + filledArcLength,
                                currentAngle + arcLength,
                                emptyColor,
                                emptyColor
                        );
                    }

                    // draw filled portion after to prevent gaps
                    if (filledArcLength > 0)
                    {
                        FFTypes.ColorType spiritColor = FFTypes.getTypeColorRGBA(FFTypes.getTypeColorInt(spiritTag.getString(STORAGE_SPIRIT_TYPE)));

                        drawArc(
                                buffer,
                                BOUNDARY_1,
                                BOUNDARY_2,
                                currentAngle,
                                currentAngle + filledArcLength + (SEPARATION_ARC_LENGTH / 5),
                                spiritColor,
                                spiritColor
                        );
                    }

                    // draw spirit separation lines
                    drawArc(
                            buffer,
                            BOUNDARY_1,
                            BOUNDARY_2,
                            currentAngle + arcLength - (SEPARATION_ARC_LENGTH / 5),
                            currentAngle + arcLength + SEPARATION_ARC_LENGTH + (SEPARATION_ARC_LENGTH / 5),
                            LINE_COLOR,
                            LINE_COLOR
                    );

                    currentAngle += arcLength + SEPARATION_ARC_LENGTH;
                }
            }
        }

        // draw middle ring
        drawArc(
                buffer,
                BOUNDARY_2,
                BOUNDARY_3,
                0,
                360,
                LINE_COLOR,
                LINE_COLOR
        );

        // top of circle with offset
        currentAngle = TOP_ANGLE - ((360.0f / wandEffectList.size()) * 0.5f) + (SEPARATION_ARC_LENGTH * 0.5f);

        if (!wandEffectList.isEmpty())
        {
            for (Tag tag : wandEffectList)
            {
                CompoundTag wandEffectTag = (CompoundTag) tag;
                final BaseWandEffect wandEffect = WandEffectItemHelper.getSelectedWandEffect(wandEffectTag.getString(STORAGE_WAND_EFFECT_TYPE));

                assert wandEffect != null;

                FFTypes.ColorType wandEffectColor = FFTypes.getTypeColorRGBA(wandEffect.getColor());
                float arcLength = (360.0f / wandEffectList.size()) - SEPARATION_ARC_LENGTH;

                // draw wand effect
                if (tag == wandEffectList.get(newSelectionIndex)) // section highlighted
                {
                    drawArc(
                            buffer,
                            BOUNDARY_3,
                            BOUNDARY_4,
                            currentAngle + (SEPARATION_ARC_LENGTH / 5),
                            currentAngle + arcLength,
                            wandEffectColor,
                            wandEffectColor.modAlpha(0.0f)
                    );
                }
                else // section not highlighted
                {
                    drawArc(
                            buffer,
                            BOUNDARY_3,
                            BOUNDARY_4,
                            currentAngle + (SEPARATION_ARC_LENGTH / 5),
                            currentAngle + arcLength,
                            wandEffectColor.modRed(0.7f).modGreen(0.7f).modBlue(0.7f).modAlpha(0.6f),
                            wandEffectColor.modAlpha(0.0f)
                    );
                }

                // draw wand effect separation lines
                drawArc(
                        buffer,
                        BOUNDARY_3,
                        BOUNDARY_4 + 5,
                        currentAngle + arcLength - (SEPARATION_ARC_LENGTH / 5),
                        currentAngle + arcLength + SEPARATION_ARC_LENGTH + (SEPARATION_ARC_LENGTH / 5),
                        LINE_COLOR,
                        LINE_COLOR
                );

                currentAngle += arcLength + SEPARATION_ARC_LENGTH;
            }
        }

        // draw outer ring
        drawArc(
                buffer,
                BOUNDARY_4,
                BOUNDARY_5,
                0,
                360,
                LINE_COLOR,
                LINE_COLOR
        );
    }

    private void drawArc(BufferBuilder buffer, float innerRadius, float outerRadius, float startAngle, float endAngle, FFTypes.ColorType innerColor, FFTypes.ColorType outerColor)
    {
        buffer.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);

        for (float angle = startAngle; angle <= endAngle; angle += (endAngle - startAngle) / 360.0f)
        {
            float radians = (float) Math.toRadians(angle);
            float cos = (float) Math.cos(radians);
            float sin = (float) Math.sin(radians);

            float outerX = centerX + outerRadius * cos;
            float outerY = centerY + outerRadius * sin;
            buffer.vertex(outerX, outerY, 0).color(outerColor.red(), outerColor.green(), outerColor.blue(), outerColor.alpha()).endVertex();

            // Inner vertex
            float innerX = centerX + innerRadius * cos;
            float innerY = centerY + innerRadius * sin;
            buffer.vertex(innerX, innerY, 0).color(innerColor.red(), innerColor.green(), innerColor.blue(), innerColor.alpha()).endVertex();
        }

        BufferUploader.drawWithShader(buffer.end());
    }
}
