package com.beesechurger.flyingfamiliars.item.common.entity_items.SoulWand;

import com.beesechurger.flyingfamiliars.item.FFItemClientExtension;
import com.beesechurger.flyingfamiliars.item.common.entity_items.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.tags.WandEffectTagRef;
import com.beesechurger.flyingfamiliars.wand_effect.common.BaseWandEffect;
import com.beesechurger.flyingfamiliars.wand_effect.common.WandEffectItemHelper;
import com.beesechurger.flyingfamiliars.wand_effect.common.projectile.CaptureWandEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

import static com.beesechurger.flyingfamiliars.util.FFConstants.CHAT_GRAY;

public abstract class BaseSoulWand extends BaseEntityTagItem
{
    public BaseSoulWand(Properties properties)
    {
        super(properties);
    }

//////////////////
/// Accessors: ///
//////////////////

/// Booleans:

    @Override
    public boolean canCycle(Player player, ItemStack stack)
    {
        BaseWandEffect selectedWandEffect = getSelectedWandEffect(stack);

        return super.canCycle(player, stack) && selectedWandEffect != null && selectedWandEffect instanceof CaptureWandEffect;
    }

/// Integers:

    @Override
    public int getUseDuration(ItemStack stack)
    {
        BaseWandEffect selectedWandEffect = getSelectedWandEffect(stack);

        return selectedWandEffect != null ? selectedWandEffect.getUseDurationMax() : super.getUseDuration(stack);
    }

    @Override
    public int getBarColor(ItemStack stack)
    {
        BaseWandEffect selectedWandEffect = getSelectedWandEffect(stack);

        return selectedWandEffect != null ? selectedWandEffect.getBarColor() : CHAT_GRAY;
    }

/// Misc:

    @Override
    public Component getName(ItemStack stack)
    {
        BaseWandEffect selectedWandEffect = getSelectedWandEffect(stack);

        return selectedWandEffect != null ? Component.translatable(super.getDescriptionId(stack))
                                            .append(" (")
                                            .append(Component.translatable(selectedWandEffect.getTranslatableName()))
                                            .append(")")
                                          : Component.translatable(super.getDescriptionId(stack));
    }

    public BaseWandEffect getSelectedWandEffect(ItemStack stack)
    {
        return WandEffectItemHelper.getSelectedWandEffect(WandEffectTagRef.INSTANCE.getSelectedWandEffect(stack.getOrCreateTag()));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack)
    {
        BaseWandEffect selectedWandEffect = getSelectedWandEffect(stack);

        return selectedWandEffect != null ? selectedWandEffect.getUseAnimation() : super.getUseAnimation(stack);
    }

//////////////////
/// Cosmetics: ///
//////////////////

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tipFlag)
    {
        BaseWandEffect selectedWandEffect = getSelectedWandEffect(stack);

        if(stack.hasTag() && selectedWandEffect != null)
        {
            tooltip.add(Component.translatable(selectedWandEffect.getTranslatableName())
                    .withStyle(selectedWandEffect.getTooltipColor()));
        }

        super.appendHoverText(stack, level, tooltip, tipFlag);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer)
    {
        consumer.accept(FFItemClientExtension.INSTANCE);
    }

/////////////////////
/// Item actions: ///
/////////////////////

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        BaseWandEffect selectedWandEffect = getSelectedWandEffect(stack);

        if (selectedWandEffect != null && !selectedWandEffect.usableOnBlockOnly())
        {
            // determine if there is enough 'fuel' for action
            if (!player.isCreative() && false) //flag instead of false TODO
            {
                return InteractionResultHolder.fail(stack);
            }
            else if (selectedWandEffect.canBePartiallyDrawn() || selectedWandEffect.canBeContinuouslyDrawn() || selectedWandEffect.getUseDurationMax() > 0)
            {
                player.startUsingItem(hand);
                return InteractionResultHolder.pass(stack);
            }
            else
            {
                selectedWandEffect.use(level, player);

                player.awardStat(Stats.ITEM_USED.get(this));
                player.getCooldowns().addCooldown(this, selectedWandEffect.getCooldown());
            }
        }

        if (selectedWandEffect != null && selectedWandEffect.usableOnBlockOnly())
        {
            return InteractionResultHolder.pass(stack);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        ItemStack stack = context.getItemInHand();
        BaseWandEffect selectedWandEffect = getSelectedWandEffect(stack);
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());

        if (selectedWandEffect != null && selectedWandEffect.checkLookedAtBlock(state) && selectedWandEffect.usableOnBlockOnly())
        {
            Player player = context.getPlayer();

            // determine if there is enough 'fuel' for action
            if (!player.isCreative() && !false) //flag instead of false TODO
            {
                return InteractionResult.FAIL;
            }
            else
            {
                player.startUsingItem(context.getHand());
                return InteractionResult.PASS;
            }
        }

        if (selectedWandEffect != null && !selectedWandEffect.usableOnBlockOnly())
        {
            return InteractionResult.PASS;
        }

        return InteractionResult.CONSUME;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int duration)
    {
        BaseWandEffect selectedWandEffect = getSelectedWandEffect(stack);

        if (entity instanceof Player player && selectedWandEffect != null)
        {
            BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
            BlockPos pos = result.getBlockPos();
            BlockState state = level.getBlockState(pos);

            // looked at invalid block, stop using
            if (!selectedWandEffect.checkLookedAtBlock(state))
            {
                player.stopUsingItem();
                return;
            }

            if (selectedWandEffect.canBeContinuouslyDrawn() && duration % selectedWandEffect.getCooldown() == 0)
            {
                if (selectedWandEffect.usableOnBlockOnly())
                {
                    selectedWandEffect.useOn(level, player, pos);
                }
                else
                {
                    selectedWandEffect.use(level, player);
                }

                // consume fuel

                player.getCooldowns().addCooldown(this, selectedWandEffect.getCooldown());
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int duration)
    {
        BaseWandEffect selectedWandEffect = getSelectedWandEffect(stack);

        if (entity instanceof Player player && selectedWandEffect != null)
        {
            BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
            BlockPos pos = result.getBlockPos();
            BlockState state = level.getBlockState(pos);

            // looked at invalid block, stop using
            if (!selectedWandEffect.checkLookedAtBlock(state))
            {
                player.stopUsingItem();
                return;
            }

            if (selectedWandEffect.canBePartiallyDrawn() || selectedWandEffect.getUseDurationMax() - duration > selectedWandEffect.getUseDurationMin())
            {
                if (selectedWandEffect.usableOnBlockOnly())
                {
                    selectedWandEffect.useOn(level, player, pos);
                }
                else
                {
                    selectedWandEffect.use(level, player);
                }

                // consume fuel

                player.awardStat(Stats.ITEM_USED.get(this));
                player.getCooldowns().addCooldown(this, selectedWandEffect.getCooldown());
            }
        }
    }
}
