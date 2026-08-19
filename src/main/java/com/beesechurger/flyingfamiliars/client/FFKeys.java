package com.beesechurger.flyingfamiliars.client;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;

import java.util.ArrayList;
import java.util.List;

public class FFKeys
{
/////////////
/// Keys: ///
/////////////

    public static KeyMapping FAMILIAR_ASCEND;
    public static KeyMapping FAMILIAR_DESCEND;
    public static KeyMapping FAMILIAR_ACTION;
    public static KeyMapping SOUL_WAND_SELECT;
    public static KeyMapping SOUL_WAND_SHIFT;

    public static KeyMapping registerKey(String name, int keycode, String category, RegisterKeyMappingsEvent event)
    {
        final var key = new KeyMapping("key." + FlyingFamiliars.MOD_ID + "." + name, keycode, category);
        key.setKeyConflictContext(KeyConflictContext.IN_GAME);
        event.register(key);
        return key;
    }

//////////////////
/// KeyStates: ///
//////////////////

    private static final List<KeyState> KEY_STATES = new ArrayList<>();

    public static KeyState WAND_EFFECT_SELECT_STATE;

    public static KeyState registerKeyState(KeyMapping key)
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
