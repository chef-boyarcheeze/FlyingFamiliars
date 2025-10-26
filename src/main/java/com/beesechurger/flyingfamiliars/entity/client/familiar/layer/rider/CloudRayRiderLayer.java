package com.beesechurger.flyingfamiliars.entity.client.familiar.layer.rider;

import com.beesechurger.flyingfamiliars.entity.client.familiar.cloud_ray.CloudRayRenderer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.griffonfly.GriffonflyRenderer;
import com.beesechurger.flyingfamiliars.entity.common.familiar.CloudRayEntity;
import com.beesechurger.flyingfamiliars.entity.common.familiar.GriffonflyEntity;

import static com.beesechurger.flyingfamiliars.util.FFConstants.LAYER_RIDER_BONE_CLOUD_RAY;
import static com.beesechurger.flyingfamiliars.util.FFConstants.LAYER_RIDER_BONE_GRIFFONFLY;

public class CloudRayRiderLayer extends BaseFamiliarRiderLayer<CloudRayEntity>
{
    public CloudRayRiderLayer(CloudRayRenderer renderIn)
    {
        super(renderIn);
    }

    protected String getBone()
    {
        return LAYER_RIDER_BONE_CLOUD_RAY;
    }
}
