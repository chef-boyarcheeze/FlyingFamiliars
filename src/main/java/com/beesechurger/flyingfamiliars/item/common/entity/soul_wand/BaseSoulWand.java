package com.beesechurger.flyingfamiliars.item.common.entity.soul_wand;

import com.beesechurger.flyingfamiliars.item.FFItemClientExtension;
import com.beesechurger.flyingfamiliars.item.FFItemHandler;
import com.beesechurger.flyingfamiliars.item.common.entity.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.tags.EntityTagUtil;
import com.beesechurger.flyingfamiliars.tags.SpiritTagUtil;
import com.beesechurger.flyingfamiliars.tags.WandEffectTagUtil;
import com.beesechurger.flyingfamiliars.wand_effect.common.BaseWandEffect;
import com.beesechurger.flyingfamiliars.wand_effect.common.WandEffectItemHelper;
import com.beesechurger.flyingfamiliars.wand_effect.common.projectile.CaptureWandEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
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
import java.util.Map;
import java.util.function.Consumer;

import static com.beesechurger.flyingfamiliars.util.FFConstants.STORAGE_SPIRIT_TYPE;

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
    public boolean canCycle(Player player, ItemStack scrollStack, List<ItemStack> allStacks)
    {
        BaseWandEffect selectedWandEffect = getSelectedWandEffect(scrollStack);

        return super.canCycle(player, scrollStack, allStacks) && selectedWandEffect != null && selectedWandEffect instanceof CaptureWandEffect;
    }

    public boolean canCastWandEffect(ItemStack phylacteryStack, Map<String, Integer> requiredSpiritContents)
    {
        if (Minecraft.getInstance().player.isCreative())
        {
            return true;
        }

        if (!phylacteryStack.isEmpty())
        {
            Map<String, Integer> spiritContents = SpiritTagUtil.INSTANCE.getSpiritContents(phylacteryStack.getOrCreateTag());

            for (var entry : requiredSpiritContents.entrySet())
            {
                if (!spiritContents.containsKey(entry.getKey()) || spiritContents.get(entry.getKey()) < requiredSpiritContents.get(entry.getKey()))
                {
                    return false;
                }
            }

            return true;
        }

        return false;
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

        return selectedWandEffect != null ? selectedWandEffect.getColor() : ChatFormatting.GRAY.getColor();
    }

/// Misc:

    @Override
    public Component getName(ItemStack stack)
    {
        BaseWandEffect selectedWandEffect = getSelectedWandEffect(stack);

        return selectedWandEffect != null ? Component.translatable(super.getDescriptionId(stack)).withStyle(Style.EMPTY.withColor(getColor()))
                                            .append(Component.literal(" (").withStyle(ChatFormatting.GRAY))
                                            .append(Component.translatable(selectedWandEffect.getTranslatableName()).withStyle(Style.EMPTY.withColor(selectedWandEffect.getColor())))
                                            .append(Component.literal(")").withStyle(ChatFormatting.GRAY))
                                          : Component.translatable(super.getDescriptionId(stack)).withStyle(Style.EMPTY.withColor(getColor()));
    }

    public BaseWandEffect getSelectedWandEffect(ItemStack stack)
    {
        return WandEffectItemHelper.getSelectedWandEffect(WandEffectTagUtil.INSTANCE.getSelectedWandEffect(stack.getOrCreateTag()));
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

        if((EntityTagUtil.INSTANCE.isEmpty(stack.getOrCreateTag()) || !Screen.hasShiftDown()) && stack.hasTag() && selectedWandEffect != null)
        {
            tooltip.add(Component.translatable(selectedWandEffect.getTranslatableName())
                    .withStyle(Style.EMPTY.withColor(selectedWandEffect.getColor())));
        }

        super.appendHoverText(stack, level, tooltip, tipFlag);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer)
    {
        consumer.accept(FFItemClientExtension.INSTANCE);
    }

/////////////////////
/// Item Actions: ///
/////////////////////

    protected boolean consumeFuel(ItemStack phylacteryStack, Map<String, Integer> requiredSpiritContents)
    {
        if (Minecraft.getInstance().player.isCreative())
        {
            return true;
        }

        if (!phylacteryStack.isEmpty() && canCastWandEffect(phylacteryStack, requiredSpiritContents))
        {
            ListTag spiritEntryList = SpiritTagUtil.INSTANCE.getEntryList(phylacteryStack.getOrCreateTag());

            for (int i = 0; i < spiritEntryList.size();)
            {
                CompoundTag spiritEntryTag = (CompoundTag) spiritEntryList.get(i);
                String type = spiritEntryTag.getString(STORAGE_SPIRIT_TYPE);

                if (requiredSpiritContents.containsKey(type))
                {
                    SpiritTagUtil.INSTANCE.removeSpirit(spiritEntryTag, requiredSpiritContents.get(type));
                }

                if (!(SpiritTagUtil.INSTANCE.isEntryEmpty(spiritEntryTag) && SpiritTagUtil.INSTANCE.removeEntry(phylacteryStack.getOrCreateTag(), spiritEntryTag)))
                {
                    i++; // did not remove entry from phylacteryStack, advance normally
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        BaseWandEffect selectedWandEffect = getSelectedWandEffect(stack);

        if (selectedWandEffect != null && !selectedWandEffect.usableOnBlockOnly())
        {
            if (canCastWandEffect(FFItemHandler.getPhylacteryCharm(player), selectedWandEffect.getCost()))
            {
                if (selectedWandEffect.canBePartiallyDrawn() || selectedWandEffect.canBeContinuouslyDrawn() || selectedWandEffect.getUseDurationMax() > 0)
                {
                    player.startUsingItem(hand);
                    return InteractionResultHolder.pass(stack);
                }
                else if (consumeFuel(FFItemHandler.getPhylacteryCharm(player), selectedWandEffect.getCost()))
                {
                    selectedWandEffect.use(level, player, selectedWandEffect.getUseDurationMax());

                    player.awardStat(Stats.ITEM_USED.get(this));
                    player.getCooldowns().addCooldown(this, selectedWandEffect.getCooldown());
                }
            }
            else
            {
                // TODO play failure sound

                return InteractionResultHolder.fail(stack);
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

            if (canCastWandEffect(FFItemHandler.getPhylacteryCharm(player), selectedWandEffect.getCost()))
            {
                if (selectedWandEffect.canBePartiallyDrawn() || selectedWandEffect.canBeContinuouslyDrawn() || selectedWandEffect.getUseDurationMax() > 0)
                {
                    player.startUsingItem(context.getHand());
                    return InteractionResult.PASS;
                }
                else if (consumeFuel(FFItemHandler.getPhylacteryCharm(player), selectedWandEffect.getCost()))
                {
                    selectedWandEffect.useOn(context.getLevel(), player, context.getClickedPos(), selectedWandEffect.getUseDurationMax());

                    player.awardStat(Stats.ITEM_USED.get(this));
                    player.getCooldowns().addCooldown(this, selectedWandEffect.getCooldown());
                }
            }
            else
            {
                // TODO play failure sound

                return InteractionResult.FAIL;
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
                if (consumeFuel(FFItemHandler.getPhylacteryCharm(player), selectedWandEffect.getCost()))
                {
                    if (selectedWandEffect.usableOnBlockOnly())
                    {
                        selectedWandEffect.useOn(level, player, pos, duration);
                    }
                    else
                    {
                        selectedWandEffect.use(level, player, duration);
                    }

                    player.getCooldowns().addCooldown(this, selectedWandEffect.getCooldown());
                }
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
                if (consumeFuel(FFItemHandler.getPhylacteryCharm(player), selectedWandEffect.getCost()))
                {
                    if (selectedWandEffect.usableOnBlockOnly())
                    {
                        selectedWandEffect.useOn(level, player, pos, duration);
                    }
                    else
                    {
                        selectedWandEffect.use(level, player, duration);
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                    player.getCooldowns().addCooldown(this, selectedWandEffect.getCooldown());
                }
            }
        }
    }
}