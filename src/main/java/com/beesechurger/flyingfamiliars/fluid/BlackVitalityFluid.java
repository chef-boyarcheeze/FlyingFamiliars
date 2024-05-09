package com.beesechurger.flyingfamiliars.fluid;

import org.joml.Vector3f;

public class BlackVitalityFluid extends BaseVitalityFluid
{
    public BlackVitalityFluid(Properties properties)
    {
        super(properties);
    }

    @Override
    int getTintColorValue()
    {
        return VITALITY_TYPE_SHADOW;
    }

    @Override
    Vector3f getFogColorValue()
    {
        return new Vector3f(32f / 255f, 32f / 255f, 32f / 255f);
    }
}
