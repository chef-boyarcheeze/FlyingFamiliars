package com.beesechurger.flyingfamiliars.entity.client.wand_effect.projectile.runic_cube;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.common.wand_effect.projectile.RunicCubeProjectile;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class RunicCubeProjectileModel extends GeoModel<RunicCubeProjectile>
{
    @Override
    public ResourceLocation getAnimationResource(RunicCubeProjectile animatable)
    {
        return new ResourceLocation(FlyingFamiliars.MOD_ID, "animations/wand_effect/projectile/runic_cube_projectile.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(RunicCubeProjectile animatable)
    {
        return new ResourceLocation(FlyingFamiliars.MOD_ID, "geo/wand_effect/projectile/runic_cube_projectile.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RunicCubeProjectile animatable)
    {
        return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/wand_effect/projectile/runic_cube_projectile/runic_cube_projectile_empty.png"); // TODO change on different element type
    }
}
