package com.beesechurger.flyingfamiliars.wand_effect.common;

import com.beesechurger.flyingfamiliars.util.tags.WandEffectTagUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public abstract class BaseWandEffect
{
    protected static final int MAX_CHARGE_TIME = 72000;

//////////////////
/// Accessors: ///
//////////////////

/// Strings:

    // nbt tag name of wand effect
    public abstract String getName();

    // translatable name of wand effect for UI
    public final String getTranslatableName()
    {
        return "tooltip.flyingfamiliars.wand_effect." + getName();
    }

/// Booleans:

    // effect only usable for useOn context (looking directly at block)
    public boolean usableOnBlockOnly()
    {
        return false;
    }

    // block currently looked at is correct
    public boolean checkLookedAtBlock(BlockState state)
    {
        return true;
    }

    // effect works when continuously drawn
    public boolean canBeContinuouslyDrawn()
    {
        return false;
    }

    // effect works when partially drawn
    public boolean canBePartiallyDrawn()
    {
        return false;
    }

/// Integers:

    // minimum use duration of soul wand
    public int getUseDurationMin()
    {
        return 0;
    }

    // maximum use duration of soul wand
    public int getUseDurationMax()
    {
        return 0;
    }

    // cost of wand effect cast in (units?)
    public abstract Map<String, Integer> getCost();

    // cooldown time in ticks
    public abstract int getCooldown();

    // soul wand durability bar / tooltip color
    public abstract int getColor();

/// Misc:

    // soul wand use animation
    public UseAnim getUseAnimation()
    {
        return UseAnim.NONE;
    }

    public BaseWandEffect getSelectedWandEffect(ItemStack stack)
    {
        return WandEffectItemHelper.getSelectedWandEffect(WandEffectTagUtil.INSTANCE.getSelectedWandEffect(stack.getOrCreateTag()));
    }

///////////////////////////
/// Wand Effect Action: ///
///////////////////////////

    // perform left-click function
    public void attack(Level level, Player player) {
    }

    // perform wand effect cast
    public void use(Level level, Player player, int duration) {
    }

    // perform block-only wand effect cast
    public void useOn(Level level, Player player, BlockPos pos, int duration) {
    }
}
