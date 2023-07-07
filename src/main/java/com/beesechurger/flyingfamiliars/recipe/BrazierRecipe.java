package com.beesechurger.flyingfamiliars.recipe;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

public class BrazierRecipe implements Recipe<SimpleContainer>
{
	private final ResourceLocation id;
	private final ItemStack output;
	private final NonNullList<Ingredient> recipeItems;
	
	public BrazierRecipe(ResourceLocation i, ItemStack o, NonNullList<Ingredient> r)
	{
		id = i;
		output = o;
		recipeItems = r;
	}
	
	@Override
	public boolean matches(SimpleContainer container, Level level)
	{
		return false;
	}
	
	/*	
	 * 	For every ingredient in a recipe, compare to brazier's stored items.
	 * 	Allows for any order of ingredients to craft a recipe.
	 */	
	public boolean matches(NonNullList<ItemStack> items)
	{
        if(items == null) return false;
        
        List<ItemStack> handlerItems = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            if (!items.get(i).isEmpty()) handlerItems.add(items.get(i).copy());
        }
        for(Ingredient ingredient : recipeItems)
        {
            boolean found = false;
            for (ItemStack stack : ingredient.getItems())
            {
                int i = 0;
                for (; i < handlerItems.size(); i++)
                {
                    if (handlerItems.get(i).sameItem(stack))
                    {
                        found = true;
                        break;
                    }
                }
                if (found)
                {
                	handlerItems.remove(i);
                    break;
                }
            }
            if (!found) return false;
        }
        return handlerItems.size() == 0;
    }

	@Override
	public ItemStack assemble(SimpleContainer container)
	{
		return output;
	}

	@Override
	public boolean canCraftInDimensions(int p_43999_, int p_44000_)
	{
		return true;
	}

	@Override
	public ItemStack getResultItem()
	{
		return output.copy();
	}

	@Override
	public ResourceLocation getId()
	{
		return id;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return Serializer.INSTANCE;
	}

	@Override
	public RecipeType<?> getType()
	{
		return Type.INSTANCE;
	}
	
	public static class Type implements RecipeType<BrazierRecipe>
	{
		private Type() {}
		public static final Type INSTANCE = new Type();
		public static final String ID = "brazier";
	}
	
	public static class Serializer implements RecipeSerializer<BrazierRecipe>
	{
		public static final Serializer INSTANCE = new Serializer();
		public static final ResourceLocation ID = new ResourceLocation(FlyingFamiliars.MOD_ID, "brazier");
		
		@Override
		public BrazierRecipe fromJson(ResourceLocation id, JsonObject json)
		{
			ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
			
			JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(ingredients.size(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++)
            {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new BrazierRecipe(id, output, inputs);
		}
		
		@Override
        public BrazierRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++)
            {
                inputs.set(i, Ingredient.fromNetwork(buf));
            }

            ItemStack output = buf.readItem();
            return new BrazierRecipe(id, output, inputs);
        }
		
		@Override
        public void toNetwork(FriendlyByteBuf buf, BrazierRecipe recipe)
		{
            buf.writeInt(recipe.getIngredients().size());
            
            for (Ingredient ing : recipe.getIngredients())
            {
                ing.toNetwork(buf);
            }
            
            buf.writeItemStack(recipe.getResultItem(), false);
        }

        @Override
        public RecipeSerializer<?> setRegistryName(ResourceLocation name)
        {
            return INSTANCE;
        }

        @Nullable
        @Override
        public ResourceLocation getRegistryName()
        {
            return ID;
        }

        @Override
        public Class<RecipeSerializer<?>> getRegistryType()
        {
            return Serializer.castClass(RecipeSerializer.class);
        }

        @SuppressWarnings("unchecked")
        private static <G> Class<G> castClass(Class<?> cls)
        {
            return (Class<G>)cls;
        }
	}
}
