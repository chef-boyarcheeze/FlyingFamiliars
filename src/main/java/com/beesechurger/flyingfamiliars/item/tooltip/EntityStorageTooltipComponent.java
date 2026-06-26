package com.beesechurger.flyingfamiliars.item.tooltip;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.joml.Matrix4f;

import java.util.List;

import static com.beesechurger.flyingfamiliars.util.FFConstants.STORAGE_ENTITY_TYPE;

public record EntityStorageTooltipComponent(List<CompoundTag> entryList, boolean hasMoreEntities, int color) implements TooltipComponent
{
    public static class Client implements ClientTooltipComponent
    {
        protected static final ResourceLocation ENTRY_BACKGROUND = new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/gui/entity_entry_background.png");
        protected static final int ENTRY_BOX_SIZE = 24;

        private final EntityStorageTooltipComponent tooltipComponent;

        public Client(EntityStorageTooltipComponent tooltipComponent)
        {
            this.tooltipComponent = tooltipComponent;
        }

        @Override
        public int getHeight()
        {
            int height = 0;

            var entryList = tooltipComponent.entryList;
            if (!entryList.isEmpty())
            {
                height += ENTRY_BOX_SIZE + 4;
            }

            return height;
        }

        @Override
        public int getWidth(Font font)
        {
            int width = 0;

            var entryList = tooltipComponent.entryList;
            if (!entryList.isEmpty())
            {
                width += entryList.size() * ENTRY_BOX_SIZE;

                if (tooltipComponent.hasMoreEntities)
                {
                    width += 10;
                }
            }

            return width;
        }

        @Override
        public void renderText(Font font, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource buffer)
        {
            int yoff = (ENTRY_BOX_SIZE - font.lineHeight) / 2;

            var entryList = tooltipComponent.entryList;
            if (!entryList.isEmpty())
            {
                int xoff = entryList.size() * ENTRY_BOX_SIZE;

                if (tooltipComponent.hasMoreEntities)
                {
                    font.drawInBatch(Component.translatable("tooltip.flyingfamiliars.tag.more_entries").append("\u2026").withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.ITALIC), x + xoff + 4, y + yoff + 1, -1, false, matrix, buffer,
                            Font.DisplayMode.NORMAL, 0, LightTexture.FULL_BRIGHT);
                }

                y += ENTRY_BOX_SIZE;
            }
        }

        @Override
        public void renderImage(Font font, int x, int y, GuiGraphics graphics)
        {
            var entryList = tooltipComponent.entryList;
            if (!entryList.isEmpty())
            {
                int xOff = 0;
                int yOff = ENTRY_BOX_SIZE / 4;

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

                            var size = ENTRY_BOX_SIZE / 2;
                            var entityScale = size / Math.max(entity.getBbWidth(), 0.8f * entity.getBbHeight());

                            var mouseHandler = Minecraft.getInstance().mouseHandler;
                            var guiLeftEdge = (Minecraft.getInstance().getWindow().getWidth() - graphics.guiWidth()) / 2;
                            var guiTopEdge = (Minecraft.getInstance().getWindow().getHeight() - graphics.guiHeight()) / 2;

                            var entityX = x + xOff;
                            var entityY = y + yOff;

                            // Set color of entity boxes to color of item type
                            RenderSystem.setShaderColor(
                                    FastColor.ARGB32.red(tooltipComponent.color) / 255.0f,
                                    FastColor.ARGB32.green(tooltipComponent.color) / 255.0f,
                                    FastColor.ARGB32.blue(tooltipComponent.color) / 255.0f,
                                    FastColor.ARGB32.alpha(tooltipComponent.color) / 255.0f
                            );
                            graphics.blit(ENTRY_BACKGROUND, x + xOff, y, 0, 0, ENTRY_BOX_SIZE, ENTRY_BOX_SIZE, 32, 32);
                            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

                            graphics.pose().pushPose();
                            PoseStack modelView = RenderSystem.getModelViewStack();
                            modelView.pushPose();
                            modelView.mulPoseMatrix(graphics.pose().last().pose());
                            RenderSystem.applyModelViewMatrix();

                            // turn off hitboxes for rendered entities
                            boolean renderHitboxes = Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes();
                            Minecraft.getInstance().getEntityRenderDispatcher().setRenderHitBoxes(false);

                            // force all buffer-stored items to be drawn *before* enabling scissor
                            graphics.flush();
                            graphics.enableScissor(entityX, y, entityX + ENTRY_BOX_SIZE, y + ENTRY_BOX_SIZE);

                            InventoryScreen.renderEntityInInventoryFollowsMouse(
                                    graphics,
                                    entityX + (ENTRY_BOX_SIZE / 2),
                                    entityY + (ENTRY_BOX_SIZE / 2),
                                    (int) entityScale,
                                    (float) (guiLeftEdge + entityX - mouseHandler.xpos()),
                                    (float) (guiTopEdge + entityY - mouseHandler.ypos()),
                                    (LivingEntity) entity
                            );

                            graphics.disableScissor();

                            // if hitboxes were being rendered before, turn on hitboxes
                            Minecraft.getInstance().getEntityRenderDispatcher().setRenderHitBoxes(renderHitboxes);

                            modelView.popPose();
                            RenderSystem.applyModelViewMatrix();
                            graphics.pose().popPose();

                            xOff += ENTRY_BOX_SIZE;
                        }
                    }
                }
            }
        }
    }
}