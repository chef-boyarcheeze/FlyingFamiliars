package com.beesechurger.flyingfamiliars.wand_effect.common.charm;

import com.beesechurger.flyingfamiliars.registries.FFSounds;
import com.beesechurger.flyingfamiliars.tags.WandEffectTagRef;
import com.beesechurger.flyingfamiliars.util.FFColors;
import com.beesechurger.flyingfamiliars.wand_effect.common.BaseWandEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;

import static com.beesechurger.flyingfamiliars.wand_effect.common.WandEffectItemHelper.WAND_EFFECT_CLOUD_CALL;

public class CloudCallWandEffect extends BaseWandEffect
{
    private static final String SETTINGS_CLOUD_CALL = "CloudCallSettings";
    private static final List<String> WEATHER_TYPES = Arrays.asList(
        "Clear",
        "Rain",
        "Thunder"
    );

//////////////////
/// Accessors: ///
//////////////////

/// Strings:
    @Override
    public String getName()
    {
        return WAND_EFFECT_CLOUD_CALL;
    }

    private String getWeatherType(CompoundTag selectedEntryTag)
    {
        if (!selectedEntryTag.contains(SETTINGS_CLOUD_CALL))
        {
            selectedEntryTag.putString(SETTINGS_CLOUD_CALL, WEATHER_TYPES.get(0));
        }

        return selectedEntryTag.getString(SETTINGS_CLOUD_CALL);
    }

/// Integers:

    @Override
    public int getUseDurationMin()
    {
        return 20;
    }

    @Override
    public int getUseDurationMax()
    {
        return MAX_CHARGE_TIME;
    }

    @Override
    public int getCost()
    {
        return 0;
    }

    @Override
    public int getCooldown()
    {
        return 60;
    }

    @Override
    public int getColor()
    {
        return FFColors.FAMILIAR_TYPE_WATER;
    }

    @Override
    public UseAnim getUseAnimation()
    {
        return UseAnim.BOW;
    }

///////////////////////////
/// Wand effect action: ///
///////////////////////////

    @Override
    public void attack(Level level, Player player)
    {
        ItemStack stack = player.getMainHandItem();
        CompoundTag selectedEntryTag = WandEffectTagRef.INSTANCE.getSelectedEntry(stack.getOrCreateTag());

        String weatherType = getWeatherType(selectedEntryTag);
        int index = WEATHER_TYPES.indexOf(weatherType);

        if (player.isShiftKeyDown())
        {
            index--;

            if (index < 0)
            {
                index = WEATHER_TYPES.size() - 1;
            }
        }
        else
        {
            index++;

            if (index > WEATHER_TYPES.size() - 1)
            {
                index = 0;
            }
        }

        selectedEntryTag.putString(SETTINGS_CLOUD_CALL, WEATHER_TYPES.get(index));
        MutableComponent message = Component.translatable("message.flyingfamiliars.wand_effect_tag.current_mode").append(selectedEntryTag.getString(SETTINGS_CLOUD_CALL));

        player.displayClientMessage(message.withStyle(ChatFormatting.WHITE), true);
        level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), FFSounds.SOUL_WAND_SWAP.get(), SoundSource.NEUTRAL, 0.5f, FFSounds.getPitch());
    }

    @Override
    public void use(Level level, Player player)
    {
        ItemStack stack = player.getMainHandItem();
        CompoundTag selectedEntryTag = WandEffectTagRef.INSTANCE.getSelectedEntry(stack.getOrCreateTag());
        String weatherType = getWeatherType(selectedEntryTag);

        if (level instanceof ServerLevel server)
        {
            // TODO: randomize length of time with reasonable values
            // TODO: use in world structures to determine time and cost and success chance
            switch (weatherType)
            {
                case "Clear" -> server.setWeatherParameters(10000, 0, false, false);
                case "Rain" -> server.setWeatherParameters(0, 10000, true, false);
                case "Thunder" -> server.setWeatherParameters(0, 10000, true, true);
            }
        }
    }
}
