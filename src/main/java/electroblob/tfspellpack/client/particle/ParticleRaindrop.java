package electroblob.tfspellpack.client.particle;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.wizardry.client.particle.ParticleWizardry;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ParticleRaindrop extends ParticleWizardry {

	private static final ResourceLocation TEXTURE = new ResourceLocation(TFSpellPack.MODID, "particle/raindrop");

	public ParticleRaindrop(World world, double x, double y, double z){
		
		super(world, x, y, z, TEXTURE);
		
		this.setRBGColorF(0.25f, 0.75f, 0.75f);
		this.particleMaxAge = 48 + this.rand.nextInt(12);
		this.particleScale *= 1.25f;
		this.setGravity(true);
		this.canCollide = true;
		this.shaded = true;
	}

	@Override
	public void onUpdate(){
		super.onUpdate();
		if(this.onGround) this.isExpired = true;
	}

	@Override
	public void renderParticle(BufferBuilder buffer, Entity viewer, float partialTicks, float lookZ, float lookY, float lookX, float lookXY, float lookYZ){
		// Removes pitch rotation so it just rotates horizontally (yaw) to face the player
		// Pitch is zero, lookY = cos(pitch) therefore lookY = cos(0) = 1
		// Similarly, lookXY and lookYZ both involve sin(pitch), sin(0) = 0 therefore lookXY = lookXZ = 0
		super.renderParticle(buffer, viewer, partialTicks, lookZ, 1, lookX, 0, 0);
	}

	@SubscribeEvent
	public static void onTextureStitchEvent(TextureStitchEvent.Pre event){
		event.getMap().registerSprite(TEXTURE);
	}

}
