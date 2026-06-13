package com.beesechurger.flyingfamiliars.integration.jei;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.recipe.TectonicSunderRecipe;
import com.mojang.blaze3d.systems.RenderSystem;
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
import org.jetbrains.annotations.NotNull;

public class TectonicSunderRecipeCategory implements IRecipeCategory<TectonicSunderRecipe>
{
    public static final RecipeType<TectonicSunderRecipe> TYPE = RecipeType.create(FlyingFamiliars.MOD_ID, "tectonic_sunder", TectonicSunderRecipe.class);
    public static final ResourceLocation INPUTS =
            new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/gui/brazier_inputs_jei.png");
    public static final ResourceLocation SINGLE_ARROW =
            new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/gui/brazier_single_arrow_jei.png");
    public static final ResourceLocation DOUBLE_ARROW =
            new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/gui/brazier_double_arrow_jei.png");
    public static final ResourceLocation OUTPUTS =
            new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/gui/brazier_outputs_jei.png");

    private final IDrawable background;
    private final IDrawable singleArrow;

    public TectonicSunderRecipeCategory(IGuiHelper helper)
    {
        this.background = helper.createBlankDrawable(176, 85);
        this.singleArrow = helper.createDrawable(SINGLE_ARROW, 0, 0, 64, 64);
    }

    @Override
    public RecipeType<TectonicSunderRecipe> getRecipeType()
    {
        return TYPE;
    }

    @Override
    public Component getTitle()
    {
        return Component.translatable("tooltip.flyingfamiliars.wand_effect.tectonic_sunder_charm");
    }

    @Override
    public IDrawable getBackground()
    {
        return background;
    }

    @Override
    public IDrawable getIcon()
    {
        return singleArrow;
    }

    @Override
    public void draw(@NotNull TectonicSunderRecipe recipe, @NotNull IRecipeSlotsView view, @NotNull GuiGraphics graphics, double mouseX, double mouseY)
    {
        RenderSystem.enableBlend();

        //inputs.draw(graphics, 21, 10);

/*        if(recipe.getOutputItem() != ItemStack.EMPTY && recipe.getOutputEntity() != null)
        {
            doubleArrow.draw(graphics, 89, 25);
            outputs.draw(graphics, 122, 11);
            outputs.draw(graphics, 122, 41);
        }
        else*/
        {
            //singleArrow.draw(graphics, 89, 38);
            //outputs.draw(graphics, 122, 26);
        }

        RenderSystem.disableBlend();
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull TectonicSunderRecipe recipe, @NotNull IFocusGroup focuses)
    {
        int xCenterInput = 40;
        int yCenterInput = 35;

        builder.addSlot(RecipeIngredientRole.INPUT, xCenterInput, yCenterInput)
                .addItemStack(recipe.getInputItem());

        int xCenterOutput = 122;
        int yCenterOutput = 35;

        builder.addSlot(RecipeIngredientRole.OUTPUT, xCenterOutput, yCenterOutput)
                .addItemStack(recipe.getOutputItem());
    }
}
