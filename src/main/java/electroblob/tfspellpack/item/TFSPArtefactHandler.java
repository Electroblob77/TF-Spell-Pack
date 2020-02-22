package electroblob.tfspellpack.item;

import electroblob.tfspellpack.registry.TFSPItems;
import electroblob.tfspellpack.registry.TFSPPotions;
import electroblob.wizardry.event.SpellCastEvent;
import electroblob.wizardry.integration.DamageSafetyChecker;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.item.ItemWizardArmour;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import twilightforest.TFConfig;
import twilightforest.entity.EntityTFTomeBolt;

import java.util.Arrays;

@Mod.EventBusSubscriber
public class TFSPArtefactHandler {

	private TFSPArtefactHandler(){} // No instances!

	private static final Potion[] ACCURSED_TOME_EFFECTS = {MobEffects.POISON, MobEffects.BLINDNESS, MobEffects.HUNGER,
			MobEffects.NAUSEA, MobEffects.LEVITATION, MobEffects.MINING_FATIGUE, MobEffects.WEAKNESS, MobEffects.SLOWNESS};

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onSpellCastPreEvent(SpellCastEvent.Pre event){

		if(event.getCaster() instanceof EntityPlayer){

			EntityPlayer player = (EntityPlayer)event.getCaster();
			SpellModifiers modifiers = event.getModifiers();

			for(ItemArtefact artefact : ItemArtefact.getActiveArtefacts(player)){

				float potency = modifiers.get("potency");

				if(artefact == TFSPItems.ring_twilight){

					if(player.dimension == TFConfig.dimension.dimensionID){
						modifiers.set("potency", 1.1F * potency, false);
					}
				}

			}
		}

	}

	@SubscribeEvent
	public static void onLivingAttackEvent(LivingAttackEvent event){

		if(event.getSource() != null && event.getSource().getTrueSource() instanceof EntityPlayer){

			EntityPlayer player = (EntityPlayer)event.getSource().getTrueSource();

			for(ItemArtefact artefact : ItemArtefact.getActiveArtefacts(player)){

				if(artefact == TFSPItems.ring_stealth_attack){

					if(!event.getSource().isMagicDamage() && player.isPotionActive(TFSPPotions.mist_cloak)
							&& player.getBrightness() < 0.5f && player.getLastAttackedEntity() != event.getEntity()
							&& (!(event.getEntityLiving() instanceof EntityLiving)
							// Mobs must not have noticed the player for the effect to trigger
							|| ((EntityLiving)event.getEntityLiving()).getAttackTarget() != player)){
						// Rather than cancel the event, just add some extra damage on and make it magic so it doesn't
						// cause an infinite loop
						// Also reset the hurt resistant time and don't apply knockback twice
						DamageSafetyChecker.attackEntitySafely(event.getEntity(), MagicDamage.causeDirectMagicDamage(
								player, MagicDamage.DamageType.MAGIC), event.getAmount() * 0.5f,
								event.getSource().damageType, DamageSource.MAGIC, false);
						event.getEntityLiving().hurtResistantTime = 0;
					}

				}

			}
		}

	}

	@SubscribeEvent
	public static void onLivingHurtEvent(LivingHurtEvent event){

		if(event.getEntity() instanceof EntityPlayer){

			EntityPlayer player = (EntityPlayer)event.getEntity();

			for(ItemArtefact artefact : ItemArtefact.getActiveArtefacts(player)){

				if(artefact == TFSPItems.amulet_steeleaf){

					if(player.dimension == TFConfig.dimension.dimensionID && Arrays.stream(WizardryUtilities.ARMOUR_SLOTS)
							.map(s -> player.getItemStackFromSlot(s).getItem())
							.allMatch(i -> i instanceof ItemWizardArmour)){
						event.setAmount(event.getAmount() * 0.9f);
					}

				}

			}
		}

		if(event.getSource() != null && event.getSource().getTrueSource() instanceof EntityPlayer){

			EntityPlayer player = (EntityPlayer)event.getSource().getTrueSource();

			for(ItemArtefact artefact : ItemArtefact.getActiveArtefacts(player)){

				// Sure, attack effects should be rings but I don't care, the lore is nice - besides, this is an addon!
				if(artefact == TFSPItems.charm_accursed_tome){

					if(event.getSource().getImmediateSource() instanceof EntityTFTomeBolt){
						Potion potion = ACCURSED_TOME_EFFECTS[player.getRNG().nextInt(ACCURSED_TOME_EFFECTS.length)];
						event.getEntityLiving().addPotionEffect(new PotionEffect(potion, 160, 0));
					}
				}

			}
		}

	}

}
