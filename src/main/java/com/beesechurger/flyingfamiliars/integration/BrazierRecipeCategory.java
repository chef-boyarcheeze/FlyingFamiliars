package com.beesechurger.flyingfamiliars.integration;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.recipe.BrazierRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BrazierRecipeCategory implements IRecipeCategory<BrazierRecipe>
{
    public final static ResourceLocation UID = new ResourceLocation(FlyingFamiliars.MOD_ID, "brazier");

    public BrazierRecipeCategory(IGuiHelper helper)
    {
    }

    @Override
    public Component getTitle()
    {
        return new TranslatableComponent("block.flyingfamiliars.brazier");
    }

    @Override
    public IDrawable getBackground()
    {
        return null;
    }

    @Override
    public IDrawable getIcon()
    {
        return null;
    }

    @Override
    public ResourceLocation getUid()
    {
        return UID;
    }

    @Override
    public Class<? extends BrazierRecipe> getRecipeClass()
    {
        return BrazierRecipe.class;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull BrazierRecipe recipe, @NotNull IFocusGroup focuses)
    {
        IRecipeCategory.super.setRecipe(builder, recipe, focuses);
    }
}
