package electroblob.tfspellpack.client.model;

import electroblob.tfspellpack.registry.TFSPItems;
import electroblob.wizardry.item.IMultiTexturedItem;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Class responsible for registering all of tf spell pack's item and block models.
 *
 * @author Electroblob
 * @since Wizardry 2.1
 */
@Mod.EventBusSubscriber(Side.CLIENT)
public final class TFSPModels {

	private TFSPModels(){} // No instances!

	@SubscribeEvent
	public static void register(ModelRegistryEvent event){

		// ItemBlocks

		// Items

		registerItemModel(TFSPItems.twilight_spell_book);
		registerItemModel(TFSPItems.twilight_scroll);

		registerItemModel(TFSPItems.ring_twilight);
		registerItemModel(TFSPItems.ring_hydra_kaboom);
		registerItemModel(TFSPItems.ring_aurora);
		registerItemModel(TFSPItems.ring_trollsteinn);
		registerItemModel(TFSPItems.ring_stealth_attack);

		registerItemModel(TFSPItems.amulet_steeleaf);
		registerItemModel(TFSPItems.amulet_carminite);
		registerItemModel(TFSPItems.amulet_life_charm);

		registerItemModel(TFSPItems.charm_ice_exploder);
		registerItemModel(TFSPItems.charm_goblin);
		registerItemModel(TFSPItems.charm_troll);
		registerItemModel(TFSPItems.charm_accursed_tome);
	}

	/**
	 * Registers an item model, using the item's registry name as the model name (this convention makes it easier to
	 * keep track of everything). Variant defaults to "normal". Registers the model for all metadata values.
	 */
	private static void registerItemModel(Item item){
		// Changing the last parameter from null to "inventory" fixed the item/block model weirdness. No idea why!
		ModelBakery.registerItemVariants(item, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		// Assigns the model for all metadata values
		ModelLoader.setCustomMeshDefinition(item, s -> new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}

	/**
	 * Registers an item model, using the itemstack-sensitive {@link IMultiTexturedItem#getModelName(ItemStack)} as the
	 * model name. This allows items to change their texture based on metadata/NBT. Variant defaults to "normal". Registers the
	 * model for metadata 0 automatically, plus all the other metadata values that the item can take, as defined in
	 * {@link Item#getSubItems(CreativeTabs, NonNullList)}. The creative tab supplied
	 * to the aforementioned method will be whichever one the item is in.
	 */
	private static <T extends Item & IMultiTexturedItem> void registerMultiTexturedModel(T item){

		if(item.getHasSubtypes()){
			NonNullList<ItemStack> items = NonNullList.create();
			item.getSubItems(item.getCreativeTab(), items);
			for(ItemStack stack : items){
				ModelLoader.setCustomModelResourceLocation(item, stack.getMetadata(),
						new ModelResourceLocation(item.getModelName(stack), "inventory"));
			}
		}
	}

	/**
	 * Registers an item model for the given metadata, using the item's registry name as the model name (this convention
	 * makes it easier to keep track of everything). This is intended for registering additional metadata values which
	 * aren't displayed in the creative menu, for example the wildcard spell book used in wizard trades.
	 */
	private static void registerItemModel(Item item, int metadata, String variant){
		ModelLoader.setCustomModelResourceLocation(item, metadata,
				new ModelResourceLocation(item.getRegistryName(), variant));
	}

}
