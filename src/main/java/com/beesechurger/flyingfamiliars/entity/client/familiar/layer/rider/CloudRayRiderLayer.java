package com.beesechurger.flyingfamiliars.entity.client.familiar.layer.rider;

import com.beesechurger.flyingfamiliars.entity.client.familiar.cloud_ray.CloudRayRenderer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.griffonfly.GriffonflyRenderer;
import com.beesechurger.flyingfamiliars.entity.common.familiar.CloudRayEntity;
import com.beesechurger.flyingfamiliars.entity.common.familiar.GriffonflyEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import static com.beesechurger.flyingfamiliars.util.FFConstants.LAYER_RIDER_BONE_CLOUD_RAY;
import static com.beesechurger.flyingfamiliars.util.FFConstants.LAYER_RIDER_BONE_GRIFFONFLY;

public class CloudRayRiderLayer extends BaseFamiliarRiderLayer<CloudRayEntity>
{
    public CloudRayRiderLayer(CloudRayRenderer renderIn)
    {
        super(renderIn);
    }

    @Override
    protected String getSeatBone()
    {
        return LAYER_RIDER_BONE_CLOUD_RAY;
    }

    @Override
    protected Vec3 getRenderOffset(CloudRayEntity animatable, Entity passenger)
    {
        return new Vec3(0, 1.2,0);
    }
}