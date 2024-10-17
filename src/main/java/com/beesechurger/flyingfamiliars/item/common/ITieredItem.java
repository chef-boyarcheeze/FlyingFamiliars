package com.beesechurger.flyingfamiliars.item.common;

import static com.beesechurger.flyingfamiliars.util.FFConstants.*;

public interface ITieredItem
{
    public static enum ItemTier
    {
        BLUE(1, FAMILIAR_TYPE_WATER),
        GREEN(2, FAMILIAR_TYPE_PLANT),
        YELLOW(3, FAMILIAR_TYPE_AIR),
        GOLD(4, FAMILIAR_TYPE_EARTH),
        RED(5, FAMILIAR_TYPE_FIRE),
        BLACK(6, FAMILIAR_TYPE_SHADOW),
        WHITE(7, FAMILIAR_TYPE_LIGHT);

        public final int VALUE;
        public final int COLOR;

        private ItemTier(int value, int color)
        {
            this.VALUE = value;
            this.COLOR = color;
        }
    }
}
