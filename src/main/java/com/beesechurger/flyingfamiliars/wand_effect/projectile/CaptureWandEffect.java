package com.beesechurger.flyingfamiliars.wand_effect.projectile;

import com.beesechurger.flyingfamiliars.entity.common.wand_effect.projectile.CaptureProjectile;
import com.beesechurger.flyingfamiliars.registries.FFKeys;
import com.beesechurger.flyingfamiliars.wand_effect.BaseWandEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static com.beesechurger.flyingfamiliars.util.FFValueConstants.CHAT_GRAY;

public class CaptureWandEffect extends BaseWandEffect
{
////////////////
// Accessors: //
////////////////

// Strings:

    public String getName()
    {
        return "capture_projectile";
    }

    public String getTranslatableName()
    {
        return "tooltip.flyingfamiliars.wand_effect.capture_projectile";
    }

// Integers:

    public int getCost()
    {
        return 0;
    }

    public int getCooldown()
    {
        return 5;
    }

    public int getBarColor()
    {
        return CHAT_GRAY;
    }

// Misc:

    public ChatFormatting getTooltipColor()
    {
        return ChatFormatting.GRAY;
    }

/////////////////////////
// Wand effect action: //
/////////////////////////

    public void action(Level level, Player player)
    {
        CaptureProjectile capture = new CaptureProjectile(level, player, FFKeys.SOUL_WAND_SHIFT.isDown());
        capture.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 1.2f, 1.0f);
        level.addFreshEntity(capture);
    }
}
