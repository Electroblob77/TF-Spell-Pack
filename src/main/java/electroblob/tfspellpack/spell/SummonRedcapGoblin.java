package electroblob.tfspellpack.spell;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.entity.living.EntityRedcapMinion;
import electroblob.tfspellpack.entity.living.EntityRedcapSapperMinion;
import electroblob.tfspellpack.registry.TFSPItems;
import electroblob.tfspellpack.util.TFSPUtils;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.spell.SpellMinion;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class SummonRedcapGoblin extends SpellMinion<EntityRedcapMinion> {

	public SummonRedcapGoblin(){
		super(TFSpellPack.MODID, "summon_goblin", EntityRedcapMinion::new);
		this.npcSelector(TFSPUtils.IN_TF_DIMENSION);
	}

	@Override
	public boolean applicableForItem(Item item){
		return item == TFSPItems.twilight_spell_book || item == TFSPItems.twilight_scroll;
	}

	@Override
	protected EntityRedcapMinion createMinion(World world, EntityLivingBase caster, SpellModifiers modifiers){
		if(caster instanceof EntityPlayer && ItemArtefact.isArtefactActive((EntityPlayer)caster, TFSPItems.charm_goblin)){
			return new EntityRedcapSapperMinion(world);
		}else{
			return super.createMinion(world, caster, modifiers);
		}
	}
}
