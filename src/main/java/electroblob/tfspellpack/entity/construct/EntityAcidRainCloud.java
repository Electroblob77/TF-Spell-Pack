package electroblob.tfspellpack.entity.construct;

import electroblob.tfspellpack.registry.TFSPItems;
import electroblob.tfspellpack.registry.TFSPSounds;
import electroblob.tfspellpack.registry.TFSPSpells;
import electroblob.tfspellpack.util.TFSPParticles;
import electroblob.wizardry.entity.construct.EntityMagicConstruct;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.EntityUtils;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import java.util.List;

public class EntityAcidRainCloud extends EntityMagicConstruct {

	private static final int DROPS_PER_TICK = 15;
	private static final int FADE_TICKS = 30;

//	private static final DataParameter<Float> RADIUS = new DataParameter<>(21, DataSerializers.FLOAT);

	public EntityAcidRainCloud(World world){
		super(world);
		this.height = 2.0f;
		this.width = TFSPSpells.acid_rain.getProperty(Spell.EFFECT_RADIUS).floatValue() * 2;
	}

//	@Override
//	protected void entityInit(){
//		super.entityInit();
//		this.getDataManager().register(RADIUS, width); // TODO: This doesn't work, implement blast modifiers properly into SpellConstruct
//	}

//	public void multiplyWidth(float multiplier){
//		this.setSize(width * multiplier, height);
//		this.getDataManager().set(RADIUS, width);
//	}

	public void onUpdate(){

		super.onUpdate();

//		if(world.isRemote){
//			float radius = this.getDataManager().get(RADIUS);
//			if(radius != width) this.setSize(radius, height);
//		}else{
//			if(this.getDataManager().get(RADIUS) != width) this.getDataManager().set(RADIUS, width);
//		}

		if(this.ticksExisted % 35 == 0) this.playSound(TFSPSounds.ENTITY_ACID_RAIN_AMBIENT, 1, 1);

		if(this.world.isRemote){

			float areaFactor = (width * width) / 36; // Ensures cloud/raindrop density stays the same for different sizes

			for(int i = 0; i < 2 * areaFactor; i++) ParticleBuilder.create(TFSPParticles.CLOUD, this)
					.clr(0.3f, 0.3f, 0.3f).shaded(true).spawn(world);

			float dropsPerTick = areaFactor * DROPS_PER_TICK;
			// Rain gets heavier at the start and lighter at the end
			int n = Math.round(Math.min(FADE_TICKS, Math.min(ticksExisted, lifetime - ticksExisted)) / (FADE_TICKS / dropsPerTick));
			for(int i=0; i<n; i++) ParticleBuilder.create(TFSPParticles.RAINDROP)
					.pos(posX + (rand.nextDouble() - 0.5) * width, posY - rand.nextDouble(), posZ + (rand.nextDouble() - 0.5) * width)
					.vel(0, -0.3, 0).gravity(true).shaded(true).spawn(world);
		}

		List<EntityLivingBase> targets = world.getEntitiesWithinAABB(EntityLivingBase.class,
				this.getEntityBoundingBox().expand(0, -10, 0));

		float damage = TFSPSpells.acid_rain.getProperty(Spell.DAMAGE).floatValue() * this.damageMultiplier;

		for(EntityLivingBase target : targets){

			target.extinguish(); // We might be burning them with toxic rain but at least it puts the fire out!

			if(this.isValidTarget(target)){

				if(target.ticksExisted % 20 == 0){ // Use target's lifetime so they don't all get hit at once, looks better

					if(!this.world.isRemote){
						EntityUtils.attackEntityWithoutKnockback(target, MagicDamage.causeIndirectMagicDamage(this,
								this.getCaster(), MagicDamage.DamageType.MAGIC), damage);
						if(getCaster() instanceof EntityPlayer && ItemArtefact.isArtefactActive((EntityPlayer)getCaster(),
								TFSPItems.ring_trollsteinn)){
							target.addPotionEffect(new PotionEffect(MobEffects.POISON, 200, 1));
						}
					}

					target.playSound(TFSPSounds.ENTITY_ACID_RAIN_ATTACK, 1.2f, 1);
				}
			}
		}

//		BlockPos pos = new BlockPos(this);
//
//		for(int x = -(int)(this.width/2); x <= this.width/2 + 0.5; x++){
//			for(int z = -(int)(this.width/2); z <= this.width/2 + 0.5; z++){
//
//				int y = WizardryUtilities.getNearestFloor()
//
//			}
//		}

	}

}
