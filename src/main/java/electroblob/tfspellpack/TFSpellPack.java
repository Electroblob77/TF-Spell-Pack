package electroblob.tfspellpack;

import electroblob.tfspellpack.registry.TFSPItems;
import electroblob.tfspellpack.registry.TFSPLoot;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;

/**
 * <i>"The official Twilight Forest addon for Electroblob's Wizardry, adding spells and items for all things twilight!"</i>
 * <p></p>
 * Main mod class for the Twilight Forest Spell Pack. Contains the logger and settings instances, along with all the
 * other stuff that's normally in a main mod class.
 * @author Electroblob
 * @since TF Spell Pack 1.0
 */
@Mod(modid = TFSpellPack.MODID, name = TFSpellPack.NAME, version = TFSpellPack.VERSION, acceptedMinecraftVersions = "[1.12.2]",
		dependencies = "required-after:forge@[14.23.5.2814,);required-after:ebwizardry@[4.3.1,4.4);required-after:twilightforest@[3.11.1021,)")

public class TFSpellPack {

    public static final String MODID = "tfspellpack";
    public static final String NAME = "Electroblob's Wizardry: Twilight Forest Spell Pack";
    public static final String VERSION = "1.0.2";

    public static Logger logger;

    // The instance of tf spell pack that Forge uses.
    @Mod.Instance(TFSpellPack.MODID)
    public static TFSpellPack instance;

    // Location of the proxy code, used by Forge.
    @SidedProxy(clientSide = "electroblob.tfspellpack.client.ClientProxy", serverSide = "electroblob.tfspellpack.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();
        TFSPLoot.register();
        proxy.registerRenderers();
    }

    @EventHandler
    public void init(FMLInitializationEvent event){
        TFSPItems.registerDispenseBehaviours();
        proxy.registerParticles();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new TFSPGuiHandler());
    }
	
}
