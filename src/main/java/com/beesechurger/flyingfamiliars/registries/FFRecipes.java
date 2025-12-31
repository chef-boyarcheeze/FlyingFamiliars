package com.beesechurger.flyingfamiliars.registries;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.recipe.BrazierRecipe;
import com.beesechurger.flyingfamiliars.recipe.TectonicSunderRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.beesechurger.flyingfamiliars.wand_effect.common.WandEffectItemHelper.EFFECT_TECTONIC_SUNDER;

@Mod.EventBusSubscriber(modid = FlyingFamiliars.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FFRecipes
{
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER_REG = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, FlyingFamiliars.MOD_ID);
	
	public static final RegistryObject<RecipeSerializer<BrazierRecipe>> BRAZIER_SERIALIZER = RECIPE_SERIALIZER_REG.register("brazier", () -> BrazierRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeSerializer<TectonicSunderRecipe>> TECTONIC_CRUSH_SERIALIZER = RECIPE_SERIALIZER_REG.register(EFFECT_TECTONIC_SUNDER, () -> TectonicSunderRecipe.Serializer.INSTANCE);
}