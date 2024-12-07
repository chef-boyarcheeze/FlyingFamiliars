package com.beesechurger.flyingfamiliars.entity.client.wand_effect.sentry;

import com.beesechurger.flyingfamiliars.entity.common.wand_effect.sentry.BaseWandEffectSentry;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BaseWandEffectSentryRenderer<T extends BaseWandEffectSentry & GeoAnimatable> extends GeoEntityRenderer<T>
{
    public BaseWandEffectSentryRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model)
    {
        super(renderManager, model);
    }
}