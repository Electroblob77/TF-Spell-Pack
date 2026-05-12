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

        // We calculate values on both sides so the return statement is accurate,
        // but we only PERFORM the actions on the server later on.
        boolean isAmuletActive = caster instanceof EntityPlayer && ItemArtefact.isArtefactActive((EntityPlayer)caster, TFSPItems.amulet_life_charm);

        if(isAmuletActive){
            healthLevel = caster.getMaxHealth();
        }

        float healAmount = healthLevel - caster.getHealth();

        if(!caster.world.isRemote){
            if(isAmuletActive){
                caster.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 600, 0));
            }

            if(healAmount > 0) caster.heal(healAmount);
        }

        return super.applyEffects(caster, modifiers) || healAmount > 0;
    }

}
