package electroblob.tfspellpack.entity.construct;

import electroblob.tfspellpack.entity.projectile.EntityTear;
import electroblob.wizardry.entity.construct.EntityMagicConstruct;
import electroblob.wizardry.entity.projectile.EntityIceShard;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityTearRain extends EntityMagicConstruct {

	public EntityTearRain(World world){
		super(world);
		this.height = 3.0f;
		this.width = 5.0f;
	}

	public void onUpdate(){

		super.onUpdate();

		if(!this.world.isRemote){
			EntityTear tear = new EntityTear(world);
			tear.setPosition(this.posX + rand.nextDouble() * 6 - 3, this.posY + rand.nextDouble() * 4 - 2,
					this.posZ + rand.nextDouble() * 6 - 3);
			// The tears fall from stationary
			tear.setCaster(this.getCaster());
			tear.damageMultiplier = this.damageMultiplier;
			this.world.spawnEntity(tear);
		}
	}

}
