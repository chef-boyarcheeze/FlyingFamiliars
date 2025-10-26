package com.beesechurger.flyingfamiliars.entity.client.familiar.layer.rider;

import com.beesechurger.flyingfamiliars.entity.client.familiar.cloud_ray.CloudRayRenderer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.magic_carpet.MagicCarpetRenderer;
import com.beesechurger.flyingfamiliars.entity.common.familiar.CloudRayEntity;
import com.beesechurger.flyingfamiliars.entity.common.familiar.MagicCarpetEntity;

import static com.beesechurger.flyingfamiliars.util.FFConstants.LAYER_RIDER_BONE_CLOUD_RAY;
import static com.beesechurger.flyingfamiliars.util.FFConstants.LAYER_RIDER_BONE_MAGIC_CARPET;

public class MagicCarpetRiderLayer extends BaseFamiliarRiderLayer<MagicCarpetEntity>
{
    public MagicCarpetRiderLayer(MagicCarpetRenderer renderIn)
    {
        super(renderIn);
    }

    protected String getBone()
    {
        return LAYER_RIDER_BONE_MAGIC_CARPET;
    }
}
