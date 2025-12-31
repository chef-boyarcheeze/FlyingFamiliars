package com.beesechurger.flyingfamiliars.recipe;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.datafix.fixes.ItemStackTagFix;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

import static com.beesechurger.flyingfamiliars.util.FFConstants.STORAGE_EMPTY;

public class TectonicCrushRecipe implements Recipe<SimpleContainer>
{
    private final ResourceLocation id;
    private final ItemStack outputItem;
    private final ItemStack inputItem;

    public TectonicCrushRecipe(ResourceLocation id, ItemStack outputItem, ItemStack inputItem)
    {
        this.id = id;
        this.outputItem = outputItem;
        this.inputItem = inputItem;
    }

    @Override
    public boolean matches(SimpleContainer container, Level level)
    {
        return false;
    }

    /*
     * 	For every ingredient in a recipe, compare to block's stored items.
     * 	Allows for any order of stored ingredients to be used in crafting.
     */
    public boolean itemMatches(ItemStack item)
    {
        if(item == null)
            return false;

        return this.inputItem.getItem() == item.getItem();
    }

    @Override
    public ItemStack assemble(SimpleContainer container, RegistryAccess var)
    {
        return outputItem;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_)
    {
        return true;
    }

    public ItemStack getInputItem()
    {
        return inputItem.copy();
    }

    @Override
    public ItemStack getResultItem(RegistryAccess var)
    {
        return outputItem.copy();
    }

    public ItemStack getOutputItem()
    {
        return outputItem.copy();
    }

    @Override
    public ResourceLocation getId()
    {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer()
    {
        return TectonicCrushRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType()
    {
        return TectonicCrushRecipe.Type.INSTANCE;
    }

    public static class Type implements RecipeType<TectonicCrushRecipe>
    {
        private Type() {}
        public static final TectonicCrushRecipe.Type INSTANCE = new TectonicCrushRecipe.Type();
        public static final String ID = "tectonic_crush";
    }

    public static class Serializer implements RecipeSerializer<TectonicCrushRecipe>
    {
        public static final TectonicCrushRecipe.Serializer INSTANCE = new TectonicCrushRecipe.Serializer();
        public static final ResourceLocation ID = new ResourceLocation(FlyingFamiliars.MOD_ID, "tectonic_crush");

        @Override
        public TectonicCrushRecipe fromJson(ResourceLocation id, JsonObject json)
        {
            ItemStack inputItem = GsonHelper.getAsJsonObject(json, "inputItem").size() != 0 ? ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "inputItem")) : ItemStack.EMPTY;
            ItemStack outputItem = GsonHelper.getAsJsonObject(json, "outputItem").size() != 0 ? ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "outputItem")) : ItemStack.EMPTY;

            return new TectonicCrushRecipe(id, outputItem, inputItem);
        }

        @Override
        public TectonicCrushRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf)
        {
            ItemStack outputItem = buf.readItem();
            ItemStack inputItem = buf.readItem();

            return new TectonicCrushRecipe(id, outputItem, inputItem);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, TectonicCrushRecipe recipe)
        {
            buf.writeItemStack(recipe.getOutputItem(), false);
            buf.writeItemStack(recipe.getInputItem(), false);
        }

        @SuppressWarnings("unchecked")
        private static <G> Class<G> castClass(Class<?> cls)
        {
            return (Class<G>)cls;
        }
    }
}
