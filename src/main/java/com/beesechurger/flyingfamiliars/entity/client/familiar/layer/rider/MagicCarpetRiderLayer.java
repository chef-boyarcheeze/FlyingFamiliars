package com.beesechurger.flyingfamiliars.entity.client.familiar.layer.rider;

import com.beesechurger.flyingfamiliars.entity.client.familiar.magic_carpet.MagicCarpetRenderer;
import com.beesechurger.flyingfamiliars.entity.common.familiar.MagicCarpetEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class MagicCarpetRiderLayer extends BaseFamiliarRiderLayer<MagicCarpetEntity>
{
    private static final String LAYER_RIDER_BONE_MAGIC_CARPET = "body5";

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
    protected Vec3 getRenderOffset(MagicCarpetEntity entity, Entity passenger)
    {
        return new Vec3(0, 0.4,0);
    }
}
