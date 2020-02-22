package electroblob.tfspellpack.spell;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.entity.construct.EntityTearRain;
import electroblob.tfspellpack.registry.TFSPItems;
import electroblob.tfspellpack.util.TFSPUtils;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.spell.SpellConstructRanged;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

public class RainOfTears extends SpellConstructRanged<EntityTearRain> {

	public RainOfTears(){
		super(TFSpellPack.MODID, "rain_of_tears", EntityTearRain::new, false);
		this.npcSelector(TFSPUtils.IN_TF_DIMENSION);
		this.addProperties(Spell.DAMAGE, BURN_DURATION);
		this.floor(true);
	}

	@Override
	public boolean applicableForItem(Item item){
		return item == TFSPItems.twilight_spell_book || item == TFSPItems.twilight_scroll;
	}

	@Override
	protected void addConstructExtras(EntityTearRain construct, EnumFacing side, @Nullable EntityLivingBase caster, SpellModifiers modifiers){
		construct.posY += 7;
	}
}
