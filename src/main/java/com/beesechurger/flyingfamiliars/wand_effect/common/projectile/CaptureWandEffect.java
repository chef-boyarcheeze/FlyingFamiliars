package com.beesechurger.flyingfamiliars.wand_effect.common.projectile;

import com.beesechurger.flyingfamiliars.entity.common.wand_effect.projectile.CaptureProjectile;
import com.beesechurger.flyingfamiliars.registries.FFKeys;
import com.beesechurger.flyingfamiliars.wand_effect.common.BaseWandEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static com.beesechurger.flyingfamiliars.util.FFConstants.CHAT_GRAY;

public class CaptureWandEffect extends BaseWandEffect
{
////////////////
// Accessors: //
////////////////

// Strings:
    @Override
    public String getName()
    {
        return "capture_projectile";
    }

    @Override
    public String getTranslatableName()
    {
        return "tooltip.flyingfamiliars.wand_effect.capture_projectile";
    }

// Integers:
    @Override
    public int getCost()
    {
        return 0;
    }

    @Override
    public int getCooldown()
    {
        return 5;
    }

    @Override
    public int getBarColor()
    {
        return CHAT_GRAY;
    }

// Misc:
    @Override
    public ChatFormatting getTooltipColor()
    {
        return ChatFormatting.GRAY;
    }

/////////////////////////
// Wand effect action: //
/////////////////////////

    @Override
    public void action(Level level, Player player)
    {
        CaptureProjectile capture = new CaptureProjectile(level, player, FFKeys.SOUL_WAND_SHIFT.isDown());
        capture.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 1.2f, 1.0f);
        level.addFreshEntity(capture);
    }
}
