package electroblob.tfspellpack;

/**
 * The common proxy for tf spell pack, serving the usual purpose of dealing with all things that need to be handled
 * differently on the client and the server. A lot of the methods here appear to do absolutely nothing; this is because
 * they do client-only things which are only handled in the client proxy.
 *
 * @see electroblob.tfspellpack.client.ClientProxy ClientProxy
 * @author Electroblob
 * @since TF Spell Pack 1.0
 */
public class CommonProxy {

	/** Called from init() in the main mod class to initialise the particle factories. */
	public void registerParticles(){} // Does nothing since particles are client-side only

	/** Called from preInit() in the main mod class to initialise the renderers. */
	public void registerRenderers(){} // Does nothing since renderers are client-side only

}
