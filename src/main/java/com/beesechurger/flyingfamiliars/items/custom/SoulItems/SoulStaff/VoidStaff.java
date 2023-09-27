package com.beesechurger.flyingfamiliars.items.custom.SoulItems.SoulStaff;

import com.beesechurger.flyingfamiliars.items.custom.SoulItems.IModeCycleItem;
import com.beesechurger.flyingfamiliars.keys.FFKeys;
import com.beesechurger.flyingfamiliars.entity.custom.projectile.VoidStaffProjectile;
import com.beesechurger.flyingfamiliars.sound.FFSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class VoidStaff extends SoulWand implements IModeCycleItem
{
    public VoidStaff(Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);

        level.playSound((Player)null, player.getX(), player.getY(), player.getZ(), FFSounds.SOUL_WAND_THROW.get(), SoundSource.NEUTRAL, 0.5F, FFSounds.getPitch());

        if (!level.isClientSide())
        {
            VoidStaffProjectile capture = new VoidStaffProjectile(level, player, stack, FFKeys.soul_wand_shift.isDown());
            capture.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.2F, 1.0F);
            level.addFreshEntity(capture);
        }

        player.awardStat(Stats.ITEM_USED.get(this));

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public int getBarColor(ItemStack stack)
    {
        CompoundTag compound = stack.getTag();

        int light_purple = 16733695;
        int dark_red = 11141120;
        int blue = 5592575;

        if(compound != null && stack.getItem() instanceof IModeCycleItem item)
        {
            int mode = item.getMode(stack);

            return switch(mode)
            {
                case 1 -> dark_red; // damage based on selected mob's health
                case 2 -> blue; // special mob-based effects
                default -> light_purple; // default, or normal soul wand behavior
            };
        }
        else
            return light_purple;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tipFlag)
    {
        CompoundTag compound = stack.getTag();

        if(compound != null && stack.getItem() instanceof IModeCycleItem item)
        {
            int mode = item.getMode(stack);

            switch(mode)
            {
                case 1 -> tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.void_staff.tooltip.mode_damage")
                        .withStyle(ChatFormatting.DARK_RED));
                case 2 -> tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.void_staff.tooltip.mode_special")
                        .withStyle(ChatFormatting.BLUE));
                default -> tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.void_staff.tooltip.mode_normal")
                        .withStyle(ChatFormatting.LIGHT_PURPLE));
            };
        }
        else
            tooltip.add(new TranslatableComponent("tooltip.flyingfamiliars.void_staff.tooltip.mode_normal").withStyle(ChatFormatting.LIGHT_PURPLE));

        super.appendHoverText(stack, level, tooltip, tipFlag);
    }
}
