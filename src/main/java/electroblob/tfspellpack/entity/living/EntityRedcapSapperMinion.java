package electroblob.tfspellpack.entity.living;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import twilightforest.entity.ai.EntityAITFRedcapPlantTNT;
import twilightforest.item.TFItems;

public class EntityRedcapSapperMinion extends EntityRedcapMinion {

	/** Creates a new redcap sapper minion in the given world. */
	public EntityRedcapSapperMinion(World world){
		super(world);
		this.heldPick = new ItemStack(TFItems.ironwood_pickaxe);
		this.heldTNT.setCount(3);
	}

	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		super.setEquipmentBasedOnDifficulty(difficulty);
		this.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(TFItems.ironwood_boots));
	}

	@Override
	protected void initEntityAI() {
		super.initEntityAI();
		this.tasks.addTask(4, new EntityAITFRedcapPlantTNT(this));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
	}

}