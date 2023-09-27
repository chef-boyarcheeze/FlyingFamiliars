package com.beesechurger.flyingfamiliars.keys;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.networking.FFMessages;
import com.beesechurger.flyingfamiliars.networking.packet.ModeChangeC2SPacket;
import com.beesechurger.flyingfamiliars.networking.packet.SoulWandSelectC2SPacket;
import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class FFKeys
{
	public static KeyMapping familiar_ascend;
	public static KeyMapping familiar_descend;
	public static KeyMapping familiar_action;
	public static KeyMapping soul_wand_shift;
	public static ModeChangeKeyMapping item_mode_cycle;
	
	private FFKeys()
	{}
	
	public static void init()
	{
		familiar_ascend = registerKey("familiar_ascend", GLFW.GLFW_KEY_SPACE, "Flying Familiars");
		familiar_descend = registerKey("familiar_descend", GLFW.GLFW_KEY_LEFT_CONTROL, "Flying Familiars");
		familiar_action = registerKey("familiar_action", GLFW.GLFW_KEY_Y, "Flying Familiars");
		item_mode_cycle = ModeChangeKeyMapping.registerKey("item_mode_cycle", GLFW.GLFW_KEY_V, "Flying Familiars");
		soul_wand_shift = registerKey("soul_wand_shift", GLFW.GLFW_KEY_LEFT_SHIFT, "Flying Familiars");
	}

	private static KeyMapping registerKey(String name, int keycode, String category)
	{
		final var key = new KeyMapping("key." + FlyingFamiliars.MOD_ID + "." + name, keycode, category);
		ClientRegistry.registerKeyBinding(key);
		key.setKeyConflictContext(KeyConflictContext.IN_GAME);
		return key;
	}

	public static class ModeChangeKeyMapping extends KeyMapping
	{
		protected boolean isPressed;

		public ModeChangeKeyMapping(String description, int keyCode, String category)
		{
			super(description, keyCode, category);
		}

		public void setDown(boolean valueIn)
		{
			boolean wasDown = isPressed;

			super.setDown(valueIn);
			isPressed = valueIn;

			if (isPressed && !wasDown)
				FFMessages.sendToServer(new ModeChangeC2SPacket());
		}

		public static ModeChangeKeyMapping registerKey(String name, int keycode, String category)
		{
			final var key = new ModeChangeKeyMapping("key." + FlyingFamiliars.MOD_ID + "." + name, keycode, category);
			ClientRegistry.registerKeyBinding(key);
			key.setKeyConflictContext(KeyConflictContext.IN_GAME);
			return key;
		}
	}
}
