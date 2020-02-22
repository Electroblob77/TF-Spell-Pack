package electroblob.tfspellpack.entity.living;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITarget;

import java.util.Collections;
import java.util.List;

// Copy of Twilight Forest's copy of vanilla's EntityAIFindEntityNearestPlayer (yeah...), but with livingbase targets
// and an assignable target selector. This effectively mirrors the behaviour of EntityNearestAttackableTarget, but for
// entities that don't extend EntityCreature. (Unfortunately TF has perpetuated the exact same problem that caused them
// to copy the above class the first place, by making everything private)
public class EntityAIFindNearestTarget extends EntityAIFindEntityNearestPlayer {

	// Nobody's ever going to extend this but I'm making these protected out of principle!
	protected final EntityLiving host;
	protected final Predicate<Entity> filter;
	protected final EntityAINearestAttackableTarget.Sorter sorter;
	protected EntityLivingBase target;

	public EntityAIFindNearestTarget(EntityLiving host, Predicate<Entity> targetSelector){

		super(host);
		this.host = host;

		this.filter = target -> {
			double maxRange = EntityAIFindNearestTarget.this.maxTargetRange();
			if(targetSelector != null && !targetSelector.apply(target)) return false;
			return !((double)target.getDistance(EntityAIFindNearestTarget.this.host) > maxRange)
					&& EntityAITarget.isSuitableTarget(EntityAIFindNearestTarget.this.host, (EntityLivingBase)target, false, false);
		};

		this.sorter = new EntityAINearestAttackableTarget.Sorter(host);
	}

	@Override
	public boolean shouldExecute(){

		double maxRange = this.maxTargetRange();
		List<EntityLivingBase> list = this.host.world.getEntitiesWithinAABB(EntityLivingBase.class, this.host.getEntityBoundingBox().grow(maxRange), this.filter);
		Collections.sort(list, this.sorter);

		if(list.isEmpty()){
			return false;
		}else{
			this.target = list.get(0);
			return true;
		}
	}

	@Override
	public void startExecuting(){
		this.host.setAttackTarget(this.target);
	}

	@Override
	public void resetTask(){
		this.target = null;
		super.resetTask();
	}

}
