package electroblob.tfspellpack.entity.construct;

import electroblob.tfspellpack.registry.TFSPSounds;
import electroblob.tfspellpack.registry.TFSPSpells;
import electroblob.wizardry.entity.construct.EntityMagicConstruct;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.ParticleBuilder.Type;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

import java.util.List;

public class EntityCinderCloud extends EntityMagicConstruct {

	private static final double MAX_PARTICLE_VELOCITY = 0.015;

	public EntityCinderCloud(World world){
		super(world);
		this.height = TFSPSpells.cinder_cloud.getProperty(Spell.EFFECT_RADIUS).floatValue() * 2;
		this.width = TFSPSpells.cinder_cloud.getProperty(Spell.EFFECT_RADIUS).floatValue() * 2;
	}

	@Override
	public void onUpdate(){

		super.onUpdate();

		if(this.ticksExisted % 40 == 1){
			this.playSound(TFSPSounds.ENTITY_CINDER_CLOUD_AMBIENT, 4.0f, 0.7f);
		}

		if(world.isRemote){

			float volume = width * width * height;

			for(int i = 0; i < volume * 0.05f; i++){ // 1 particle per block per second

				ParticleBuilder.create(Type.DUST, this).vel((rand.nextDouble() * 2 - 1) * MAX_PARTICLE_VELOCITY,
						(rand.nextDouble() * 2 - 1) * MAX_PARTICLE_VELOCITY, (rand.nextDouble() * 2 - 1) * MAX_PARTICLE_VELOCITY)
						.clr(1, 0.2f + rand.nextFloat() * 0.6f, 0.1f).spawn(world);

				ParticleBuilder.create(Type.DUST, this).vel((rand.nextDouble() * 2 - 1) * MAX_PARTICLE_VELOCITY,
						(rand.nextDouble() * 2 - 1) * MAX_PARTICLE_VELOCITY, (rand.nextDouble() * 2 - 1) * MAX_PARTICLE_VELOCITY)
						.clr(0.3f, 0.3f, 0.3f).spawn(world);
			}
		}

		if(!this.world.isRemote){

			List<EntityLivingBase> targets = world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox());

			float damage = TFSPSpells.cinder_cloud.getProperty(Spell.DAMAGE).floatValue() * this.damageMultiplier;

			for(EntityLivingBase target : targets){

				if(this.isValidTarget(target) && !MagicDamage.isEntityImmune(MagicDamage.DamageType.FIRE, target)){

					if(target.ticksExisted % 20 == 0){ // Use target's lifetime so they don't all get hit at once, looks better
						EntityUtils.attackEntityWithoutKnockback(target, MagicDamage.causeIndirectMagicDamage(this,
								this.getCaster(), MagicDamage.DamageType.FIRE), damage);
						target.setFire(TFSPSpells.cinder_cloud.getProperty(Spell.BURN_DURATION).intValue());
					}
				}
			}
		}

	}

}
