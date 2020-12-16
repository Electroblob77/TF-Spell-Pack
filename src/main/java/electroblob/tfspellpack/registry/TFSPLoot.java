package electroblob.tfspellpack.registry;

import electroblob.tfspellpack.Settings;
import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.util.LootConditionInTF;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

/**
 * Class responsible for registering tf spell pack's loot tables. Also handles loot injection.
 * 
 * @author Electroblob
 * @since TF Spell Pack 1.0
 */
@Mod.EventBusSubscriber
public final class TFSPLoot {

	private TFSPLoot(){} // No instances!

	/** Called from the preInit method in the main mod class to register the custom dungeon loot. */
	public static void register(){

		LootTableList.register(new ResourceLocation(TFSpellPack.MODID, "chests/tf_dungeon_additions"));
		LootTableList.register(new ResourceLocation(TFSpellPack.MODID, "subsets/uncommon_artefacts"));
		LootTableList.register(new ResourceLocation(TFSpellPack.MODID, "subsets/rare_artefacts"));
		LootTableList.register(new ResourceLocation(TFSpellPack.MODID, "subsets/epic_artefacts"));
		LootTableList.register(new ResourceLocation(TFSpellPack.MODID, "entities/druid_mage"));
	}

	@SubscribeEvent
	public static void onLootTableLoadEvent(LootTableLoadEvent event){
		ResourceLocation[] locations = electroblob.wizardry.Settings.toResourceLocations(Settings.lootInjectionLocations);
		// General TF dungeon loot
		if(Arrays.asList(locations).contains(event.getName())){
			event.getTable().addPool(getAdditive(TFSpellPack.MODID + ":chests/tf_dungeon_additions", TFSpellPack.MODID + "_additional_dungeon_loot"));
		}
	}

	private static LootPool getAdditive(String entryName, String poolName){
		return new LootPool(new LootEntry[]{getAdditiveEntry(entryName, 1)}, new LootCondition[0],
				new RandomValueRange(1), new RandomValueRange(0, 1), TFSpellPack.MODID + "_" + poolName);
	}

	private static LootEntryTable getAdditiveEntry(String name, int weight){
		return new LootEntryTable(new ResourceLocation(name), weight, 0, new LootCondition[0],
				TFSpellPack.MODID + "_additive_entry");
	}

}
