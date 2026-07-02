package com.beesechurger.flyingfamiliars.integration.jei;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EntityTypeRenderer implements IIngredientRenderer<EntityTypeIngredient>
{
    private final int size;

    public EntityTypeRenderer(int size)
    {
        this.size = size;
    }

    @Override
    public void render(GuiGraphics graphics, @Nullable EntityTypeIngredient ingredient)
    {
        if (Minecraft.getInstance().level != null
                && Minecraft.getInstance().player != null
                && ingredient != null
                && ingredient.getEntity() != null
                && ingredient.getEntity() instanceof LivingEntity entity)
        {
            graphics.pose().pushPose();
            graphics.pose().translate(0.5f * size, 0.9f * size, 0);

            entity.tickCount = Minecraft.getInstance().player.tickCount;
            float scale = 0.6f * size / Math.max(entity.getBbWidth(), 0.8f * entity.getBbHeight());

            renderEntity(graphics, entity, scale);
            graphics.pose().popPose();
        }
    }

    private void renderEntity(GuiGraphics graphics, LivingEntity entity, float scale)
    {
        MouseHandler mouseHandler = Minecraft.getInstance().mouseHandler;
        float recipeX = graphics.pose().last().pose().m30();
        float recipeY = graphics.pose().last().pose().m31();
        boolean renderHitboxes = Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes();

        PoseStack modelView = RenderSystem.getModelViewStack();
        modelView.pushPose();
        modelView.mulPoseMatrix(graphics.pose().last().pose());
        Minecraft.getInstance().getEntityRenderDispatcher().setRenderHitBoxes(false);
        InventoryScreen.renderEntityInInventoryFollowsMouse(
                graphics,
                0,
                0,
                (int) scale,
                (float) (Minecraft.getInstance().getWindow().getGuiScale() * recipeX - mouseHandler.xpos()),
                (float) (Minecraft.getInstance().getWindow().getGuiScale() * (recipeY - getHeight() / 2) - mouseHandler.ypos()),
                entity
        );
        Minecraft.getInstance().getEntityRenderDispatcher().setRenderHitBoxes(renderHitboxes);
        modelView.popPose();
        RenderSystem.applyModelViewMatrix();
    }

    @Override
    public List<Component> getTooltip(EntityTypeIngredient ingredient, TooltipFlag tooltipFlag)
    {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(ingredient.getEntity().getDisplayName());

        if(tooltipFlag.isAdvanced())
        {
            tooltip.add(Component.translatable(ForgeRegistries.ENTITY_TYPES.getKey(ingredient.getEntityType()).toString()).withStyle(ChatFormatting.DARK_GRAY));
        }

        return tooltip;
    }

    @Override
    public int getWidth()
    {
        return size;
    }

    @Override
    public int getHeight()
    {
        return size;
    }
}
