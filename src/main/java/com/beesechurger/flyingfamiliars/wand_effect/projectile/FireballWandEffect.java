package com.beesechurger.flyingfamiliars.wand_effect.projectile;

import com.beesechurger.flyingfamiliars.entity.common.wand_effect.projectile.FireballProjectile;
import com.beesechurger.flyingfamiliars.wand_effect.BaseWandEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static com.beesechurger.flyingfamiliars.util.FFConstants.CHAT_DARK_RED;

public class FireballWandEffect extends BaseWandEffect
{
////////////////
// Accessors: //
////////////////

// Strings:

    public String getName()
    {
        return "fireball_projectile";
    }

    public String getTranslatableName()
    {
        return "tooltip.flyingfamiliars.wand_effect.fireball_projectile";
    }

// Integers:

    public int getCost()
    {
        return 0;
    }

    public int getCooldown()
    {
        return 10;
    }

    public int getBarColor()
    {
        return CHAT_DARK_RED;
    }

// Misc:

    public ChatFormatting getTooltipColor()
    {
        return ChatFormatting.DARK_RED;
    }

/////////////////////////
// Wand effect action: //
/////////////////////////

    public void action(Level level, Player player)
    {
        FireballProjectile fireball = new FireballProjectile(level, player);
        fireball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 1.2f, 1.0f);
        level.addFreshEntity(fireball);
    }
}
