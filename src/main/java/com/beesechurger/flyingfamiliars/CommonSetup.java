package com.beesechurger.flyingfamiliars;

import com.beesechurger.flyingfamiliars.client.FFCommands;
import com.beesechurger.flyingfamiliars.client.FFKeys;
import com.beesechurger.flyingfamiliars.data.FFBlockTags;
import com.beesechurger.flyingfamiliars.data.FFItemTags;
import com.beesechurger.flyingfamiliars.packet.FFPackets;
import com.beesechurger.flyingfamiliars.pantheon.PantheonAffinityProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.CompletableFuture;

public class CommonSetup
{
    public static final CommonSetup INSTANCE = new CommonSetup();

    public void register(IEventBus modEventBus, IEventBus forgeEventBus)
    {
        modEventBus.addListener(this::registerPackets);
        modEventBus.addListener(this::registerData);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(this::registerKeys);

        forgeEventBus.addListener(this::registerCommands);
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

    private void registerCapabilities(RegisterCapabilitiesEvent event)
    {
        event.register(PantheonAffinityProvider.PantheonAffinity.class);
    }

    private void registerKeys(RegisterKeyMappingsEvent event)
    {
        FFKeys.FAMILIAR_ASCEND = FFKeys.registerKey("familiar_ascend", GLFW.GLFW_KEY_SPACE, "Flying Familiars", event);
        FFKeys.FAMILIAR_DESCEND = FFKeys.registerKey("familiar_descend", GLFW.GLFW_KEY_LEFT_CONTROL, "Flying Familiars", event);
        FFKeys.FAMILIAR_ACTION = FFKeys.registerKey("familiar_action", GLFW.GLFW_KEY_Y, "Flying Familiars", event);
        FFKeys.SOUL_WAND_SELECT = FFKeys.registerKey("soul_wand_select", GLFW.GLFW_KEY_V, "Flying Familiars", event);
        FFKeys.SOUL_WAND_SHIFT = FFKeys.registerKey("soul_wand_shift", GLFW.GLFW_KEY_LEFT_SHIFT, "Flying Familiars", event);

        FFKeys.WAND_EFFECT_SELECT_STATE = FFKeys.registerKeyState(FFKeys.SOUL_WAND_SELECT);
    }

    private void registerCommands(RegisterCommandsEvent event)
    {
        FFCommands.register(event.getDispatcher());
    }
}
