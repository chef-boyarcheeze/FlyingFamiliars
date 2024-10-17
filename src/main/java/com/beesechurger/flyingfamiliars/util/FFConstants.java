package com.beesechurger.flyingfamiliars.util;

import java.util.Arrays;
import java.util.List;

public class FFConstants
{
////////////////////
// String values: //
////////////////////

    // Tag IDs:
    public static final String STORAGE_EMPTY = "Empty";

    public static final String STORAGE_ENTITY_TAGNAME = "ff.base.entity";
    public static final String STORAGE_ENTITY_TYPE = "EntityType";

    public static final String STORAGE_FLUID_TAGNAME = "ff.item.fluid";
    public static final String STORAGE_FLUID_TYPE = "FluidType";
    public static final String STORAGE_FLUID_STORAGE = "FluidStorage";

    public static final String STORAGE_WAND_EFFECT_TAGNAME = "ff.item.wand_effect";
    public static final String STORAGE_WAND_EFFECT_TYPE = "WandEffectType";

    public static final String BLOCK_PROGRESS_TAGNAME = "ff.block.progress";

    // Fluid tag constants:
    public static final String VITALITY_BLUE = "blue";
    public static final String VITALITY_GREEN = "green";
    public static final String VITALITY_YELLOW = "yellow";
    public static final String VITALITY_GOLD = "gold";
    public static final String VITALITY_RED = "red";
    public static final String VITALITY_BLACK = "black";
    public static final String VITALITY_WHITE = "white";

    public static final List<String> VITALITY_TYPES = Arrays.asList(
            VITALITY_BLUE,
            VITALITY_GREEN,
            VITALITY_YELLOW,
            VITALITY_GOLD,
            VITALITY_RED,
            VITALITY_BLACK,
            VITALITY_WHITE
    );

    // Animation constants:
    public static final String ANIMATION_EMPTY = "Empty";

    // Mod IDs:
    public static final String CURIOS_MODNAME = "curios";

/////////////////////
// Integer values: //
/////////////////////

    // Minecraft chat formatting colors in decimal format:
    public static final int CHAT_BLACK = 0;
    public static final int CHAT_DARK_BLUE = 170;
    public static final int CHAT_DARK_GREEN = 43520;
    public static final int CHAT_DARK_AQUA = 43690;
    public static final int CHAT_DARK_RED = 11141120;
    public static final int CHAT_DARK_PURPLE = 11141290;
    public static final int CHAT_GOLD = 16755200;
    public static final int CHAT_GRAY = 11184810;
    public static final int CHAT_DARK_GRAY = 5592405;
    public static final int CHAT_BLUE = 5592575;
    public static final int CHAT_GREEN = 5635925;
    public static final int CHAT_AQUA = 5636095;
    public static final int CHAT_RED = 16733525;
    public static final int CHAT_LIGHT_PURPLE = 16733695;
    public static final int CHAT_YELLOW = 16777045;
    public static final int CHAT_WHITE = 16777215;

    // Familiar "type" colors in hexadecimal format:
    public static final int FAMILIAR_TYPE_WATER = 0x000BAB;
    public static final int FAMILIAR_TYPE_PLANT = 0x00A300;
    public static final int FAMILIAR_TYPE_AIR = 0xFAF000;
    public static final int FAMILIAR_TYPE_EARTH = 0xDEAA00;
    public static final int FAMILIAR_TYPE_FIRE = 0xB02000;
    public static final int FAMILIAR_TYPE_SHADOW = 0x202020;
    public static final int FAMILIAR_TYPE_LIGHT = 0xF0F0F0;

    // Familiar goal values:
    public static final int BUILDING_LIMIT_LOW = -64;
    public static final int BUILDING_LIMIT_HIGH = 320;
    public static final int RANDOM_MOVE_CHANCE = 50;

    // Familiar movement values:
    public static final float BASE_FLYING_SPEED = 0.1f;
    public static final float BASE_MOVEMENT_SPEED = 0.1f;
}
