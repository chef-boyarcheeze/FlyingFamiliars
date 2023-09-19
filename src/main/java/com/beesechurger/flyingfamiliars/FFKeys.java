package com.beesechurger.flyingfamiliars;

import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public class FFKeys
{
	public static KeyMapping familiar_ascend;
	public static KeyMapping familiar_descend;
	public static KeyMapping familiar_action;
	
	private FFKeys()
	{}
	
	public static void init()
	{
		familiar_ascend = registerKey("familiar_ascend", GLFW.GLFW_KEY_SPACE, KeyMapping.CATEGORY_MOVEMENT);
		familiar_descend = registerKey("familiar_descend", GLFW.GLFW_KEY_LEFT_CONTROL, KeyMapping.CATEGORY_MOVEMENT);
		familiar_action = registerKey("familiar_action", GLFW.GLFW_KEY_Y, KeyMapping.CATEGORY_GAMEPLAY);
	}

	private static KeyMapping registerKey(String name, int keycode, String category)
	{
		final var key = new KeyMapping("key." + FlyingFamiliars.MOD_ID + "." + name, keycode, category);
		ClientRegistry.registerKeyBinding(key);
		return key;
	}
}
