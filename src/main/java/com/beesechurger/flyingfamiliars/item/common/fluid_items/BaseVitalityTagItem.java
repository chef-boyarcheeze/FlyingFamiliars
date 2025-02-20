package com.beesechurger.flyingfamiliars.item.common.fluid_items;

import com.beesechurger.flyingfamiliars.item.common.BaseStorageTagItem;
import com.beesechurger.flyingfamiliars.tags.VitalityTagRef;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

import static com.beesechurger.flyingfamiliars.util.FFConstants.*;
import static net.minecraft.network.chat.Component.literal;
import static net.minecraft.network.chat.Component.translatable;

public abstract class BaseVitalityTagItem extends BaseStorageTagItem
{
    public BaseVitalityTagItem(Properties properties)
    {
        super(properties);
    }

////////////////
// Accessors: //
////////////////

// Booleans:
    @Override
    public boolean isFoil(ItemStack stack)
    {
        return false;
    }

    @Override
    public boolean isBarVisible(ItemStack stack)
    {
        return false;
    }

    @Override
    public boolean getManipMode(ItemStack stack)
    {
        return VitalityTagRef.INSTANCE.getManipMode(stack.getOrCreateTag());
    }

///////////////
// Mutators: //
///////////////

// Booleans:
    @Override
    public void toggleManipMode(ItemStack stack)
    {
        VitalityTagRef.INSTANCE.toggleManipMode(stack.getOrCreateTag());
    }

////////////////
// Cosmetics: //
////////////////

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tipFlag)
    {
        CompoundTag stackTag = stack.getOrCreateTag();

        Map<String, Integer> entryMap = VitalityTagRef.INSTANCE.getStoredVitality(stackTag);

        ListTag entryList = VitalityTagRef.INSTANCE.getEntryList(stackTag);

        if (Screen.hasShiftDown())
        {
            for (String type : VITALITY_TYPES)
            {
                ChatFormatting format = switch (type)
                {
                    case VITALITY_BLUE -> ChatFormatting.BLUE;
                    case VITALITY_GREEN -> ChatFormatting.GREEN;
                    case VITALITY_YELLOW -> ChatFormatting.YELLOW;
                    case VITALITY_GOLD -> ChatFormatting.GOLD;
                    case VITALITY_RED -> ChatFormatting.RED;
                    case VITALITY_BLACK -> ChatFormatting.DARK_PURPLE;
                    case VITALITY_WHITE -> ChatFormatting.LIGHT_PURPLE;
                    default -> ChatFormatting.GRAY;
                };

                Integer volume = entryMap.get(type);

                if (volume != null && volume > 0)
                {
                    tooltip.add(translatable("tooltip.flyingfamiliars.fluid_tag.vitality_" + type)
                            .withStyle(format).append(literal(String.valueOf(volume))));
                }
            }
        }
        else
        {
            switch (entryMap.size())
            {
                case 0: tooltip.add(translatable("tooltip.flyingfamiliars.fluid_tag.empty")
                        .withStyle(ChatFormatting.GRAY));
                    break;

                case 1: tooltip.add(translatable("tooltip.flyingfamiliars.fluid_tag.stored_1")
                        .withStyle(ChatFormatting.GRAY));
                    break;

                default : tooltip.add(literal(String.valueOf(entryMap.size())).withStyle(ChatFormatting.GRAY).append(translatable("tooltip.flyingfamiliars.fluid_tag.stored_multiple")
                        .withStyle(ChatFormatting.GRAY)));
                    break;
            }
        }
    }
}
