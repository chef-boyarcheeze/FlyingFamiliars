package com.beesechurger.flyingfamiliars.registries;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = FlyingFamiliars.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FFKeys
{
	public static KeyMapping FAMILIAR_ASCEND;
	public static KeyMapping FAMILIAR_DESCEND;
	public static KeyMapping FAMILIAR_ACTION;
	public static KeyMapping SOUL_WAND_SHIFT;

	private FFKeys()
	{}

	@SubscribeEvent
	public static void init(RegisterKeyMappingsEvent event)
	{
		FAMILIAR_ASCEND = registerKey("familiar_ascend", GLFW.GLFW_KEY_SPACE, "Flying Familiars", event);
		FAMILIAR_DESCEND = registerKey("familiar_descend", GLFW.GLFW_KEY_LEFT_CONTROL, "Flying Familiars", event);
		FAMILIAR_ACTION = registerKey("familiar_action", GLFW.GLFW_KEY_Y, "Flying Familiars", event);
		//ITEM_MODE_CYCLE = ModeChangeKeyMapping.registerKey("item_mode_cycle", GLFW.GLFW_KEY_V, "Flying Familiars", event);
		SOUL_WAND_SHIFT = registerKey("soul_wand_shift", GLFW.GLFW_KEY_LEFT_SHIFT, "Flying Familiars", event);
	}

	private static KeyMapping registerKey(String name, int keycode, String category, RegisterKeyMappingsEvent event)
	{
		final var key = new KeyMapping("key." + FlyingFamiliars.MOD_ID + "." + name, keycode, category);
		key.setKeyConflictContext(KeyConflictContext.IN_GAME);
		event.register(key);
		return key;
	}

	/*public static class ModeChangeKeyMapping extends KeyMapping
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
				FFPackets.sendToServer(new 'Packet');
		}

		public static ModeChangeKeyMapping registerKey(String name, int keycode, String category, RegisterKeyMappingsEvent event)
		{
			final var key = new ModeChangeKeyMapping("key." + FlyingFamiliars.MOD_ID + "." + name, keycode, category);
			key.setKeyConflictContext(KeyConflictContext.IN_GAME);
			event.register(key);
			return key;
		}
	}*/
}
