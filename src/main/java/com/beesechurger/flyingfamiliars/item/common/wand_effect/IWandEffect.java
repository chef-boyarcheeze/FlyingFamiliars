package com.beesechurger.flyingfamiliars.item.common.wand_effect;

public interface IWandEffect
{
    default int getCost()
    {
        // Math.round(get discount);
        return 0;
    }
}
