package com.beesechurger.flyingfamiliars.entity.client.wand_effect.charm;

import com.beesechurger.flyingfamiliars.entity.common.wand_effect.charm.BaseWandEffectCharm;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BaseWandEffectCharmRenderer<T extends BaseWandEffectCharm & GeoAnimatable> extends GeoEntityRenderer<T>
{
    public BaseWandEffectCharmRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model)
    {
        super(renderManager, model);
    }
}