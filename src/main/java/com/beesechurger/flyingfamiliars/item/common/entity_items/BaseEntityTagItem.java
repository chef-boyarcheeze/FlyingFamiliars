package com.beesechurger.flyingfamiliars.item.common.entity_items;

import com.beesechurger.flyingfamiliars.item.common.BaseStorageTagItem;
import com.beesechurger.flyingfamiliars.tags.EntityTagRef;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

import static com.beesechurger.flyingfamiliars.util.FFConstants.CHAT_GRAY;
import static net.minecraft.network.chat.Component.literal;
import static net.minecraft.network.chat.Component.translatable;

public abstract class BaseEntityTagItem extends BaseStorageTagItem implements IEntityCycleItem
{
    public EntityTagRef entities;

    public BaseEntityTagItem(Properties properties)
    {
        super(properties);

        entities = new EntityTagRef(3);
    }

    public BaseEntityTagItem(Properties properties, int entryModifier)
    {
        super(properties);

        entities = new EntityTagRef(3 * entryModifier);
    }

////////////////
// Accessors: //
////////////////

// Booleans:
    @Override
    public boolean isFoil(ItemStack stack)
    {
        return entities.isFull(stack.getOrCreateTag());
    }

    @Override
    public boolean isBarVisible(ItemStack stack)
    {
        return getBarWidth(stack) > 0;
    }

    @Override
    public boolean canCycle(ItemStack stack)
    {
        return entities.getEntryCount(stack.getOrCreateTag()) > 1;
    }

// Integers:
    @Override
    public int getBarWidth(ItemStack stack)
    {
        return Math.round((float) entities.getEntryCount(stack.getOrCreateTag()) * 13.0f / (float) entities.getMaxEntries());
    }

    @Override
    public int getBarColor(ItemStack stack)
    {
        return CHAT_GRAY;
    }

////////////////
// Cosmetics: //
////////////////

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tipFlag)
    {
        ListTag entryList = entities.getEntryList(stack.getOrCreateTag());
        int entryCount = entities.getEntryCount(stack.getOrCreateTag());

        if (Screen.hasShiftDown())
        {
            int count = 0;

            for (Tag entry : entryList)
            {
                ChatFormatting format = entities.isEntityTamed((CompoundTag) entry) ? ChatFormatting.GREEN : ChatFormatting.YELLOW;

                tooltip.add(translatable("tooltip.flyingfamiliars.entity_tag.slot")
                        .withStyle(format).append(" " + (count+1) + ": " + entities.getEntityID((CompoundTag) entry)));

                count++;
            }
        }
        else
        {
            switch (entryCount)
            {
                case 0: tooltip.add(translatable("tooltip.flyingfamiliars.entity_tag.empty")
                        .withStyle(ChatFormatting.GRAY));
                    break;

                case 1: tooltip.add(translatable("tooltip.flyingfamiliars.entity_tag.stored_1")
                        .withStyle(ChatFormatting.GRAY));
                    break;

                default : tooltip.add(literal(String.valueOf(entryCount)).append(translatable("tooltip.flyingfamiliars.entity_tag.stored_multiple")
                        .withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.GRAY));
                    break;
            }

            tooltip.add(translatable("tooltip.flyingfamiliars.entity_tag.left_shift").withStyle(ChatFormatting.GRAY));
        }
    }
}
