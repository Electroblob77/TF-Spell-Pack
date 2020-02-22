package electroblob.tfspellpack.client;

import electroblob.tfspellpack.registry.TFSPPotions;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;

@Mod.EventBusSubscriber(Side.CLIENT)
public class TFSPClientEventHandler {

	private TFSPClientEventHandler(){} // No instances!

	@SubscribeEvent
	public static void onRenderLivingPreEvent(RenderLivingEvent.Pre<EntityLivingBase> event){

		if(event.getEntity().isPotionActive(TFSPPotions.mist_cloak)){

			GlStateManager.enableBlend();
			GlStateManager.disableAlpha();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			// Fiddled with the numbers a bit because player skins are normally brighter than a mistwolf, and not
			// everywhere is dark forest!
			float misty = event.getEntity().getBrightness() * 2 + 0.25f;
			misty /= event.getEntity().getActivePotionEffect(TFSPPotions.mist_cloak).getAmplifier() + 1.5f;
			misty = Math.min(1, misty);

			float smoky = event.getEntity().getBrightness() * 2 + 0.5f;
			smoky /= event.getEntity().getActivePotionEffect(TFSPPotions.mist_cloak).getAmplifier() + 1.5f;

			GlStateManager.color(misty, misty, misty, smoky);
		}
	}

	@SubscribeEvent
	public static void onRenderLivingPostEvent(RenderLivingEvent.Post<EntityLivingBase> event){

		if(event.getEntity().isPotionActive(TFSPPotions.mist_cloak)){
			GlStateManager.disableBlend();
			GlStateManager.enableAlpha();
			GlStateManager.color(1, 1, 1, 1);
		}
	}

}
