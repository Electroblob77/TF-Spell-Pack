package electroblob.tfspellpack.spell;

import electroblob.tfspellpack.TFSPGuiHandler;
import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.registry.TFSPItems;
import electroblob.tfspellpack.util.TFSPUtils;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class PocketTinkerer extends Spell {

	public PocketTinkerer(){
		super(TFSpellPack.MODID, "pocket_tinkerer", EnumAction.BOW, false);
	}

	@Override
	public boolean requiresPacket(){
		return false;
	}

	@Override
	public boolean applicableForItem(Item item){
		return item == TFSPItems.twilight_spell_book || item == TFSPItems.twilight_scroll;
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers){

		if(!world.isRemote){
			caster.openGui(TFSpellPack.instance, TFSPGuiHandler.PORTABLE_UNCRAFTING, world, (int)caster.posX,
					(int)caster.posY, (int)caster.posZ);
		}

		this.playSound(world, caster, ticksInUse, -1, modifiers);

		return true;
	}

}
