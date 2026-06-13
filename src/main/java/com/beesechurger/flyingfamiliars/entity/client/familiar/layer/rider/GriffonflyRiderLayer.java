package com.beesechurger.flyingfamiliars.entity.client.familiar.layer.rider;

import com.beesechurger.flyingfamiliars.entity.client.familiar.griffonfly.GriffonflyRenderer;
import com.beesechurger.flyingfamiliars.entity.common.familiar.GriffonflyEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class GriffonflyRiderLayer extends BaseFamiliarRiderLayer<GriffonflyEntity>
{
    private static final String LAYER_RIDER_BONE_GRIFFONFLY = "center_thorax";

    public GriffonflyRiderLayer(GriffonflyRenderer renderIn)
    {
        super(renderIn);
    }

    @Override
    protected String getSeatBone()
    {
        return LAYER_RIDER_BONE_GRIFFONFLY;
    }

    @Override
    protected Vec3 getRenderOffset(GriffonflyEntity entity, Entity passenger)
    {
        return new Vec3(0, 0.75,0);
    }
}
