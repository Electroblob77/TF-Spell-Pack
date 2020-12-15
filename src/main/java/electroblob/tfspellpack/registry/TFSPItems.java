package electroblob.tfspellpack.registry;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.item.ItemTwilightSpellBook;
import electroblob.wizardry.block.BlockBookshelf;
import electroblob.wizardry.inventory.ContainerBookshelf;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.ItemScroll;
import electroblob.wizardry.misc.BehaviourSpellDispense;
import electroblob.wizardry.registry.WizardryTabs.CreativeTabListed;
import electroblob.wizardry.registry.WizardryTabs.CreativeTabSorted;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

/**
 * Class responsible for defining, storing and registering all of tf spell pack's items.
 * 
 * @author Electroblob
 * @since TF Spell Pack 1.0
 */
@ObjectHolder(TFSpellPack.MODID)
@Mod.EventBusSubscriber
public final class TFSPItems {

	private TFSPItems(){} // No instances!

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T placeholder(){ return null; }

	public static final Item twilight_spell_book = placeholder();
	public static final Item twilight_scroll = placeholder();

	public static final Item ring_twilight = placeholder();
	public static final Item ring_hydra_kaboom = placeholder();
	public static final Item ring_aurora = placeholder();
	public static final Item ring_trollsteinn = placeholder();
	public static final Item ring_stealth_attack = placeholder();

	public static final Item amulet_steeleaf = placeholder();
	public static final Item amulet_carminite = placeholder();
	public static final Item amulet_life_charm = placeholder();

	public static final Item charm_ice_exploder = placeholder();
	public static final Item charm_goblin = placeholder();
	public static final Item charm_troll = placeholder();
	public static final Item charm_accursed_tome = placeholder();

	/**
	 * Sets both the registry and unlocalised names of the given item, then registers it with the given registry. Use
	 * this instead of {@link Item#setRegistryName(String)} and {@link Item#setTranslationKey(String)} during
	 * construction, for convenience and consistency. As of wizardry 4.2, this also automatically adds it to the order
	 * list for its creative tab if that tab is a {@link CreativeTabListed}, meaning the order can be defined simply
	 * by the order in which the items are registered in this class.
	 *
	 * @param registry The registry to register the given item to.
	 * @param name The name of the item, without the mod ID or the .name stuff. The registry name will be
	 *        {@code tfspellpack:[name]}. The unlocalised name will be {@code item.tfspellpack:[name].name}.
	 * @param item The item to register.
	 */
	// It now makes sense to have the name first, since it's shorter than an entire item declaration.
	public static void registerItem(IForgeRegistry<Item> registry, String name, Item item){
		registerItem(registry, name, item, false);
	}

	/**
	 * Sets both the registry and unlocalised names of the given item, then registers it with the given registry. Use
	 * this instead of {@link Item#setRegistryName(String)} and {@link Item#setTranslationKey(String)} during
	 * construction, for convenience and consistency. As of wizardry 4.2, this also automatically adds it to the order
	 * list for its creative tab if that tab is a {@link CreativeTabListed}, meaning the order can be defined simply
	 * by the order in which the items are registered in this class.
	 *
	 * @param registry The registry to register the given item to.
	 * @param name The name of the item, without the mod ID or the .name stuff. The registry name will be
	 *        {@code tfspellpack:[name]}. The unlocalised name will be {@code item.tfspellpack:[name].name}.
	 * @param item The item to register.
	 * @param setTabIcon True to set this item as the icon for its creative tab.
	 */
	// It now makes sense to have the name first, since it's shorter than an entire item declaration.
	public static void registerItem(IForgeRegistry<Item> registry, String name, Item item, boolean setTabIcon){

		item.setRegistryName(TFSpellPack.MODID, name);
		item.setTranslationKey(item.getRegistryName().toString());
		registry.register(item);

		if(setTabIcon && item.getCreativeTab() instanceof CreativeTabSorted){
			((CreativeTabSorted)item.getCreativeTab()).setIconItem(new ItemStack(item));
		}

		if(item.getCreativeTab() instanceof CreativeTabListed){
			((CreativeTabListed)item.getCreativeTab()).order.add(item);
		}
	}

	/** Registers an ItemBlock for the given block, with the same registry name as that block. As of wizardry 4.2, this
	 * also automatically adds it to the order list for its creative tab if that tab is a {@link CreativeTabListed},
	 * meaning the order can be defined simply by the order in which the items are registered in this class. */
	private static void registerItemBlock(IForgeRegistry<Item> registry, Block block){
		// We don't need to keep a reference to the ItemBlock
		Item itemblock = new ItemBlock(block).setRegistryName(block.getRegistryName());
		registry.register(itemblock);

		if(block.getCreativeTab() instanceof CreativeTabListed){
			((CreativeTabListed)block.getCreativeTab()).order.add(itemblock);
		}
	}

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Item> event){

		IForgeRegistry<Item> registry = event.getRegistry();

		// Items
		registerItem(registry, "twilight_spell_book", new ItemTwilightSpellBook());
		registerItem(registry, "twilight_scroll", new ItemScroll());

		registerItem(registry, "ring_twilight", new ItemArtefact(EnumRarity.UNCOMMON, ItemArtefact.Type.RING));
		registerItem(registry, "ring_hydra_kaboom", new ItemArtefact(EnumRarity.EPIC, ItemArtefact.Type.RING));
		registerItem(registry, "ring_aurora", new ItemArtefact(EnumRarity.EPIC, ItemArtefact.Type.RING));
		registerItem(registry, "ring_trollsteinn", new ItemArtefact(EnumRarity.RARE, ItemArtefact.Type.RING));
		registerItem(registry, "ring_stealth_attack", new ItemArtefact(EnumRarity.UNCOMMON, ItemArtefact.Type.RING));

		registerItem(registry, "amulet_steeleaf", new ItemArtefact(EnumRarity.UNCOMMON, ItemArtefact.Type.AMULET));
		registerItem(registry, "amulet_carminite", new ItemArtefact(EnumRarity.RARE, ItemArtefact.Type.AMULET));
		registerItem(registry, "amulet_life_charm", new ItemArtefact(EnumRarity.EPIC, ItemArtefact.Type.AMULET));

		registerItem(registry, "charm_ice_exploder", new ItemArtefact(EnumRarity.RARE, ItemArtefact.Type.CHARM));
		registerItem(registry, "charm_goblin", new ItemArtefact(EnumRarity.EPIC, ItemArtefact.Type.CHARM));
		registerItem(registry, "charm_troll", new ItemArtefact(EnumRarity.RARE, ItemArtefact.Type.CHARM));
		registerItem(registry, "charm_accursed_tome", new ItemArtefact(EnumRarity.RARE, ItemArtefact.Type.CHARM));

	}

	/** Called from init() in the main mod class to register tf spell pack's dispenser behaviours. */
	public static void registerDispenseBehaviours(){
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(twilight_scroll, new BehaviourSpellDispense());
	}

	/** Called from preInit() in the main mod class to register tf spell pack's bookshelf model textures. */
	public static void registerBookshelfModelTextures(){
		BlockBookshelf.registerBookModelTexture(() -> TFSPItems.twilight_spell_book, new ResourceLocation(TFSpellPack.MODID, "blocks/books_twilight"));
		BlockBookshelf.registerBookModelTexture(() -> TFSPItems.twilight_scroll, new ResourceLocation(TFSpellPack.MODID, "blocks/scrolls_twilight"));
	}

	/** Called from init() in the main mod class to register tf spell pack's book items with wizardry's bookshelves. */
	public static void registerBookItems(){
		ContainerBookshelf.registerBookItem(TFSPItems.twilight_spell_book);
		ContainerBookshelf.registerBookItem(TFSPItems.twilight_scroll);
	}

}