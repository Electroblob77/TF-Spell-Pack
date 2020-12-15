package electroblob.tfspellpack.client.renderer;

import electroblob.tfspellpack.registry.TFSPSpells;
import electroblob.wizardry.util.EntityUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;

@Mod.EventBusSubscriber(Side.CLIENT)
public class RenderIceShield {

	public static final float ROTATION_SPEED = 0.09f;
	public static final float RADIUS = 1;

	// First person
	@SubscribeEvent
	public static void onRenderWorldLastEvent(RenderWorldLastEvent event){
		// Only render in first person
		if(Minecraft.getMinecraft().gameSettings.thirdPersonView == 0){

			EntityPlayer player = Minecraft.getMinecraft().player;

			if(EntityUtils.isCasting(player, TFSPSpells.chariot_of_ice)){
				int i = player.getBrightnessForRender();
				int j = i % 65536;
				int k = i / 65536;
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
				render(player, event.getPartialTicks(), true);
			}
		}
	}

	// Third person
	@SubscribeEvent
	public static void onRenderPlayerEvent(RenderPlayerEvent.Post event){

		EntityPlayer player = event.getEntityPlayer();

		if(EntityUtils.isCasting(player, TFSPSpells.chariot_of_ice)){

			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();

			Vec3d delta = player.getPositionEyes(event.getPartialRenderTick())
					.subtract(Minecraft.getMinecraft().player.getPositionEyes(event.getPartialRenderTick()));
			GlStateManager.translate(delta.x, delta.y, delta.z);

			render(player, event.getPartialRenderTick(), false);

			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}
	}

	private static void render(EntityPlayer player, float partialTicks, boolean transparent){

		IBlockState iblockstate = transparent ? Blocks.ICE.getDefaultState() : Blocks.PACKED_ICE.getDefaultState();

		World world = player.world;

		if(iblockstate != world.getBlockState(new BlockPos(player)) && iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE){

			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			GlStateManager.pushMatrix();
			if(transparent){
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			}

			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();

			BlockPos blockpos = new BlockPos(player.posX, player.getEntityBoundingBox().maxY, player.posZ);
			GlStateManager.translate((float)(-(double)blockpos.getX() - 0.5D), (float)(-(double)blockpos.getY()), (float)(-(double)blockpos.getZ() - 0.5D));

			BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();

			bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
			blockrendererdispatcher.getBlockModelRenderer().renderModel(world, blockrendererdispatcher.getModelForState(iblockstate), iblockstate, blockpos.down(), bufferbuilder, false, MathHelper.getPositionRandom(BlockPos.ORIGIN));
			tessellator.draw();

			for(float theta = 0; theta < Math.PI * 2 - 0.1; theta += Math.PI/3){ // -0.1 to avoid doubling up at 360deg

				float angle = theta + (player.ticksExisted + partialTicks) * ROTATION_SPEED;

				GlStateManager.pushMatrix();
				GlStateManager.translate(RADIUS * MathHelper.cos(angle), 0.1, RADIUS * MathHelper.sin(angle));

				bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
				blockrendererdispatcher.getBlockModelRenderer().renderModel(world, blockrendererdispatcher.getModelForState(iblockstate), iblockstate, blockpos, bufferbuilder, false, MathHelper.getPositionRandom(BlockPos.ORIGIN));
				tessellator.draw();

				GlStateManager.popMatrix();
			}

			if(transparent) GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}
	}

}
