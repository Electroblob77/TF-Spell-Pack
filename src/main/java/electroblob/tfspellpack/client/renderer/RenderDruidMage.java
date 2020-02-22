package electroblob.tfspellpack.client.renderer;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.client.model.ModelDruidMage;
import electroblob.tfspellpack.entity.living.EntityDruidMage;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderDruidMage extends RenderBiped<EntityDruidMage> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(TFSpellPack.MODID, "textures/entity/druid_mage.png");

	public RenderDruidMage(RenderManager renderManager){
		super(renderManager, new ModelDruidMage(), 0.5F);
		this.addLayer(new LayerDruidMageEyes<>(this));
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityDruidMage entity){
		return TEXTURE;
	}

}