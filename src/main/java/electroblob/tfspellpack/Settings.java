package electroblob.tfspellpack;

import net.minecraftforge.common.config.Config;

@Config(modid = TFSpellPack.MODID) // No fancy configs here so we can use the annotation, hurrah!
public class Settings {

	@Config.Comment("List of loot tables to inject twilight forest spell pack loot (as specified in loot_tables/chests/tf_dungeon_additions.json) into.")
	@Config.LangKey("config.tfspellpack.loot_injection_locations")
	public static String[] lootInjectionLocations = {
			"twilightforest:structures/aurora_cache/aurora_cache",
//			"twilightforest:structures/basement/basement",
			"twilightforest:structures/darktower_cache/darktower_cache",
			"twilightforest:structures/hill_1/hill_1",
			"twilightforest:structures/hill_2/hill_2",
			"twilightforest:structures/hill_3/hill_3",
			"twilightforest:structures/labyrinth_dead_end/labyrinth_dead_end",
			"twilightforest:structures/stronghold_cache/stronghold_cache",
			"twilightforest:structures/tower_library/tower_library",
			"twilightforest:structures/troll_garden/troll_garden"
	};

	@Config.Comment("The chance for a druid hut basement to contain a druid mage. Set to zero to disable this mechanic.")
	@Config.LangKey("config.tfspellpack.druid_mage_spawn_chance")
	@Config.RangeDouble(min = 0, max = 1)
	@Config.SlidingOption
	public static double druidMageSpawnChance = 0.5;

}
