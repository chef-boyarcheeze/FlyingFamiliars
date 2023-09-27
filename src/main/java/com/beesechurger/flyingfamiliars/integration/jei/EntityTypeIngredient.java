package com.beesechurger.flyingfamiliars.integration.jei;

import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

public class EntityTypeIngredient
{
    public static final IIngredientType<EntityTypeIngredient> ENTITY = () -> EntityTypeIngredient.class;
    private final EntityType<?> type;
    @Nullable
    private Entity entity;

    public EntityTypeIngredient(String e)
    {
        type = EntityType.byString(e).orElse(null);

        if(Minecraft.getInstance().level != null && type != null)
            entity = type.create(Minecraft.getInstance().level);
    }

    EntityType<?> getEntityType()
    {
        return type;
    }

    @Nullable
    public Entity getEntity()
    {
        return entity;
    }
}
