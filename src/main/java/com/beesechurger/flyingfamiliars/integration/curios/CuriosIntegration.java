package com.beesechurger.flyingfamiliars.integration.curios;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.CURIOS_MODNAME;

public class CuriosIntegration
{
    private static CuriosIntegration instance;

    public static void register()
    {
        instance = new CuriosIntegration();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(CuriosIntegration::sendImc);
    }

    public static void sendImc(final InterModEnqueueEvent event)
    {
        InterModComms.sendTo(CURIOS_MODNAME, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.CHARM.getMessageBuilder().build());
    }

    public static LazyOptional<IItemHandlerModifiable> getAllWorn(LivingEntity living)
    {
        return instance.getAllWornItems(living);
    }

    public LazyOptional<IItemHandlerModifiable> getAllWornItems(LivingEntity living)
    {
        return CuriosApi.getCuriosHelper().getEquippedCurios(living);
    }
}
