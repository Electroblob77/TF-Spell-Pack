package electroblob.tfspellpack.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import twilightforest.entity.boss.EntityTFHydraMortar;

public class EntityMagicHydraMortar extends EntityTFHydraMortar {

	public EntityMagicHydraMortar(World world){
		super(world);
	}

	public EntityMagicHydraMortar(World world, EntityLivingBase caster){
		this(world);
		this.setPosition(caster.posX, caster.posY + caster.getEyeHeight() - 0.1, caster.posZ);
		this.thrower = caster;
	}

}
