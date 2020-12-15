package electroblob.tfspellpack.spell;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.entity.construct.EntityFireJet;
import electroblob.tfspellpack.registry.TFSPItems;
import electroblob.tfspellpack.util.TFSPUtils;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.SpellConstruct;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class FireJets extends SpellConstruct<EntityFireJet> {

	public static final String FIRE_JETS = "fire_jets";

	public FireJets(){
		super(TFSpellPack.MODID, "fire_jets", EnumAction.BOW, EntityFireJet::new, false);
		this.soundValues(3, 1, 0.2f);
		addProperties(EFFECT_RADIUS, FIRE_JETS, DAMAGE, BURN_DURATION);
		this.overlap(true);
		this.npcSelector(TFSPUtils.IN_TF_DIMENSION);
	}

	@Override
	public boolean applicableForItem(Item item){
		return item == TFSPItems.twilight_spell_book || item == TFSPItems.twilight_scroll;
	}

	@Override
	protected boolean spawnConstruct(World world, double x, double y, double z, @Nullable EnumFacing side, @Nullable EntityLivingBase caster, SpellModifiers modifiers){

		boolean success = false;

		double radius = getProperty(EFFECT_RADIUS).doubleValue() * modifiers.get(WizardryItems.blast_upgrade);

		int jetCount = getProperty(FIRE_JETS).intValue();

		for(int i = 0; i < jetCount; i++){

			float angle =  (float)Math.PI * 2 * (i + world.rand.nextFloat() * 0.5f)/jetCount;

			double px = x + radius * MathHelper.cos(angle) + world.rand.nextDouble() * 2 - 1;
			double pz = z + radius * MathHelper.sin(angle) + world.rand.nextDouble() * 2 - 1;
			Integer py = BlockUtils.getNearestFloor(world, new BlockPos(px, y, pz), (int)radius);

			if(py != null){
				if(super.spawnConstruct(world, px, py, pz, side, caster, modifiers)){
					success = true;
					BlockPos pos = new BlockPos(px, py, pz);
					if(!world.isRemote && BlockUtils.canPlaceBlock(caster, world, pos))
						world.setBlockState(pos, Blocks.FIRE.getDefaultState());
				}
			}
		}

		return success;
	}

}
