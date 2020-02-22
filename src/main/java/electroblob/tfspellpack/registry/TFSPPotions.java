package electroblob.tfspellpack.registry;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.potion.PotionIronwoodHeart;
import electroblob.tfspellpack.potion.PotionMistCloak;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

/**
 * Class responsible for defining, storing and registering all of wizardry's potion effects.
 * 
 * @author Electroblob
 * @since Wizardry 2.1
 */
@ObjectHolder(TFSpellPack.MODID)
@Mod.EventBusSubscriber
public final class TFSPPotions {

	private TFSPPotions(){} // No instances!

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T placeholder(){ return null; }

	public static final Potion mist_cloak = placeholder();
	public static final Potion ironwood_heart = placeholder();

	/**
	 * Sets both the registry and unlocalised names of the given potion, then registers it with the given registry. Use
	 * this instead of {@link Potion#setRegistryName(String)} and {@link Potion#setPotionName(String)} during
	 * construction, for convenience and consistency.
	 * 
	 * @param registry The registry to register the given potion to.
	 * @param name The name of the potion, without the mod ID or the .name stuff. The registry name will be
	 *        {@code ebwizardry:[name]}. The unlocalised name will be {@code potion.ebwizardry:[name].name}.
	 * @param potion The potion to register.
	 */
	public static void registerPotion(IForgeRegistry<Potion> registry, String name, Potion potion){
		potion.setRegistryName(TFSpellPack.MODID, name);
		// For some reason, Potion#getName() doesn't prepend "potion." itself, so it has to be done here.
		potion.setPotionName("potion." + potion.getRegistryName().toString());
		registry.register(potion);
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Potion> event){

		IForgeRegistry<Potion> registry = event.getRegistry();

		registerPotion(registry, "mist_cloak", new PotionMistCloak());
		registerPotion(registry, "ironwood_heart", new PotionIronwoodHeart());

	}

}