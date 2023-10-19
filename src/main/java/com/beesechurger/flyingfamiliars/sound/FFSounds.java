package com.beesechurger.flyingfamiliars.sound;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class FFSounds
{
	private static float[] SCALE = {0.5f, 0.56f, 0.64f, 0.68f, 0.76f, 0.85f, 0.95f, 1.0f};
	
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, FlyingFamiliars.MOD_ID);
	
	public static final RegistryObject<SoundEvent> BRAZIER_ADD_ENTITY = registerSoundEvent("brazier_add_entity");
	public static final RegistryObject<SoundEvent> BRAZIER_ADD_ITEM = registerSoundEvent("brazier_add_item");
	public static final RegistryObject<SoundEvent> BRAZIER_AMBIENT = registerSoundEvent("brazier_ambient");
	public static final RegistryObject<SoundEvent> BRAZIER_CRAFT = registerSoundEvent("brazier_craft");
	public static final RegistryObject<SoundEvent> BRAZIER_REMOVE_ENTITY = registerSoundEvent("brazier_remove_entity");
	public static final RegistryObject<SoundEvent> BRAZIER_REMOVE_ITEM = registerSoundEvent("brazier_remove_item");
	public static final RegistryObject<SoundEvent> BRAZIER_RESULT = registerSoundEvent("brazier_result");

	public static final RegistryObject<SoundEvent> CLOUD_RAY_APPLY_DOUSED = registerSoundEvent("cloud_ray_apply_doused");
	public static final RegistryObject<SoundEvent> CLOUD_RAY_DEATH = registerSoundEvent("cloud_ray_death");
	public static final RegistryObject<SoundEvent> CLOUD_RAY_IDLE1 = registerSoundEvent("cloud_ray_idle1");
	public static final RegistryObject<SoundEvent> CLOUD_RAY_IDLE2 = registerSoundEvent("cloud_ray_idle2");
	public static final RegistryObject<SoundEvent> CLOUD_RAY_IDLE3 = registerSoundEvent("cloud_ray_idle3");
	public static final RegistryObject<SoundEvent> CLOUD_RAY_HURT = registerSoundEvent("cloud_ray_hurt");
	public static final RegistryObject<SoundEvent> CLOUD_RAY_STEP1 = registerSoundEvent("cloud_ray_step1");
	public static final RegistryObject<SoundEvent> CLOUD_RAY_STEP2 = registerSoundEvent("cloud_ray_step2");
	public static final RegistryObject<SoundEvent> CLOUD_RAY_STEP3 = registerSoundEvent("cloud_ray_step3");

	public static final RegistryObject<SoundEvent> CORMORANT_SQUAWK1 = registerSoundEvent("cormorant_squawk1");
	public static final RegistryObject<SoundEvent> CORMORANT_SQUAWK2 = registerSoundEvent("cormorant_squawk2");
	
	public static final RegistryObject<SoundEvent> GRIFFONFLY_CHITTER1 = registerSoundEvent("griffonfly_chitter1");
	public static final RegistryObject<SoundEvent> GRIFFONFLY_CHITTER2 = registerSoundEvent("griffonfly_chitter2");
	public static final RegistryObject<SoundEvent> GRIFFONFLY_DEATH = registerSoundEvent("griffonfly_death");
	public static final RegistryObject<SoundEvent> GRIFFONFLY_FLAP1 = registerSoundEvent("griffonfly_flap1");
	public static final RegistryObject<SoundEvent> GRIFFONFLY_FLAP2 = registerSoundEvent("griffonfly_flap2");
	public static final RegistryObject<SoundEvent> GRIFFONFLY_FLAP3 = registerSoundEvent("griffonfly_flap3");
	public static final RegistryObject<SoundEvent> GRIFFONFLY_GRAB = registerSoundEvent("griffonfly_grab");
	public static final RegistryObject<SoundEvent> GRIFFONFLY_HURT = registerSoundEvent("griffonfly_hurt");
	public static final RegistryObject<SoundEvent> GRIFFONFLY_RELEASE = registerSoundEvent("griffonfly_release");
	
	public static final RegistryObject<SoundEvent> SOUL_WAND_SWAP = registerSoundEvent("soul_wand_swap");
	public static final RegistryObject<SoundEvent> SOUL_WAND_THROW = registerSoundEvent("soul_wand_throw");
	
	private static RegistryObject<SoundEvent> registerSoundEvent(String name)
	{
		return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(FlyingFamiliars.MOD_ID, name)));
	}
	
	public static void register(IEventBus eventBus)
	{
		SOUND_EVENTS.register(eventBus);
	}
	
	public static float getPitch()
	{
		return SCALE[(int) Math.floor(Math.random()*SCALE.length)];
	}
}
