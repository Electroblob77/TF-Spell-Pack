package electroblob.tfspellpack.spell;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.registry.TFSPItems;
import electroblob.tfspellpack.util.TFSPUtils;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.*;
import electroblob.wizardry.util.MagicDamage.DamageType;
import electroblob.wizardry.util.ParticleBuilder.Type;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;
import twilightforest.client.particle.TFParticleType;

import javax.annotation.Nullable;
import java.util.List;

public class CarminiteSingularity extends Spell {

	/** Fraction of the effect (suction) radius within which mobs take damage. */
	private static final float DAMAGE_RADIUS_FRACTION = 0.8f;
	/** Fraction of the effect (suction) radius within which mobs do not get sucked further in. */
	private static final float EPICENTRE_RADIUS_FRACTION = 0.5f;
	private static final double SUCTION_STRENGTH = 0.05;

	public CarminiteSingularity(){
		super(TFSpellPack.MODID, "carminite_singularity", EnumAction.BOW, true);
		addProperties(EFFECT_RADIUS, DAMAGE);
		this.npcSelector(TFSPUtils.IN_TF_DIMENSION);
	}

	@Override
	public boolean canBeCastBy(TileEntityDispenser dispenser){
		return true;
	}

	@Override
	public boolean applicableForItem(Item item){
		return item == TFSPItems.twilight_spell_book || item == TFSPItems.twilight_scroll;
	}

	@Override
	protected SoundEvent[] createSounds(){
		return createContinuousSpellSounds();
	}

	@Override
	protected void playSound(World world, EntityLivingBase entity, int ticksInUse, int duration, SpellModifiers modifiers, String... sounds){
		this.playSoundLoop(world, entity, ticksInUse);
	}

	@Override
	protected void playSound(World world, double x, double y, double z, int ticksInUse, int duration, SpellModifiers modifiers, String... sounds){
		this.playSoundLoop(world, x, y, z, ticksInUse, duration);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers){
		performEffect(world, caster.getPositionEyes(0).subtract(0, 0.1, 0), caster, modifiers);
		this.playSound(world, caster, ticksInUse, -1, modifiers);
		return true;
	}

	@Override
	public boolean cast(World world, EntityLiving caster, EnumHand hand, int ticksInUse, EntityLivingBase target, SpellModifiers modifiers){
		performEffect(world, caster.getPositionEyes(0).subtract(0, 0.1, 0), caster, modifiers);
		this.playSound(world, caster, ticksInUse, -1, modifiers);
		return true;
	}

	@Override
	public boolean cast(World world, double x, double y, double z, EnumFacing direction, int ticksInUse, int duration, SpellModifiers modifiers){
		performEffect(world, new Vec3d(x, y, z), null, modifiers);
		this.playSound(world, x - direction.getXOffset(), y - direction.getYOffset(), z - direction.getZOffset(), ticksInUse, duration, modifiers);
		return true;
	}

	private void performEffect(World world, Vec3d centre, @Nullable EntityLivingBase caster, SpellModifiers modifiers){

		double radius = getProperty(EFFECT_RADIUS).floatValue() * modifiers.get(WizardryItems.blast_upgrade);

		List<EntityLivingBase> targets = EntityUtils.getLivingWithinRadius(radius, centre.x, centre.y, centre.z, world);

		for(EntityLivingBase target : targets){

			if(AllyDesignationSystem.isValidTarget(caster, target)){

				double distance = centre.distanceTo(target.getPositionVector());

				// Suck 'em up!
				if(distance > radius * EPICENTRE_RADIUS_FRACTION){
					Vec3d vel = centre.subtract(target.getPositionVector()).normalize().scale(SUCTION_STRENGTH);
					target.addVelocity(vel.x, vel.y, vel.z);
					// Player motion is handled on that player's client so needs packets
					if(target instanceof EntityPlayerMP){
						((EntityPlayerMP)target).connection.sendPacket(new SPacketEntityVelocity(target));
					}
				}

				// Shock 'em!
				if(distance < radius * DAMAGE_RADIUS_FRACTION && !MagicDamage.isEntityImmune(DamageType.SHOCK, target)
						&& target.ticksExisted % target.maxHurtResistantTime == 1){

					EntityUtils.attackEntityWithoutKnockback(target, MagicDamage.causeDirectMagicDamage(caster,
							DamageType.SHOCK), getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY));

					if(world.isRemote){
						ParticleBuilder.create(Type.LIGHTNING).entity(caster).clr(0xff4384)
								.pos(caster != null ? centre.subtract(caster.getPositionVector()) : centre).target(target).spawn(world);
					}
				}
			}
		}

		// Projectile repulsion
		if(caster instanceof EntityPlayer && ItemArtefact.isArtefactActive((EntityPlayer)caster, TFSPItems.amulet_carminite)){
			List<Entity> projectiles = EntityUtils.getEntitiesWithinRadius(radius, centre.x, centre.y, centre.z, world, Entity.class);
			for(Entity projectile : projectiles){
				if(projectile instanceof IProjectile){
					Vec3d vec = projectile.getPositionVector().subtract(centre).normalize().scale(0.2);
					projectile.addVelocity(vec.x, vec.y, vec.z);
				}
			}
		}

		if(world.isRemote){
			// Clouds
			for(int i=0; i<10; i++){
				double dx = (world.rand.nextDouble() * 2 - 1) * radius;
				double dy = (world.rand.nextDouble() * 2 - 1) * radius;
				double dz = (world.rand.nextDouble() * 2 - 1) * radius;
				// These particles use the velocity args differently; they behave more like portal particles
				TwilightForestMod.proxy.spawnParticle(TFParticleType.GHAST_TRAP, centre.x, centre.y, centre.z, dx, dy, dz);
			}
			// Sparks
			if(world.rand.nextInt(6) == 0){
				ParticleBuilder.create(Type.FLASH).entity(caster).clr(0xffd8fa).scale(2)
						.pos(caster != null ? centre.subtract(caster.getPositionVector()) : centre).spawn(world);
				ParticleBuilder.create(Type.LIGHTNING).entity(caster).clr(0xff4384).scale(0.5f)
						.pos(caster != null ? centre.subtract(caster.getPositionVector()) : centre)
						.target(centre.add(world.rand.nextDouble() * 2 - 1, world.rand.nextDouble() * 2 - 1,
								world.rand.nextDouble() * 2 - 1)).spawn(world);
			}
		}

	}

}
