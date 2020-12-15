package electroblob.tfspellpack.spell;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.registry.TFSPItems;
import electroblob.tfspellpack.util.TFSPParticles;
import electroblob.tfspellpack.util.TFSPUtils;
import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.BlockUtils;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import twilightforest.block.BlockTFCastleMagic;
import twilightforest.block.TFBlocks;
import twilightforest.network.PacketAnnihilateBlock;
import twilightforest.network.TFPacketHandler;

import javax.annotation.Nullable;

public class Annihilation extends SpellRay {

	public Annihilation(){
		super(TFSpellPack.MODID, "annihilation", EnumAction.NONE, false);
		addProperties(DAMAGE);
		this.npcSelector(TFSPUtils.IN_TF_DIMENSION);
	}

	@Override
	public boolean applicableForItem(Item item){
		return item == TFSPItems.twilight_spell_book || item == TFSPItems.twilight_scroll;
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers){

		target.attackEntityFrom(caster == null ? DamageSource.MAGIC :
						MagicDamage.causeDirectMagicDamage(caster, MagicDamage.DamageType.MAGIC),
				getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY));

		return true;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers){

		IBlockState state = world.getBlockState(pos);

		if(!state.getBlock().isAir(state, world, pos)){
			if(canAnnihilate(world, pos, state, caster) && BlockUtils.canBreakBlock(caster, world, pos)){
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, WizardrySounds.SPELLS, 0.125f, world.rand.nextFloat() * 0.25F + 0.75F, false);
				if(!world.isRemote){
					world.setBlockToAir(pos);
					sendAnnihilateBlockPacket(world, pos);
				}
			}
		}

		return true;
	}

	@Override
	protected boolean onMiss(World world, @Nullable EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers){
		return true;
	}

	@Override
	protected void spawnParticleRay(World world, Vec3d origin, Vec3d direction, EntityLivingBase caster, double distance){
		Vec3d endpoint = origin.add(direction.scale(distance));
		ParticleBuilder.create(TFSPParticles.DARK_BEAM).clr(0.2f, 0, 0.5f).fade(0, 0, 0.2f).time(4).pos(origin).target(endpoint).spawn(world);
	}

	/** Copied from EntityTFCubeOfAnnihilation */
	private static boolean canAnnihilate(World world, BlockPos pos, IBlockState state, EntityLivingBase caster){
		// whitelist many castle blocks
		Block block = state.getBlock();
		if(block == TFBlocks.deadrock || block == TFBlocks.castle_brick || (block == TFBlocks.castle_rune_brick && state.getValue(BlockTFCastleMagic.COLOR) != EnumDyeColor.PURPLE) || block == TFBlocks.force_field || block == TFBlocks.thorns){
			return true;
		}

		return block.getExplosionResistance(caster) < 8F && state.getBlockHardness(world, pos) >= 0;
	}

	/** Copied from EntityTFCubeOfAnnihilation */
	private static void sendAnnihilateBlockPacket(World world, BlockPos pos){
		// send packet
		IMessage message = new PacketAnnihilateBlock(pos);
		NetworkRegistry.TargetPoint targetPoint = new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64);
		TFPacketHandler.CHANNEL.sendToAllAround(message, targetPoint);
	}

}
