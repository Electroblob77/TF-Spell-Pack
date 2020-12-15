package electroblob.tfspellpack.entity.living;

import electroblob.tfspellpack.Settings;
import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.registry.TFSPItems;
import electroblob.tfspellpack.registry.TFSPSounds;
import electroblob.tfspellpack.registry.TFSPSpells;
import electroblob.wizardry.constants.Element;
import electroblob.wizardry.constants.Tier;
import electroblob.wizardry.entity.living.EntityEvilWizard;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.SpellProperties;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import twilightforest.TFConfig;
import twilightforest.block.BlockTFTrapDoor;
import twilightforest.entity.EntityTFSkeletonDruid;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber
public class EntityDruidMage extends EntityEvilWizard {

	/** The resource location for the druid mage's loot table. */
	private static final ResourceLocation LOOT_TABLE = new ResourceLocation(TFSpellPack.MODID, "entities/druid_mage");

	private static final float TWILIGHT_SPELL_CHANCE = 0.75f;
	private static final int BASEMENT_SEARCH_RADIUS = 10;

	private static final Method getEntityId;

	static {
		// There are about 20 methods called getEntityId and no way of telling which is which in the mappings...
		// I found this one in the access transformer files in iChunUtil
		getEntityId = ObfuscationReflectionHelper.findMethod(MobSpawnerBaseLogic.class, "func_190895_g", ResourceLocation.class);
	}

	public EntityDruidMage(World world){
		super(world);
	}

	@Override
	protected void applyEntityAttributes(){
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40);
	}

	@Override
	public Element getElement(){
		return Element.MAGIC;
	}

	@Override
	public void setElement(Element element){}

	@Override
	protected SoundEvent getAmbientSound(){
		return TFSPSounds.ENTITY_DRUID_MAGE_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source){
		return TFSPSounds.ENTITY_DRUID_MAGE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound(){
		return TFSPSounds.ENTITY_DRUID_MAGE_DEATH;
	}

	@Override
	protected float getSoundPitch(){
		return 0.7f;
	}

	@Override
	public void onUpdate(){

		super.onUpdate();

		if(world.isRemote){
			world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX + (rand.nextDouble() - 0.5) * width, posY,
					posZ + (rand.nextDouble() - 0.5) * width, 0, 0, 0);
		}

	}

	@Override
	protected void dropFewItems(boolean hitByPlayer, int lootingLevel){

	}

	@Override
	protected ResourceLocation getLootTable(){
		return LOOT_TABLE;
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData data){
		// All druid mages know druid hex, even if it is disabled.
		getSpells().add(TFSPSpells.druid_hex);
		populateSpells(this, this.getSpells(), 3, this.rand);
		return data; // Don't call super, it does a load of stuff we don't want
	}

	@Override
	public void writeSpawnData(ByteBuf data){} // Don't sync stuff unnecessarily

	@Override
	public void readSpawnData(ByteBuf data){}

	// TODO: Hmmm super prevents any instances of EntityEvilWizard from spawning in dimensions not specified in wizardry's config
	// I guess these guys don't spawn naturally (only as part of structures) so it's probably ok

	/**
	 * Adds n random spells to the given list. The spells will be of the given element if possible. Extracted as a
	 * separate function since it was the same in both EntityWizard and EntityEvilWizard.
	 *
	 * @param wizard The wizard whose spells are to be populated.
	 * @param spells The spell list to be populated.
	 * @param n The number of spells to add.
	 * @param random A random number generator to use.
	 */
	private static void populateSpells(final EntityLiving wizard, List<Spell> spells, int n, Random random){

		List<Spell> twilightSpells = Spell.getSpells(s -> s.canBeCastBy(wizard, true)); // These guys use TF spells anywhere
		twilightSpells.removeIf(s -> !s.applicableForItem(TFSPItems.twilight_spell_book));

		List<Spell> regularSpells = Spell.getSpells(s -> s.canBeCastBy(wizard, false));
		regularSpells.removeIf(s -> !s.applicableForItem(WizardryItems.spell_book));

		for(int i = 0; i < n; i++){

			List<Spell> pool = random.nextFloat() < TWILIGHT_SPELL_CHANCE ? twilightSpells : regularSpells;

			Tier tier;
			// If the wizard has no element, it picks a random one each time.
			Element element = Element.values()[random.nextInt(Element.values().length)];

			int randomiser = random.nextInt(20);

			// Uses its own special weighting
			if(randomiser < 10){
				tier = Tier.NOVICE;
			}else if(randomiser < 16){
				tier = Tier.APPRENTICE;
			}else if(randomiser < 19){
				tier = Tier.ADVANCED;
			}else{
				tier = Tier.MASTER;
			}

			// Finds all the spells of the chosen tier and element
			List<Spell> list = Spell.getSpells(new Spell.TierElementFilter(tier, element, SpellProperties.Context.NPCS));
			// Keeps only spells which can be cast by NPCs
			list.retainAll(pool);
			// Removes spells that the wizard already has
			list.removeAll(spells);

			// Ensures the tier chosen actually has spells in it
			if(list.isEmpty()){
				// If there are no spells applicable, tier and element restrictions are removed to give maximum
				// possibility of there being an applicable spell.
				list = pool;
				// Removes spells that the wizard already has
				list.removeAll(spells);
			}

			// If the list is still empty now, there must be less than 3 enabled spells that can be cast by wizards
			// (excluding magic missile). In this case, having empty slots seems reasonable.
			if(!list.isEmpty()) spells.add(list.get(random.nextInt(list.size())));

		}

	}

	@SubscribeEvent
	public static void onDecorateBiomePostEvent(DecorateBiomeEvent.Post event){ // Could equally be pre since TF calls super after it's done

		if(Settings.druidMageSpawnChance == 0) return; // Do nothing if spawning is disabled

		if(event.getWorld().provider.getDimension() == TFConfig.dimension.dimensionID){

			// How the heck can we get a CME from iterating over the loaded tile entities during worldgen?!
			List<TileEntity> tileEntities = new ArrayList<>(event.getWorld().loadedTileEntityList);

			for(TileEntity tileEntity : tileEntities){

				BlockPos pos = tileEntity.getPos();

				if(tileEntity instanceof TileEntityMobSpawner && new ChunkPos(pos).equals(event.getChunkPos())){

					try {
						// Druid spawners are only found in druid huts
						if(getEntityId.invoke(((TileEntityMobSpawner)tileEntity).getSpawnerBaseLogic())
								.equals(EntityList.getKey(EntityTFSkeletonDruid.class))){

							lookForBasementAndSpawnDruidMage(event.getWorld(), pos);
							return; // Some huts have 2 spawners, but we only want to do this once per hut
						}

					}catch(IllegalAccessException | InvocationTargetException e){
						TFSpellPack.logger.error("Error reflectively getting spawner entity: ", e);
					}
				}
			}
		}
	}

	private static void lookForBasementAndSpawnDruidMage(World world, BlockPos spawnerPos){

		for(int dx = -BASEMENT_SEARCH_RADIUS; dx <= BASEMENT_SEARCH_RADIUS; dx++){
			for(int dy = -BASEMENT_SEARCH_RADIUS; dy <= BASEMENT_SEARCH_RADIUS; dy++){
				for(int dz = -BASEMENT_SEARCH_RADIUS; dz <= BASEMENT_SEARCH_RADIUS; dz++){

					BlockPos pos = spawnerPos.add(dx, dy, dz);

					// Look for a trapdoor, since this means there must be a basement
					if(world.getBlockState(pos).getBlock() instanceof BlockTrapDoor
							|| world.getBlockState(pos).getBlock() instanceof BlockTFTrapDoor){

						if(world.rand.nextFloat() < Settings.druidMageSpawnChance){

							// Only search downwards (but flip the surface criteria so we get floors, not ceilings!)
							Integer floor = BlockUtils.getNearestSurface(world, pos, EnumFacing.DOWN, 16, false,
									BlockUtils.SurfaceCriteria.COLLIDABLE.flip());

							if(floor != null){
								EntityDruidMage druidMage = new EntityDruidMage(world);
								druidMage.setPosition(pos.getX() + 0.5, floor, pos.getZ() + 0.5);
								druidMage.hasStructure = true;
								druidMage.onInitialSpawn(world.getDifficultyForLocation(pos), null);
								world.spawnEntity(druidMage);
							}
						}

						return; // Return if a trapdoor was found, regardless of whether a druid was actually spawned
					}
				}
			}
		}
	}

}
