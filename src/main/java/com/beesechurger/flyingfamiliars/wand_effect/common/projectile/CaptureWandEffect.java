package com.beesechurger.flyingfamiliars.wand_effect.common.projectile;

import com.beesechurger.flyingfamiliars.entity.common.wand_effect.projectile.CaptureProjectile;
import com.beesechurger.flyingfamiliars.item.common.entity_items.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.registries.FFKeys;
import com.beesechurger.flyingfamiliars.registries.FFSounds;
import com.beesechurger.flyingfamiliars.util.FFConstants;
import com.beesechurger.flyingfamiliars.wand_effect.common.BaseWandEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.beesechurger.flyingfamiliars.wand_effect.common.WandEffectItemHelper.WAND_EFFECT_CAPTURE;

public class CaptureWandEffect extends BaseWandEffect
{
//////////////////
/// Accessors: ///
//////////////////

/// Strings:

    @Override
    public String getName()
    {
        return WAND_EFFECT_CAPTURE;
    }

/// Integers:

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
    public int getColor()
    {
        return FFConstants.FAMILIAR_TYPE_VOID;
    }

///////////////////////////
/// Wand effect action: ///
///////////////////////////

    @Override
    public void attack(Level level, Player player)
    {
        ItemStack stack = player.getMainHandItem();

        if (stack.getItem() instanceof BaseEntityTagItem item)
        {
            item.toggleManipMode(stack);
            MutableComponent message = item.getManipMode(stack)
                    ? Component.translatable("message.flyingfamiliars.wand_effect_tag.capture_projectile.place")
                    : Component.translatable("message.flyingfamiliars.wand_effect_tag.capture_projectile.remove");

            player.displayClientMessage(message.withStyle(ChatFormatting.WHITE), true);
            level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), FFSounds.SOUL_WAND_SWAP.get(), SoundSource.PLAYERS, 0.5f, FFSounds.getPitch());
        }
    }

    @Override
    public void use(Level level, Player player)
    {
        CaptureProjectile capture = new CaptureProjectile(level, player, FFKeys.SOUL_WAND_SHIFT.isDown());
        capture.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 1.2f, 1.0f);
        level.addFreshEntity(capture);
    }
}
