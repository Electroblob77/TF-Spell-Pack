package electroblob.tfspellpack;

import electroblob.tfspellpack.util.ContainerPortableUncrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import twilightforest.client.GuiTFGoblinCrafting;

import javax.annotation.Nullable;

public class TFSPGuiHandler implements IGuiHandler {

	public static final int PORTABLE_UNCRAFTING = 0;

	@Nullable
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z){
		if(id == PORTABLE_UNCRAFTING) return new ContainerPortableUncrafting(player.inventory, world, x, y, z);
		return null;
	}

	@Nullable
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z){
		if(id == PORTABLE_UNCRAFTING) return new GuiTFGoblinCrafting(player.inventory, world, x, y, z);
		return null;
	}
}
