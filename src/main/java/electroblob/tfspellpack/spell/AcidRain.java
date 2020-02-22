package electroblob.tfspellpack.spell;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.entity.construct.EntityAcidRainCloud;
import electroblob.tfspellpack.registry.TFSPItems;
import electroblob.tfspellpack.util.TFSPUtils;
import electroblob.wizardry.spell.SpellConstructRanged;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

public class AcidRain extends SpellConstructRanged<EntityAcidRainCloud> {

	public AcidRain(){
		super(TFSpellPack.MODID, "acid_rain", EntityAcidRainCloud::new, false);
		this.addProperties(DAMAGE, EFFECT_RADIUS);
		this.floor(true);
		this.npcSelector(TFSPUtils.IN_TF_DIMENSION);
	}

	@Override
	public boolean applicableForItem(Item item){
		return item == TFSPItems.twilight_spell_book || item == TFSPItems.twilight_scroll;
	}

	@Override
	protected void addConstructExtras(EntityAcidRainCloud construct, EnumFacing side, @Nullable EntityLivingBase caster, SpellModifiers modifiers){
		construct.posY += 5;
//		construct.multiplyWidth(modifiers.get(WizardryItems.blast_upgrade));
	}
}
