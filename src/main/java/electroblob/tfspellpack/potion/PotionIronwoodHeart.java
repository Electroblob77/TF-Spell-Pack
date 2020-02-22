package electroblob.tfspellpack.potion;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.wizardry.potion.PotionMagicEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class PotionIronwoodHeart extends PotionMagicEffect {

	private static final float REGENERATION_SPEED_INCREASE_PER_LEVEL = 0.5f;

	public PotionIronwoodHeart(){
		super(false, 0x757869, new ResourceLocation(TFSpellPack.MODID,
				"textures/gui/potion_icon_ironwood_heart.png"));
		this.setBeneficial();
	}

	@Override
	public boolean isReady(int duration, int amplifier){
		return true; // Execute the effect every tick
	}

	@Override
	public void performEffect(EntityLivingBase host, int strength){

		if(host instanceof EntityPlayer && host.world.getGameRules().getBoolean("naturalRegeneration")){

			EntityPlayer player = ((EntityPlayer)host);

			if(player.getFoodStats().getFoodLevel() >= 18 && player.shouldHeal()){
				// Fancy maths:
				// - Ironwood Heart I gives 50% extra natural regen = 1 extra update every other tick
				// - Ironwood Heart II gives 100% extra natural regen = 1 extra update every tick
				// - Ironwood Heart III gives 150% extra natural regen = 1 extra update every tick + 1 every other tick
				// - Ironwood Heart IV gives 200% extra natural regen = 2 extra updates every tick
				// ... and so on
				float extraRegeneration = REGENERATION_SPEED_INCREASE_PER_LEVEL * (strength + 1);
				int extraUpdatesEveryTick = (int)extraRegeneration; // Simply round down
				int remainderTicks = Math.round(1/(extraRegeneration - extraUpdatesEveryTick));

				for(int i=0; i<extraUpdatesEveryTick; i++) player.getFoodStats().onUpdate(player);
				if(player.ticksExisted % remainderTicks == 0) player.getFoodStats().onUpdate(player);

			}
		}

	}

}
