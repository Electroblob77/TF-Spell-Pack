package electroblob.tfspellpack.client.particle;

import electroblob.wizardry.client.particle.ParticleWizardry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;
import twilightforest.TwilightForestMod;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ParticleDarkMist extends ParticleWizardry {

	private static final ResourceLocation TEXTURE = new ResourceLocation(TwilightForestMod.ID, "textures/environment/darkstream.png");

	private static final float ASPECT_RATIO = 0.25f;
	private static final float END_FRACTION = 0.2f;

	public ParticleDarkMist(World world, double x, double y, double z){

		super(world, x, y, z);

		this.setRBGColorF(0, 0, 0);
		this.particleScale = 4;
		this.particleAlpha = 0;
		this.particleMaxAge = 128;
		this.setGravity(true);
		this.canCollide = true;
		this.shaded = true;
		this.seed = rand.nextLong();
	}

	@Override
	public boolean shouldDisableDepth(){
		return true;
	}

	@Override
	public int getFXLayer(){
		return 3;
	}

	@Override
	public void onUpdate(){

		super.onUpdate();

		// Fading
		float fadeTime = this.particleMaxAge * 0.3f;
		this.setAlphaF(MathHelper.clamp(Math.min(this.particleAge / fadeTime, (this.particleMaxAge - this.particleAge) / fadeTime), 0, 0.5f));

	}

	@Override
	public void renderParticle(BufferBuilder buffer, Entity viewer, float partialTicks, float lookZ, float lookY, float lookX, float lookXY, float lookYZ){

		// Why does this fix it? No idea!
		Particle.interpPosX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * (double)partialTicks;
		Particle.interpPosY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * (double)partialTicks;
		Particle.interpPosZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * (double)partialTicks;

		random.setSeed(seed);

		lookY = 1;
		lookXY = 0;
		lookYZ = 0;

		// Copied from ParticleWizardry, needs to be here since we're not calling super
		updateEntityLinking(viewer, partialTicks);

		GlStateManager.pushMatrix();

		GlStateManager.enableBlend();
		GlStateManager.shadeModel(GL11.GL_SMOOTH); // Allows the vertex colours to produce a gradient
		GlStateManager.disableCull();
		GlStateManager.disableLighting();
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

		GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

		// Texture translation/wrapping
		GlStateManager.matrixMode(GL11.GL_TEXTURE);
		GlStateManager.loadIdentity();

		float yScale = 0.1f * this.particleScale;
		float xScale = 0.5f;

		GlStateManager.translate(0, (this.particleAge + partialTicks)/(float)this.particleMaxAge * 0.2f + random.nextDouble() * yScale, 0);

		GlStateManager.matrixMode(GL11.GL_MODELVIEW);

		float x = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTicks - interpPosX);
		float y = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTicks - interpPosY);
		float z = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTicks - interpPosZ);

		GlStateManager.translate(x, y, z);

		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);

		int i = this.getBrightnessForRender(partialTicks);
		int j = i >> 16 & 65535;
		int k = i & 65535;

		// Main part
		drawQuad(buffer, lookZ, lookY, lookX, lookXY, lookYZ, yScale, xScale, 0, j, k, particleAlpha, particleAlpha);
		// Top fade out
		drawQuad(buffer, lookZ, lookY, lookX, lookXY, lookYZ, yScale * END_FRACTION, xScale, yScale * (1 + END_FRACTION), j, k, particleAlpha, 0);
		// Bottom fade out
		drawQuad(buffer, lookZ, lookY, lookX, lookXY, lookYZ, yScale * END_FRACTION, xScale, -yScale * (1 + END_FRACTION), j, k, 0, particleAlpha);

		Tessellator.getInstance().draw();

		GlStateManager.disableBlend();
		GlStateManager.enableCull();
		GlStateManager.enableLighting();
		// Undoes the texture transformations
		GlStateManager.matrixMode(GL11.GL_TEXTURE);
		GlStateManager.loadIdentity();
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);

		GlStateManager.popMatrix();
	}

	private void drawQuad(BufferBuilder buffer, float lookZ, float lookY, float lookX, float lookXY, float lookYZ, float yScale, float xScale, float y, int j, int k, float a1, float a2){

		float u1 = 0;
		float u2 = 1;
		float v1 = (y + yScale) * ASPECT_RATIO;
		float v2 = (y - yScale) * ASPECT_RATIO;

		// What the heck is this
		Vec3d[] vec = new Vec3d[] {new Vec3d(-lookZ * xScale - lookXY * xScale, -lookY * yScale, -lookX * xScale - lookYZ * xScale),
				new Vec3d(-lookZ * xScale + lookXY * xScale, lookY * yScale, -lookX * xScale + lookYZ * xScale),
				new Vec3d(lookZ * xScale + lookXY * xScale, lookY * yScale, lookX * xScale + lookYZ * xScale),
				new Vec3d(lookZ * xScale - lookXY * xScale, -lookY * yScale, lookX * xScale - lookYZ * xScale)};

		buffer.pos(vec[0].x, y + vec[0].y, vec[0].z).tex(u2, v2).color(this.particleRed, this.particleGreen, this.particleBlue, a1).lightmap(j, k).endVertex();
		buffer.pos(vec[1].x, y + vec[1].y, vec[1].z).tex(u2, v1).color(this.particleRed, this.particleGreen, this.particleBlue, a2).lightmap(j, k).endVertex();
		buffer.pos(vec[2].x, y + vec[2].y, vec[2].z).tex(u1, v1).color(this.particleRed, this.particleGreen, this.particleBlue, a2).lightmap(j, k).endVertex();
		buffer.pos(vec[3].x, y + vec[3].y, vec[3].z).tex(u1, v2).color(this.particleRed, this.particleGreen, this.particleBlue, a1).lightmap(j, k).endVertex();
	}

}
