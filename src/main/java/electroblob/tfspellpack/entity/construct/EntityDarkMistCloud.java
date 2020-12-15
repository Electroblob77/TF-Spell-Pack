package electroblob.tfspellpack.entity.construct;

import electroblob.tfspellpack.registry.TFSPSounds;
import electroblob.tfspellpack.registry.TFSPSpells;
import electroblob.tfspellpack.spell.SuffocatingMist;
import electroblob.tfspellpack.util.TFSPParticles;
import electroblob.wizardry.entity.construct.EntityMagicConstruct;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import java.util.List;

public class EntityDarkMistCloud extends EntityMagicConstruct {

	public EntityDarkMistCloud(World world){
		super(world);
		this.height = TFSPSpells.suffocating_mist.getProperty(Spell.EFFECT_RADIUS).floatValue() * 2;
		this.width = TFSPSpells.suffocating_mist.getProperty(Spell.EFFECT_RADIUS).floatValue() * 2;
	}

	@Override
	public void onUpdate(){

		super.onUpdate();

		if(this.ticksExisted % 100 == 1){
			this.playSound(TFSPSounds.ENTITY_DARK_MIST_AMBIENT, 4.0f, 0.5f + rand.nextFloat());
		}

		if(world.isRemote && ticksExisted % 8 == 0 && ticksExisted < lifetime - 64){

			float footprint = width * width;

			for(int i = 0; i < footprint * 0.05f; i++){ // 1 particle per block per second
				ParticleBuilder.create(TFSPParticles.DARK_MIST).pos(this.posX + this.width * (rand.nextDouble() - 0.5),
						this.posY + this.height/2, this.posZ + this.width * (rand.nextDouble() - 0.5)).scale(height).spawn(world);
			}
		}

		if(!this.world.isRemote){

			List<EntityLivingBase> targets = world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox());

			float damage = TFSPSpells.suffocating_mist.getProperty(Spell.DAMAGE).floatValue() * this.damageMultiplier;

			for(EntityLivingBase target : targets){

				if(this.isValidTarget(target) && !MagicDamage.isEntityImmune(MagicDamage.DamageType.FIRE, target)){

					if(target.ticksExisted % 30 == 0){ // Use target's lifetime so they don't all get hit at once, looks better

						EntityUtils.attackEntityWithoutKnockback(target, MagicDamage.causeIndirectMagicDamage(this,
								this.getCaster(), MagicDamage.DamageType.MAGIC), damage);

						target.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS,
								TFSPSpells.suffocating_mist.getProperty(SuffocatingMist.BLINDNESS_DURATION).intValue(), 0));

						target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS,
								TFSPSpells.suffocating_mist.getProperty(SuffocatingMist.SLOWNESS_DURATION).intValue(),
								TFSPSpells.suffocating_mist.getProperty(SuffocatingMist.SLOWNESS_STRENGTH).intValue()));
					}
				}
			}
		}

	}

}
