package com.beesechurger.flyingfamiliars.item.common.entity;

import com.beesechurger.flyingfamiliars.item.common.BaseStorageTagItem;
import com.beesechurger.flyingfamiliars.item.tooltip.EntityStorageTooltipComponent;
import com.beesechurger.flyingfamiliars.tags.EntityTagRef;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

import static com.beesechurger.flyingfamiliars.item.FFItemHandler.getEntityStackList;

public abstract class BaseEntityTagItem extends BaseStorageTagItem implements IEntityCycleItem
{
    protected static final int ENTITY_TOOLTIP_VISIBLE_MAX = 3;

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
        return ChatFormatting.GRAY.getColor();
    }

    protected abstract int getColor();

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
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag tipFlag)
    {
        ListTag entryList = EntityTagRef.INSTANCE.getEntryList(stack.getOrCreateTag());

        if (EntityTagRef.INSTANCE.isEmpty(stack.getOrCreateTag()))
        {
            tooltip.add(Component.translatable("tooltip.flyingfamiliars.entity_tag.empty")
                    .withStyle(ChatFormatting.GRAY));
        }
        else
        {
            if (!Screen.hasShiftDown())
            {
                if (entryList.size() > 1)
                {
                    tooltip.add(Component.literal(String.valueOf(entryList.size())).append(Component.translatable("tooltip.flyingfamiliars.entity_tag.stored_multiple")
                                    .withStyle(ChatFormatting.GRAY))
                            .withStyle(ChatFormatting.GRAY));
                }
                else
                {
                    tooltip.add(Component.translatable("tooltip.flyingfamiliars.entity_tag.stored_1")
                            .withStyle(ChatFormatting.GRAY));
                }

                tooltip.add(Component.translatable("tooltip.flyingfamiliars.entity_tag.left_shift").withStyle(ChatFormatting.GRAY));
            }
            else
            {
                if (!Screen.hasAltDown())
                {
                    tooltip.add(Component.translatable("tooltip.flyingfamiliars.tag.left_alt")
                            .withStyle(ChatFormatting.GRAY));
                }
                if (!Screen.hasControlDown())
                {
                    tooltip.add(Component.translatable("tooltip.flyingfamiliars.tag.left_control")
                            .withStyle(ChatFormatting.GRAY));
                }

                /*ChatFormatting format = EntityTagRef.INSTANCE.isEntityTamed((CompoundTag) entryTag) ? ChatFormatting.GREEN : ChatFormatting.YELLOW;
                    tooltip.add(translatable("tooltip.flyingfamiliars.entity_tag.slot")
                            .withStyle(format).append(" " + (count+1) + ": " + EntityTagRef.INSTANCE.getEntityID((CompoundTag) entryTag)));*/
            }
        }
    }

    public List<TooltipComponent> getTooltipComponents(ItemStack stack)
    {
        List<TooltipComponent> componentTooltips = new ArrayList<>();

        if (Screen.hasShiftDown())
        {
            ListTag entryList = EntityTagRef.INSTANCE.getEntryList(stack.getOrCreateTag());
            List<List<CompoundTag>> componentEntryLists = new ArrayList<>();

            boolean hasMoreEntities = false;

            // separate entryTag list into rows of 9, accounting for remainder
            for (int i = 0; i < entryList.size() && (Screen.hasAltDown() || i < ENTITY_TOOLTIP_VISIBLE_MAX); i++)
            {
                if (i % 9 == 0)
                {
                    componentEntryLists.add(new ArrayList<>());
                }

                componentEntryLists.get(componentEntryLists.size() - 1).add((CompoundTag) entryList.get(i));
            }

            if (entryList.size() > ENTITY_TOOLTIP_VISIBLE_MAX && !Screen.hasAltDown())
            {
                hasMoreEntities = true;
            }

            for (var componentEntryList : componentEntryLists)
            {
                componentTooltips.add(new EntityStorageTooltipComponent(componentEntryList, hasMoreEntities, getColor()));
            }
        }

        return componentTooltips;
    }
}
