package com.beesechurger.flyingfamiliars.entity.client.familiar.layer.rider;

import com.beesechurger.flyingfamiliars.entity.client.familiar.cloud_ray.CloudRayRenderer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.magic_carpet.MagicCarpetRenderer;
import com.beesechurger.flyingfamiliars.entity.common.familiar.CloudRayEntity;
import com.beesechurger.flyingfamiliars.entity.common.familiar.MagicCarpetEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import static com.beesechurger.flyingfamiliars.util.FFConstants.LAYER_RIDER_BONE_CLOUD_RAY;
import static com.beesechurger.flyingfamiliars.util.FFConstants.LAYER_RIDER_BONE_MAGIC_CARPET;

public class MagicCarpetRiderLayer extends BaseFamiliarRiderLayer<MagicCarpetEntity>
{
    public MagicCarpetRiderLayer(MagicCarpetRenderer renderIn)
    {
        super(renderIn);
    }

    @Override
    protected String getSeatBone()
    {
        return LAYER_RIDER_BONE_MAGIC_CARPET;
    }

    @Override
    protected Vec3 getRenderOffset(MagicCarpetEntity animatable, Entity passenger)
    {
        return new Vec3(0, 0.4,0);
    }
}
