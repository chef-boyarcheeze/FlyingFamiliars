package com.beesechurger.flyingfamiliars;

import com.beesechurger.flyingfamiliars.registries.FFPackets;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonSetup
{
    public static final CommonSetup INSTANCE = new CommonSetup();

    public void register(IEventBus modEventBus)
    {
        modEventBus.addListener(this::registerPackets);
    }

    private void registerPackets(final FMLCommonSetupEvent event)
    {
        FFPackets.register();
    }
}
