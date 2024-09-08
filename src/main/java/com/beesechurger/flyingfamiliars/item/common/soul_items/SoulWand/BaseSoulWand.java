package com.beesechurger.flyingfamiliars.item.common.soul_items.SoulWand;

import com.beesechurger.flyingfamiliars.entity.common.wand_effect.projectile.CaptureProjectile;
import com.beesechurger.flyingfamiliars.item.EntityTagItemHelper;
import com.beesechurger.flyingfamiliars.item.WandEffectItemHelper;
import com.beesechurger.flyingfamiliars.item.common.soul_items.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.item.common.soul_items.IModeCycleItem;
import com.beesechurger.flyingfamiliars.item.common.soul_items.IWandEffectItem;
import com.beesechurger.flyingfamiliars.registries.FFKeys;
import com.beesechurger.flyingfamiliars.wand_effect.BaseWandEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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

            // determine if there is enough 'fuel' for action

            player.awardStat(Stats.ITEM_USED.get(this));
            player.getCooldowns().addCooldown(this, selectedWandEffect.getCooldown());
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public Component getName(ItemStack stack)
    {
        if(stack.hasTag())
        {
            BaseWandEffect selectedWandEffect = WandEffectItemHelper.getSelectedWandEffect(getSelection(stack));

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
        if(stack.hasTag())
        {
            BaseWandEffect selectedWandEffect = WandEffectItemHelper.getSelectedWandEffect(getSelection(stack));

            return selectedWandEffect.getBarColor();
        }
        else
            return CHAT_GRAY;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tipFlag)
    {
        if(stack.hasTag())
        {
            BaseWandEffect selectedWandEffect = WandEffectItemHelper.getSelectedWandEffect(getSelection(stack));

            tooltip.add(Component.translatable(selectedWandEffect.getTranslatableName())
                    .withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, level, tooltip, tipFlag);
    }
}
