package electroblob.tfspellpack.util;

import electroblob.tfspellpack.TFSpellPack;
import net.minecraft.util.ResourceLocation;

public class TFSPParticles {

	private TFSPParticles(){} // No instances!

	/** 3D-rendered darkness-beam particle.<p></p><b>Defaults:</b><br>Lifetime: 1 tick<br> Colour: white */
	public static final ResourceLocation DARK_BEAM = new ResourceLocation(TFSpellPack.MODID,"dark_beam");
	/** Large, thick cloud.<p></p><b>Defaults:</b><br>Lifetime: 48-60 ticks<br> Colour: dark grey */
	public static final ResourceLocation CLOUD = new ResourceLocation(TFSpellPack.MODID,"cloud");
	/** Raindrop; always renders vertically.<p></p><b>Defaults:</b><br>Lifetime: 48-60 ticks<br> Colour: blue-green */
	public static final ResourceLocation RAINDROP = new ResourceLocation(TFSpellPack.MODID,"raindrop");
	/** Dark mist; always renders vertically.<p></p><b>Defaults:</b><br>Lifetime: 48-60 ticks<br> Colour: black */
	public static final ResourceLocation DARK_MIST = new ResourceLocation(TFSpellPack.MODID,"dark_mist");

}
