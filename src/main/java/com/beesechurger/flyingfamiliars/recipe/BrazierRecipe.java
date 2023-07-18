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
	private final NonNullList<String> entityTags;
	
	public BrazierRecipe(ResourceLocation i, ItemStack o, NonNullList<Ingredient> r, NonNullList<String> e)
	{
		id = i;
		output = o;
		recipeItems = r;
		entityTags = e;
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
	public boolean itemsMatch(NonNullList<ItemStack> items)
	{
        if(items == null) return false;
        
        List<ItemStack> handlerItems = new ArrayList<>();
        
        for (int i = 0; i < items.size(); i++)
        {
            if(!items.get(i).isEmpty()) handlerItems.add(items.get(i).copy());
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
	
	/*
	 * 	For every required entity, compare to Soul Wand stored entities.
	 * 	Allows for specific removal of entities to be used in crafting.
	 */
	public boolean entitiesMatch(NonNullList<String> entities)
	{
		if(entities == null) return false;
		
		List<String> handlerEntities = new ArrayList<>();
		
		for(int i = 0; i < entities.size(); i++)
		{
			if(entities.get(i) != "Empty") handlerEntities.add(entities.get(i));
		}
		
		for(String entity : entityTags)
		{
			boolean found = false;
			
			int i = 0;
			for(; i < handlerEntities.size(); i++)
			{
				if(handlerEntities.get(i))
			}
		}
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
	
	public NonNullList<String> getEntities()
	{
		return entityTags;
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
		
		public String entityParse(String start)
		{
			int count = 0;
			while(start.charAt(count) != '"') count++;
			
			return start.substring(0, count);
		}
		
		@Override
		public BrazierRecipe fromJson(ResourceLocation id, JsonObject json)
		{			
			JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputItems = NonNullList.withSize(ingredients.size(), Ingredient.EMPTY);

            for (int i = 0; i < inputItems.size(); i++)
            {
                inputItems.set(i, Ingredient.fromJson(ingredients.get(i)));
            }
            
            JsonArray entities = GsonHelper.getAsJsonArray(json, "entities");
            NonNullList<String> inputEntities = NonNullList.withSize(entities.size(), "");
            
            for(int i = 0; i < inputEntities.size(); i++)
            {
            	inputEntities.set(i, entityParse(entities.get(i).toString().substring(11)));
            }
            
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            return new BrazierRecipe(id, output, inputItems, inputEntities);
		}
		
		@Override
        public BrazierRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf)
		{
            NonNullList<Ingredient> inputItems = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputItems.size(); i++)
            {
                inputItems.set(i, Ingredient.fromNetwork(buf));
            }
            
            NonNullList<String> inputEntities = NonNullList.withSize(buf.readInt(), "");
            
            for(int i = 0; i < inputEntities.size(); i++)
            {
            	inputEntities.set(i, buf.readUtf());
            }
            
            ItemStack output = buf.readItem();
            return new BrazierRecipe(id, output, inputItems, inputEntities);
        }
		
		@Override
        public void toNetwork(FriendlyByteBuf buf, BrazierRecipe recipe)
		{
            buf.writeInt(recipe.getIngredients().size());
            
            for (Ingredient ing : recipe.getIngredients())
            {
                ing.toNetwork(buf);
            }
            
            buf.writeInt(recipe.getEntities().size());
            
            for (String entity : recipe.getEntities())
            {
                buf.writeUtf(entity);
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
