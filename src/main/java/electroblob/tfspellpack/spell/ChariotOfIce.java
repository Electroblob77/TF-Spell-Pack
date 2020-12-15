package electroblob.tfspellpack.spell;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.registry.TFSPItems;
import electroblob.tfspellpack.registry.TFSPSpells;
import electroblob.wizardry.Wizardry;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.item.ItemArtefact;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.*;
import electroblob.wizardry.util.MagicDamage.DamageType;
import electroblob.wizardry.util.ParticleBuilder.Type;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerFlyableFallEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import twilightforest.TwilightForestMod;
import twilightforest.client.particle.TFParticleType;

import java.util.List;

@Mod.EventBusSubscriber
public class ChariotOfIce extends Spell {

	public static final String SPEED = "speed";
	public static final String ACCELERATION = "acceleration";
	public static final String HOVER_HEIGHT = "hover_height";
	public static final String MAX_HOVER_HEIGHT = "max_hover_height";
	public static final String KNOCKBACK_STRENGTH = "knockback_strength";
	public static final String MAX_CRUSH_DAMAGE = "max_crush_damage";

	private static final float VERTICAL_MOVEMENT_FACTOR = 0.25f;
	private static final float VERTICAL_ACCELERATION_FACTOR = 0.2f;
	private static final float HOVER_ANIMATION_PERIOD = 5;
	private static final double HOVER_ANIMATION_MAGNITUDE = 0.1;
	private static final float SLAM_HEIGHT_FRACTION = 0.7f;
	private static final float BLOCK_BREAK_RADIUS_FRACTION = 0.6f;
	private static final double SLAM_SPEED = 1.5;

	public ChariotOfIce(){
		super(TFSpellPack.MODID, "chariot_of_ice", EnumAction.NONE, true);
		// N.B. DAMAGE is for sideways hits, MAX_CRUSH_DAMAGE is for ground slam
		addProperties(SPEED, ACCELERATION, HOVER_HEIGHT, MAX_HOVER_HEIGHT, DAMAGE, KNOCKBACK_STRENGTH, BLAST_RADIUS, MAX_CRUSH_DAMAGE);
	}

	@Override
	public boolean applicableForItem(Item item){
		return item == TFSPItems.twilight_spell_book || item == TFSPItems.twilight_scroll;
	}

	@Override
	protected SoundEvent[] createSounds(){
		return createSoundsWithSuffixes("ambient", "hit", "ground_slam", "rumble");
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers){

		float speed = getProperty(SPEED).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		float acceleration = getProperty(ACCELERATION).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		float damage = getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
		float knockback = getProperty(KNOCKBACK_STRENGTH).floatValue();

		// Horizontal movement
		Vec3d hLookVec = GeometryUtils.replaceComponent(caster.getLookVec(), EnumFacing.Axis.Y, 0).normalize();
		double hSpeedSquared = caster.motionX * caster.motionX + caster.motionZ * caster.motionZ;

		if(caster.moveForward != 0){
			float direction = Math.signum(caster.moveForward);
			if(hSpeedSquared < speed * speed){
				caster.addVelocity(direction * hLookVec.x * acceleration, 0, direction * hLookVec.z * acceleration);
			}
		}

		if(caster.moveStrafing != 0){
			float direction = Math.signum(caster.moveStrafing);
			if(hSpeedSquared < speed * speed){
				caster.addVelocity(direction * hLookVec.z * acceleration, 0, direction * -hLookVec.x * acceleration);
			}
		}

		// Can't be velocity-based because player motion is client-side
		List<EntityLivingBase> targets = world.getEntitiesWithinAABB(EntityLivingBase.class,
				caster.getEntityBoundingBox().expand(-1, -1, -1).expand(1, 0, 1));
		targets.remove(caster);

		for(EntityLivingBase target : targets){
			target.attackEntityFrom(MagicDamage.causeDirectMagicDamage(caster, DamageType.FROST), damage);
			target.addVelocity((target.posX - caster.posX) * knockback, 0.2f, (target.posZ - caster.posZ) * knockback);
		}

		if(!targets.isEmpty()) playSound(world, caster, ticksInUse, -1, modifiers, "hit");

		// Vertical movement
		float minHoverHeight = getProperty(HOVER_HEIGHT).floatValue();
		float maxHoverHeight = getProperty(MAX_HOVER_HEIGHT).floatValue();

		if(caster.isSneaking()){ // Ground slam
			caster.motionY = -SLAM_SPEED;

		}else{ // Hover

			if(world.isRemote){
				
				caster.fallDistance = 0; // Prevents cheesing of the ground slam effect

				// FIXME: EntityPlayerSP doesn't exist on the server! Is this supposed to be client-only?
				float hoverHeight = caster instanceof EntityPlayerSP && ((EntityPlayerSP)caster).movementInput.jump
						? maxHoverHeight : minHoverHeight;

				hoverHeight += MathHelper.sin(ticksInUse / HOVER_ANIMATION_PERIOD) * HOVER_ANIMATION_MAGNITUDE; // Floaty

				Integer floor = BlockUtils.getNearestFloor(world, caster.getPosition(), MathHelper.ceil(hoverHeight) + 1);

				if(floor != null){ // If floor is more than 3 blocks away, allow caster to fall normally
					caster.motionY = Math.min(((floor + hoverHeight - caster.posY) * VERTICAL_ACCELERATION_FACTOR),
							speed * VERTICAL_MOVEMENT_FACTOR);
				}
			}
		}

		// Particles
		if(world.isRemote){
			for(int i=0; i<10; i++){
				double x = caster.posX + caster.width * (world.rand.nextDouble() - 0.5);
				double y = caster.posY + caster.height * world.rand.nextDouble() - 0.3;
				double z = caster.posZ + caster.width * (world.rand.nextDouble() - 0.5);
				TwilightForestMod.proxy.spawnParticle(TFParticleType.SNOW, x, y, z, 0, 0, 0);
			}
		}

		if(ticksInUse % 100 == 0){
			playSound(world, caster, ticksInUse, -1, modifiers, "ambient");
		}

		return true;
	}

	private void slamGround(EntityPlayer caster){

		World world = caster.world;
		SpellModifiers modifiers = WizardData.get(caster).itemCastingModifiers;

		float radius = getProperty(BLAST_RADIUS).floatValue() * modifiers.get(WizardryItems.blast_upgrade);
		float minHoverHeight = getProperty(HOVER_HEIGHT).floatValue();
		float maxHoverHeight = getProperty(MAX_HOVER_HEIGHT).floatValue();

		// Must be at least 70% up to max hover height to cause crush damage
		if(caster.onGround && caster.fallDistance > minHoverHeight + (maxHoverHeight - minHoverHeight) * SLAM_HEIGHT_FRACTION){

			float maxDamage = getProperty(MAX_CRUSH_DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);

			List<EntityLivingBase> targets = EntityUtils.getLivingWithinRadius(radius, caster.posX, caster.posY, caster.posZ, world);

			for(EntityLivingBase target : targets){

				if(target != caster){
					// Constant until halfway out, then ramp down to zero at radius
					float damage1 = maxDamage * Math.min(1, ((radius - target.getDistance(caster)) / radius) * 2);
					target.attackEntityFrom(MagicDamage.causeDirectMagicDamage(caster, DamageType.FROST), damage1);
				}

				if(world.isRemote && target instanceof EntityPlayer){
					Wizardry.proxy.shakeScreen((EntityPlayer)target, 10);
				}
			}

			// Smashing!
			if(!world.isRemote && EntityUtils.canDamageBlocks(caster, world)
					&& ItemArtefact.isArtefactActive(caster, TFSPItems.ring_aurora)){
				double radius1 = radius * BLOCK_BREAK_RADIUS_FRACTION;
				BlockUtils.getBlockSphere(caster.getPosition(), radius1).forEach(pos -> {
					// Greater chance of destroying block nearer to the centre
					if(!BlockUtils.isBlockUnbreakable(world, pos) && world.rand.nextDouble() >
							GeometryUtils.getCentre(pos).distanceTo(caster.getPositionVector()) / radius1 - 0.3
							&& BlockUtils.canBreakBlock(caster, world, pos))
						world.destroyBlock(pos, true);
				});
			}

			if(world.isRemote){

				ParticleBuilder.create(Type.SPHERE).clr(0.7f, 0.9f, 1).scale(4).entity(caster).spawn(world);

				for(int i = 0; i < 40; i++){

					double x = caster.posX - 1 + 2 * world.rand.nextDouble();
					double z = caster.posZ - 1 + 2 * world.rand.nextDouble();
					IBlockState block = BlockUtils.getBlockEntityIsStandingOn(caster);

					if(block != null){
						world.spawnParticle(EnumParticleTypes.BLOCK_DUST, x, caster.getEntityBoundingBox().minY,
								z, x - caster.posX, 0, z - caster.posZ, Block.getStateId(block));
					}
				}
			}

			playSound(world, caster, 0, -1, modifiers, "rumble");
			playSound(world, caster, 0, -1, modifiers, "ground_slam");
		}
	}

	@SubscribeEvent
	public static void onPlayerFlyableFallEvent(PlayerFlyableFallEvent event){
		if(EntityUtils.isCasting(event.getEntityLiving(), TFSPSpells.chariot_of_ice)){
			((ChariotOfIce)TFSPSpells.chariot_of_ice).slamGround(event.getEntityPlayer());
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onLivingFallEvent(LivingFallEvent event){
		if(event.getEntity() instanceof EntityPlayer && EntityUtils.isCasting(event.getEntityLiving(), TFSPSpells.chariot_of_ice)){
			((ChariotOfIce)TFSPSpells.chariot_of_ice).slamGround((EntityPlayer)event.getEntity());
			event.setCanceled(true);
		}
	}

}
