package electroblob.tfspellpack.util;

import net.minecraft.entity.EntityLiving;
import twilightforest.TFConfig;

import java.util.function.BiPredicate;

public class TFSPUtils {

	/** Y-level of lakes/rivers in the twilight forest dimension. */
	public static final int TF_SEA_LEVEL = 32;

	/** Predicate that is true only for entities in the twilight forest dimension (or when override is true). */
	public static final BiPredicate<EntityLiving, Boolean> IN_TF_DIMENSION = (e, o) -> o || e.dimension == TFConfig.dimension.dimensionID;

}
