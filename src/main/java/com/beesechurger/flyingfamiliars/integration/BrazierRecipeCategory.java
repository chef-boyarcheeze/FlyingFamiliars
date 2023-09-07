package com.beesechurger.flyingfamiliars.integration;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.blocks.FFBlocks;
import com.beesechurger.flyingfamiliars.recipe.BrazierRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class BrazierRecipeCategory implements IRecipeCategory<BrazierRecipe>
{
    public final static ResourceLocation UID = new ResourceLocation(FlyingFamiliars.MOD_ID, "brazier");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/block/brazier/brazier.png");

    private final IDrawable background;
    private final IDrawable icon;

    public BrazierRecipeCategory(IGuiHelper helper)
    {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
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
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull BrazierRecipe recipe, @NotNull IFocusGroup focuses)
    {
        int xCenter = 45;
        int yCenter = 35;
        int xMod = 30;
        int yMod = 25;

        for(int i = 0; i < recipe.getInputItems().size(); i++)
        {
            float angleSpacing = 30f;
            float angle = 270f - ((angleSpacing / 2) * (recipe.getInputItems().size() - 1)) + (angleSpacing * i);

            int x = (int) (xCenter + xMod * Math.cos(Math.toRadians(angle)));
            int y = (int) (yCenter - yMod * Math.sin(Math.toRadians(angle)));

            Ingredient inputItem = recipe.getInputItems().get(i);
            builder.addSlot(RecipeIngredientRole.INPUT, x, y)
                    .addIngredients(inputItem);
        }

        for(int i = 0; i < recipe.getInputEntities().size(); i++)
        {
            float angleSpacing = 30f;
            float angle = 90f + ((angleSpacing / 2) * (recipe.getInputEntities().size() - 1)) - (angleSpacing * i);

            int x = (int) (xCenter + xMod * Math.cos(Math.toRadians(angle)));
            int y = (int) (yCenter - yMod * Math.sin(Math.toRadians(angle)));

            builder.addSlot(RecipeIngredientRole.INPUT, x, y)
                    .addIngredient(EntityTypeIngredient.ENTITY, new EntityTypeIngredient(recipe.getInputEntities().get(i)));
        }

        if(recipe.getResultItem() != null)
            builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 50)
                    .addItemStack(recipe.getResultItem());
        if(recipe.getResultEntity() != null)
            builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 20)
                    .addIngredient(EntityTypeIngredient.ENTITY, new EntityTypeIngredient(recipe.getResultEntity()));
    }
}
