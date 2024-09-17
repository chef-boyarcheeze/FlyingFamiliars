package com.beesechurger.flyingfamiliars.item.common.soul_items.SoulWand;

import com.beesechurger.flyingfamiliars.item.EntityTagItemHelper;
import com.beesechurger.flyingfamiliars.item.WandEffectItemHelper;
import com.beesechurger.flyingfamiliars.item.common.soul_items.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.item.common.soul_items.IWandEffectItem;
import com.beesechurger.flyingfamiliars.wand_effect.BaseWandEffect;
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

import static com.beesechurger.flyingfamiliars.util.FFValueConstants.CHAT_GRAY;

public class BaseSoulWand extends BaseEntityTagItem implements IWandEffectItem
{
    public BaseSoulWand(Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        EntityTagItemHelper.ensureTagPopulated(stack);

        if(!level.isClientSide())
        {
            // Get selected wand effect
            BaseWandEffect selectedWandEffect = WandEffectItemHelper.getSelectedWandEffect(getSelection(stack));

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

    @Override
    public Component getName(ItemStack stack)
    {
        BaseWandEffect selectedWandEffect = WandEffectItemHelper.getSelectedWandEffect(getSelection(stack));

        if(stack.hasTag() && selectedWandEffect != null)
        {
            return Component.translatable(super.getDescriptionId(stack))
                    .append(" (")
                    .append(Component.translatable(selectedWandEffect.getTranslatableName()))
                    .append(")");
        }
        else
            return Component.translatable(super.getDescriptionId(stack));
    }

    @Override
    public int getBarColor(ItemStack stack)
    {
        BaseWandEffect selectedWandEffect = WandEffectItemHelper.getSelectedWandEffect(getSelection(stack));

        if(stack.hasTag() && selectedWandEffect != null)
        {
            return selectedWandEffect.getBarColor();
        }
        else
            return CHAT_GRAY;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tipFlag)
    {
        BaseWandEffect selectedWandEffect = WandEffectItemHelper.getSelectedWandEffect(getSelection(stack));

        if(stack.hasTag() && selectedWandEffect != null)
        {
            tooltip.add(Component.translatable(selectedWandEffect.getTranslatableName())
                    .withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, level, tooltip, tipFlag);
    }
}
