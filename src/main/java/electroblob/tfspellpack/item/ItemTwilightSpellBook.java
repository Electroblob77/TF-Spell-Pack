package electroblob.tfspellpack.item;

import electroblob.tfspellpack.TFSpellPack;
import electroblob.wizardry.item.ItemSpellBook;
import electroblob.wizardry.spell.Spell;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemTwilightSpellBook extends ItemSpellBook {

	private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(TFSpellPack.MODID, "textures/gui/spell_book_twilight.png");

	@Override
	public ResourceLocation getGuiTexture(Spell spell){
		return GUI_TEXTURE;
	}

	@Override
	public EnumRarity getRarity(ItemStack stack){
		return EnumRarity.UNCOMMON;
	}
}
