package com.beesechurger.flyingfamiliars.block.client.obelisk;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.block.entity.common.ObeliskBlockEntity;
import com.beesechurger.flyingfamiliars.entity.common.CloudRayEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class ObeliskModel extends GeoModel<ObeliskBlockEntity>
{
    @Override
    public ResourceLocation getModelResource(ObeliskBlockEntity obeliskBlockEntity)
    {
        return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/block/obelisk.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ObeliskBlockEntity obeliskBlockEntity)
    {
        return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/block/obelisk/obelisk_inactive.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ObeliskBlockEntity obeliskBlockEntity) {
        return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/block/obelisk.animation.json");
    }
}
