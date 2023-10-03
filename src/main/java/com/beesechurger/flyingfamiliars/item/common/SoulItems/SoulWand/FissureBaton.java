package com.beesechurger.flyingfamiliars.item.common.SoulItems.SoulWand;

import net.minecraft.ChatFormatting;

import static com.beesechurger.flyingfamiliars.util.FFValueConstants.*;

public class FissureBaton extends BaseSoulWand
{
    public FissureBaton(Properties properties)
    {
        super(properties);

        attackColorInt = CHAT_GOLD;
        defenseColorInt = CHAT_DARK_GREEN;
        attackColorChat = ChatFormatting.GOLD;
        defenseColorChat = ChatFormatting.DARK_GREEN;
    }
}
