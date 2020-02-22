package electroblob.tfspellpack.spell;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.entity.construct.EntityCinderCloud;
import electroblob.tfspellpack.registry.TFSPItems;
import electroblob.tfspellpack.util.TFSPUtils;
import electroblob.wizardry.spell.SpellConstructRanged;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

public class CinderCloud extends SpellConstructRanged<EntityCinderCloud> {

	public CinderCloud(){
		super(TFSpellPack.MODID, "cinder_cloud", EntityCinderCloud::new, false);
		this.addProperties(DAMAGE, EFFECT_RADIUS, BURN_DURATION);
		this.npcSelector(TFSPUtils.IN_TF_DIMENSION);
	}

	@Override
	public boolean applicableForItem(Item item){
		return item == TFSPItems.twilight_spell_book || item == TFSPItems.twilight_scroll;
	}

	@Override
	protected void addConstructExtras(EntityCinderCloud construct, EnumFacing side, @Nullable EntityLivingBase caster, SpellModifiers modifiers){
		construct.posY -= construct.height / 2;
	}

}
