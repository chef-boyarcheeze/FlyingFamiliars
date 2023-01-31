package com.beesechurger.flyingfamiliars;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;

public class FFKeys
{
	public static KeyMapping ascend;
	public static KeyMapping descend;
	
	private FFKeys()
	{}
	
	public static void init()
	{
		ascend = registerKey("ascend", InputConstants.KEY_SPACE, KeyMapping.CATEGORY_MOVEMENT);
		descend = registerKey("descend", InputConstants.KEY_LCONTROL, KeyMapping.CATEGORY_MOVEMENT);	
	}

	private static KeyMapping registerKey(String name, int keycode, String category)
	{
		final var key = new KeyMapping("key." + FlyingFamiliars.MOD_ID + "." + name, keycode, category);
		ClientRegistry.registerKeyBinding(key);
		return key;
	}
}
