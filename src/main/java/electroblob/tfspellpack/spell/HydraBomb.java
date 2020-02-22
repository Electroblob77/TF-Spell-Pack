package electroblob.tfspellpack.spell;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.entity.projectile.EntityMagicHydraMortar;
import electroblob.tfspellpack.registry.TFSPItems;
import electroblob.tfspellpack.util.TFSPUtils;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.SpellThrowable;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

public class HydraBomb extends SpellThrowable<EntityMagicHydraMortar> {

	public HydraBomb(){
		super(TFSpellPack.MODID, "hydra_bomb", EntityMagicHydraMortar::new);
		this.npcSelector(TFSPUtils.IN_TF_DIMENSION);
	}

	@Override
	public boolean applicableForItem(Item item){
		return item == TFSPItems.twilight_spell_book || item == TFSPItems.twilight_scroll;
	}

	@Override
	protected void addProjectileExtras(EntityMagicHydraMortar projectile, EntityLivingBase caster, SpellModifiers modifiers){
		projectile.fuse *= modifiers.get(WizardryItems.duration_upgrade);
		if(caster instanceof EntityPlayer && ItemArtefact.isArtefactActive((EntityPlayer)caster, TFSPItems.ring_hydra_kaboom)){
			projectile.setToBlasting();
		}
	}

}
