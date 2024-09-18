package com.beesechurger.flyingfamiliars.item.common.entity_items;

import com.beesechurger.flyingfamiliars.item.BaseExtraTagItem;
import com.beesechurger.flyingfamiliars.util.FFValueConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BaseEntityTagItem extends BaseExtraTagItem implements IEntityTagItem, ISoulCycleItem
{
    public BaseEntityTagItem(Properties properties)
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
        return stack.hasTag() && getEntityCount(stack) == getMaxEntities();
    }

    @Override
    public boolean isBarVisible(ItemStack stack)
    {
        return stack.hasTag() && getEntityCount(stack) > 0;
    }

// Integers:
    @Override
    public int getBarWidth(ItemStack stack)
    {
        if(stack.hasTag())
            return Math.round((float) getEntityCount(stack) * 13.0f / (float) getMaxEntities());

        return 0;
    }

    @Override
    public int getBarColor(ItemStack stack)
    {
        return FFValueConstants.CHAT_GRAY;
    }

////////////////
// Cosmetics: //
////////////////

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tipFlag)
    {
        int entityCount = getEntityCount(stack);

        if (stack.hasTag())
        {
            CompoundTag stackTag = stack.getTag();

            if (entityCount != 0)
            {
                if (Screen.hasShiftDown())
                {
                    for (int i = 0; i < getMaxEntities(); i++)
                    {
                        CompoundTag tag = getEntityTag(stack, i);
                        ChatFormatting format = isEntityTamed(tag) && !isEntityEmpty(tag) ? ChatFormatting.GREEN : ChatFormatting.YELLOW;

                        if (!isEntityEmpty(tag))
                            tooltip.add(Component.translatable("tooltip.flyingfamiliars.entity_tag.slot")
                                    .withStyle(format).append(" " + (i+1) + ": " + getEntityID(tag)));
                    }
                }
                else
                {
                    switch (entityCount)
                    {
                        case 1: tooltip.add(Component.translatable("tooltip.flyingfamiliars.entity_tag.stored_1").withStyle(ChatFormatting.GRAY));
                            break;

                        case 2: tooltip.add(Component.translatable("tooltip.flyingfamiliars.entity_tag.stored_2").withStyle(ChatFormatting.GRAY));
                            break;

                        case 3: tooltip.add(Component.translatable("tooltip.flyingfamiliars.entity_tag.stored_3").withStyle(ChatFormatting.GRAY));
                            break;
                    }

                    tooltip.add(Component.translatable("tooltip.flyingfamiliars.entity_tag.left_shift").withStyle(ChatFormatting.GRAY));
                }
            }
            else
            {
                tooltip.add(Component.translatable("tooltip.flyingfamiliars.entity_tag.empty").withStyle(ChatFormatting.GRAY));
            }
        }
        else
        {
            tooltip.add(Component.translatable("tooltip.flyingfamiliars.entity_tag.empty").withStyle(ChatFormatting.GRAY));
        }
    }

///////////////////
// Item actions: //
///////////////////

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        ensureTagPopulated(stack);

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
