package com.beesechurger.flyingfamiliars.recipe;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import static com.beesechurger.flyingfamiliars.wand_effect.common.WandEffectItemHelper.WAND_EFFECT_TECTONIC_SUNDER;

public class TectonicSunderRecipe implements Recipe<SimpleContainer>
{
    private final ResourceLocation id;
    private final ItemStack outputItem;
    private final ItemStack inputItem;

    public TectonicSunderRecipe(ResourceLocation id, ItemStack outputItem, ItemStack inputItem)
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
        return TectonicSunderRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType()
    {
        return TectonicSunderRecipe.Type.INSTANCE;
    }

    public static class Type implements RecipeType<TectonicSunderRecipe>
    {
        private Type() {}
        public static final TectonicSunderRecipe.Type INSTANCE = new TectonicSunderRecipe.Type();
        public static final String ID = WAND_EFFECT_TECTONIC_SUNDER;
    }

    public static class Serializer implements RecipeSerializer<TectonicSunderRecipe>
    {
        public static final TectonicSunderRecipe.Serializer INSTANCE = new TectonicSunderRecipe.Serializer();
        public static final ResourceLocation ID = new ResourceLocation(FlyingFamiliars.MOD_ID, WAND_EFFECT_TECTONIC_SUNDER);

        @Override
        public TectonicSunderRecipe fromJson(ResourceLocation id, JsonObject json)
        {
            ItemStack inputItem = GsonHelper.getAsJsonObject(json, "inputItem").size() != 0 ? ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "inputItem")) : ItemStack.EMPTY;
            ItemStack outputItem = GsonHelper.getAsJsonObject(json, "outputItem").size() != 0 ? ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "outputItem")) : ItemStack.EMPTY;

            return new TectonicSunderRecipe(id, outputItem, inputItem);
        }

        @Override
        public TectonicSunderRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf)
        {
            ItemStack outputItem = buf.readItem();
            ItemStack inputItem = buf.readItem();

            return new TectonicSunderRecipe(id, outputItem, inputItem);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, TectonicSunderRecipe recipe)
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
