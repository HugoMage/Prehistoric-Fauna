package superlord.prehistoricfauna.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import superlord.prehistoricfauna.PrehistoricFauna;
import superlord.prehistoricfauna.client.model.SillosuchusModel;
import superlord.prehistoricfauna.common.entities.SillosuchusEntity;

@OnlyIn(Dist.CLIENT)
public class SillosuchusRenderer extends MobRenderer<SillosuchusEntity, EntityModel<SillosuchusEntity>>{
	
	private static final SillosuchusModel SILLOSUCHUS_MODEL = new SillosuchusModel();
	private static final ResourceLocation SILLOSUCHUS = new ResourceLocation(PrehistoricFauna.MOD_ID, "textures/entities/sillosuchus/sillosuchus.png");
	private static final ResourceLocation ALBINO = new ResourceLocation(PrehistoricFauna.MOD_ID, "textures/entities/sillosuchus/albino.png");
	private static final ResourceLocation MELANISTIC = new ResourceLocation(PrehistoricFauna.MOD_ID, "textures/entities/sillosuchus/melanistic.png");
	
	public SillosuchusRenderer() {
		super(Minecraft.getInstance().getRenderManager(), SILLOSUCHUS_MODEL, 1.0F);
	}
	
	protected void preRenderCallback(SillosuchusEntity sillosuchus, MatrixStack matrixStackIn, float partialTickTime) {
		if (sillosuchus.isChild()) {
			matrixStackIn.scale(0.5F, 0.5F, 0.5F);
		}
	}
	
	public ResourceLocation getEntityTexture(SillosuchusEntity entity) {
		if (entity.isAlbino()) {
			return ALBINO;
		} else if (entity.isMelanistic()) {
			return MELANISTIC;
		} else {
			return SILLOSUCHUS;
		}
	}

}
