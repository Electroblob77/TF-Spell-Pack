package electroblob.tfspellpack.registry;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.entity.living.*;
import electroblob.tfspellpack.spell.*;
import electroblob.tfspellpack.util.TFSPUtils;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.spell.SpellBuff;
import electroblob.wizardry.spell.SpellMinion;
import electroblob.wizardry.spell.SpellThrowable;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;
import twilightforest.entity.EntityTFMoonwormShot;
import twilightforest.entity.EntityTFNatureBolt;
import twilightforest.entity.EntityTFTomeBolt;
import twilightforest.entity.EntityTFTwilightWandBolt;
import twilightforest.entity.boss.EntityTFIceBomb;

import javax.annotation.Nonnull;

/**
 * Class responsible for defining, storing and registering all of tf spell pack's spells.
 * 
 * @author Electroblob
 * @since TF Spell Pack 1.0
 */
@ObjectHolder(TFSpellPack.MODID)
@Mod.EventBusSubscriber
public final class TFSPSpells {

	private TFSPSpells(){} // No instances!

	@Nonnull
	@SuppressWarnings("ConstantConditions")
	private static <T> T placeholder(){ return null; }

	public static final Spell frost_breath = placeholder();
	public static final Spell summon_skeleton_druid = placeholder();
	public static final Spell summon_goblin = placeholder();
	public static final Spell druid_hex = placeholder();
	public static final Spell moonworm = placeholder();
	public static final Spell mist_cloak = placeholder();
	public static final Spell transformation = placeholder();
	public static final Spell death_page = placeholder();
	public static final Spell twilight_orb = placeholder();

	public static final Spell summon_fire_beetle = placeholder();
	public static final Spell hydra_bomb = placeholder();
	public static final Spell cinder_cloud = placeholder();
	public static final Spell summon_ice_core = placeholder();
	public static final Spell ice_bomb = placeholder();
	public static final Spell acid_rain = placeholder();
	public static final Spell twilight_catalyst = placeholder();
	public static final Spell summon_twilight_wraith = placeholder();
	public static final Spell summon_cave_troll = placeholder();
	public static final Spell summon_slime_beetle = placeholder();
	public static final Spell fortification = placeholder();
	public static final Spell life_charm = placeholder();

	public static final Spell rain_of_tears = placeholder();
	public static final Spell fire_jets = placeholder();
	public static final Spell snow_guardian_horde = placeholder();
	public static final Spell chariot_of_ice = placeholder();
	public static final Spell carminite_singularity = placeholder();
	public static final Spell ghast_trap = placeholder();
	public static final Spell suffocating_mist = placeholder();
	public static final Spell call_of_the_horn = placeholder();
	public static final Spell annihilation = placeholder();
	public static final Spell pocket_tinkerer = placeholder();
	public static final Spell hydras_vigour = placeholder();

	@SubscribeEvent
	public static void register(RegistryEvent.Register<Spell> event){

		IForgeRegistry<Spell> registry = event.getRegistry();

		Item[] tfSpellItems = {TFSPItems.twilight_spell_book, TFSPItems.twilight_scroll};

		registry.register(new FrostBreath());
		registry.register(new SpellMinion<>(TFSpellPack.MODID, "summon_skeleton_druid", EntitySkeletonDruidMinion::new).npcSelector(TFSPUtils.IN_TF_DIMENSION).items(tfSpellItems));
		registry.register(new SummonRedcapGoblin());
		registry.register(new SpellThrowable<>(TFSpellPack.MODID, "druid_hex", EntityTFNatureBolt::new).npcSelector(TFSPUtils.IN_TF_DIMENSION).items(tfSpellItems));
		registry.register(new SpellThrowable<>(TFSpellPack.MODID, "moonworm", EntityTFMoonwormShot::new).npcSelector(TFSPUtils.IN_TF_DIMENSION).items(tfSpellItems));
		registry.register(new SpellBuff(TFSpellPack.MODID, "mist_cloak", 0.17f, 0.2f, 0.2f, () -> TFSPPotions.mist_cloak).npcSelector(TFSPUtils.IN_TF_DIMENSION).items(tfSpellItems));
		registry.register(new Transformation());
		registry.register(new SpellThrowable<>(TFSpellPack.MODID, "death_page", EntityTFTomeBolt::new).npcSelector(TFSPUtils.IN_TF_DIMENSION).items(tfSpellItems).soundValues(0.5f, 0.4f, 0.2f));
		registry.register(new SpellThrowable<>(TFSpellPack.MODID, "twilight_orb", EntityTFTwilightWandBolt::new).npcSelector(TFSPUtils.IN_TF_DIMENSION).items(tfSpellItems));
		registry.register(new SpellBuff(TFSpellPack.MODID, "ironwood_heart", 0.4f, 0.5f, 0.2f, () -> TFSPPotions.ironwood_heart).npcSelector((e, o) -> false).items(tfSpellItems)); // No point in non-players using this

		registry.register(new SpellMinion<>(TFSpellPack.MODID, "summon_fire_beetle", EntityFireBeetleMinion::new).npcSelector(TFSPUtils.IN_TF_DIMENSION).items(tfSpellItems));
		registry.register(new HydraBomb());
		registry.register(new CinderCloud());
		registry.register(new SummonIceCore());
		registry.register(new SpellThrowable<>(TFSpellPack.MODID, "ice_bomb", EntityTFIceBomb::new).npcSelector(TFSPUtils.IN_TF_DIMENSION).items(tfSpellItems));
		registry.register(new AcidRain());
		registry.register(new TwilightCatalyst());
		registry.register(new SpellMinion<>(TFSpellPack.MODID, "summon_twilight_wraith", EntityTwilightWraithMinion::new).flying(true).npcSelector(TFSPUtils.IN_TF_DIMENSION).items(tfSpellItems));
		registry.register(new SummonCaveTroll());
		registry.register(new SpellMinion<>(TFSpellPack.MODID, "summon_slime_beetle", EntitySlimeBeetleMinion::new).npcSelector(TFSPUtils.IN_TF_DIMENSION).items(tfSpellItems));
		registry.register(new Fortification());
		registry.register(new LifeCharm());

		registry.register(new RainOfTears());
		registry.register(new FireJets());
		registry.register(new SpellMinion<>(TFSpellPack.MODID, "snow_guardian_horde", EntitySnowGuardianMinion::new).npcSelector(TFSPUtils.IN_TF_DIMENSION).items(tfSpellItems));
		registry.register(new ChariotOfIce());
		registry.register(new CarminiteSingularity());
		registry.register(new GhastTrap());
		registry.register(new SuffocatingMist());
		registry.register(new CallOfTheHorn());
		registry.register(new Annihilation());
		registry.register(new PocketTinkerer());
		registry.register(new HydrasVigour());

	}

}