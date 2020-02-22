package electroblob.tfspellpack.spell;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.registry.TFSPItems;
import electroblob.tfspellpack.util.TFSPUtils;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.spell.SpellBuff;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;

public class LifeCharm extends SpellBuff {

	public static final String HEALTH_LEVEL = "health_level";

	public LifeCharm(){
		super(TFSpellPack.MODID, "life_charm", 0.82f, 0.36f, 0.46f, () -> MobEffects.REGENERATION);
		this.npcSelector(TFSPUtils.IN_TF_DIMENSION);
		addProperties(HEALTH_LEVEL);
	}

	@Override
	public boolean applicableForItem(Item item){
		return item == TFSPItems.twilight_spell_book || item == TFSPItems.twilight_scroll;
	}

	@Override
	protected boolean applyEffects(EntityLivingBase caster, SpellModifiers modifiers){

		float healthLevel = getProperty(HEALTH_LEVEL).floatValue();

		if(caster instanceof EntityPlayer && ItemArtefact.isArtefactActive((EntityPlayer)caster, TFSPItems.amulet_life_charm)){
			healthLevel = caster.getMaxHealth();
			caster.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 600, 0));
		}

		float healAmount = healthLevel - caster.getHealth();

		if(healAmount > 0) caster.heal(healAmount);

		return super.applyEffects(caster, modifiers) || healAmount > 0;
	}

}
