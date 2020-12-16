package electroblob.tfspellpack.registry;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.block.BlockGildedTFWood;
import electroblob.wizardry.block.BlockBookshelf;
import electroblob.wizardry.block.BlockLectern;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

/**
 * Class responsible for defining, storing and registering all of tf spell pack's blocks.
 *
 * @author Electroblob
 * @since TF Spell Pack 1.1
 */
@ObjectHolder(TFSpellPack.MODID)
@Mod.EventBusSubscriber
public class TFSPBlocks {

	private TFSPBlocks(){} // No instances!

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T placeholder(){ return null; }

	public static final Block gilded_twilight_oak_wood = placeholder();
	public static final Block gilded_canopy_wood = placeholder();
	public static final Block gilded_mangrove_wood = placeholder();
	public static final Block gilded_darkwood = placeholder();
	public static final Block gilded_timewood = placeholder();
	public static final Block gilded_transwood = placeholder();
	public static final Block gilded_minewood = placeholder();
	public static final Block gilded_sortingwood = placeholder();

	public static final Block twilight_oak_bookshelf = placeholder();
	public static final Block canopy_bookshelf = placeholder();
	public static final Block mangrove_bookshelf = placeholder();
	public static final Block darkwood_bookshelf = placeholder();
	public static final Block timewood_bookshelf = placeholder();
	public static final Block transwood_bookshelf = placeholder();
	public static final Block minewood_bookshelf = placeholder();
	public static final Block sortingwood_bookshelf = placeholder();

	public static final Block twilight_oak_lectern = placeholder();
	public static final Block canopy_lectern = placeholder();
	public static final Block mangrove_lectern = placeholder();
	public static final Block darkwood_lectern = placeholder();
	public static final Block timewood_lectern = placeholder();
	public static final Block transwood_lectern = placeholder();
	public static final Block minewood_lectern = placeholder();
	public static final Block sortingwood_lectern = placeholder();

	/**
	 * Sets both the registry and unlocalised names of the given block, then registers it with the given registry. Use
	 * this instead of {@link Block#setRegistryName(String)} and {@link Block#setTranslationKey(String)} during
	 * construction, for convenience and consistency.
	 *
	 * @param registry The registry to register the given block to.
	 * @param name The name of the block, without the mod ID or the .name stuff. The registry name will be
	 *        {@code tfspellpack:[name]}. The unlocalised name will be {@code tile.tfspellpack:[name].name}.
	 * @param block The block to register.
	 */
	public static void registerBlock(IForgeRegistry<Block> registry, String name, Block block){
		block.setRegistryName(TFSpellPack.MODID, name);
		block.setTranslationKey(block.getRegistryName().toString());
		registry.register(block);
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Block> event){

		IForgeRegistry<Block> registry = event.getRegistry();

		registerBlock(registry, "gilded_twilight_oak_wood", 	new BlockGildedTFWood());
		registerBlock(registry, "gilded_canopy_wood", 		new BlockGildedTFWood());
		registerBlock(registry, "gilded_mangrove_wood", 		new BlockGildedTFWood());
		registerBlock(registry, "gilded_darkwood", 			new BlockGildedTFWood());
		registerBlock(registry, "gilded_timewood", 			new BlockGildedTFWood());
		registerBlock(registry, "gilded_transwood", 			new BlockGildedTFWood());
		registerBlock(registry, "gilded_minewood", 			new BlockGildedTFWood());
		registerBlock(registry, "gilded_sortingwood", 		new BlockGildedTFWood());

		// Strange but true... without their item models, these don't load textures properly
		registerBlock(registry, "twilight_oak_bookshelf", 	new BlockBookshelf());
		registerBlock(registry, "canopy_bookshelf", 			new BlockBookshelf());
		registerBlock(registry, "mangrove_bookshelf", 		new BlockBookshelf());
		registerBlock(registry, "darkwood_bookshelf", 		new BlockBookshelf());
		registerBlock(registry, "timewood_bookshelf", 		new BlockBookshelf());
		registerBlock(registry, "transwood_bookshelf", 		new BlockBookshelf());
		registerBlock(registry, "minewood_bookshelf", 		new BlockBookshelf());
		registerBlock(registry, "sortingwood_bookshelf", 		new BlockBookshelf());

		registerBlock(registry, "twilight_oak_lectern", 		new BlockLectern());
		registerBlock(registry, "canopy_lectern", 			new BlockLectern());
		registerBlock(registry, "mangrove_lectern", 			new BlockLectern());
		registerBlock(registry, "darkwood_lectern", 			new BlockLectern());
		registerBlock(registry, "timewood_lectern", 			new BlockLectern());
		registerBlock(registry, "transwood_lectern", 			new BlockLectern());
		registerBlock(registry, "minewood_lectern", 			new BlockLectern());
		registerBlock(registry, "sortingwood_lectern", 		new BlockLectern());

	}

}
