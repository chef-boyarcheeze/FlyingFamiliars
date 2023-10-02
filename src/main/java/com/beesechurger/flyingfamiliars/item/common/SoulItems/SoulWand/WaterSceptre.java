package com.beesechurger.flyingfamiliars.item.common.SoulItems.SoulWand;

import net.minecraft.ChatFormatting;

import static com.beesechurger.flyingfamiliars.util.FFValueConstants.*;

public class WaterSceptre extends BaseSoulWand
{
	public WaterSceptre(Properties properties)
	{
		super(properties);

		attackColorInt = CHAT_DARK_AQUA;
		defenseColorInt = CHAT_GREEN;
		attackColorChat = ChatFormatting.DARK_AQUA;
		defenseColorChat = ChatFormatting.GREEN;
	}
}
