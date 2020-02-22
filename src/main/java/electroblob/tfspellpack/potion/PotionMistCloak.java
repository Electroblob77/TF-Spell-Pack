package electroblob.tfspellpack.potion;

import com.google.common.collect.Streams;
import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.registry.TFSPPotions;
import electroblob.wizardry.item.ItemWizardArmour;
import electroblob.wizardry.potion.ISyncedPotion;
import electroblob.wizardry.potion.PotionMagicEffect;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class PotionMistCloak extends PotionMagicEffect implements ISyncedPotion {

	public PotionMistCloak(){
		super(false, 0x293134, new ResourceLocation(TFSpellPack.MODID,
				"textures/gui/potion_icon_mist_cloak.png"));
		this.setBeneficial();
	}

	@SubscribeEvent
	public static void onLivingSetAttackTargetEvent(LivingSetAttackTargetEvent event){
		// Similar to the method used in ItemWizardArmour, but the other way round
		if(event.getTarget() != null && event.getEntityLiving() instanceof EntityLiving
				&& event.getTarget().isPotionActive(TFSPPotions.mist_cloak)){

			int armourPieces = (int)Streams.stream(event.getTarget().getArmorInventoryList())
					.filter(s -> !s.isEmpty() && !(s.getItem() instanceof ItemWizardArmour)).count();

			// Repeat the calculation from EntityAIFindNearestPlayer, but ignoring wizard armour
			IAttributeInstance attribute = event.getEntityLiving().getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
			double followRange = attribute == null ? 16 : attribute.getAttributeValue();
			if(event.getTarget().isSneaking()) followRange *= 0.8;
			float f = armourPieces / 4f; // Let's just hardcode it, everything will always have 4 armour slots (right?)

			float smoky = event.getTarget().getBrightness() * 2 + 0.6f;
			smoky /= event.getTarget().getActivePotionEffect(TFSPPotions.mist_cloak).getAmplifier() + 1;
			f = Math.min(smoky + f, 1);

			if(f < 0.1F) f = 0.1F;
			followRange *= 0.7F * f;
			// Don't need to worry about the isSuitableTarget check since it must already have been checked to get this far
			if(event.getTarget().getDistance(event.getEntity()) > followRange) ((EntityLiving)event.getEntityLiving()).setAttackTarget(null);
		}
	}

}
