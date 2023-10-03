package com.beesechurger.flyingfamiliars.item.common.SoulItems.SoulWand;

import net.minecraft.ChatFormatting;

import static com.beesechurger.flyingfamiliars.util.FFValueConstants.*;

public class VerdantBough extends BaseSoulWand
{
    public VerdantBough(Properties properties)
    {
        super(properties);

        attackColorInt = CHAT_DARK_GREEN;
        defenseColorInt = CHAT_LIGHT_PURPLE;
        attackColorChat = ChatFormatting.DARK_GREEN;
        defenseColorChat = ChatFormatting.LIGHT_PURPLE;
    }
}
