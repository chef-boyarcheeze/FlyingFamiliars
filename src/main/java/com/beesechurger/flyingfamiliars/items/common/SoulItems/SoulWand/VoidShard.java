package com.beesechurger.flyingfamiliars.items.common.SoulItems.SoulWand;

import com.beesechurger.flyingfamiliars.items.common.SoulItems.IModeCycleItem;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

import static com.beesechurger.flyingfamiliars.util.FFValueConstants.*;

public class VoidShard extends BaseSoulWand
{
    public VoidShard(Properties properties)
    {
        super(properties);

        attackColorInt = CHAT_LIGHT_PURPLE;
        defenseColorInt = CHAT_DARK_PURPLE;
        attackColorChat = ChatFormatting.LIGHT_PURPLE;
        defenseColorChat = ChatFormatting.DARK_PURPLE;
    }
}
