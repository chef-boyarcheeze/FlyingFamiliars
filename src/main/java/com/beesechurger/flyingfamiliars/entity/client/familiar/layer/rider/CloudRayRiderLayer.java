package com.beesechurger.flyingfamiliars.entity.client.familiar.layer.rider;

import com.beesechurger.flyingfamiliars.entity.client.familiar.cloud_ray.CloudRayRenderer;
import com.beesechurger.flyingfamiliars.entity.common.familiar.CloudRayEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class CloudRayRiderLayer extends BaseFamiliarRiderLayer<CloudRayEntity>
{
    private static final String LAYER_RIDER_BONE_CLOUD_RAY = "torso";

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
    protected Vec3 getRenderOffset(CloudRayEntity entity, Entity passenger)
    {
        return new Vec3(0, 1.2,0);
    }
}