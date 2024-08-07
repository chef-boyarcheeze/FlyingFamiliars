package com.beesechurger.flyingfamiliars.registries;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FFSounds
{
	private static float[] SCALE = {0.5f, 0.56f, 0.64f, 0.68f, 0.76f, 0.85f, 0.95f, 1.0f};

	// Sound Events
	public static final DeferredRegister<SoundEvent> SOUND_EVENT_REG = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, FlyingFamiliars.MOD_ID);

	// Block sounds:
	public static final RegistryObject<SoundEvent> RUNIC_BRICKS_BREAK = registerSoundEvent("runic_bricks_break");
	public static final RegistryObject<SoundEvent> RUNIC_BRICKS_STEP = registerSoundEvent("runic_bricks_step");
	public static final RegistryObject<SoundEvent> RUNIC_BRICKS_PLACE = registerSoundEvent("runic_bricks_place");
	public static final RegistryObject<SoundEvent> RUNIC_BRICKS_HIT = registerSoundEvent("runic_bricks_hit");
	public static final RegistryObject<SoundEvent> RUNIC_BRICKS_FALL = registerSoundEvent("runic_bricks_fall");

	public static final RegistryObject<SoundEvent> TAG_BLOCK_ADD_ENTITY = registerSoundEvent("tag_block_add_entity");
	public static final RegistryObject<SoundEvent> TAG_BLOCK_ADD_ITEM = registerSoundEvent("tag_block_add_item");
	public static final RegistryObject<SoundEvent> TAG_BLOCK_REMOVE_ENTITY = registerSoundEvent("tag_block_remove_entity");
	public static final RegistryObject<SoundEvent> TAG_BLOCK_REMOVE_ITEM = registerSoundEvent("tag_block_remove_item");

	public static final RegistryObject<SoundEvent> BRAZIER_AMBIENT = registerSoundEvent("brazier_ambient");
	public static final RegistryObject<SoundEvent> BRAZIER_CRAFT = registerSoundEvent("brazier_craft");
	public static final RegistryObject<SoundEvent> BRAZIER_RESULT = registerSoundEvent("brazier_result");

	// Familiar sounds:
	public static final RegistryObject<SoundEvent> CLOUD_RAY_APPLY_DOUSED = registerSoundEvent("cloud_ray_apply_doused");
	public static final RegistryObject<SoundEvent> CLOUD_RAY_DEATH = registerSoundEvent("cloud_ray_death");
	public static final RegistryObject<SoundEvent> CLOUD_RAY_IDLE = registerSoundEvent("cloud_ray_idle");
	public static final RegistryObject<SoundEvent> CLOUD_RAY_HURT = registerSoundEvent("cloud_ray_hurt");
	public static final RegistryObject<SoundEvent> CLOUD_RAY_STEP = registerSoundEvent("cloud_ray_step");

	public static final RegistryObject<SoundEvent> CORMORANT_SQUAWK = registerSoundEvent("cormorant_squawk");
	
	public static final RegistryObject<SoundEvent> GRIFFONFLY_CHITTER = registerSoundEvent("griffonfly_chitter");
	public static final RegistryObject<SoundEvent> GRIFFONFLY_DEATH = registerSoundEvent("griffonfly_death");
	public static final RegistryObject<SoundEvent> GRIFFONFLY_FLAP = registerSoundEvent("griffonfly_flap");
	public static final RegistryObject<SoundEvent> GRIFFONFLY_GRAB = registerSoundEvent("griffonfly_grab");
	public static final RegistryObject<SoundEvent> GRIFFONFLY_HURT = registerSoundEvent("griffonfly_hurt");
	public static final RegistryObject<SoundEvent> GRIFFONFLY_RELEASE = registerSoundEvent("griffonfly_release");

	// Wand effect sounds:
	public static final RegistryObject<SoundEvent> SOUL_WAND_SWAP = registerSoundEvent("soul_wand_swap");

	public static final RegistryObject<SoundEvent> CAPTURE_PROJECTILE_IMPACT = registerSoundEvent("capture_projectile_impact");

	public static final RegistryObject<SoundEvent> FIREBALL_PROJECTILE_CAST = registerSoundEvent("fireball_projectile_cast");
	public static final RegistryObject<SoundEvent> FIREBALL_PROJECTILE_EXPLODE = registerSoundEvent("fireball_projectile_explode");
	public static final RegistryObject<SoundEvent> FIREBALL_PROJECTILE_IDLE = registerSoundEvent("fireball_projectile_idle");

	// Sound Types
	public static final ForgeSoundType RUNIC_BRICKS = new ForgeSoundType(2f, 1f,
			FFSounds.RUNIC_BRICKS_BREAK, FFSounds.RUNIC_BRICKS_STEP, FFSounds.RUNIC_BRICKS_PLACE,
			FFSounds.RUNIC_BRICKS_HIT, FFSounds.RUNIC_BRICKS_FALL);
	
	private static RegistryObject<SoundEvent> registerSoundEvent(String name)
	{
		return SOUND_EVENT_REG.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FlyingFamiliars.MOD_ID, name)));
	}
	
	public static float getPitch()
	{
		return SCALE[(int) Math.floor(Math.random()*SCALE.length)];
	}
}
