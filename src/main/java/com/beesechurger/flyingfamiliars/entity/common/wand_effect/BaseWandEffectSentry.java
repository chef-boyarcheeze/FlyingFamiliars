package com.beesechurger.flyingfamiliars.entity.common.wand_effect;

import com.beesechurger.flyingfamiliars.item.common.wand_effect.IWandEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;

public abstract class BaseWandEffectSentry extends Entity implements IWandEffect, GeoEntity
{
    public BaseWandEffectSentry(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }
}
