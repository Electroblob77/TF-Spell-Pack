package electroblob.tfspellpack.spell;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.entity.living.EntityCaveTrollMinion;
import electroblob.tfspellpack.registry.TFSPItems;
import electroblob.tfspellpack.util.TFSPUtils;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.spell.SpellMinion;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class SummonCaveTroll extends SpellMinion<EntityCaveTrollMinion> {

	public SummonCaveTroll(){
		super(TFSpellPack.MODID, "summon_cave_troll", EntityCaveTrollMinion::new);
		this.npcSelector(TFSPUtils.IN_TF_DIMENSION);
	}

	@Override
	public boolean applicableForItem(Item item){
		return item == TFSPItems.twilight_spell_book || item == TFSPItems.twilight_scroll;
	}

	@Override
	protected void addMinionExtras(EntityCaveTrollMinion minion, BlockPos pos, @Nullable EntityLivingBase caster, SpellModifiers modifiers, int alreadySpawned){
		if(caster instanceof EntityPlayer && ItemArtefact.isArtefactActive((EntityPlayer)caster, TFSPItems.charm_troll)){
			minion.setHasRock(true);
		}
	}
}
