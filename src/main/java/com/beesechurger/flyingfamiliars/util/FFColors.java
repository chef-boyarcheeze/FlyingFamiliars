package com.beesechurger.flyingfamiliars.util;

import com.beesechurger.flyingfamiliars.registries.FFItems;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.util.FastColor;

import java.util.Map;

public class FFColors
{
    public static final int FAMILIAR_TYPE_WATER = 0xFF2040D0;
    public static final int FAMILIAR_TYPE_LIFE = 0xFF008020;
    public static final int FAMILIAR_TYPE_AIR = 0xFFE0E000;
    public static final int FAMILIAR_TYPE_EARTH = 0xFFE09020;
    public static final int FAMILIAR_TYPE_FIRE = 0xFFB03020;
    public static final int FAMILIAR_TYPE_LIGHT = 0xFFF0D0F0;
    public static final int FAMILIAR_TYPE_VOID = 0xFF402080;

    private static final Map<String, Integer> TYPE_COLOR_MAP = (Map) Util.make(Maps.newHashMap(), (map) -> {
        map.put(FFItems.WET_SPIRIT_FRAGMENT.get().toString(), FAMILIAR_TYPE_WATER);
        map.put(FFItems.LUSH_SPIRIT_FRAGMENT.get().toString(), FAMILIAR_TYPE_LIFE);
        map.put(FFItems.GUSTING_SPIRIT_FRAGMENT.get().toString(), FAMILIAR_TYPE_AIR);
        map.put(FFItems.STONY_SPIRIT_FRAGMENT.get().toString(), FAMILIAR_TYPE_EARTH);
        map.put(FFItems.BURNING_SPIRIT_FRAGMENT.get().toString(), FAMILIAR_TYPE_FIRE);
        map.put(FFItems.LUMINOUS_SPIRIT_FRAGMENT.get().toString(), FAMILIAR_TYPE_LIGHT);
        map.put(FFItems.VACUOUS_SPIRIT_FRAGMENT.get().toString(), FAMILIAR_TYPE_VOID);
    });

    public static int getTypeColorInt(String type)
    {
        return TYPE_COLOR_MAP.get(type);
    }

    public static ColorType getTypeColorRGBA(int color)
    {
        float red = FastColor.ARGB32.red(color) / 255.0f;
        float green = FastColor.ARGB32.green(color) / 255.0f;
        float blue = FastColor.ARGB32.blue(color) / 255.0f;
        float alpha = FastColor.ARGB32.alpha(color) / 255.0f;

        return new ColorType(red, green, blue, alpha);
    }

    public static class ColorType
    {
        public final float red;
        public final float green;
        public final float blue;
        public final float alpha;

        public ColorType(float red, float green, float blue, float alpha)
        {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }
    }
}