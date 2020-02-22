package electroblob.tfspellpack.spell;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.registry.TFSPItems;
import electroblob.tfspellpack.util.TFSPUtils;
import electroblob.wizardry.constants.Constants;
import electroblob.wizardry.spell.SpellBuff;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.shield.IShieldCapability;

public class Fortification extends SpellBuff {

	public static final String SHIELD_COUNT = "shield_count";

	public Fortification(){
		super(TFSpellPack.MODID, "fortification", 1, 0.95f, 0.35f);
		addProperties(SHIELD_COUNT);
		this.npcSelector(TFSPUtils.IN_TF_DIMENSION);
	}

	@Override
	public boolean applicableForItem(Item item){
		return item == TFSPItems.twilight_spell_book || item == TFSPItems.twilight_scroll;
	}

	@Override
	protected boolean applyEffects(EntityLivingBase caster, SpellModifiers modifiers){

		if(caster.hasCapability(CapabilityList.SHIELDS, null)){

			if(!caster.world.isRemote){
				IShieldCapability shieldHandler = caster.getCapability(CapabilityList.SHIELDS, null);
				// 5 shields plus 1 extra per tier of matching wand
				int shieldCount = getProperty(SHIELD_COUNT).intValue()
						+ (int)((modifiers.get(SpellModifiers.POTENCY) - 1) / Constants.POTENCY_INCREASE_PER_TIER + 0.5f);
				if(shieldHandler != null) shieldHandler.setShields(shieldCount, true);
			}

			return true;
		}

		return false;
	}

}
