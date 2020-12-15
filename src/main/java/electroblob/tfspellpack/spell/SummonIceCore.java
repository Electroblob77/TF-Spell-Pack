package electroblob.tfspellpack.spell;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.entity.living.EntityStableIceCoreMinion;
import electroblob.tfspellpack.entity.living.EntityUnstableIceCoreMinion;
import electroblob.tfspellpack.registry.TFSPItems;
import electroblob.tfspellpack.util.TFSPUtils;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.SpellMinion;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.EntityUtils.Operations;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SummonIceCore extends SpellMinion<EntityStableIceCoreMinion> {

	private static final String POTENCY_ATTRIBUTE_MODIFIER = "potency"; // Why is this private in super?

	public SummonIceCore(){
		super(TFSpellPack.MODID, "summon_ice_core", EntityStableIceCoreMinion::new);
		this.npcSelector(TFSPUtils.IN_TF_DIMENSION);
	}

	@Override
	public boolean applicableForItem(Item item){
		return item == TFSPItems.twilight_spell_book || item == TFSPItems.twilight_scroll;
	}

	@Override
	protected boolean spawnMinions(World world, EntityLivingBase caster, SpellModifiers modifiers){

		if(caster instanceof EntityPlayer && ItemArtefact.isArtefactActive((EntityPlayer)caster, TFSPItems.charm_ice_exploder)){
			return spawnUnstableIceCore(world, caster, modifiers);
		}

		return super.spawnMinions(world, caster, modifiers);
	}

	// Best way is to just copy this
	private boolean spawnUnstableIceCore(World world, EntityLivingBase caster, SpellModifiers modifiers){

		if(!world.isRemote){
			for(int i=0; i<getProperty(MINION_COUNT).intValue(); i++){

				int range = getProperty(SUMMON_RADIUS).intValue();

				// Try and find a nearby floor space
				BlockPos pos = BlockUtils.findNearbyFloorSpace(caster, range, range*2);

				// If there was no floor around and the entity isn't a flying one, the spell fails.
				// As per the javadoc for findNearbyFloorSpace, there's no point trying the rest of the minions.
				if(pos == null) return false;

				EntityUnstableIceCoreMinion minion = new EntityUnstableIceCoreMinion(world);

				minion.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
				minion.setCaster(caster);
				// Modifier implementation
				// Attribute modifiers are pretty opaque, see https://minecraft.gamepedia.com/Attribute#Modifiers
				minion.setLifetime((int)(getProperty(MINION_LIFETIME).floatValue() * modifiers.get(WizardryItems.duration_upgrade)));
				IAttributeInstance attribute = minion.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
				if(attribute != null) attribute.applyModifier( // Apparently some things don't have an attack damage
						new AttributeModifier(POTENCY_ATTRIBUTE_MODIFIER, modifiers.get(SpellModifiers.POTENCY) - 1, Operations.MULTIPLY_CUMULATIVE));
				// This is only used for artefacts, but it's a nice example of custom spell modifiers
				minion.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(
						new AttributeModifier(HEALTH_MODIFIER, modifiers.get(HEALTH_MODIFIER) - 1, Operations.MULTIPLY_CUMULATIVE));
				minion.setHealth(minion.getMaxHealth()); // Need to set this because we may have just modified the value

				minion.onInitialSpawn(minion.world.getDifficultyForLocation(pos), null); // Replaces addMinionExtras

				world.spawnEntity(minion);
			}
		}

		return true;
	}

}
