package electroblob.tfspellpack.spell;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.registry.TFSPItems;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import twilightforest.block.TFBlocks;

import javax.annotation.Nullable;

public class TwilightCatalyst extends SpellRay {

	public TwilightCatalyst(){
		super(TFSpellPack.MODID, "twilight_catalyst", EnumAction.NONE, false);
		this.hitLiquids(true);
	}

	@Override
	public boolean canBeCastBy(EntityLiving npc, boolean override){
		return false;
	}

	@Override
	public boolean applicableForItem(Item item){
		return item == TFSPItems.twilight_spell_book || item == TFSPItems.twilight_scroll;
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers){
		return false;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers){
		if(world.getBlockState(pos).getBlock() != Blocks.WATER) pos = pos.offset(side); // hitLiquids is playing up so this should fix it
		EntityItem dummyItem = new EntityItem(world);
		return TFBlocks.twilight_portal.tryToCreatePortal(world, pos, dummyItem, caster instanceof EntityPlayer ? (EntityPlayer)caster : null);
	}

	@Override
	protected boolean onMiss(World world, @Nullable EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers){
		return true;
	}

	@Override
	protected void spawnParticle(World world, double x, double y, double z, double vx, double vy, double vz){
		world.spawnParticle(EnumParticleTypes.CRIT_MAGIC, x, y, z, vx, vy, vz);
	}

}
