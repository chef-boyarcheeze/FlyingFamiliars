package com.beesechurger.flyingfamiliars.util;

import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

import java.util.Map;

public class FFTypes
{
    public static final FamiliarType FAMILIAR_TYPE_WATER = new FamiliarType("water", 0xFF2040D0);
    public static final FamiliarType FAMILIAR_TYPE_LIFE = new FamiliarType("life", 0xFF008020);
    public static final FamiliarType FAMILIAR_TYPE_AIR = new FamiliarType("air", 0xFFE0E000);
    public static final FamiliarType FAMILIAR_TYPE_EARTH = new FamiliarType("earth", 0xFFE09020);
    public static final FamiliarType FAMILIAR_TYPE_FIRE = new FamiliarType("fire", 0xFFB03020);
    public static final FamiliarType FAMILIAR_TYPE_LIGHT = new FamiliarType("light", 0xFFF0D0F0);
    public static final FamiliarType FAMILIAR_TYPE_VOID = new FamiliarType("void", 0xFF402080);

    public static class FamiliarType
    {
        public final String type;
        public final int color;

        public FamiliarType(String type, int color)
        {
            this.type = type;
            this.color = color;
        }
    }

    private static final Map<String, Integer> TYPE_COLOR_MAP = Map.ofEntries(
        Map.entry(FAMILIAR_TYPE_WATER.type, FAMILIAR_TYPE_WATER.color),
        Map.entry(FAMILIAR_TYPE_LIFE.type, FAMILIAR_TYPE_LIFE.color),
        Map.entry(FAMILIAR_TYPE_AIR.type, FAMILIAR_TYPE_AIR.color),
        Map.entry(FAMILIAR_TYPE_EARTH.type, FAMILIAR_TYPE_EARTH.color),
        Map.entry(FAMILIAR_TYPE_FIRE.type, FAMILIAR_TYPE_FIRE.color),
        Map.entry(FAMILIAR_TYPE_LIGHT.type, FAMILIAR_TYPE_LIGHT.color),
        Map.entry(FAMILIAR_TYPE_VOID.type, FAMILIAR_TYPE_VOID.color)
    );

    public static int getTypeColorInt(String type)
    {
        if (TYPE_COLOR_MAP.containsKey(type))
        {
            return TYPE_COLOR_MAP.get(type);
        }

        return 0xFFFFFFFF;
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
        private float red;
        private float green;
        private float blue;
        private float alpha;

        public ColorType(float red, float green, float blue, float alpha)
        {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }

        public float red()
        {
            return red;
        }

        public ColorType modRed(float mod)
        {
            return new ColorType(red * mod, green, blue, alpha);
        }

        public float green()
        {
            return green;
        }

        public ColorType modGreen(float mod)
        {
            return new ColorType(red, green * mod, blue, alpha);
        }

        public float blue()
        {
            return blue;
        }

        public ColorType modBlue(float mod)
        {
            return new ColorType(red, green, blue * mod, alpha);
        }

        public float alpha()
        {
            return alpha;
        }

        public ColorType modAlpha(float mod)
        {
            return new ColorType(red, green, blue, alpha * mod);
        }
    }

    private static final Map<String, Component> TYPE_NAME_MAP = Map.ofEntries(
            Map.entry(FAMILIAR_TYPE_WATER.type, Component.translatable("tooltip.flyingfamiliars.spirit_tag.type_water")),
            Map.entry(FAMILIAR_TYPE_LIFE.type, Component.translatable("tooltip.flyingfamiliars.spirit_tag.type_life")),
            Map.entry(FAMILIAR_TYPE_AIR.type, Component.translatable("tooltip.flyingfamiliars.spirit_tag.type_air")),
            Map.entry(FAMILIAR_TYPE_EARTH.type, Component.translatable("tooltip.flyingfamiliars.spirit_tag.type_earth")),
            Map.entry(FAMILIAR_TYPE_FIRE.type, Component.translatable("tooltip.flyingfamiliars.spirit_tag.type_fire")),
            Map.entry(FAMILIAR_TYPE_LIGHT.type, Component.translatable("tooltip.flyingfamiliars.spirit_tag.type_light")),
            Map.entry(FAMILIAR_TYPE_VOID.type, Component.translatable("tooltip.flyingfamiliars.spirit_tag.type_void"))
    );

    public static Component getTypeName(String type)
    {
        if (TYPE_NAME_MAP.containsKey(type))
        {
            return TYPE_NAME_MAP.get(type);
        }

        return Component.literal("null: ");
    }
}