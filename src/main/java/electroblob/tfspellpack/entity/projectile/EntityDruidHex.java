package electroblob.tfspellpack.entity.projectile;

import electroblob.wizardry.util.BlockUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import twilightforest.entity.EntityTFNatureBolt;

/**
 * Extension of Twilight Forest's nature bolt which respects mob griefing and block protection, since the original
 * turns any block it hits into leaves regardless of the {@code mobGriefing} game rule.
 */
public class EntityDruidHex extends EntityTFNatureBolt {

	public EntityDruidHex(World world){
		super(world);
	}

	public EntityDruidHex(World world, EntityLivingBase caster){
		super(world, caster);
	}

	@Override
	protected void onImpact(RayTraceResult ray){

		if(!world.isRemote && ray.getBlockPos() != null && !BlockUtils.canPlaceBlock(getThrower(), world, ray.getBlockPos())){
			// Blocks may not be modified here (mob griefing is off or the area is protected), so the bolt just fizzles
			world.setEntityState(this, (byte)3);
			this.setDead();
			return;
		}

		super.onImpact(ray);
	}

}
