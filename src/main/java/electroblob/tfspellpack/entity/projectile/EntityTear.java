package electroblob.tfspellpack.entity.projectile;

import electroblob.tfspellpack.registry.TFSPSounds;
import electroblob.tfspellpack.registry.TFSPSpells;
import electroblob.wizardry.entity.projectile.EntityMagicProjectile;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.ParticleBuilder.Type;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityTear extends EntityMagicProjectile {

	public EntityTear(World world){
		super(world);
	}

	@Override
	public int getLifetime(){
		return -1;
	}

	@Override
	protected void onImpact(RayTraceResult result){

		if(result.entityHit instanceof EntityLivingBase){

			float damage = TFSPSpells.rain_of_tears.getProperty(Spell.DAMAGE).floatValue() * this.damageMultiplier;

			EntityUtils.attackEntityWithoutKnockback(result.entityHit, MagicDamage.causeIndirectMagicDamage(
					this, this.thrower, MagicDamage.DamageType.FIRE), damage);

			result.entityHit.setFire(TFSPSpells.rain_of_tears.getProperty(Spell.BURN_DURATION).intValue());
		}

		if(world.isRemote){
			for(int i=0; i<5; i++){
				world.spawnParticle(EnumParticleTypes.WATER_SPLASH, posX + rand.nextFloat() - 0.5, posY,
						posZ + rand.nextFloat() - 0.5, 0, 0, 0);
				ParticleBuilder.create(Type.MAGIC_FIRE, rand, posX, posY, posZ, 0.5, false).scale(1.5f).time(8).spawn(world);
			}
		}

		this.playSound(TFSPSounds.ENTITY_FIERY_TEAR_FIRE, 1, 1);
		this.playSound(TFSPSounds.ENTITY_FIERY_TEAR_SPLASH, 1, 1);

		this.setDead();

	}

	@Override
	public void onUpdate(){
		super.onUpdate();
		if(world.isRemote) ParticleBuilder.create(Type.MAGIC_FIRE, this).spawn(world);
	}

	@Override
	public boolean canRenderOnFire(){
		return true; // These are fiery ur-ghast tears!
	}

}
