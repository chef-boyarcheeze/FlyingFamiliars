package com.beesechurger.flyingfamiliars.wand_effect;

import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public abstract class BaseWandEffect
{
////////////////
// Accessors: //
////////////////

// Strings:

    // nbt tag name of wand effect
    public abstract String getName();

    // translatable name of wand effect for UI
    public abstract String getTranslatableName();

// Integers:

    // cost of wand effect cast in (units?)
    public abstract int getCost();

    // cooldown time in ticks
    public abstract int getCooldown();

    // soul wand durability bar color, in minecraft colors
    public abstract int getBarColor();

// Misc:

    // soul wand inventory tooltip color, in minecraft chat colors
    public abstract ChatFormatting getTooltipColor();

/////////////////////////
// Wand effect action: //
/////////////////////////

    // perform wand effect cast
    public abstract void action(Level level, Player player);
}
