package com.beesechurger.flyingfamiliars.item.common.fluid_items;

import com.beesechurger.flyingfamiliars.item.common.BaseStorageTagItem;
import com.beesechurger.flyingfamiliars.item.common.ITieredItem;
import com.beesechurger.flyingfamiliars.tags.VitalityTagRef;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

import static com.beesechurger.flyingfamiliars.util.FFConstants.*;
import static net.minecraft.network.chat.Component.literal;
import static net.minecraft.network.chat.Component.translatable;

public abstract class BaseVitalityTagItem extends BaseStorageTagItem
{
    public VitalityTagRef fluids;

    public BaseVitalityTagItem(Properties properties)
    {
        super(properties);

        fluids = new VitalityTagRef(1, 1000);
    }

    public BaseVitalityTagItem(Properties properties, int entryModifer, int volume)
    {
        super(properties);

        fluids = new VitalityTagRef(entryModifer, volume);
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

////////////////
// Cosmetics: //
////////////////

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tipFlag)
    {
        CompoundTag stackTag = stack.getOrCreateTag();

        List<Integer> entryList = fluids.getStoredVitality(stackTag);
        int entryCount = fluids.getEntryCount(stackTag);

        if (Screen.hasShiftDown())
        {
            for (int i = 0; i < entryCount; i++)
            {
                ChatFormatting format = switch (i)
                {
                    case 0 -> ChatFormatting.BLUE;
                    case 1 -> ChatFormatting.GREEN;
                    case 2 -> ChatFormatting.YELLOW;
                    case 3 -> ChatFormatting.GOLD;
                    case 4 -> ChatFormatting.RED;
                    case 5 -> ChatFormatting.DARK_PURPLE;
                    case 6 -> ChatFormatting.LIGHT_PURPLE;
                    default -> ChatFormatting.DARK_GRAY;
                };

                tooltip.add(translatable("tooltip.flyingfamiliars.fluid_tag.vitality_" + VITALITY_TYPES.get(i)).withStyle(format));
            }
        }
        else
        {
            switch (entryCount)
            {
                case 0: tooltip.add(translatable("tooltip.flyingfamiliars.fluid_tag.empty")
                        .withStyle(ChatFormatting.GRAY));
                    break;

                case 1: tooltip.add(translatable("tooltip.flyingfamiliars.fluid_tag.stored_1")
                        .withStyle(ChatFormatting.GRAY));
                    break;

                default : tooltip.add(literal(String.valueOf(entryCount)).append(translatable("tooltip.flyingfamiliars.fluid_tag.stored_multiple")
                        .withStyle(ChatFormatting.GRAY)));
                    break;
            }
        }
    }
}
