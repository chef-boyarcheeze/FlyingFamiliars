package com.beesechurger.flyingfamiliars.item.common.entity_items.SoulWand;

import com.beesechurger.flyingfamiliars.item.common.entity_items.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.tags.WandEffectTagRef;
import com.beesechurger.flyingfamiliars.wand_effect.BaseWandEffect;
import com.beesechurger.flyingfamiliars.wand_effect.WandEffectItemHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

import static com.beesechurger.flyingfamiliars.util.FFConstants.CHAT_GRAY;

public abstract class BaseSoulWand extends BaseEntityTagItem
{
    public WandEffectTagRef wandEffects;

    public BaseSoulWand(Properties properties)
    {
        super(properties);

        wandEffects = new WandEffectTagRef(5);
    }

////////////////
// Accessors: //
////////////////

// Integers:
    @Override
    public int getBarColor(ItemStack stack)
    {
        BaseWandEffect selectedWandEffect = WandEffectItemHelper.getSelectedWandEffect(wandEffects.getSelectedWandEffect(stack.getOrCreateTag()));

        if(selectedWandEffect != null)
        {
            return selectedWandEffect.getBarColor();
        }
        else
        {
            return CHAT_GRAY;
        }
    }

// Misc:
    @Override
    public Component getName(ItemStack stack)
    {
        BaseWandEffect selectedWandEffect = WandEffectItemHelper.getSelectedWandEffect(wandEffects.getSelectedWandEffect(stack.getOrCreateTag()));

        if(selectedWandEffect != null)
        {
            return Component.translatable(super.getDescriptionId(stack))
                    .append(" (")
                    .append(Component.translatable(selectedWandEffect.getTranslatableName()))
                    .append(")");
        }
        else
        {
            return Component.translatable(super.getDescriptionId(stack));
        }
    }

////////////////
// Cosmetics: //
////////////////

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tipFlag)
    {
        BaseWandEffect selectedWandEffect = WandEffectItemHelper.getSelectedWandEffect(wandEffects.getSelectedWandEffect(stack.getOrCreateTag()));

        if(stack.hasTag() && selectedWandEffect != null)
        {
            tooltip.add(Component.translatable(selectedWandEffect.getTranslatableName())
                    .withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, level, tooltip, tipFlag);
    }

///////////////////
// Item actions: //
///////////////////

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);

        if(!level.isClientSide())
        {
            // Get selected wand effect
            BaseWandEffect selectedWandEffect = WandEffectItemHelper.getSelectedWandEffect(wandEffects.getSelectedWandEffect(stack.getOrCreateTag()));

            if(selectedWandEffect != null)
            {
                // determine if there is enough 'fuel' for action

                selectedWandEffect.action(level, player);

                player.awardStat(Stats.ITEM_USED.get(this));
                player.getCooldowns().addCooldown(this, selectedWandEffect.getCooldown());
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
