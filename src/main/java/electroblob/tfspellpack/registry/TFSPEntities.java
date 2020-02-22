package electroblob.tfspellpack.registry;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.entity.construct.*;
import electroblob.tfspellpack.entity.living.*;
import electroblob.tfspellpack.entity.projectile.EntityMagicHydraMortar;
import electroblob.tfspellpack.entity.projectile.EntityTear;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Class responsible for registering all of TF spell pack's entities and their spawning conditions.
 *
 * @author Electroblob
 * @since TF Spell Pack 1.0
 */
@Mod.EventBusSubscriber
public final class TFSPEntities {

	private TFSPEntities(){} // No instances!

	/** Most entity trackers fall into one of a few categories, so they are defined here for convenience. This
	 * generally follows the values used in vanilla for each entity type. */
	enum TrackingType {

		LIVING(80, 3, true),
		PROJECTILE(64, 10, true),
		CONSTRUCT(160, 10, false);

		int range;
		int interval;
		boolean trackVelocity;

		TrackingType(int range, int interval, boolean trackVelocity){
			this.range = range;
			this.interval = interval;
			this.trackVelocity = trackVelocity;
		}
	}

	/** Incrementing index for the mod-specific entity network ID. */
	private static int id = 0;

	@SubscribeEvent
	public static void register(RegistryEvent.Register<EntityEntry> event){

		IForgeRegistry<EntityEntry> registry = event.getRegistry();

		// TF summoned creatures
		registry.register(createEntry(EntitySkeletonDruidMinion.class, 	"skeleton_druid_minion", 		TrackingType.LIVING).build());
		registry.register(createEntry(EntityStableIceCoreMinion.class, 	"stable_ice_core_minion", 	TrackingType.LIVING).build());
		registry.register(createEntry(EntityUnstableIceCoreMinion.class,"unstable_ice_core_minion", 	TrackingType.LIVING).build());
		registry.register(createEntry(EntityFireBeetleMinion.class, 	"fire_beetle_minion", 		TrackingType.LIVING).build());
		registry.register(createEntry(EntitySlimeBeetleMinion.class, 	"slime_beetle_minion", 		TrackingType.LIVING).build());
		registry.register(createEntry(EntityRedcapMinion.class, 		"redcap_goblin_minion", 		TrackingType.LIVING).build());
		registry.register(createEntry(EntityRedcapSapperMinion.class, 	"redcap_sapper_minion", 		TrackingType.LIVING).build());
		registry.register(createEntry(EntityTwilightWraithMinion.class, "twilight_wraith_minion", 	TrackingType.LIVING).build());
		registry.register(createEntry(EntitySnowGuardianMinion.class, 	"snow_guardian_minion", 		TrackingType.LIVING).build());
		registry.register(createEntry(EntityMiniGhastMinion.class, 		"carminite_ghastling_minion",	TrackingType.LIVING).build());
		registry.register(createEntry(EntityCaveTrollMinion.class, 		"cave_troll_minion",			TrackingType.LIVING).build());

		// Other living entities
		registry.register(createEntry(EntityDruidMage.class, 			"druid_mage",					TrackingType.LIVING).egg(0x0e1408, 0xd1ff00).build());

		// Projectiles
		registry.register(createEntry(EntityMagicHydraMortar.class, 	"magic_hydra_mortar", 		TrackingType.PROJECTILE).build());
		registry.register(createEntry(EntityTear.class, 				"fiery_tear", 				TrackingType.PROJECTILE).build());

		// Constructs
		registry.register(createEntry(EntityTearRain.class, 			"rain_of_tears", 				TrackingType.CONSTRUCT).build());
		registry.register(createEntry(EntityAcidRainCloud.class, 		"acid_rain_cloud", 			TrackingType.CONSTRUCT).build());
		registry.register(createEntry(EntityCinderCloud.class, 			"cinder_cloud", 				TrackingType.CONSTRUCT).build());
		registry.register(createEntry(EntityFireJet.class, 				"fire_jet", 					TrackingType.CONSTRUCT).build());
		registry.register(createEntry(EntityDarkMistCloud.class, 		"dark_mist_cloud", 			TrackingType.CONSTRUCT).build());

	}

	/**
	 * Private helper method that simplifies the parts of an {@link EntityEntry} that are common to all entities.
	 * This automatically assigns a network id, and accepts a {@link TrackingType} for automatic tracker assignment.
	 * @param entityClass The entity class to use.
	 * @param name The name of the entity. This will form the path of a {@code ResourceLocation} with domain
	 * 		       {@code ebwizardry}, which in turn will be used as both the registry name and the 'command' name.
	 * @param tracking The {@link TrackingType} to use for this entity.
	 * @param <T> The type of entity.
	 * @return The (part-built) builder instance, allowing other builder methods to be added as necessary.
	 */
	private static <T extends Entity> EntityEntryBuilder<T> createEntry(Class<T> entityClass, String name, TrackingType tracking){
		return createEntry(entityClass, name).tracker(tracking.range, tracking.interval, tracking.trackVelocity);
	}

	/**
	 * Private helper method that simplifies the parts of an {@link EntityEntry} that are common to all entities.
	 * This automatically assigns a network id.
	 * @param entityClass The entity class to use.
	 * @param name The name of the entity. This will form the path of a {@code ResourceLocation} with domain
	 * 		       {@code ebwizardry}, which in turn will be used as both the registry name and the 'command' name.
	 * @param <T> The type of entity.
	 * @return The (part-built) builder instance, allowing other builder methods to be added as necessary.
	 */
	private static <T extends Entity> EntityEntryBuilder<T> createEntry(Class<T> entityClass, String name){
		ResourceLocation registryName = new ResourceLocation(TFSpellPack.MODID, name);
		return EntityEntryBuilder.<T>create().entity(entityClass).id(registryName, id++).name(registryName.toString());
	}

}
