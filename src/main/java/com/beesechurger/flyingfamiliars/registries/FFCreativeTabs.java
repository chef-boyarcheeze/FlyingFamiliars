package com.beesechurger.flyingfamiliars.registries;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class FFCreativeTabs
{
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TAB_REG = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FlyingFamiliars.MOD_ID);

    public static final RegistryObject<CreativeModeTab> FAMILIARS = CREATIVE_TAB_REG.register("familiars", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.flyingfamiliars.familiars"))
            .icon(() -> new ItemStack(FFItems.PHOENIX_FEATHER.get()))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .displayItems((enabledFeatures, output) -> {

                // Familiar spawn eggs
                output.accept(FFItems.CLOUD_RAY_SPAWN_EGG.get());
                output.accept(FFItems.CORMORANT_SPAWN_EGG.get());
                output.accept(FFItems.GRIFFONFLY_SPAWN_EGG.get());
                output.accept(FFItems.MAGIC_CARPET_SPAWN_EGG.get());
                output.accept(FFItems.PHOENIX_SPAWN_EGG.get());
                output.accept(FFItems.VOID_MOTH_SPAWN_EGG.get());

                // Familiar items
                output.accept(FFItems.PHOENIX_FEATHER.get());
                output.accept(FFItems.CORMORANT_RING.get());
            })
            .build());

    public static final RegistryObject<CreativeModeTab> WANDS_AND_TOOLS = CREATIVE_TAB_REG.register("wands_and_tools", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.flyingfamiliars.wands_and_tools"))
            .icon(() -> new ItemStack(FFItems.WATER_SCEPTRE.get()))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .withTabsBefore(FAMILIARS.getKey())
            .displayItems((enabledFeatures, output) -> {

                // Wands
                output.accept(FFItems.WATER_SCEPTRE.get());
                output.accept(FFItems.VERDANT_BOUGH.get());
                output.accept(FFItems.TEMPEST_STAFF.get());
                output.accept(FFItems.FISSURE_BATON.get());
                output.accept(FFItems.FIERY_CROOK.get());
                output.accept(FFItems.VOID_SHARD.get());
                output.accept(FFItems.LIGHT_PRISM.get());

                // Phylacteries
                output.accept(FFItems.PHYLACTERY_BLUE.get());
                output.accept(FFItems.PHYLACTERY_GREEN.get());
                output.accept(FFItems.PHYLACTERY_YELLOW.get());
                output.accept(FFItems.PHYLACTERY_GOLD.get());
                output.accept(FFItems.PHYLACTERY_RED.get());
                output.accept(FFItems.PHYLACTERY_BLACK.get());
                output.accept(FFItems.PHYLACTERY_WHITE.get());

                // Misc items
                output.accept(FFItems.SOUL_WAND_ROD.get());
                output.accept(FFItems.PELT_OF_THE_FOREST.get());
                output.accept(FFItems.EYE_OF_THE_STORM.get());
                output.accept(FFItems.BONES_OF_THE_EARTH.get());
                output.accept(FFItems.HAND_OF_THE_HELLS.get());
                output.accept(FFItems.PEARL_OF_THE_VOID.get());
                output.accept(FFItems.STRAND_OF_THE_LIGHT.get());
            })
            .build());

    public static final RegistryObject<CreativeModeTab> BLOCKS = CREATIVE_TAB_REG.register("blocks", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.flyingfamiliars.blocks"))
            .icon(() -> new ItemStack(FFBlocks.BRAZIER.get()))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .withTabsBefore(FAMILIARS.getKey())
            .withTabsBefore(WANDS_AND_TOOLS.getKey())
            .displayItems((enabledFeatures, output) -> {

                // Normal blocks
                output.accept(FFBlocks.RUNIC_BRICKS.get());
                output.accept(FFBlocks.RUNIC_BRICK_SLAB.get());

                output.accept(FFBlocks.INSCRIBED_RUNIC_BRICKS.get());
                output.accept(FFBlocks.INSCRIBED_RUNIC_BRICK_SLAB.get());

                // Block entities
                output.accept(FFBlocks.BRAZIER.get());
                output.accept(FFBlocks.OBELISK.get());
            })
            .build());
}
