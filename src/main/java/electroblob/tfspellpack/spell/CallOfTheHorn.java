package electroblob.tfspellpack.spell;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.tfspellpack.registry.TFSPItems;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.SpellRay;
import electroblob.wizardry.util.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import twilightforest.item.ItemTFCrumbleHorn;
import twilightforest.item.TFItems;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CallOfTheHorn extends SpellRay {

	private static final Method crumbleBlocksInAABB; // Get ready to crrrrrumble! (sorry not sorry)

	static {
		// Don't need obf reflection helper since twilight forest isn't obfuscated!
		Method m = null;

		try {
			m = ItemTFCrumbleHorn.class.getDeclaredMethod("crumbleBlocksInAABB", ItemStack.class, World.class, EntityLivingBase.class, AxisAlignedBB.class);
			m.setAccessible(true);
		}catch(NoSuchMethodException e){
			e.printStackTrace();
		}

		crumbleBlocksInAABB = m;
	}

	public CallOfTheHorn(){
		super(TFSpellPack.MODID, "call_of_the_horn", EnumAction.BOW, true);
		this.ignoreLivingEntities(true);
		this.hitLiquids(false);
		this.soundValues(1, 0.8f, 0);
		addProperties(EFFECT_RADIUS);
	}

	@Override
	public boolean canBeCastBy(EntityLiving npc, boolean override){ return false; }

	@Override
	public boolean applicableForItem(Item item){
		return item == TFSPItems.twilight_spell_book || item == TFSPItems.twilight_scroll;
	}

	@Override
	protected boolean onEntityHit(World world, Entity target, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers){
		return false;
	}

	@Override
	protected boolean onBlockHit(World world, BlockPos pos, EnumFacing side, Vec3d hit, @Nullable EntityLivingBase caster, Vec3d origin, int ticksInUse, SpellModifiers modifiers){

		if(!(caster instanceof EntityPlayer)) return false; // Just in case old entities still have this spell

		if(!world.isRemote && ticksInUse > 10 && ticksInUse % 5 == 0){

			double radius = getProperty(EFFECT_RADIUS).doubleValue() * modifiers.get(WizardryItems.blast_upgrade);

			AxisAlignedBB crumbleBox = new AxisAlignedBB(hit.x - radius, hit.y - radius, hit.z - radius, hit.x + radius, hit.y + radius, hit.z + radius);

			// Should be ok to use the empty stack here, it's only used for advancement triggers
			try{
				// Returns the number of blocks crumbled, we don't need that though (for now)
				crumbleBlocksInAABB.invoke(TFItems.crumble_horn, ItemStack.EMPTY, world, caster, crumbleBox);
			}catch(IllegalAccessException | InvocationTargetException e){
				e.printStackTrace();
			}
		}

		// N.B. The spell won't stop if we return false so in theory it could just consume mana when there are blocks
		// to be crumbled - this would kind of work by the virtue that continuous spells only do mana calcs every 10
		// casting ticks, which happens to line up with the crumbling frequency, but that should not be relied upon!
		return true;
	}

	@Override
	protected boolean onMiss(World world, @Nullable EntityLivingBase caster, Vec3d origin, Vec3d direction, int ticksInUse, SpellModifiers modifiers){
		return true; // Consume mana on miss
	}

	@Override
	protected void playSound(World world, double x, double y, double z, int ticksInUse, int duration, SpellModifiers modifiers, String... sounds){
		if(ticksInUse > 10 && ticksInUse % 5 == 0) super.playSound(world, x, y, z, ticksInUse, duration, modifiers, sounds);
	}

}
