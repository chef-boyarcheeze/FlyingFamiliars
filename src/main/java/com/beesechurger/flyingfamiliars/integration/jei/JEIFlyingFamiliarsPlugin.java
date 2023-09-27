package com.beesechurger.flyingfamiliars.integration.jei;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.blocks.FFBlocks;
import com.beesechurger.flyingfamiliars.recipe.BrazierRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEIFlyingFamiliarsPlugin implements IModPlugin
{
    @Override
    public @NotNull ResourceLocation getPluginUid()
    {
        return new ResourceLocation(FlyingFamiliars.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration)
    {
        registration.addRecipeCategories(new BrazierRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration)
    {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<BrazierRecipe> recipes = rm.getAllRecipesFor(BrazierRecipe.Type.INSTANCE);
        registration.addRecipes(new RecipeType<>(BrazierRecipeCategory.UID, BrazierRecipe.class), recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
    {
        registration.addRecipeCatalyst(new ItemStack(FFBlocks.BRAZIER.get()), new RecipeType<>(BrazierRecipeCategory.UID, BrazierRecipe.class));
    }

    @Override
    public void registerIngredients(@NotNull IModIngredientRegistration registration)
    {
        registration.register(EntityTypeIngredient.ENTITY, List.of(), new EntityTypeHelper(), new EntityTypeRenderer(16));
    }
}
