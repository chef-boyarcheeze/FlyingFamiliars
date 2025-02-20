package com.beesechurger.flyingfamiliars.registries;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = FlyingFamiliars.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FFKeys
{
	@SubscribeEvent
	public static void initKeybinds(RegisterKeyMappingsEvent event)
	{
		FAMILIAR_ASCEND = registerKey("familiar_ascend", GLFW.GLFW_KEY_SPACE, "Flying Familiars", event);
		FAMILIAR_DESCEND = registerKey("familiar_descend", GLFW.GLFW_KEY_LEFT_CONTROL, "Flying Familiars", event);
		FAMILIAR_ACTION = registerKey("familiar_action", GLFW.GLFW_KEY_Y, "Flying Familiars", event);
		SOUL_WAND_SELECT = registerKey("soul_wand_select", GLFW.GLFW_KEY_V, "Flying Familiars", event);
		SOUL_WAND_SHIFT = registerKey("soul_wand_shift", GLFW.GLFW_KEY_LEFT_SHIFT, "Flying Familiars", event);

		WAND_EFFECT_SELECT_STATE = registerKeyState(FFKeys.SOUL_WAND_SELECT);
	}

///////////
// Keys: //
///////////

	public static KeyMapping FAMILIAR_ASCEND;
	public static KeyMapping FAMILIAR_DESCEND;
	public static KeyMapping FAMILIAR_ACTION;
	public static KeyMapping SOUL_WAND_SELECT;
	public static KeyMapping SOUL_WAND_SHIFT;

	private static KeyMapping registerKey(String name, int keycode, String category, RegisterKeyMappingsEvent event)
	{
		final var key = new KeyMapping("key." + FlyingFamiliars.MOD_ID + "." + name, keycode, category);
		key.setKeyConflictContext(KeyConflictContext.IN_GAME);
		event.register(key);
		return key;
	}

////////////////
// KeyStates: //
////////////////

	private static final List<KeyState> KEY_STATES = new ArrayList<>();

	public static KeyState WAND_EFFECT_SELECT_STATE;

	private static KeyState registerKeyState(KeyMapping key)
	{
		var state = new KeyState(key);
		KEY_STATES.add(state);

		return state;
	}

	public static void update()
	{
		for (KeyState state : KEY_STATES)
		{
			state.update();
		}
	}

	public static class KeyState
	{
		private boolean heldDown;
		private final KeyMapping key;
		private int heldTicks;

		public KeyState(KeyMapping key)
		{
			this.key = key;
		}

		public boolean wasPressed()
		{
			return !heldDown && key.isDown();
		}

		public boolean wasReleased()
		{
			return heldDown && !key.isDown();
		}

		public boolean wasHeldMoreThan(int ticks)
		{
			return heldTicks >= ticks;
		}

		public boolean heldDown()
		{
			return heldDown;
		}

		public void update()
		{
			if (key != null && key.isDown())
			{
				heldTicks++;
				heldDown = true;
			}
			else
			{
				heldTicks = 0;
				heldDown = false;
			}
		}
	}
}
