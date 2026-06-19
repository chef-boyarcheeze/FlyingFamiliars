package com.beesechurger.flyingfamiliars.util;

import java.util.Arrays;
import java.util.List;

public class FFConstants
{
//////////////////////
/// String values: ///
//////////////////////

/// Tag IDs:

    public static final String STORAGE_EMPTY = "Empty";

    public static final String STORAGE_ENTITY_TAGNAME = "ff.base.entity";
    public static final String STORAGE_ENTITY_TYPE = "EntityType";

    public static final String STORAGE_FRAGMENT_TAGNAME = "ff.item.fragment";
    public static final String STORAGE_FRAGMENT_TYPE = "FragmentType";
    public static final String STORAGE_FRAGMENT_STORAGE = "FragmentStorage";
    public static final String STORAGE_FRAGMENT_STORAGE_MAX = "FragmentStorageMax";

    public static final String STORAGE_WAND_EFFECT_TAGNAME = "ff.item.wand_effect";
    public static final String STORAGE_WAND_EFFECT_TYPE = "WandEffectType";
    public static final String STORAGE_WAND_EFFECT_SELECTION = "WandEffectSelection";

    public static final String STORAGE_SETTINGS = "Settings";
    public static final String STORAGE_ENTRY_LIST = "EntryList";
    public static final String STORAGE_ENTRY_STORAGE_MAX = "EntryStorageMax";
    public static final String STORAGE_ENTRY_MANIP_MODE = "EntryManipMode";

    public static final String BLOCK_PROGRESS_TAGNAME = "ff.block.progress";

/// Fluid tag constants:

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

/// Animation constants:

    public static final String ANIMATION_EMPTY = "Empty";

///////////////////////
/// Integer values: ///
///////////////////////

/// Familiar type colors in hexadecimal format:

    public static final int FAMILIAR_TYPE_WATER = 0x000BAB;
    public static final int FAMILIAR_TYPE_LIFE = 0x00A300;
    public static final int FAMILIAR_TYPE_AIR = 0xFAF000;
    public static final int FAMILIAR_TYPE_EARTH = 0xDEAA00;
    public static final int FAMILIAR_TYPE_FIRE = 0xB02000;
    public static final int FAMILIAR_TYPE_LIGHT = 0xF0F0F0;
    public static final int FAMILIAR_TYPE_VOID = 0x202020;

/// Familiar goal values:

    public static final int BUILDING_LIMIT_LOW = -64;
    public static final int BUILDING_LIMIT_HIGH = 320;
    public static final int RANDOM_MOVE_CHANCE = 50;
}
