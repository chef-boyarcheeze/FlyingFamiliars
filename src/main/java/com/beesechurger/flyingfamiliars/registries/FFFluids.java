package com.beesechurger.flyingfamiliars.registries;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FFFluids
{
    public static final DeferredRegister<Fluid> FLUIDS_REG = DeferredRegister.create(ForgeRegistries.FLUIDS, FlyingFamiliars.MOD_ID);

    public static final RegistryObject<FlowingFluid> SOURCE_BLUE_VITALITY = FLUIDS_REG.register("blue_vitality_fluid", () -> new ForgeFlowingFluid.Source(FFFluids.BLUE_VITALITY_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_BLUE_VITALITY = FLUIDS_REG.register("flowing_blue_vitality", () -> new ForgeFlowingFluid.Flowing(FFFluids.BLUE_VITALITY_FLUID_PROPERTIES));

    public static final ForgeFlowingFluid.Properties BLUE_VITALITY_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            FFFluidTypes.BLUE_VITALITY_FLUID_TYPE, SOURCE_BLUE_VITALITY, FLOWING_BLUE_VITALITY)
            .slopeFindDistance(2)
            .levelDecreasePerBlock(1)
            .block(FFBlocks.BLUE_VITALITY_BLOCK)
            .bucket(FFItems.BLUE_VITALITY_BUCKET);

    public static final RegistryObject<FlowingFluid> SOURCE_GREEN_VITALITY = FLUIDS_REG.register("green_vitality_fluid", () -> new ForgeFlowingFluid.Source(FFFluids.GREEN_VITALITY_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_GREEN_VITALITY = FLUIDS_REG.register("flowing_green_vitality", () -> new ForgeFlowingFluid.Flowing(FFFluids.GREEN_VITALITY_FLUID_PROPERTIES));

    public static final ForgeFlowingFluid.Properties GREEN_VITALITY_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            FFFluidTypes.GREEN_VITALITY_FLUID_TYPE, SOURCE_GREEN_VITALITY, FLOWING_GREEN_VITALITY)
            .slopeFindDistance(2)
            .levelDecreasePerBlock(1)
            .block(FFBlocks.GREEN_VITALITY_BLOCK)
            .bucket(FFItems.GREEN_VITALITY_BUCKET);

    public static final RegistryObject<FlowingFluid> SOURCE_YELLOW_VITALITY = FLUIDS_REG.register("yellow_vitality_fluid", () -> new ForgeFlowingFluid.Source(FFFluids.YELLOW_VITALITY_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_YELLOW_VITALITY = FLUIDS_REG.register("flowing_yellow_vitality", () -> new ForgeFlowingFluid.Flowing(FFFluids.YELLOW_VITALITY_FLUID_PROPERTIES));

    public static final ForgeFlowingFluid.Properties YELLOW_VITALITY_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            FFFluidTypes.YELLOW_VITALITY_FLUID_TYPE, SOURCE_YELLOW_VITALITY, FLOWING_YELLOW_VITALITY)
            .slopeFindDistance(2)
            .levelDecreasePerBlock(1)
            .block(FFBlocks.YELLOW_VITALITY_BLOCK)
            .bucket(FFItems.YELLOW_VITALITY_BUCKET);

    public static final RegistryObject<FlowingFluid> SOURCE_GOLD_VITALITY = FLUIDS_REG.register("gold_vitality_fluid", () -> new ForgeFlowingFluid.Source(FFFluids.GOLD_VITALITY_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_GOLD_VITALITY = FLUIDS_REG.register("flowing_gold_vitality", () -> new ForgeFlowingFluid.Flowing(FFFluids.GOLD_VITALITY_FLUID_PROPERTIES));

    public static final ForgeFlowingFluid.Properties GOLD_VITALITY_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            FFFluidTypes.GOLD_VITALITY_FLUID_TYPE, SOURCE_GOLD_VITALITY, FLOWING_GOLD_VITALITY)
            .slopeFindDistance(2)
            .levelDecreasePerBlock(1)
            .block(FFBlocks.GOLD_VITALITY_BLOCK)
            .bucket(FFItems.GOLD_VITALITY_BUCKET);

    public static final RegistryObject<FlowingFluid> SOURCE_RED_VITALITY = FLUIDS_REG.register("red_vitality_fluid", () -> new ForgeFlowingFluid.Source(FFFluids.RED_VITALITY_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_RED_VITALITY = FLUIDS_REG.register("flowing_red_vitality", () -> new ForgeFlowingFluid.Flowing(FFFluids.RED_VITALITY_FLUID_PROPERTIES));

    public static final ForgeFlowingFluid.Properties RED_VITALITY_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            FFFluidTypes.RED_VITALITY_FLUID_TYPE, SOURCE_RED_VITALITY, FLOWING_RED_VITALITY)
            .slopeFindDistance(2)
            .levelDecreasePerBlock(1)
            .block(FFBlocks.RED_VITALITY_BLOCK)
            .bucket(FFItems.RED_VITALITY_BUCKET);

    public static final RegistryObject<FlowingFluid> SOURCE_BLACK_VITALITY = FLUIDS_REG.register("black_vitality_fluid", () -> new ForgeFlowingFluid.Source(FFFluids.BLACK_VITALITY_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_BLACK_VITALITY = FLUIDS_REG.register("flowing_black_vitality", () -> new ForgeFlowingFluid.Flowing(FFFluids.BLACK_VITALITY_FLUID_PROPERTIES));

    public static final ForgeFlowingFluid.Properties BLACK_VITALITY_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            FFFluidTypes.BLACK_VITALITY_FLUID_TYPE, SOURCE_BLACK_VITALITY, FLOWING_BLACK_VITALITY)
            .slopeFindDistance(2)
            .levelDecreasePerBlock(1)
            .block(FFBlocks.BLACK_VITALITY_BLOCK)
            .bucket(FFItems.BLACK_VITALITY_BUCKET);

    public static final RegistryObject<FlowingFluid> SOURCE_WHITE_VITALITY = FLUIDS_REG.register("white_vitality_fluid", () -> new ForgeFlowingFluid.Source(FFFluids.WHITE_VITALITY_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> FLOWING_WHITE_VITALITY = FLUIDS_REG.register("flowing_white_vitality", () -> new ForgeFlowingFluid.Flowing(FFFluids.WHITE_VITALITY_FLUID_PROPERTIES));

    public static final ForgeFlowingFluid.Properties WHITE_VITALITY_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(
            FFFluidTypes.WHITE_VITALITY_FLUID_TYPE, SOURCE_WHITE_VITALITY, FLOWING_WHITE_VITALITY)
            .slopeFindDistance(2)
            .levelDecreasePerBlock(1)
            .block(FFBlocks.WHITE_VITALITY_BLOCK)
            .bucket(FFItems.WHITE_VITALITY_BUCKET);
}
