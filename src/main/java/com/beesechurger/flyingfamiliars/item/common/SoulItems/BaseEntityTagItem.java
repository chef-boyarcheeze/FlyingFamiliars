package com.beesechurger.flyingfamiliars.item.common.SoulItems;

import com.beesechurger.flyingfamiliars.item.EntityTagItemHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.BASE_ENTITY_TAGNAME;
import static com.beesechurger.flyingfamiliars.util.FFStringConstants.ENTITY_EMPTY;

public abstract class BaseEntityTagItem extends Item implements ISoulCycleItem
{
    protected int capacityMod = 1;

    public BaseEntityTagItem(Properties properties)
    {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);

        EntityTagItemHelper.ensureTagPopulated(stack);

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    public int getEntityCount(ItemStack stack)
    {
        EntityTagItemHelper.ensureTagPopulated(stack);

        int entityCount = 0;

        if(stack.hasTag())
        {
            CompoundTag stackTag = stack.getTag();
            ListTag tagList = stackTag.getList(BASE_ENTITY_TAGNAME, 10);

            for(int i = 0; i < getMaxEntities(); i++)
            {
                // Need to use regular Tag object here, not CompoundTag
                if(!tagList.get(i).toString().contains(ENTITY_EMPTY)) entityCount++;
            }
        }

        return entityCount;
    }

    public int getMaxEntities()
    {
        return EntityTagItemHelper.MAX_ENTITIES * capacityMod;
    }

    public String getID(int listValue, ItemStack stack)
    {
        return stack.getTag().getList(BASE_ENTITY_TAGNAME, 10).getCompound(listValue).getString(BASE_ENTITY_TAGNAME);
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player)
    {
        return !player.isCreative();
    }

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
        return 32767;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tipFlag)
    {
        int entityCount = getEntityCount(stack);

        if (stack.hasTag())
        {
            CompoundTag stackTag = stack.getTag();

            if(entityCount != 0)
            {
                if(Screen.hasShiftDown())
                {
                    for (int i = 0; i < getMaxEntities(); i++)
                    {
                        if(getID(i, stack) != ENTITY_EMPTY)
                            tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.entity_tag.slot").withStyle(ChatFormatting.YELLOW).append(" " + (i+1) + ": " + getID(i, stack)));
                    }
                }
                else
                {
                    switch(entityCount)
                    {
                        case 1: tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.entity_tag.stored_1").withStyle(ChatFormatting.GRAY));
                            break;

                        case 2: tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.entity_tag.stored_2").withStyle(ChatFormatting.GRAY));
                            break;

                        case 3: tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.entity_tag.stored_3").withStyle(ChatFormatting.GRAY));
                            break;
                    }

                    tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.entity_tag.left_shift").withStyle(ChatFormatting.GRAY));
                }
            }
            else
            {
                tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.entity_tag.empty").withStyle(ChatFormatting.GRAY));
            }
        }
        else
        {
            tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.entity_tag.empty").withStyle(ChatFormatting.GRAY));
        }
    }
}
