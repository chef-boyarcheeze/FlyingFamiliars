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
	
	public static final RegistryObject<SoundEvent> CLOUD_RAY_IDLE1 = registerSoundEvent("cloud_ray_idle1");
	public static final RegistryObject<SoundEvent> CLOUD_RAY_IDLE2 = registerSoundEvent("cloud_ray_idle2");
	public static final RegistryObject<SoundEvent> CLOUD_RAY_IDLE3 = registerSoundEvent("cloud_ray_idle3");
	public static final RegistryObject<SoundEvent> CLOUD_RAY_STEP1 = registerSoundEvent("cloud_ray_step1");
	public static final RegistryObject<SoundEvent> CLOUD_RAY_STEP2 = registerSoundEvent("cloud_ray_step2");
	public static final RegistryObject<SoundEvent> CLOUD_RAY_STEP3 = registerSoundEvent("cloud_ray_step3");
	public static final RegistryObject<SoundEvent> CLOUD_RAY_HURT = registerSoundEvent("cloud_ray_hurt");
	public static final RegistryObject<SoundEvent> CLOUD_RAY_DEATH = registerSoundEvent("cloud_ray_death");
	
	public static final RegistryObject<SoundEvent> SOUL_WAND_THROW = registerSoundEvent("soul_wand_throw");
	public static final RegistryObject<SoundEvent> SPIRIT_CRYSTAL_THROW = registerSoundEvent("spirit_crystal_throw");
	
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
