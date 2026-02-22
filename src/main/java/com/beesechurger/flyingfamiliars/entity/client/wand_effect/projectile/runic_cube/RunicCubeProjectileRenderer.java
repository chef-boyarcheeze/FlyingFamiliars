package com.beesechurger.flyingfamiliars.entity.client.wand_effect.projectile.capture_projectile;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.client.wand_effect.projectile.BaseWandEffectProjectileRenderer;
import com.beesechurger.flyingfamiliars.entity.client.wand_effect.projectile.runic_cube.RunicCubeProjectileModel;
import com.beesechurger.flyingfamiliars.entity.common.wand_effect.projectile.RunicCubeProjectile;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RunicCubeProjectileRenderer extends BaseWandEffectProjectileRenderer<RunicCubeProjectile>
{
    public RunicCubeProjectileRenderer(Context renderManager)
    {
        super(renderManager, new RunicCubeProjectileModel());
        this.withScale(1.2f);
    }

    @Override
    public ResourceLocation getTextureLocation(RunicCubeProjectile animatable)
    {
        return new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/wand_effect/projectile/runic_cube_projectile/runic_cube_projectile_empty.png"); // TODO change on different element type
    }
}
