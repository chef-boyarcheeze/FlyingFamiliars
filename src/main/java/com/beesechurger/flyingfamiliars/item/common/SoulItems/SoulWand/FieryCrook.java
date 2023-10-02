package com.beesechurger.flyingfamiliars.item.common.SoulItems.SoulWand;

import net.minecraft.ChatFormatting;

import static com.beesechurger.flyingfamiliars.util.FFValueConstants.*;

public class FieryCrook extends BaseSoulWand
{
    public FieryCrook(Properties properties)
    {
        super(properties);

        attackColorInt = CHAT_DARK_RED;
        defenseColorInt = CHAT_GOLD;
        attackColorChat = ChatFormatting.DARK_RED;
        defenseColorChat = ChatFormatting.GOLD;
    }
}
