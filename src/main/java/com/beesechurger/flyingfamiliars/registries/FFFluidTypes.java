package com.beesechurger.flyingfamiliars.registries;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.fluid.*;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FFFluidTypes {
    public static final DeferredRegister<FluidType> FLUID_TYPES_REG = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, FlyingFamiliars.MOD_ID);

    // Water:
    public static final RegistryObject<FluidType> BLUE_VITALITY_FLUID_TYPE = FLUID_TYPES_REG.register("blue_vitality_fluid", () -> new BlueVitalityFluid(FluidType.Properties.create()
            .canConvertToSource(false)
            .canExtinguish(true)
            .canDrown(true)
            .canSwim(true)
            .canHydrate(true)
            .canPushEntity(true)
            .motionScale(1)
    ));

    // Plant:
    public static final RegistryObject<FluidType> GREEN_VITALITY_FLUID_TYPE = FLUID_TYPES_REG.register("green_vitality_fluid", () -> new GreenVitalityFluid(FluidType.Properties.create()
            .canConvertToSource(false)
            .canExtinguish(false)
            .canDrown(true)
            .canSwim(true)
            .canHydrate(true)
            .canPushEntity(true)
    ));

    // Air:
    public static final RegistryObject<FluidType> YELLOW_VITALITY_FLUID_TYPE = FLUID_TYPES_REG.register("yellow_vitality_fluid", () -> new YellowVitalityFluid(FluidType.Properties.create()
            .canConvertToSource(false)
            .canExtinguish(false)
            .canDrown(false)
            .canSwim(true)
            .canHydrate(false)
            .canPushEntity(true)
    ));

    // Earth:
    public static final RegistryObject<FluidType> GOLD_VITALITY_FLUID_TYPE = FLUID_TYPES_REG.register("gold_vitality_fluid", () -> new GoldVitalityFluid(FluidType.Properties.create()
            .canConvertToSource(false)
            .canExtinguish(true)
            .canDrown(true)
            .canSwim(true)
            .canHydrate(false)
            .canPushEntity(true)
            .supportsBoating(false)
    ));

    // Red:
    public static final RegistryObject<FluidType> RED_VITALITY_FLUID_TYPE = FLUID_TYPES_REG.register("red_vitality_fluid", () -> new RedVitalityFluid(FluidType.Properties.create()
            .canConvertToSource(false)
            .canExtinguish(false)
            .canDrown(false)
            .canSwim(true)
            .canHydrate(false)
            .canPushEntity(true)
            .lightLevel(15)
            .supportsBoating(false)
    ));

    // Shadow:
    public static final RegistryObject<FluidType> BLACK_VITALITY_FLUID_TYPE = FLUID_TYPES_REG.register("black_vitality_fluid", () -> new BlackVitalityFluid(FluidType.Properties.create()
            .canConvertToSource(false)
            .canExtinguish(true)
            .canDrown(true)
            .canSwim(true)
            .canHydrate(false)
            .canPushEntity(true)
            .supportsBoating(false)
    ));

    // Light:
    public static final RegistryObject<FluidType> WHITE_VITALITY_FLUID_TYPE = FLUID_TYPES_REG.register("white_vitality_fluid", () -> new WhiteVitalityFluid(FluidType.Properties.create()
            .canConvertToSource(false)
            .canExtinguish(false)
            .canDrown(false)
            .canSwim(true)
            .canHydrate(false)
            .canPushEntity(true)
            .lightLevel(15)
    ));
}
