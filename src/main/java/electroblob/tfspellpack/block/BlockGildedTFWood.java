package electroblob.tfspellpack.block;

import electroblob.wizardry.registry.WizardryTabs;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockGildedTFWood extends Block {

	public BlockGildedTFWood(){
		super(Material.WOOD);
		this.setCreativeTab(WizardryTabs.WIZARDRY);
		this.setHardness(2.0F);
		this.setResistance(5.0F);
		this.setSoundType(SoundType.WOOD); // Why is this protected?!
	}

}