package com.beesechurger.flyingfamiliars.integration.jei;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
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
            entity.tickCount = Minecraft.getInstance().player.tickCount;
            graphics.pose().translate(0.5f * size, 0.9f * size, 0);

            float scale = 0.5f * size * Math.min(1.7f / entity.getBbHeight(), 1f);
            renderEntity(graphics, entity, ingredient.x, ingredient.y, scale);
            graphics.pose().popPose();
        }
    }

    private void renderEntity(GuiGraphics graphics, LivingEntity entity, int x, int y, float scale)
    {
        var mouseHandler = Minecraft.getInstance().mouseHandler;
        var guiLeftEdge = (Minecraft.getInstance().getWindow().getWidth() - graphics.guiWidth()) / 2;
        var guiTopEdge = (Minecraft.getInstance().getWindow().getHeight() - graphics.guiHeight()) / 2;

        boolean renderHitboxes = Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes();

        PoseStack modelView = RenderSystem.getModelViewStack();
        modelView.pushPose();
        modelView.mulPoseMatrix(graphics.pose().last().pose());
        Minecraft.getInstance().getEntityRenderDispatcher().setRenderHitBoxes(false);
        InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, 0, 0, (int) scale, (float) (guiLeftEdge + x - mouseHandler.xpos()), (float) (guiTopEdge + y - mouseHandler.ypos()), entity);
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
