package com.beesechurger.flyingfamiliars.item.common.SoulItems.SoulWand;

import net.minecraft.ChatFormatting;

import static com.beesechurger.flyingfamiliars.util.FFValueConstants.*;

public class LightPrism extends BaseSoulWand
{
    public LightPrism(Properties properties)
    {
        super(properties);

        attackColorInt = CHAT_WHITE;
        defenseColorInt = CHAT_AQUA;
        attackColorChat = ChatFormatting.WHITE;
        defenseColorChat = ChatFormatting.AQUA;
    }
}
