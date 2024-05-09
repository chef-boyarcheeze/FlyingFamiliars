package com.beesechurger.flyingfamiliars.fluid;

import org.joml.Vector3f;

public class GoldVitalityFluid extends BaseVitalityFluid
{
    public GoldVitalityFluid(Properties properties)
    {
        super(properties);
    }

    @Override
    int getTintColorValue()
    {
        return VITALITY_TYPE_EARTH;
    }

    @Override
    Vector3f getFogColorValue()
    {
        return new Vector3f(222f / 255f, 170f / 255f, 0);
    }
}
