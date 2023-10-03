package com.beesechurger.flyingfamiliars.item.common.SoulItems.SoulWand;

import net.minecraft.ChatFormatting;

import static com.beesechurger.flyingfamiliars.util.FFValueConstants.*;

public class TempestStaff extends BaseSoulWand
{
    public TempestStaff(Properties properties)
    {
        super(properties);

        attackColorInt = CHAT_YELLOW;
        defenseColorInt = CHAT_WHITE;
        attackColorChat = ChatFormatting.YELLOW;
        defenseColorChat = ChatFormatting.WHITE;
    }
}
