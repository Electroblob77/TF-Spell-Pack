package electroblob.tfspellpack.spell;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.entity.construct.EntityDarkMistCloud;
import electroblob.tfspellpack.registry.TFSPItems;
import electroblob.tfspellpack.util.TFSPUtils;
import electroblob.wizardry.spell.SpellConstructRanged;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

public class SuffocatingMist extends SpellConstructRanged<EntityDarkMistCloud> {

	public static final String BLINDNESS_DURATION = "blindness_duration";
	public static final String SLOWNESS_DURATION = "slowness_duration";
	public static final String SLOWNESS_STRENGTH = "slowness_strength";

	public SuffocatingMist(){
		super(TFSpellPack.MODID, "suffocating_mist", EntityDarkMistCloud::new, false);
		this.addProperties(DAMAGE, EFFECT_RADIUS, BLINDNESS_DURATION, SLOWNESS_DURATION, SLOWNESS_STRENGTH);
		this.npcSelector(TFSPUtils.IN_TF_DIMENSION);
	}

	@Override
	public boolean applicableForItem(Item item){
		return item == TFSPItems.twilight_spell_book || item == TFSPItems.twilight_scroll;
	}

	@Override
	protected void addConstructExtras(EntityDarkMistCloud construct, EnumFacing side, @Nullable EntityLivingBase caster, SpellModifiers modifiers){
		construct.posY -= 2;
	}

}
