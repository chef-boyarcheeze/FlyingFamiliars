package com.beesechurger.flyingfamiliars.fluid;

import org.joml.Vector3f;

public class RedVitalityFluid extends BaseVitalityFluid
{
    public RedVitalityFluid(Properties properties)
    {
        super(properties);
    }

    @Override
    int getTintColorValue()
    {
        return VITALITY_TYPE_FIRE;
    }

    @Override
    Vector3f getFogColorValue()
    {
        return new Vector3f(176f / 255f, 32f / 255f, 0);
    }
}
