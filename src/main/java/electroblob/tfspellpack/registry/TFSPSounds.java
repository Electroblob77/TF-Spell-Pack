package electroblob.tfspellpack.registry;

import electroblob.tfspellpack.TFSpellPack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Class responsible for defining, storing and registering all of wizardry's sound events.
 * 
 * @author Electroblob
 * @since Wizardry 2.1
 */
@Mod.EventBusSubscriber
public final class TFSPSounds {

	private TFSPSounds(){} // No instances!
	
	public static final SoundEvent ENTITY_ACID_RAIN_AMBIENT = createSound("entity.acid_rain_cloud.ambient");
	public static final SoundEvent ENTITY_ACID_RAIN_ATTACK = createSound("entity.acid_rain_cloud.attack");
	public static final SoundEvent ENTITY_FIERY_TEAR_SPLASH = createSound("entity.fiery_tear.splash");
	public static final SoundEvent ENTITY_FIERY_TEAR_FIRE = createSound("entity.fiery_tear.fire");
	public static final SoundEvent ENTITY_CINDER_CLOUD_AMBIENT = createSound("entity.cinder_cloud.ambient");
	public static final SoundEvent ENTITY_FIRE_JET_FIRE = createSound("entity.fire_jet.fire");
	public static final SoundEvent ENTITY_DARK_MIST_AMBIENT = createSound("entity.dark_mist.ambient");
	public static final SoundEvent ENTITY_DRUID_MAGE_AMBIENT = createSound("entity.druid_mage.ambient");
	public static final SoundEvent ENTITY_DRUID_MAGE_HURT = createSound("entity.druid_mage.hurt");
	public static final SoundEvent ENTITY_DRUID_MAGE_DEATH = createSound("entity.druid_mage.death");

	public static SoundEvent createSound(String name){
		// All the setRegistryName methods delegate to this one, it doesn't matter which you use.
		return new SoundEvent(new ResourceLocation(TFSpellPack.MODID, name)).setRegistryName(name);
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<SoundEvent> event){

		event.getRegistry().register(ENTITY_ACID_RAIN_AMBIENT);
		event.getRegistry().register(ENTITY_ACID_RAIN_ATTACK);
		event.getRegistry().register(ENTITY_FIERY_TEAR_SPLASH);
		event.getRegistry().register(ENTITY_FIERY_TEAR_FIRE);
		event.getRegistry().register(ENTITY_CINDER_CLOUD_AMBIENT);
		event.getRegistry().register(ENTITY_FIRE_JET_FIRE);
		event.getRegistry().register(ENTITY_DARK_MIST_AMBIENT);

	}

}