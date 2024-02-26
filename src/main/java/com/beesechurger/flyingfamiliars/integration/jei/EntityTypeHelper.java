package com.beesechurger.flyingfamiliars.integration.jei;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
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
        if (ingredient.getEntity() == null)
            return "Unknown Entity";

        return ingredient.getEntity().getDisplayName().toString();
    }

    @Override
    public @NotNull String getUniqueId(EntityTypeIngredient ingredient, @NotNull UidContext context)
    {
        return ForgeRegistries.ENTITY_TYPES.getKey(ingredient.getEntityType()).toString();
    }

    @Override
    public ResourceLocation getResourceLocation(EntityTypeIngredient ingredient)
    {
        return ForgeRegistries.ENTITY_TYPES.getKey(ingredient.getEntityType());
    }

    @Override
    public EntityTypeIngredient copyIngredient(EntityTypeIngredient ingredient)
    {
        return ingredient;
    }

    @Override
    public String getErrorInfo(@Nullable EntityTypeIngredient ingredient)
    {
        return "Entity: " + ForgeRegistries.ENTITY_TYPES.getKey(ingredient.getEntityType()).toString();
    }
}
