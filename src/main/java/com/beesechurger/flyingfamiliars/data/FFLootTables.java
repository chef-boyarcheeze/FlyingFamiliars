package com.beesechurger.flyingfamiliars.data;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.item.FFItems;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.common.loot.LootTableIdCondition;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class FFLootTables
{
    public static class FFLootTableProvider extends LootTableProvider
    {
        public FFLootTableProvider(PackOutput output, Set<ResourceLocation> resourceLocations, List<SubProviderEntry> subProviderEntries)
        {
            super(output, resourceLocations, subProviderEntries);
        }

        public static class FFArchaeology implements LootTableSubProvider
        {
            public static final LootTableProvider.SubProviderEntry ARCHAEOLOGY = new LootTableProvider.SubProviderEntry(FFArchaeology::new, LootContextParamSets.ARCHAEOLOGY);

            @Override
            public void generate(BiConsumer<ResourceLocation, LootTable.Builder> writer)
            {
                ResourceLocation custom = new ResourceLocation(FlyingFamiliars.MOD_ID, "archaeology/custom");

                writer.accept(custom, LootTable.lootTable()
                    .setParamSet(LootContextParamSets.ARCHAEOLOGY)
                    .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(FFItems.EYE_OF_THE_STORM.get()).setWeight(3))
                    )
                );
            }
        }
    }

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_TABLE_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, FlyingFamiliars.MOD_ID);
    public static final RegistryObject<Codec<ArchaeologyModifier>> MODIFY_ARCHAEOLOGY_TABLE = LOOT_TABLE_MODIFIERS.register("modify_archaeology_table", () -> ArchaeologyModifier.CODEC);

    public static class FFGlobalLootModifierProvider extends GlobalLootModifierProvider
    {
        public FFGlobalLootModifierProvider(PackOutput output)
        {
            super(output, FlyingFamiliars.MOD_ID);
        }

        @Override
        protected void start()
        {
            ArchaeologyModifier.register(this);
        }
    }

    public static class ArchaeologyModifier extends LootModifier
    {
        public static final Codec<ArchaeologyModifier> CODEC = RecordCodecBuilder
            .create(inst -> codecStart(inst)
                .and(ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m -> m.item))
                .apply(inst, ArchaeologyModifier::new)
            );

        private final Item item;

        public ArchaeologyModifier(LootItemCondition[] conditionsIn, Item item)
        {
            super(conditionsIn);
            this.item = item;
        }

        @Override
        protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context)
        {
            generatedLoot.clear();
            generatedLoot.add(new ItemStack(this.item));

            return generatedLoot;
        }

        @Override
        public Codec<? extends IGlobalLootModifier> codec()
        {
            return CODEC;
        }

        private static record ArchaeologyModifierEntry(String entryName, String sourceName, String tableName, float randomChance, Item replaceItem) {}
        private static final List<ArchaeologyModifierEntry> ARCHAEOLOGY_MODIFIERS = List.of(
            new ArchaeologyModifierEntry("desert_pyramid_add_eye_of_the_storm", "minecraft", "archaeology/desert_pyramid", 0.25f, FFItems.EYE_OF_THE_STORM.get()),
            new ArchaeologyModifierEntry("desert_pyramid_add_maw_of_the_void", "minecraft", "archaeology/desert_pyramid", 0.75f, FFItems.MAW_OF_THE_VOID.get())
        );

        public static void register(FFGlobalLootModifierProvider provider)
        {
            for (var entry : ArchaeologyModifier.ARCHAEOLOGY_MODIFIERS)
            {
                provider.add(entry.entryName, new ArchaeologyModifier(
                    new LootItemCondition[] {
                        LootTableIdCondition.builder(new ResourceLocation(entry.sourceName, entry.tableName)).build(),
                        LootItemRandomChanceCondition.randomChance(entry.randomChance).build()
                    },
                    entry.replaceItem
                ));
            }
        }
    }
}