package com.beesechurger.flyingfamiliars.fluid;

import org.joml.Vector3f;

public class BlueVitalityFluid extends BaseVitalityFluid
{
    public BlueVitalityFluid(Properties properties)
    {
        super(properties);
    }

    @Override
    int getTintColorValue()
    {
        return VITALITY_TYPE_WATER;
    }

    @Override
    Vector3f getFogColorValue()
    {
        return new Vector3f(0, 11f / 255f, 171 / 255f);
    }
}
