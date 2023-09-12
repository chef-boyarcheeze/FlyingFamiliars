package com.beesechurger.flyingfamiliars.integration;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.TooltipFlag;
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
    public void render(PoseStack stack, @Nullable EntityTypeIngredient ingredient)
    {
        if (Minecraft.getInstance().level == null || Minecraft.getInstance().player == null || ingredient == null) return;
        if (ingredient.getEntity() != null && ingredient.getEntity() instanceof LivingEntity entity)
        {
            stack.pushPose();
            entity.tickCount = Minecraft.getInstance().player.tickCount;
            stack.translate(0.5f * size, 0.9f * size, 0);

            float scale = 0.5f * size * Math.min(1.7f / entity.getBbHeight(), 1f);
            renderEntity(stack, entity, scale);
            stack.popPose();
        }
    }

    private void renderEntity(PoseStack stack, LivingEntity entity, float scale)
    {
        PoseStack modelView = RenderSystem.getModelViewStack();
        modelView.pushPose();
        modelView.mulPoseMatrix(stack.last().pose());
        InventoryScreen.renderEntityInInventory(0, 0, (int) scale, 75, -20, entity);
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
            tooltip.add(new TextComponent(ingredient.getEntityType().getRegistryName().toString()).withStyle(ChatFormatting.DARK_GRAY));
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
