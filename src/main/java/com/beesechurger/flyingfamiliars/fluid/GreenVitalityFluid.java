package com.beesechurger.flyingfamiliars.fluid;

import org.joml.Vector3f;

public class GreenVitalityFluid extends BaseVitalityFluid
{
    public GreenVitalityFluid(Properties properties)
    {
        super(properties);
    }

    @Override
    int getTintColorValue()
    {
        return VITALITY_TYPE_PLANT;
    }

    @Override
    Vector3f getFogColorValue()
    {
        return new Vector3f(0, 163f / 255f, 0);
    }
}
