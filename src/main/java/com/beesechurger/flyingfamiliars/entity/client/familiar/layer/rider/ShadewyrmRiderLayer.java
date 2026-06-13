package com.beesechurger.flyingfamiliars.entity.client.familiar.layer.rider;

import com.beesechurger.flyingfamiliars.entity.client.familiar.shadewyrm.ShadewyrmRenderer;
import com.beesechurger.flyingfamiliars.entity.common.familiar.ShadewyrmEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class ShadewyrmRiderLayer extends BaseFamiliarRiderLayer<ShadewyrmEntity>
{
    private static final String LAYER_RIDER_BONE_SHADEWYRM = "center";

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
