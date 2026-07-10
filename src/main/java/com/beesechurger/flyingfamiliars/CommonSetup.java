package com.beesechurger.flyingfamiliars;

import com.beesechurger.flyingfamiliars.registries.FFBlockTags;
import com.beesechurger.flyingfamiliars.registries.FFItemTags;
import com.beesechurger.flyingfamiliars.registries.FFPackets;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.concurrent.CompletableFuture;

public class CommonSetup
{
    public static final CommonSetup INSTANCE = new CommonSetup();

    public void register(IEventBus modEventBus)
    {
        modEventBus.addListener(this::registerPackets);
        modEventBus.addListener(this::registerData);
    }

    private void registerPackets(final FMLCommonSetupEvent event)
    {
        FFPackets.register();
    }

    private void registerData(GatherDataEvent event)
    {
        if (event.includeServer())
        {
            DataGenerator gen = event.getGenerator();
            PackOutput output = gen.getPackOutput();
            CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
            ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

            BlockTagsProvider blockTags = new FFBlockTags(output, lookupProvider, existingFileHelper);
            ItemTagsProvider itemTags = new FFItemTags(output, lookupProvider, blockTags.contentsGetter(), existingFileHelper);

            gen.addProvider(true, blockTags);
            gen.addProvider(true, itemTags);
        }
    }
}
