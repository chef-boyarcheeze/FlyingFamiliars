package com.beesechurger.flyingfamiliars.integration.jei;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.registries.FFBlocks;
import com.beesechurger.flyingfamiliars.recipe.BrazierRecipe;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BrazierRecipeCategory implements IRecipeCategory<BrazierRecipe>
{
    public static final RecipeType<BrazierRecipe> TYPE = RecipeType.create(FlyingFamiliars.MOD_ID, "brazier", BrazierRecipe.class);
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
    public RecipeType<BrazierRecipe> getRecipeType()
    {
        return TYPE;
    }

    @Override
    public Component getTitle()
    {
        return Component.translatable("block.flyingfamiliars.brazier");
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

    @Override
    public void draw(@NotNull BrazierRecipe recipe, @NotNull IRecipeSlotsView view, @NotNull GuiGraphics graphics, double mouseX, double mouseY)
    {
        RenderSystem.enableBlend();

        inputs.draw(graphics, 21, 10);

        if(recipe.getOutputItem() != ItemStack.EMPTY && recipe.getOutputEntity() != null)
        {
            doubleArrow.draw(graphics, 89, 25);
            outputs.draw(graphics, 122, 11);
            outputs.draw(graphics, 122, 41);
        }
        else
        {
            singleArrow.draw(graphics, 89, 38);
            outputs.draw(graphics, 122, 26);
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

        if(recipe.getOutputItem() != ItemStack.EMPTY && recipe.getOutputEntity() != null)
            yModOutput = 15;

        if(recipe.getOutputItem() != ItemStack.EMPTY)
            builder.addSlot(RecipeIngredientRole.OUTPUT, xCenterOutput, yCenterOutput + yModOutput)
                    .addItemStack(recipe.getOutputItem());
        if(recipe.getOutputEntity() != null)
            builder.addSlot(RecipeIngredientRole.OUTPUT, xCenterOutput, yCenterOutput - yModOutput)
                    .addIngredient(EntityTypeIngredient.ENTITY, new EntityTypeIngredient(recipe.getOutputEntity()));
    }
}
