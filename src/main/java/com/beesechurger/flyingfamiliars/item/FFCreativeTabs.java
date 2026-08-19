package com.beesechurger.flyingfamiliars.item;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.block.FFBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class FFCreativeTabs
{
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FlyingFamiliars.MOD_ID);

    public static final RegistryObject<CreativeModeTab> FLYING_FAMILIARS = CREATIVE_TABS.register("flying_familiars", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.flyingfamiliars.flying_familiars"))
            .icon(() -> new ItemStack(FFItems.WATER_SCEPTRE.get()))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .displayItems((enabledFeatures, output) -> {
                // Wands:
                output.accept(FFItems.WATER_SCEPTRE.get());
                output.accept(FFItems.VERDANT_BOUGH.get());
                output.accept(FFItems.TEMPEST_STAFF.get());
                output.accept(FFItems.FISSURE_BATON.get());
                output.accept(FFItems.FIERY_CROOK.get());
                output.accept(FFItems.LIGHT_PRISM.get());
                output.accept(FFItems.VOID_SHARD.get());

                // Other Tools:
                output.accept(FFItems.PHYLACTERY.get());
                output.accept(FFItems.CODEX.get());

                // Type items:
                output.accept(FFItems.SOUL_WAND_ROD.get());
                output.accept(FFItems.PELT_OF_THE_FOREST.get());
                output.accept(FFItems.EYE_OF_THE_STORM.get());
                output.accept(FFItems.BONES_OF_THE_CAVERN.get());
                output.accept(FFItems.HAND_OF_THE_HELLS.get());
                output.accept(FFItems.STRAND_OF_THE_LIGHT.get());
                output.accept(FFItems.MAW_OF_THE_VOID.get());

                // Spirit Fragments:
                output.accept(FFItems.WET_SPIRIT_FRAGMENT.get());
                output.accept(FFItems.LUSH_SPIRIT_FRAGMENT.get());
                output.accept(FFItems.GUSTING_SPIRIT_FRAGMENT.get());
                output.accept(FFItems.STONY_SPIRIT_FRAGMENT.get());
                output.accept(FFItems.BURNING_SPIRIT_FRAGMENT.get());
                output.accept(FFItems.LUMINOUS_SPIRIT_FRAGMENT.get());
                output.accept(FFItems.VACUOUS_SPIRIT_FRAGMENT.get());

                // Crafting items:
                output.accept(FFItems.CEREMONIAL_FONT_BASIN.get());

                // Familiar items:
                output.accept(FFItems.PHOENIX_FEATHER.get());

                // Familiar spawn eggs:
                // Water:
                output.accept(FFItems.CLOUD_RAY_SPAWN_EGG.get());
                output.accept(FFItems.DEEP_JELLYFISH_SPAWN_EGG.get());

                // Plant:
                output.accept(FFItems.GRIFFONFLY_SPAWN_EGG.get());
                output.accept(FFItems.SHRUBLING_SPAWN_EGG.get());

                // Air:
                // thunderbird
                output.accept(FFItems.ZEPHYR_FISH_SPAWN_EGG.get());

                // Earth:
                output.accept(FFItems.MAGIC_CARPET_SPAWN_EGG.get());
                output.accept(FFItems.CRYSTAL_TRESSYM_SPAWN_EGG.get());

                // Fire:
                // dragon
                output.accept(FFItems.PHOENIX_SPAWN_EGG.get());

                // Void:
                output.accept(FFItems.SHADEWYRM_SPAWN_EGG.get());
                output.accept(FFItems.VOID_MOTH_SPAWN_EGG.get());

                // Light:
                // sundog
                output.accept(FFItems.MIRROR_SHIELD_SPAWN_EGG.get());

                // Normal blocks:
                output.accept(FFBlocks.RUNIC_BRICKS.get());
                output.accept(FFBlocks.RUNIC_BRICK_SLAB.get());
                output.accept(FFBlocks.RUNIC_BRICK_STAIRS.get());
                output.accept(FFBlocks.RUNIC_BRICK_WALL.get());

                output.accept(FFBlocks.INSCRIBED_RUNIC_BRICKS.get());
                output.accept(FFBlocks.INSCRIBED_RUNIC_BRICK_SLAB.get());
                output.accept(FFBlocks.INSCRIBED_RUNIC_BRICK_STAIRS.get());
                output.accept(FFBlocks.INSCRIBED_RUNIC_BRICK_WALL.get());

                // Block entities:
                output.accept(FFBlocks.BRAZIER.get());
                output.accept(FFBlocks.RUNIC_PEDESTAL.get());
                output.accept(FFBlocks.OBELISK.get());
                output.accept(FFBlocks.VITA_ALEMBIC.get());
                output.accept(FFBlocks.CEREMONIAL_FONT.get());
            })
            .build()
    );
}
