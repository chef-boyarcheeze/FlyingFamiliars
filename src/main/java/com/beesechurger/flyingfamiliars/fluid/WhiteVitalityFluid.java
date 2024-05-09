package com.beesechurger.flyingfamiliars.fluid;

import org.joml.Vector3f;

public class WhiteVitalityFluid extends BaseVitalityFluid
{
    public WhiteVitalityFluid(Properties properties)
    {
        super(properties);
    }

    @Override
    int getTintColorValue()
    {
        return VITALITY_TYPE_LIGHT;
    }

    @Override
    Vector3f getFogColorValue()
    {
        return new Vector3f(240f / 255f, 240f / 255f, 240f / 255f);
    }
}
