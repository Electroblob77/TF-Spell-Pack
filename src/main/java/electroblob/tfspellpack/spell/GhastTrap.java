package electroblob.tfspellpack.spell;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.entity.living.EntityMiniGhastMinion;
import electroblob.tfspellpack.registry.TFSPItems;
import electroblob.tfspellpack.util.TFSPUtils;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.SpellMinion;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class GhastTrap extends SpellRay {

	public static final String MINION_LIFETIME = "minion_lifetime";
	public static final String MINION_COUNT = "minion_count";
	public static final String SUMMON_RADIUS = "summon_radius";

	public GhastTrap(){
		super(TFSpellPack.MODID, "ghast_trap", false, EnumAction.NONE);
		this.npcSelector(TFSPUtils.IN_TF_DIMENSION);
		this.ignoreLivingEntities(true);
		addProperties(MINION_LIFETIME, MINION_COUNT, SUMMON_RADIUS);
	}

	@Override public boolean requiresPacket(){ return false; }

	@Override
	public boolean applicableForItem(Item item){
		return item == TFSPItems.twilight_spell_book || item == TFSPItems.twilight_scroll;
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers){
		return false;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers){

		if(world.canBlockSeeSky(pos.up())){

			if(!world.isRemote){

				// Summon ghastlings
				for(int i=0; i<getProperty(MINION_COUNT).intValue(); i++){

					int range = getProperty(SUMMON_RADIUS).intValue();

					// Try and find a nearby floor space
					BlockPos pos1 = WizardryUtilities.findNearbyFloorSpace(world, pos, range, range*2);

					if(pos1 != null){
						// Make sure the flying entity spawns above the ground
						pos1 = pos1.up(1);
					}else{
						// If there was no floor around to spawn them on, just pick any spot in mid-air
						pos1 = pos.north(world.rand.nextInt(range*2) - range)
								.east(world.rand.nextInt(range*2) - range);
					}

					EntityMiniGhastMinion minion = new EntityMiniGhastMinion(world);

					minion.setPosition(pos1.getX() + 0.5, pos1.getY(), pos1.getZ() + 0.5);
					minion.setCaster(caster);
					// Modifier implementation
					// Attribute modifiers are pretty opaque, see https://minecraft.gamepedia.com/Attribute#Modifiers
					minion.setLifetime((int)(getProperty(MINION_LIFETIME).floatValue() * modifiers.get(WizardryItems.duration_upgrade)));
					// This is only used for artefacts, but it's a nice example of custom spell modifiers
					minion.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(
							new AttributeModifier(SpellMinion.HEALTH_MODIFIER, modifiers.get(SpellMinion.HEALTH_MODIFIER) - 1,
									WizardryUtilities.Operations.MULTIPLY_CUMULATIVE));
					minion.setHealth(minion.getMaxHealth()); // Need to set this because we may have just modified the value

					world.spawnEntity(minion);
				}

				// Temporarily disable the fire tick gamerule if player block damage is disabled
				// Bit of a hack but it works fine!
				boolean doFireTick = world.getGameRules().getBoolean("doFireTick");
				if(doFireTick && !WizardryUtilities.canDamageBlocks(caster, world)) world.getGameRules().setOrCreateGameRule("doFireTick", "false");
				EntityLightningBolt entitylightning = new EntityLightningBolt(world, pos.getX(), pos.getY(),
						pos.getZ(), false);
				world.addWeatherEffect(entitylightning);
				// Reset doFireTick to true if it was true before
				if(doFireTick && !WizardryUtilities.canDamageBlocks(caster, world)) world.getGameRules().setOrCreateGameRule("doFireTick", "true");

			}

			return true;
		}

		return false;
	}

	@Override
	protected boolean onMiss(World world, @Nullable EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers){
		return false;
	}

}