package electroblob.tfspellpack.spell;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.registry.TFSPItems;
import electroblob.tfspellpack.util.TFSPUtils;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.MagicDamage.DamageType;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;
import twilightforest.client.particle.TFParticleType;
import twilightforest.potions.TFPotions;

import javax.annotation.Nullable;

public class FrostBreath extends SpellRay {

	public FrostBreath(){
		super(TFSpellPack.MODID, "frost_breath", EnumAction.NONE, true);
		this.aimAssist(0.3f); // A bit of artistic license, makes it more unique from the regular frost ray
		this.soundValues(0.5f, 0.5f, 0);
		this.npcSelector(TFSPUtils.IN_TF_DIMENSION);
		addProperties(DAMAGE, EFFECT_DURATION, EFFECT_STRENGTH);
	}

	@Override
	public boolean applicableForItem(Item item){
		return item == TFSPItems.twilight_spell_book || item == TFSPItems.twilight_scroll;
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers){

		if(EntityUtils.isLiving(target)){

			if(target.isBurning()) target.extinguish();

			if(MagicDamage.isEntityImmune(DamageType.FROST, target)){
				if(!world.isRemote && ticksInUse == 1 && caster instanceof EntityPlayer) ((EntityPlayer)caster)
						.sendStatusMessage(new TextComponentTranslation("spell.resist", target.getName(),
								this.getNameForTranslationFormatted()), true);
			// This now only damages in line with the maxHurtResistantTime. Some mods don't play nicely and fiddle
			// with this mechanic for their own purposes, so this line makes sure that doesn't affect wizardry.
			}else if(ticksInUse % ((EntityLivingBase)target).maxHurtResistantTime == 1){

				((EntityLivingBase)target).addPotionEffect(new PotionEffect(TFPotions.frosty,
						(int)(getProperty(EFFECT_DURATION).floatValue() * modifiers.get(WizardryItems.duration_upgrade)),
						getProperty(EFFECT_STRENGTH).intValue()));

				float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
				if(target instanceof EntityBlaze || target instanceof EntityMagmaCube) damage *= 2;

				EntityUtils.attackEntityWithoutKnockback(target, MagicDamage.causeDirectMagicDamage(caster,
						DamageType.FROST), damage);

			}
		}

		return true;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers){
		return false;
	}

	@Override
	protected boolean onMiss(World world, @Nullable EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers){
		return true;
	}

	@Override
	protected void playSound(World world, double x, double y, double z, int ticksInUse, int duration, SpellModifiers modifiers, String... sounds){
		if(ticksInUse % 5 == 0) super.playSound(world, x, y, z, ticksInUse, duration, modifiers, sounds);
	}

	@Override
	protected void spawnParticleRay(World world, Vec3d origin, Vec3d direction, EntityLivingBase caster, double distance){
		// See EntityTFWinterWolf; copied and adapted to work with the SpellRay system
		final double dist = 0.5;
		double px = origin.x + direction.x * dist;
		double py = origin.y + direction.y * dist;
		double pz = origin.z + direction.z * dist;

		for(int i = 0; i < 10; i++){

			double dx = direction.x;
			double dy = direction.y;
			double dz = direction.z;

			double spread = 5.0 + world.rand.nextDouble() * 2.5;
			double velocity = 3.0 + world.rand.nextDouble() * 0.15;

			dx += world.rand.nextGaussian() * 0.0075 * spread;
			dy += world.rand.nextGaussian() * 0.0075 * spread;
			dz += world.rand.nextGaussian() * 0.0075 * spread;
			dx *= velocity;
			dy *= velocity;
			dz *= velocity;

			TwilightForestMod.proxy.spawnParticle(TFParticleType.SNOW, px, py, pz, dx, dy, dz);
		}
	}

}
