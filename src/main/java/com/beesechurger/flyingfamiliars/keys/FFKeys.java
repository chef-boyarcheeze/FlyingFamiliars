package com.beesechurger.flyingfamiliars.keys;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.networking.FFMessages;
import com.beesechurger.flyingfamiliars.networking.packet.ModeChangeC2SPacket;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class FFKeys
{
	public static KeyMapping FAMILIAR_ASCEND;
	public static KeyMapping FAMILIAR_DESCEND;
	public static KeyMapping FAMILIAR_ACTION;
	public static KeyMapping SOUL_WAND_SHIFT;
	public static ModeChangeKeyMapping ITEM_MODE_CYCLE;
	
	private FFKeys()
	{}
	
	public static void init()
	{
		FAMILIAR_ASCEND = registerKey("familiar_ascend", GLFW.GLFW_KEY_SPACE, "Flying Familiars");
		FAMILIAR_DESCEND = registerKey("familiar_descend", GLFW.GLFW_KEY_LEFT_CONTROL, "Flying Familiars");
		FAMILIAR_ACTION = registerKey("familiar_action", GLFW.GLFW_KEY_Y, "Flying Familiars");
		ITEM_MODE_CYCLE = ModeChangeKeyMapping.registerKey("item_mode_cycle", GLFW.GLFW_KEY_V, "Flying Familiars");
		SOUL_WAND_SHIFT = registerKey("soul_wand_shift", GLFW.GLFW_KEY_LEFT_SHIFT, "Flying Familiars");
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
