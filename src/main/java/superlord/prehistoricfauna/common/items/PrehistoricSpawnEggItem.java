package superlord.prehistoricfauna.common.items;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import superlord.prehistoricfauna.init.PFItems;

public class PrehistoricSpawnEggItem extends SpawnEggItem {
	
	public PrehistoricSpawnEggItem(EntityType<?> typeIn, int primaryColorIn, int secondaryColorIn, Properties builder) {
		super(typeIn, primaryColorIn, secondaryColorIn, builder);
	}

	@Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (stack.getItem() == PFItems.THESCELOSAURUS_SPAWN_EGG.get() || stack.getItem() == PFItems.ANKYLOSAURUS_SPAWN_EGG.get() || stack.getItem() == PFItems.TRICERATOPS_SPAWN_EGG.get() || stack.getItem() == PFItems.TYRANNOSAURUS_SPAWN_EGG.get() || stack.getItem() == PFItems.DIDELPHODON_SPAWN_EGG.get() || stack.getItem() == PFItems.BASILEMYS_SPAWN_EGG.get() || stack.getItem() == PFItems.DAKOTARAPTOR_SPAWN_EGG.get() || stack.getItem() == PFItems.MYLEDAPHUS_SPAWN_EGG.get() || stack.getItem() == PFItems.GAR_SPAWN_EGG.get() || stack.getItem() == PFItems.CYCLURUS_SPAWN_EGG.get()) {
			tooltip.add(new TranslationTextComponent("hell_creek").mergeStyle(TextFormatting.GRAY));
			tooltip.add(new TranslationTextComponent("cretaceous").mergeStyle(TextFormatting.DARK_GRAY));
		} else if (stack.getItem() == PFItems.DRYOSAURUS_SPAWN_EGG.get() || stack.getItem() == PFItems.ALLOSAURUS_SPAWN_EGG.get() || stack.getItem() == PFItems.EILENODON_SPAWN_EGG.get() || stack.getItem() == PFItems.STEGOSAURUS_SPAWN_EGG.get() || stack.getItem() == PFItems.CAMARASAURUS_SPAWN_EGG.get() || stack.getItem() == PFItems.HESPERORNITHOIDES_SPAWN_EGG.get() || stack.getItem() == PFItems.CERATOSAURUS_SPAWN_EGG.get() || stack.getItem() == PFItems.POTAMOCERATODUS_SPAWN_EGG.get()) {
			tooltip.add(new TranslationTextComponent("morrison").mergeStyle(TextFormatting.GRAY));
			tooltip.add(new TranslationTextComponent("jurassic").mergeStyle(TextFormatting.DARK_GRAY));
		} else if (stack.getItem() == PFItems.SAUROSUCHUS_SPAWN_EGG.get() || stack.getItem() == PFItems.SILLOSUCHUS_SPAWN_EGG.get() || stack.getItem() == PFItems.EXAERETODON_SPAWN_EGG.get() || stack.getItem() == PFItems.HYPERODAPEDON_SPAWN_EGG.get() || stack.getItem() == PFItems.ISCHIGUALASTIA_SPAWN_EGG.get() || stack.getItem() == PFItems.HERRERASAURUS_SPAWN_EGG.get() || stack.getItem() == PFItems.CHROMOGISAURUS_SPAWN_EGG.get()) {
			tooltip.add(new TranslationTextComponent("ischigualasto").mergeStyle(TextFormatting.GRAY));
			tooltip.add(new TranslationTextComponent("triassic").mergeStyle(TextFormatting.DARK_GRAY));
		} else if (stack.getItem() == PFItems.CERATODUS_SPAWN_EGG.get()) {
			tooltip.add(new TranslationTextComponent("mi").mergeStyle(TextFormatting.GRAY));
			tooltip.add(new TranslationTextComponent("jt").mergeStyle(TextFormatting.DARK_GRAY));
		}
    }
}
