package com.beesechurger.flyingfamiliars.item.common.entity_items;

import com.beesechurger.flyingfamiliars.item.FFItemHandler;
import com.beesechurger.flyingfamiliars.item.common.BaseStorageTagItem;
import com.beesechurger.flyingfamiliars.tags.EntityTagRef;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

import static com.beesechurger.flyingfamiliars.item.FFItemHandler.getEntityStackList;
import static com.beesechurger.flyingfamiliars.util.FFConstants.CHAT_GRAY;
import static net.minecraft.network.chat.Component.literal;
import static net.minecraft.network.chat.Component.translatable;

public abstract class BaseEntityTagItem extends BaseStorageTagItem implements IEntityCycleItem
{
    public BaseEntityTagItem(Properties properties)
    {
        super(properties);
    }

//////////////////
/// Accessors: ///
//////////////////

/// Booleans:

    @Override
    public boolean isFoil(ItemStack stack)
    {
        return EntityTagRef.INSTANCE.isFull(stack.getOrCreateTag());
    }

    @Override
    public boolean isBarVisible(ItemStack stack)
    {
        return getBarWidth(stack) > 0;
    }

    @Override
    public boolean canCycle(Player player, ItemStack stack)
    {
        int count = 0;

        for (ItemStack entityStack : getEntityStackList(player))
        {
            if (entityStack != player.getMainHandItem())
            {
                count += EntityTagRef.INSTANCE.getEntryCount(entityStack.getOrCreateTag());
            }
        }

        return EntityTagRef.INSTANCE.getEntryCount(stack.getOrCreateTag()) > 1 || count > 0;
    }

    public boolean getManipMode(ItemStack stack)
    {
        return EntityTagRef.INSTANCE.getManipMode(stack.getOrCreateTag());
    }

/// Integers:

    @Override
    public int getBarWidth(ItemStack stack)
    {
        return Math.round((float) EntityTagRef.INSTANCE.getEntryCount(stack.getOrCreateTag()) * 13.0f / (float) EntityTagRef.INSTANCE.getMaxEntries(stack.getOrCreateTag()));
    }

    @Override
    public int getBarColor(ItemStack stack)
    {
        return CHAT_GRAY;
    }

/////////////////
/// Mutators: ///
/////////////////

/// Booleans:

    public void toggleManipMode(ItemStack stack)
    {
        EntityTagRef.INSTANCE.toggleManipMode(stack.getOrCreateTag());
    }

//////////////////
/// Cosmetics: ///
//////////////////

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tipFlag)
    {
        ListTag entryList = EntityTagRef.INSTANCE.getEntryList(stack.getOrCreateTag());
        int entryCount = EntityTagRef.INSTANCE.getEntryCount(stack.getOrCreateTag());

        if (entryCount == 0)
        {
            tooltip.add(translatable("tooltip.flyingfamiliars.entity_tag.empty")
                    .withStyle(ChatFormatting.GRAY));
        }
        else
        {
            if (Screen.hasShiftDown())
            {
                int count = 0;

                for (Tag entry : entryList)
                {
                    ChatFormatting format = EntityTagRef.INSTANCE.isEntityTamed((CompoundTag) entry) ? ChatFormatting.GREEN : ChatFormatting.YELLOW;

                    tooltip.add(translatable("tooltip.flyingfamiliars.entity_tag.slot")
                            .withStyle(format).append(" " + (count+1) + ": " + EntityTagRef.INSTANCE.getEntityID((CompoundTag) entry)));

                    count++;
                }
            }
            else
            {
                if (entryCount > 1)
                {
                    tooltip.add(literal(String.valueOf(entryCount)).append(translatable("tooltip.flyingfamiliars.entity_tag.stored_multiple")
                            .withStyle(ChatFormatting.GRAY))
                            .withStyle(ChatFormatting.GRAY));
                }
                else
                {
                    tooltip.add(translatable("tooltip.flyingfamiliars.entity_tag.stored_1")
                            .withStyle(ChatFormatting.GRAY));
                }

                tooltip.add(translatable("tooltip.flyingfamiliars.entity_tag.left_shift").withStyle(ChatFormatting.GRAY));
            }
        }
    }
}
