package com.beesechurger.flyingfamiliars.item.common.SoulItems.SoulWand;

import net.minecraft.ChatFormatting;

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
