package com.beesechurger.flyingfamiliars.entity.client.familiar.layer.rider;

import com.beesechurger.flyingfamiliars.entity.client.familiar.cloud_ray.CloudRayRenderer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.shadewyrm.ShadewyrmRenderer;
import com.beesechurger.flyingfamiliars.entity.common.familiar.CloudRayEntity;
import com.beesechurger.flyingfamiliars.entity.common.familiar.ShadewyrmEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import static com.beesechurger.flyingfamiliars.util.FFConstants.LAYER_RIDER_BONE_CLOUD_RAY;
import static com.beesechurger.flyingfamiliars.util.FFConstants.LAYER_RIDER_BONE_SHADEWYRM;

public class ShadewyrmRiderLayer extends BaseFamiliarRiderLayer<ShadewyrmEntity>
{
    public ShadewyrmRiderLayer(ShadewyrmRenderer renderIn)
    {
        super(renderIn);
    }

    @Override
    protected String getSeatBone()
    {
        return LAYER_RIDER_BONE_SHADEWYRM;
    }

    @Override
    protected Vec3 getRenderOffset(ShadewyrmEntity entity, Entity passenger)
    {
        return new Vec3(0, 0,0);
    }
}
