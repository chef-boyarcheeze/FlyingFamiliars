package com.beesechurger.flyingfamiliars.wand_effect.common.projectile;

import com.beesechurger.flyingfamiliars.entity.common.wand_effect.projectile.FireballProjectile;
import com.beesechurger.flyingfamiliars.wand_effect.common.BaseWandEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static com.beesechurger.flyingfamiliars.util.FFConstants.CHAT_DARK_RED;
import static com.beesechurger.flyingfamiliars.wand_effect.common.WandEffectItemHelper.WAND_EFFECT_FIREBALL;

public class FireballWandEffect extends BaseWandEffect
{
////////////////
// Accessors: //
////////////////

// Strings:
    @Override
    public String getName()
    {
        return WAND_EFFECT_FIREBALL;
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
        return 10;
    }

    @Override
    public int getBarColor()
    {
        return CHAT_DARK_RED;
    }

// Misc:
    @Override
    public ChatFormatting getTooltipColor()
    {
        return ChatFormatting.DARK_RED;
    }

/////////////////////////
// Wand effect action: //
/////////////////////////

    @Override
    public void use(Level level, Player player)
    {
        FireballProjectile fireball = new FireballProjectile(level, player);
        fireball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 1.2f, 1.0f);
        level.addFreshEntity(fireball);
    }
}
