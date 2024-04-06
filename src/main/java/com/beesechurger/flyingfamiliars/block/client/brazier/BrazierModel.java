package com.beesechurger.flyingfamiliars.block.client.brazier;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.block.entity.common.BrazierBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class BrazierModel extends GeoModel<BrazierBlockEntity>
{
    @Override
    public ResourceLocation getModelResource(BrazierBlockEntity BrazierBlockEntity)
    {
        return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/block/brazier.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BrazierBlockEntity BrazierBlockEntity)
    {
        return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/block/brazier/brazier.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BrazierBlockEntity BrazierBlockEntity) {
        return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/block/brazier.animation.json");
    }
}
