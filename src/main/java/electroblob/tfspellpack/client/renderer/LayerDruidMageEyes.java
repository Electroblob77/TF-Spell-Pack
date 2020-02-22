package electroblob.tfspellpack.client.renderer;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.entity.living.EntityDruidMage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class LayerDruidMageEyes<T extends EntityDruidMage> implements LayerRenderer<T> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(TFSpellPack.MODID, "textures/entity/druid_mage_eyes.png");

	private final RenderDruidMage mainRenderer;

	public LayerDruidMageEyes(RenderDruidMage renderer){
		this.mainRenderer = renderer;
	}

	@Override
	public void doRenderLayer(T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale){

		// Copied from LayerSpiderEyes

		this.mainRenderer.bindTexture(TEXTURE);

		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.depthMask(!entity.isInvisible());

		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		Minecraft.getMinecraft().entityRenderer.setupFogColor(true);

		this.mainRenderer.getMainModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

		Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
		int i = entity.getBrightnessForRender();
		int j = i % 65536;
		int k = i / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);

		this.mainRenderer.setLightmap(entity);

		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
	}

	@Override
	public boolean shouldCombineTextures(){
		return false;
	}

}
