package com.beesechurger.flyingfamiliars.entity.client.familiar.layer.rider;

import com.beesechurger.flyingfamiliars.entity.client.familiar.cloud_ray.CloudRayRenderer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.shadewyrm.ShadewyrmRenderer;
import com.beesechurger.flyingfamiliars.entity.common.familiar.CloudRayEntity;
import com.beesechurger.flyingfamiliars.entity.common.familiar.ShadewyrmEntity;

import static com.beesechurger.flyingfamiliars.util.FFConstants.LAYER_RIDER_BONE_CLOUD_RAY;
import static com.beesechurger.flyingfamiliars.util.FFConstants.LAYER_RIDER_BONE_SHADEWYRM;

public class ShadewyrmRiderLayer extends BaseFamiliarRiderLayer<ShadewyrmEntity>
{
    public ShadewyrmRiderLayer(ShadewyrmRenderer renderIn)
    {
        super(renderIn);
    }

    protected String getBone()
    {
        return LAYER_RIDER_BONE_SHADEWYRM;
    }
}
