package electroblob.tfspellpack.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;
import twilightforest.inventory.ContainerTFUncrafting;

public class ContainerPortableUncrafting extends ContainerTFUncrafting {

	public ContainerPortableUncrafting(InventoryPlayer inventory, World world, int x, int y, int z){
		super(inventory, world, x, y, z);
	}

	// Overridden to stop the uncrafting gui from closing when there is no uncrafting table.
	@Override
	public boolean canInteractWith(EntityPlayer player){
		return true;
	}

}
