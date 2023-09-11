package com.beesechurger.flyingfamiliars.integration;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityTypeHelper implements IIngredientHelper<EntityTypeIngredient>
{
    @Override
    public @NotNull IIngredientType<EntityTypeIngredient> getIngredientType()
    {
        return EntityTypeIngredient.ENTITY;
    }

    @Override
    public @NotNull String getDisplayName(EntityTypeIngredient ingredient)
    {
        return ingredient.getEntity().getDisplayName().toString();
    }

    @Override
    public @NotNull String getUniqueId(EntityTypeIngredient ingredient, @NotNull UidContext context)
    {
        return ingredient.getEntityType().getRegistryName().toString();
    }

    @SuppressWarnings("removal")
    @Override
    public @NotNull String getModId(EntityTypeIngredient ingredient)
    {
        return ingredient.getEntityType().getRegistryName().getNamespace();
    }

    @SuppressWarnings("removal")
    @Override
    public String getResourceId(EntityTypeIngredient ingredient)
    {
        return ingredient.getEntityType().getRegistryName().getPath();
    }

    @Override
    public ResourceLocation getResourceLocation(EntityTypeIngredient ingredient)
    {
        return ingredient.getEntityType().getRegistryName();
    }

    @Override
    public EntityTypeIngredient copyIngredient(EntityTypeIngredient ingredient)
    {
        return ingredient;
    }

    @Override
    public String getErrorInfo(@Nullable EntityTypeIngredient ingredient)
    {
        return "Entity: " + ingredient.getEntityType().getRegistryName().toString();
    }
}
