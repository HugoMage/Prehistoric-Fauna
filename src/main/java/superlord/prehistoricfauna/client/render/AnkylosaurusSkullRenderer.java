package superlord.prehistoricfauna.client.render;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import superlord.prehistoricfauna.PrehistoricFauna;
import superlord.prehistoricfauna.client.model.AnkylosaurusSkullModel;
import superlord.prehistoricfauna.common.entities.AnkylosaurusSkullEntity;

public class AnkylosaurusSkullRenderer extends MobRenderer<AnkylosaurusSkullEntity, AnkylosaurusSkullModel> {

    private static final ResourceLocation SKULL = new ResourceLocation(PrehistoricFauna.MOD_ID, "textures/entities/skeleton/ankylosaurus_skull.png");

    public AnkylosaurusSkullRenderer(EntityRendererManager rm) {
        super(rm, new AnkylosaurusSkullModel(), 1.0F);
    }
    @Override
	public ResourceLocation getEntityTexture(AnkylosaurusSkullEntity entity) {
    	return SKULL;
    }
}