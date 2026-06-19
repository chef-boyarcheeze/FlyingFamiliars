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

    public final int x;
    public final int y;

    public EntityTypeIngredient(String e, int x, int y)
    {
        this.type = EntityType.byString(e).orElse(null);

        if(Minecraft.getInstance().level != null && this.type != null)
            this.entity = this.type.create(Minecraft.getInstance().level);

        this.x = x;
        this.y = y;
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
