package com.beesechurger.flyingfamiliars.integration.jei;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.blocks.FFBlocks;
import com.beesechurger.flyingfamiliars.recipe.BrazierRecipe;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BrazierRecipeCategory implements IRecipeCategory<BrazierRecipe>
{
    public final static ResourceLocation UID = new ResourceLocation(FlyingFamiliars.MOD_ID, "brazier");
    public final static ResourceLocation INPUTS =
            new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/gui/brazier_inputs_jei.png");
    public final static ResourceLocation SINGLE_ARROW =
            new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/gui/brazier_single_arrow_jei.png");
    public final static ResourceLocation DOUBLE_ARROW =
            new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/gui/brazier_double_arrow_jei.png");
    public final static ResourceLocation OUTPUTS =
            new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/gui/brazier_outputs_jei.png");

    private final IDrawable background;
    private final IDrawable inputs;
    private final IDrawable singleArrow;
    private final IDrawable doubleArrow;
    private final IDrawable outputs;
    private final IDrawable icon;

    public BrazierRecipeCategory(IGuiHelper helper)
    {
        this.background = helper.createBlankDrawable(176, 85);
        this.inputs = helper.createDrawable(INPUTS, 0, 0, 64, 64);
        this.singleArrow = helper.createDrawable(SINGLE_ARROW, 0, 0, 64, 64);
        this.doubleArrow = helper.createDrawable(DOUBLE_ARROW, 0, 0, 64, 64);
        this.outputs = helper.createDrawable(OUTPUTS, 0, 0, 64, 64);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(FFBlocks.BRAZIER.get()));
    }

    @Override
    public Component getTitle()
    {
        return new TranslatableComponent("block.flyingfamiliars.brazier");
    }

    @Override
    public IDrawable getBackground()
    {
        return background;
    }

    @Override
    public IDrawable getIcon()
    {
        return icon;
    }

    @SuppressWarnings("removal")
    @Override
    public ResourceLocation getUid()
    {
        return UID;
    }

    @SuppressWarnings("removal")
    @Override
    public Class<? extends BrazierRecipe> getRecipeClass()
    {
        return BrazierRecipe.class;
    }

    @Override
    public void draw(@NotNull BrazierRecipe recipe, @NotNull IRecipeSlotsView view, @NotNull PoseStack ms, double mouseX, double mouseY)
    {
        RenderSystem.enableBlend();

        inputs.draw(ms, 21, 10);

        if(recipe.getResultItem() != ItemStack.EMPTY && recipe.getResultEntity() != null)
        {
            doubleArrow.draw(ms, 89, 25);
            outputs.draw(ms, 122, 11);
            outputs.draw(ms, 122, 41);
        }
        else
        {
            singleArrow.draw(ms, 89, 38);
            outputs.draw(ms, 122, 26);
        }

        RenderSystem.disableBlend();
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull BrazierRecipe recipe, @NotNull IFocusGroup focuses)
    {
        int xCenterInput = 45;
        int yCenterInput = 35;
        int xModInput = 30;
        int yModInput = 30;

        builder.addSlot(RecipeIngredientRole.CATALYST, xCenterInput, yCenterInput)
                .addItemStack(new ItemStack(FFBlocks.BRAZIER.get()));

        for(int i = 0; i < recipe.getInputItems().size(); i++)
        {
            float angleSpacing = 35f;
            float angle = 270f - ((angleSpacing / 2) * (recipe.getInputItems().size() - 1)) + (angleSpacing * i);

            int x = (int) (xCenterInput + xModInput * Math.cos(Math.toRadians(angle)));
            int y = (int) (yCenterInput - yModInput * Math.sin(Math.toRadians(angle)));

            builder.addSlot(RecipeIngredientRole.INPUT, x, y)
                    .addIngredients(recipe.getInputItems().get(i));
        }

        for(int i = 0; i < recipe.getInputEntities().size(); i++)
        {
            float angleSpacing = 35f;
            float angle = 90f + ((angleSpacing / 2) * (recipe.getInputEntities().size() - 1)) - (angleSpacing * i);

            int x = (int) (xCenterInput + xModInput * Math.cos(Math.toRadians(angle)));
            int y = (int) (yCenterInput - yModInput * Math.sin(Math.toRadians(angle)));

            builder.addSlot(RecipeIngredientRole.INPUT, x, y)
                    .addIngredient(EntityTypeIngredient.ENTITY, new EntityTypeIngredient(recipe.getInputEntities().get(i)));
        }

        int xCenterOutput = 130;
        int yCenterOutput = 34;
        int yModOutput = 0;

        if(recipe.getResultItem() != ItemStack.EMPTY && recipe.getResultEntity() != null)
            yModOutput = 15;

        if(recipe.getResultItem() != ItemStack.EMPTY)
            builder.addSlot(RecipeIngredientRole.OUTPUT, xCenterOutput, yCenterOutput + yModOutput)
                    .addItemStack(recipe.getResultItem());
        if(recipe.getResultEntity() != null)
            builder.addSlot(RecipeIngredientRole.OUTPUT, xCenterOutput, yCenterOutput - yModOutput)
                    .addIngredient(EntityTypeIngredient.ENTITY, new EntityTypeIngredient(recipe.getResultEntity()));
    }
}
