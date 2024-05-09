package com.beesechurger.flyingfamiliars.fluid;

import org.joml.Vector3f;

public class YellowVitalityFluid extends BaseVitalityFluid
{
    public YellowVitalityFluid(Properties properties)
    {
        super(properties);
    }

    @Override
    int getTintColorValue()
    {
        return VITALITY_TYPE_AIR;
    }

    @Override
    Vector3f getFogColorValue()
    {
        return new Vector3f(250f / 255f, 240f / 255f, 0);
    }
}
