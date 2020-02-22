package electroblob.tfspellpack.entity.construct;

import electroblob.tfspellpack.registry.TFSPSounds;
import electroblob.tfspellpack.registry.TFSPSpells;
import electroblob.wizardry.entity.construct.EntityMagicConstruct;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.MagicDamage.DamageType;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;
import twilightforest.client.particle.TFParticleType;

import java.util.List;

public class EntityFireJet extends EntityMagicConstruct {

	public EntityFireJet(World world){
		super(world);
		this.setSize(4, 4); // Should mimic fire jet behaviour
	}

	public void onUpdate(){

		super.onUpdate();

		if(this.ticksExisted % 4 == 0){
			this.playSound(TFSPSounds.ENTITY_FIRE_JET_FIRE, 1 + rand.nextFloat(), rand.nextFloat() * 0.7f + 0.3f);
		}

		if(this.world.isRemote){
			// Mainly taken from TileEntityTFFlameJet
			if(this.ticksExisted % 2 == 0) {

				world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + 0.5, this.posY + 1, this.posZ + 0.5, 0, 0, 0);

				TwilightForestMod.proxy.spawnParticle(TFParticleType.LARGE_FLAME, posX + 0.5, posY + 1, posZ + 0.5, 0, 0.5, 0);
				TwilightForestMod.proxy.spawnParticle(TFParticleType.LARGE_FLAME, posX - 0.5, posY + 1, posZ + 0.5, 0.05, 0.5, 0);
				TwilightForestMod.proxy.spawnParticle(TFParticleType.LARGE_FLAME, posX + 0.5, posY + 1, posZ - 0.5, 0, 0.5, 0.05);
				TwilightForestMod.proxy.spawnParticle(TFParticleType.LARGE_FLAME, posX + 1.5, posY + 1, posZ + 0.5, -0.05, 0.5, 0);
				TwilightForestMod.proxy.spawnParticle(TFParticleType.LARGE_FLAME, posX + 0.5, posY + 1, posZ + 1.5, 0, 0.5, -0.05);
			}

		}else{

			if(this.ticksExisted % 5 == 0){

				List<Entity> targets = world.getEntitiesWithinAABB(Entity.class, this.getEntityBoundingBox());

				for(Entity target : targets){
					if(this.isValidTarget(target) && !MagicDamage.isEntityImmune(MagicDamage.DamageType.FIRE, target)){
						float damage = TFSPSpells.fire_jets.getProperty(Spell.DAMAGE).floatValue() * damageMultiplier;
						WizardryUtilities.attackEntityWithoutKnockback(target, MagicDamage.causeIndirectMagicDamage(this, getCaster(), DamageType.FIRE), damage);
						target.setFire(TFSPSpells.fire_jets.getProperty(Spell.BURN_DURATION).intValue());
					}
				}
			}
		}
	}

}
