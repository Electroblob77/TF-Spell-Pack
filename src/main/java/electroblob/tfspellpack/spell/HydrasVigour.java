package electroblob.tfspellpack.spell;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.registry.TFSPItems;
import electroblob.wizardry.spell.SpellBuff;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;

public class HydrasVigour extends SpellBuff {

	public static final String HUNGER_POINTS = "hunger_points";
	public static final String SATURATION_MODIFIER = "saturation_modifier";

	public HydrasVigour(){
		super(TFSpellPack.MODID, "hydras_vigour", 0.82f, 0.36f, 0.46f, () -> MobEffects.REGENERATION);
		addProperties(HUNGER_POINTS, SATURATION_MODIFIER);
	}

	@Override public boolean canBeCastBy(EntityLiving npc, boolean override){ return false; }

	@Override
	public boolean applicableForItem(Item item){
		return item == TFSPItems.twilight_spell_book || item == TFSPItems.twilight_scroll;
	}

	@Override
	protected boolean applyEffects(EntityLivingBase caster, SpellModifiers modifiers){

		if(caster instanceof EntityPlayer && ((EntityPlayer)caster).getFoodStats().needFood()){
			int foodAmount = (int)(getProperty(HUNGER_POINTS).floatValue() * modifiers.get(SpellModifiers.POTENCY));
			((EntityPlayer)caster).getFoodStats().addStats(foodAmount, getProperty(SATURATION_MODIFIER).floatValue());
			super.applyEffects(caster, modifiers);
			return true;
		}

		return super.applyEffects(caster, modifiers);
	}

}
