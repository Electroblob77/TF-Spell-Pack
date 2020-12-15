package electroblob.tfspellpack.client;

import electroblob.tfspellpack.CommonProxy;
import electroblob.tfspellpack.client.particle.ParticleCloud;
import electroblob.tfspellpack.client.particle.ParticleDarkBeam;
import electroblob.tfspellpack.client.particle.ParticleDarkMist;
import electroblob.tfspellpack.client.particle.ParticleRaindrop;
import electroblob.tfspellpack.client.renderer.RenderDruidMage;
import electroblob.tfspellpack.entity.construct.*;
import electroblob.tfspellpack.entity.living.EntityDruidMage;
import electroblob.tfspellpack.entity.living.EntityRedcapSapperMinion;
import electroblob.tfspellpack.entity.projectile.EntityTear;
import electroblob.tfspellpack.util.TFSPParticles;
import electroblob.wizardry.client.particle.ParticleWizardry;
import electroblob.wizardry.client.renderer.entity.RenderBlank;
import electroblob.wizardry.client.renderer.entity.RenderProjectile;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import twilightforest.client.model.entity.ModelTFRedcap;
import twilightforest.client.renderer.entity.RenderTFBiped;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerParticles(){
		ParticleWizardry.registerParticle(TFSPParticles.DARK_BEAM, ParticleDarkBeam::new);
		ParticleWizardry.registerParticle(TFSPParticles.CLOUD, ParticleCloud::new);
		ParticleWizardry.registerParticle(TFSPParticles.RAINDROP, ParticleRaindrop::new);
		ParticleWizardry.registerParticle(TFSPParticles.DARK_MIST, ParticleDarkMist::new);
	}

	@Override
	public void registerRenderers(){

		RenderingRegistry.registerEntityRenderingHandler(EntityTear.class, manager -> new RenderProjectile(manager,
				1.2f, new ResourceLocation("minecraft:textures/items/ghast_tear.png"), false));

		RenderingRegistry.registerEntityRenderingHandler(EntityTearRain.class, RenderBlank::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityAcidRainCloud.class, RenderBlank::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCinderCloud.class, RenderBlank::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityFireJet.class, RenderBlank::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDarkMistCloud.class, RenderBlank::new);

		// Much like husk minions in the main wizardry mod, we need to bind this ourselves
		RenderingRegistry.registerEntityRenderingHandler(EntityRedcapSapperMinion.class, m -> new RenderTFBiped<>(m, new ModelTFRedcap(), 0.4F, "redcapsapper.png"));
		RenderingRegistry.registerEntityRenderingHandler(EntityDruidMage.class, RenderDruidMage::new);

	}
}
