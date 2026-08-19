package com.beesechurger.flyingfamiliars.wand_effect.common.projectile;

import com.beesechurger.flyingfamiliars.client.FFKeys;
import com.beesechurger.flyingfamiliars.client.FFSounds;
import com.beesechurger.flyingfamiliars.entity.common.wand_effect.projectile.CaptureProjectile;
import com.beesechurger.flyingfamiliars.item.FFItems;
import com.beesechurger.flyingfamiliars.item.common.entity.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.util.FFTypes;
import com.beesechurger.flyingfamiliars.wand_effect.common.BaseWandEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Map;

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
    public Map<String, Integer> getCost()
    {
        return Map.ofEntries(
                Map.entry(FFItems.WET_SPIRIT_FRAGMENT.get().toString(), 3),
                Map.entry(FFItems.VACUOUS_SPIRIT_FRAGMENT.get().toString(), 3)
        );
    }

    @Override
    public int getCooldown()
    {
        return 5;
    }

    @Override
    public int getColor()
    {
        return FFTypes.FAMILIAR_TYPE_VOID.color;
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
    public void use(Level level, Player player, int duration)
    {
        CaptureProjectile capture = new CaptureProjectile(level, player, FFKeys.SOUL_WAND_SHIFT.isDown());
        capture.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, 1.2f, 1.0f);
        level.addFreshEntity(capture);
    }
}
