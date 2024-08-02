package com.beesechurger.flyingfamiliars.fluid;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public abstract class BaseVitalityFluid extends FluidType
{
    private final ResourceLocation stillTexture = new ResourceLocation(FlyingFamiliars.MOD_ID, "block/fluid/vitality_still");
    private final ResourceLocation flowingTexture = new ResourceLocation(FlyingFamiliars.MOD_ID, "block/fluid/vitality_flow");
    private final ResourceLocation overlayTexture = new ResourceLocation(FlyingFamiliars.MOD_ID, "block/fluid/vitality_overlay");

    protected static final int VITALITY_TYPE_WATER = 0xA8000BAB;
    protected static final int VITALITY_TYPE_PLANT = 0xA800A300;
    protected static final int VITALITY_TYPE_AIR = 0xA8FAF000;
    protected static final int VITALITY_TYPE_EARTH = 0xA8DEAA00;
    protected static final int VITALITY_TYPE_FIRE = 0xA8B02000;
    protected static final int VITALITY_TYPE_SHADOW = 0xA8202020;
    protected static final int VITALITY_TYPE_LIGHT = 0xA8F0F0F0;

    public BaseVitalityFluid(Properties properties)
    {
        super(properties);
    }

    public ResourceLocation getStillTexture()
    {
        return stillTexture;
    }

    public ResourceLocation getFlowingTexture()
    {
        return flowingTexture;
    }

    public ResourceLocation getOverlayTexture()
    {
        return overlayTexture;
    }

    abstract int getTintColorValue();

    abstract Vector3f getFogColorValue();

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer)
    {
        consumer.accept(new IClientFluidTypeExtensions()
        {
            @Override
            public ResourceLocation getStillTexture()
            {
                return stillTexture;
            }

            @Override
            public ResourceLocation getFlowingTexture()
            {
                return flowingTexture;
            }

            @Override
            public @Nullable ResourceLocation getOverlayTexture()
            {
                return overlayTexture;
            }

            @Override
            public int getTintColor()
            {
                return getTintColorValue();
            }

            @Override
            public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level,
                                                    int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor)
            {
                return getFogColorValue();
            }

            @Override
            public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick,
                                        float nearDistance, float farDistance, FogShape shape)
            {
                RenderSystem.setShaderFogStart(1f);
                RenderSystem.setShaderFogEnd(5f);
            }
        });
    }
}

