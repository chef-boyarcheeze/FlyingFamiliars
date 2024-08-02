package com.beesechurger.flyingfamiliars.item.common.soul_items.SoulWand;

import com.beesechurger.flyingfamiliars.entity.common.wand_effect.CaptureProjectile;
import com.beesechurger.flyingfamiliars.item.EntityTagItemHelper;
import com.beesechurger.flyingfamiliars.item.common.soul_items.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.item.common.soul_items.IModeCycleItem;
import com.beesechurger.flyingfamiliars.registries.FFKeys;
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

public class BaseSoulWand extends BaseEntityTagItem implements IModeCycleItem
{
    protected int normalColorInt = CHAT_GRAY;
    protected int attackColorInt = CHAT_GRAY;
    protected int defenseColorInt = CHAT_GRAY;

    protected ChatFormatting normalColorChat = ChatFormatting.GRAY;
    protected ChatFormatting attackColorChat = ChatFormatting.GRAY;
    protected ChatFormatting defenseColorChat = ChatFormatting.GRAY;

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
            CaptureProjectile capture = new CaptureProjectile(level, player, FFKeys.SOUL_WAND_SHIFT.isDown());
            capture.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.2F, 1.0F);
            level.addFreshEntity(capture);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        player.getCooldowns().addCooldown(this, 5);

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public Component getName(ItemStack stack)
    {
        if(stack.hasTag())
        {
            int mode = this.getMode(stack);
            MutableComponent base = Component.translatable(super.getDescriptionId(stack));

            return switch(mode)
            {
                case 1 -> base.append(" (")
                        .append(Component.translatable("message.flyingfamiliars.mode_tag.attack"))
                        .append(")");
                case 2 -> base.append(" (")
                        .append(Component.translatable("message.flyingfamiliars.mode_tag.defense"))
                        .append(")");
                default -> base.append(" (")
                        .append(Component.translatable("message.flyingfamiliars.mode_tag.normal"))
                        .append(")");
            };
        }
        else
            return Component.translatable(super.getDescriptionId(stack));
    }

    @Override
    public int getBarColor(ItemStack stack)
    {
        if(stack.hasTag())
        {
            int mode = this.getMode(stack);

            return switch(mode)
            {
                case 1 -> attackColorInt;
                case 2 -> defenseColorInt;
                default -> normalColorInt;
            };
        }
        else
            return normalColorInt;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tipFlag)
    {
        if(stack.hasTag())
        {
            int mode = this.getMode(stack);

            switch(mode)
            {
                case 1 -> tooltip.add(Component.translatable("tooltip.flyingfamiliars.mode_tag.attack")
                        .withStyle(attackColorChat));
                case 2 -> tooltip.add(Component.translatable("tooltip.flyingfamiliars.mode_tag.defense")
                        .withStyle(defenseColorChat));
                default -> tooltip.add(Component.translatable("tooltip.flyingfamiliars.mode_tag.normal")
                        .withStyle(normalColorChat));
            };
        }
        else
            tooltip.add(Component.translatable("tooltip.flyingfamiliars.mode_tag.normal")
                    .withStyle(normalColorChat));

        super.appendHoverText(stack, level, tooltip, tipFlag);
    }
}