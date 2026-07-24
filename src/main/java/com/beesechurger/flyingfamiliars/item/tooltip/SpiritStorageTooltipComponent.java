package com.beesechurger.flyingfamiliars.item.tooltip;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.util.FFTypes;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.joml.Matrix4f;

public record SpiritStorageTooltipComponent(String spiritType, float currentSpirit, float maxSpirit) implements TooltipComponent
{
    public static class Client implements ClientTooltipComponent
    {
        protected static final ResourceLocation ENTRY_BAR = new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/gui/entry_spirit_bar.png");
        protected static final int SPIRIT_TYPE_WIDTH = 40;
        protected static final int ENTRY_BAR_END_WIDTH = 10;
        protected static final int ENTRY_BAR_SPIRIT_WIDTH = 110;
        protected static final int ENTRY_BAR_SPIRIT_HEIGHT = 8;
        protected static final int ENTRY_BAR_INDICATOR_WIDTH = 4;

        private final SpiritStorageTooltipComponent tooltipComponent;

        public Client(SpiritStorageTooltipComponent tooltipComponent)
        {
            this.tooltipComponent = tooltipComponent;
        }

        @Override
        public int getWidth(Font font)
        {
            return ENTRY_BAR_SPIRIT_WIDTH + ENTRY_BAR_END_WIDTH;
        }

        @Override
        public int getHeight()
        {
            return 2 * ENTRY_BAR_SPIRIT_HEIGHT;
        }

        @Override
        public void renderText(Font font, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource buffer)
        {
            String typeName = FFTypes.getTypeName(tooltipComponent.spiritType).getString() + ":";
            int xOff = (SPIRIT_TYPE_WIDTH - font.width(typeName)) / 2;

            font.drawInBatch(Component.literal(typeName)
                            .withStyle(Style.EMPTY.withColor(FFTypes.getTypeColorInt(tooltipComponent.spiritType)))
                            .withStyle(ChatFormatting.ITALIC),
                    x + xOff, y + 2, -1, false, matrix, buffer, Font.DisplayMode.NORMAL, 0, LightTexture.FULL_BRIGHT);
        }

        @Override
        public void renderImage(Font font, int x, int y, GuiGraphics graphics)
        {
            FFTypes.ColorType color = FFTypes.getTypeColorRGBA(FFTypes.getTypeColorInt(tooltipComponent.spiritType));
            float ticks = Minecraft.getInstance().gui.getGuiTicks() + Minecraft.getInstance().getPartialTick();

            int xOff = SPIRIT_TYPE_WIDTH;
            int yOff = 2;

            float x1 = x + ENTRY_BAR_END_WIDTH;
            float x2 = x + ENTRY_BAR_END_WIDTH + ENTRY_BAR_SPIRIT_WIDTH;

            float fillLevel = tooltipComponent.currentSpirit < tooltipComponent.maxSpirit
                    ? (tooltipComponent.currentSpirit / tooltipComponent.maxSpirit) : 1.0f;
            x2 = (x1 + (x2 - x1) * fillLevel / 2);
            float width = x2 - x1;

            for (int horizontalPasses = 0; horizontalPasses < 5; horizontalPasses++)
            {
                for (int verticalPasses = 0; verticalPasses < (ENTRY_BAR_SPIRIT_HEIGHT / 2); verticalPasses++)
                {
                    Matrix4f matrix4f = graphics.pose().last().pose();
                    VertexConsumer vertexConsumer = graphics.bufferSource().getBuffer(RenderType.guiOverlay());

                    float tempY = y + verticalPasses + yOff;
                    float height = ENTRY_BAR_SPIRIT_HEIGHT - (ENTRY_BAR_SPIRIT_HEIGHT / 4) * verticalPasses;

                    vertexConsumer.vertex(matrix4f, x1 + xOff,          tempY + height, 0).color(color.red(), color.green(), color.blue(), Mth.clamp(color.alpha() * (0.05f + 0.015f * (float) Math.sin(0.25f * ticks + Mth.PI / 2)), 0f, 1.0f)).endVertex();
                    vertexConsumer.vertex(matrix4f, x1 + xOff + width,  tempY + height, 0).color(color.red(), color.green(), color.blue(), Mth.clamp(color.alpha() * (0.05f + 0.015f * (float) Math.sin(0.25f * ticks + Mth.PI / 2)), 0f, 1.0f)).endVertex();
                    vertexConsumer.vertex(matrix4f, x1 + xOff + width,  tempY,          0).color(color.red(), color.green(), color.blue(), Mth.clamp(color.alpha() * (0.05f + 0.015f * (float) Math.sin(0.25f * ticks + Mth.PI / 2)), 0f, 1.0f)).endVertex();
                    vertexConsumer.vertex(matrix4f, x1 + xOff,          tempY,          0).color(color.red(), color.green(), color.blue(), Mth.clamp(color.alpha() * (0.05f + 0.015f * (float) Math.sin(0.25f * ticks + Mth.PI / 2)), 0f, 1.0f)).endVertex();
                }
            }

            // draw spirit bar background tiles
            FFTypes.ColorType backgroundColor = FFTypes.getTypeColorRGBA(ChatFormatting.DARK_GRAY.getColor());
            RenderSystem.setShaderColor(backgroundColor.red(), backgroundColor.green(), backgroundColor.blue(), backgroundColor.alpha());
            for (int i = 0; i < ENTRY_BAR_SPIRIT_WIDTH / 10; i++)
            {
                graphics.blit(ENTRY_BAR, x + xOff + ENTRY_BAR_END_WIDTH - 1 + (6 * i), y, 18, 0, 7, 12, 32, 32);
            }
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

            // flush spirit bar (foreground) and spirit bar background tiles (background)
            graphics.flush();

            // draw spirit bar ends and indicator
            FFTypes.ColorType foregroundColor = FFTypes.getTypeColorRGBA(ChatFormatting.GRAY.getColor());
            RenderSystem.setShaderColor(foregroundColor.red(), foregroundColor.green(), foregroundColor.blue(), foregroundColor.alpha());
            graphics.blit(ENTRY_BAR, x + xOff, y - yOff, 0, 0, 10, 16, 32, 32);
            graphics.blit(ENTRY_BAR, x + xOff + ENTRY_BAR_END_WIDTH + (int) width, y - (yOff / 2), 12, 0, 4, 14, 32, 32);
            graphics.blit(ENTRY_BAR, x + xOff + ENTRY_BAR_END_WIDTH + ENTRY_BAR_INDICATOR_WIDTH + (ENTRY_BAR_SPIRIT_WIDTH / 2), y - yOff, 0, 0, 10, 16, 32, 32);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

            // draw spirit bar end cores in type color
            RenderSystem.setShaderColor(color.red(), color.green(), color.blue(), color.alpha());
            graphics.blit(ENTRY_BAR, x + xOff + 3, y - yOff + 5, 28, 0, 4, 6, 32, 32);
            graphics.blit(ENTRY_BAR, x + xOff + ENTRY_BAR_END_WIDTH + ENTRY_BAR_INDICATOR_WIDTH + (ENTRY_BAR_SPIRIT_WIDTH / 2) + 3, y - yOff + 5, 28, 0, 4, 6, 32, 32);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
}