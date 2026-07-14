package com.beesechurger.flyingfamiliars.item.tooltip;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.tags.EntityTagUtil;
import com.beesechurger.flyingfamiliars.util.FFTypes;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.joml.Matrix4f;

import static com.beesechurger.flyingfamiliars.util.FFConstants.STORAGE_ENTITY_TYPE;

public record EntityStorageTooltipComponent(ListTag entryList, boolean hasMoreEntities, int color) implements TooltipComponent
{
    public static class Client implements ClientTooltipComponent
    {
        protected static final ResourceLocation ENTRY_BACKGROUND = new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/gui/entity_entry_background.png");
        protected static final int ENTITY_NAME_WIDTH = 52;
        protected static final int ENTITY_NAME_END_WIDTH = 8;
        protected static final int ENTITY_NAME_BORDER_WIDTH = 5;
        protected static final int ENTRY_BACKGROUND_BOX_SIZE = 24;
        protected static final int ENTRY_HIGHLIGHT_BORDER_WIDTH = 4;

        private final EntityStorageTooltipComponent tooltipComponent;

        public Client(EntityStorageTooltipComponent tooltipComponent)
        {
            this.tooltipComponent = tooltipComponent;
        }

        @Override
        public int getWidth(Font font)
        {
            int width = 0;

            var entryList = tooltipComponent.entryList;
            if (!entryList.isEmpty())
            {
                width += entryList.size() * ENTRY_BACKGROUND_BOX_SIZE;

                if (tooltipComponent.hasMoreEntities)
                {
                    width += 10;
                }
            }

            return width;
        }

        @Override
        public int getHeight()
        {
            int height = 0;

            var entryList = tooltipComponent.entryList;
            if (!entryList.isEmpty())
            {
                height += ENTRY_BACKGROUND_BOX_SIZE + 4;
            }

            return height;
        }

        @Override
        public void renderText(Font font, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource buffer)
        {
            int yoff = (ENTRY_BACKGROUND_BOX_SIZE - font.lineHeight) / 2;

            var entryList = tooltipComponent.entryList;
            if (!entryList.isEmpty())
            {
                int xoff = entryList.size() * ENTRY_BACKGROUND_BOX_SIZE;

                if (tooltipComponent.hasMoreEntities)
                {
                    font.drawInBatch(Component.translatable("tooltip.flyingfamiliars.tag.more_entries").append("\u2026").withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.ITALIC), x + xoff + 4, y + yoff + 1, -1, false, matrix, buffer,
                            Font.DisplayMode.NORMAL, 0, LightTexture.FULL_BRIGHT);
                }

                y += ENTRY_BACKGROUND_BOX_SIZE;
            }
        }

        @Override
        public void renderImage(Font font, int x, int y, GuiGraphics graphics)
        {
            ListTag entryList = tooltipComponent.entryList;
            FFTypes.ColorType color = FFTypes.getTypeColorRGBA(tooltipComponent.color);
            var mouseHandler = Minecraft.getInstance().mouseHandler;
            var guiScale = Minecraft.getInstance().getWindow().getGuiScale();

            if (!entryList.isEmpty())
            {
                int xOff = 0;

                for (Tag tag : entryList)
                {
                    CompoundTag entryTag = (CompoundTag) tag;
                    EntityType<?> type = EntityType.byString(entryTag.getString(STORAGE_ENTITY_TYPE)).orElse(null);

                    if (type != null)
                    {
                        LivingEntity entity = (LivingEntity) type.create(Minecraft.getInstance().level);

                        if (entity != null)
                        {
                            entity.load(entryTag);
                            entity.tickCount = Minecraft.getInstance().player.tickCount;

                            var size = ENTRY_BACKGROUND_BOX_SIZE / 2;
                            var entityScale = size / Math.max(entity.getBbWidth(), 0.8f * entity.getBbHeight());

                            // draw entity boxes with color of item type
                            RenderSystem.setShaderColor(color.red, color.green, color.blue, color.alpha);
                            graphics.blit(ENTRY_BACKGROUND, x + xOff, y, 0, 0, ENTRY_BACKGROUND_BOX_SIZE, ENTRY_BACKGROUND_BOX_SIZE, 64, 64);
                            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

                            graphics.pose().pushPose();
                            PoseStack modelView = RenderSystem.getModelViewStack();
                            modelView.pushPose();
                            modelView.mulPoseMatrix(graphics.pose().last().pose());
                            RenderSystem.applyModelViewMatrix();

                            // turn off hitboxes for rendered entities
                            boolean renderHitboxes = Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes();
                            Minecraft.getInstance().getEntityRenderDispatcher().setRenderHitBoxes(false);

                            // force all buffer-stored tooltip components to be drawn before enabling scissor
                            graphics.flush();
                            graphics.enableScissor(
                                    x + xOff,
                                    y,
                                    x + xOff + ENTRY_BACKGROUND_BOX_SIZE,
                                    y + ENTRY_BACKGROUND_BOX_SIZE
                            );

                            InventoryScreen.renderEntityInInventoryFollowsMouse(
                                    graphics,
                                    x + xOff + (ENTRY_BACKGROUND_BOX_SIZE / 2),
                                    y + (3 * ENTRY_BACKGROUND_BOX_SIZE / 4),
                                    (int) entityScale,
                                    (float) (guiScale * (x + xOff + (ENTRY_BACKGROUND_BOX_SIZE / 2)) - mouseHandler.xpos()),
                                    (float) (guiScale * (y + (ENTRY_BACKGROUND_BOX_SIZE / 2)) - mouseHandler.ypos()),
                                    (LivingEntity) entity
                            );

                            graphics.disableScissor();

                            // if hitboxes were being rendered before, turn on hitboxes
                            Minecraft.getInstance().getEntityRenderDispatcher().setRenderHitBoxes(renderHitboxes);

                            var highlightBorderX = x + xOff + ENTRY_HIGHLIGHT_BORDER_WIDTH;
                            var highlightBorderY = y + ENTRY_HIGHLIGHT_BORDER_WIDTH;

                            // mouse is hovering over entity
                            if (Screen.hasControlDown()
                                    && (guiScale * highlightBorderX) <= mouseHandler.xpos() && mouseHandler.xpos() < (guiScale * (highlightBorderX + (ENTRY_BACKGROUND_BOX_SIZE - (2 * ENTRY_HIGHLIGHT_BORDER_WIDTH))))
                                    && (guiScale * highlightBorderY) <= mouseHandler.ypos() && mouseHandler.ypos() < (guiScale * (highlightBorderY + (ENTRY_BACKGROUND_BOX_SIZE - (2 * ENTRY_HIGHLIGHT_BORDER_WIDTH)))))
                            {
                                // draw hover highlight box
                                graphics.pose().pushPose();
                                graphics.pose().translate(0, 0, 200);

                                graphics.fill(
                                        highlightBorderX,
                                        highlightBorderY,
                                        highlightBorderX + (ENTRY_BACKGROUND_BOX_SIZE - (2 * ENTRY_HIGHLIGHT_BORDER_WIDTH)),
                                        highlightBorderY + (ENTRY_BACKGROUND_BOX_SIZE - (2 * ENTRY_HIGHLIGHT_BORDER_WIDTH)),
                                        0x80FFFFFF
                                );

                                graphics.pose().popPose();

                                // draw text box with color of item type
                                RenderSystem.setShaderColor(color.red, color.green, color.blue, color.alpha);
                                graphics.blit(ENTRY_BACKGROUND, x + xOff + ENTRY_BACKGROUND_BOX_SIZE, y, 0, 26, 8, 24, 64, 64);
                                for (int i = 0; i < ENTITY_NAME_WIDTH; i++)
                                {
                                    graphics.blit(ENTRY_BACKGROUND, x + xOff + ENTRY_BACKGROUND_BOX_SIZE + ENTITY_NAME_END_WIDTH + i, y, 10, 26, 1, 24, 64, 64);
                                }
                                graphics.blit(ENTRY_BACKGROUND, x + xOff + ENTRY_BACKGROUND_BOX_SIZE + ENTITY_NAME_WIDTH + ENTITY_NAME_END_WIDTH, y, 13, 26, 8, 24, 64, 64);
                                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

                                // force all buffer-stored tooltip components to be drawn before enabling scissor
                                graphics.flush();
                                graphics.enableScissor(
                                        x + xOff + ENTRY_BACKGROUND_BOX_SIZE + ENTITY_NAME_BORDER_WIDTH,
                                        y,
                                        x + xOff + ENTRY_BACKGROUND_BOX_SIZE + ENTITY_NAME_BORDER_WIDTH + ENTITY_NAME_WIDTH + 2 * (ENTITY_NAME_END_WIDTH - ENTITY_NAME_BORDER_WIDTH),
                                        y + ENTRY_BACKGROUND_BOX_SIZE
                                );

                                int lineWidth = font.width(EntityTagUtil.getEntityID(entryTag));
                                int lineOffset = 0;

                                if (lineWidth > ENTITY_NAME_WIDTH + ENTITY_NAME_END_WIDTH)
                                {
                                    float timeFactor = (System.currentTimeMillis() % 4000) / 4000.0f * Mth.TWO_PI;
                                    float scrollProgress = (Mth.sin(timeFactor) + 1.0f) / 2.0f;
                                    int maxScroll = lineWidth - (ENTITY_NAME_WIDTH + 2 * (ENTITY_NAME_END_WIDTH - ENTITY_NAME_BORDER_WIDTH));
                                    lineOffset = (int) (scrollProgress * maxScroll);
                                }

                                var nameColor = EntityTagUtil.isEntityTamed(entryTag) ? ChatFormatting.GREEN : ChatFormatting.YELLOW;

                                // Draw the line shifted left by its calculated offset
                                graphics.drawString(
                                        font,
                                        Component.literal(EntityTagUtil.getEntityID(entryTag)),
                                        x + xOff + ENTRY_BACKGROUND_BOX_SIZE + ENTITY_NAME_BORDER_WIDTH - lineOffset,
                                        y + ENTRY_BACKGROUND_BOX_SIZE / 4 + 1,
                                        nameColor.getColor(),
                                        true
                                );

                                graphics.disableScissor();

                                xOff += ENTITY_NAME_END_WIDTH + ENTITY_NAME_WIDTH + ENTITY_NAME_END_WIDTH;
                            }

                            modelView.popPose();
                            RenderSystem.applyModelViewMatrix();
                            graphics.pose().popPose();

                            xOff += ENTRY_BACKGROUND_BOX_SIZE;
                        }
                    }
                }
            }
        }
    }
}