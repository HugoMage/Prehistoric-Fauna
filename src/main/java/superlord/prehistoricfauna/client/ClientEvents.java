package superlord.prehistoricfauna.client;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.GrassColors;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import superlord.prehistoricfauna.PrehistoricFauna;
import superlord.prehistoricfauna.client.render.AllosaurusRenderer;
import superlord.prehistoricfauna.client.render.AllosaurusSkeletonRenderer;
import superlord.prehistoricfauna.client.render.AllosaurusSkullRenderer;
import superlord.prehistoricfauna.client.render.AnkylosaurusRenderer;
import superlord.prehistoricfauna.client.render.AnkylosaurusSkeletonRenderer;
import superlord.prehistoricfauna.client.render.AnkylosaurusSkullRenderer;
import superlord.prehistoricfauna.client.render.BasilemysRenderer;
import superlord.prehistoricfauna.client.render.BossRenderer;
import superlord.prehistoricfauna.client.render.CamarasaurusRenderer;
import superlord.prehistoricfauna.client.render.CeratodusRenderer;
import superlord.prehistoricfauna.client.render.CeratosaurusRenderer;
import superlord.prehistoricfauna.client.render.CeratosaurusSkeletonRenderer;
import superlord.prehistoricfauna.client.render.CeratosaurusSkullRenderer;
import superlord.prehistoricfauna.client.render.ChromogisaurusRenderer;
import superlord.prehistoricfauna.client.render.CyclurusRenderer;
import superlord.prehistoricfauna.client.render.DakotaraptorRenderer;
import superlord.prehistoricfauna.client.render.DidelphodonRenderer;
import superlord.prehistoricfauna.client.render.DryosaurusRenderer;
import superlord.prehistoricfauna.client.render.EilenodonRenderer;
import superlord.prehistoricfauna.client.render.ExaeretodonRenderer;
import superlord.prehistoricfauna.client.render.GarRenderer;
import superlord.prehistoricfauna.client.render.HerrerasaurusRenderer;
import superlord.prehistoricfauna.client.render.HerrerasaurusSkeletonRenderer;
import superlord.prehistoricfauna.client.render.HerrerasaurusSkullRenderer;
import superlord.prehistoricfauna.client.render.HesperornithoidesRenderer;
import superlord.prehistoricfauna.client.render.HyperodapedonRenderer;
import superlord.prehistoricfauna.client.render.IschigualastiaRenderer;
import superlord.prehistoricfauna.client.render.IschigualastiaSkeletonRenderer;
import superlord.prehistoricfauna.client.render.IschigualastiaSkullRenderer;
import superlord.prehistoricfauna.client.render.LandSentinelRenderer;
import superlord.prehistoricfauna.client.render.MyledaphusRenderer;
import superlord.prehistoricfauna.client.render.PaleopaintingRenderer;
import superlord.prehistoricfauna.client.render.PotamoceratodusRenderer;
import superlord.prehistoricfauna.client.render.PrehistoricBoatRenderer;
import superlord.prehistoricfauna.client.render.SaurosuchusRenderer;
import superlord.prehistoricfauna.client.render.SaurosuchusSkeletonRenderer;
import superlord.prehistoricfauna.client.render.SaurosuchusSkullRenderer;
import superlord.prehistoricfauna.client.render.SillosuchusRenderer;
import superlord.prehistoricfauna.client.render.StegosaurusRenderer;
import superlord.prehistoricfauna.client.render.StegosaurusSkeletonRenderer;
import superlord.prehistoricfauna.client.render.StegosaurusSkullRenderer;
import superlord.prehistoricfauna.client.render.ThescelosaurusRenderer;
import superlord.prehistoricfauna.client.render.TriceratopsRenderer;
import superlord.prehistoricfauna.client.render.TriceratopsSkeletonRenderer;
import superlord.prehistoricfauna.client.render.TriceratopsSkullRenderer;
import superlord.prehistoricfauna.client.render.TrilobiteSentinelRenderer;
import superlord.prehistoricfauna.client.render.TyrannosaurusRenderer;
import superlord.prehistoricfauna.client.render.TyrannosaurusSkeletonRenderer;
import superlord.prehistoricfauna.client.render.TyrannosaurusSkullRenderer;
import superlord.prehistoricfauna.client.render.WallFossilRenderer;
import superlord.prehistoricfauna.client.render.tileentity.PFSignTileEntityRenderer;
import superlord.prehistoricfauna.client.render.tileentity.gui.PaleontologyTableScreen;
import superlord.prehistoricfauna.client.render.tileentity.gui.PaleoscribeScreen;
import superlord.prehistoricfauna.common.util.PFWoodTypes;
import superlord.prehistoricfauna.compat.PFChestTileEntityRenderer;
import superlord.prehistoricfauna.init.PFBlocks;
import superlord.prehistoricfauna.init.PFContainers;
import superlord.prehistoricfauna.init.PFEntities;
import superlord.prehistoricfauna.init.PFKeybinds;
import superlord.prehistoricfauna.init.PFTileEntities;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = PrehistoricFauna.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
		
	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		PFKeybinds.register(event);
		BlockColors blockcolors = Minecraft.getInstance().getBlockColors();
		ItemColors itemcolors = Minecraft.getInstance().getItemColors();
		blockcolors.register((p_228064_0_, p_228064_1_, p_228064_2_, p_228064_3_) -> {
			return p_228064_1_ != null && p_228064_2_ != null ? BiomeColors.getGrassColor(p_228064_1_, p_228064_2_) : GrassColors.get(0.5D, 1.0D);
		}, PFBlocks.CONIOPTERIS);
		blockcolors.register((p_228061_0_, p_228061_1_, p_228061_2_, p_228061_3_) -> {
			return p_228061_1_ != null && p_228061_2_ != null ? BiomeColors.getFoliageColor(p_228061_1_, p_228061_2_) : FoliageColors.getDefault();
		}, PFBlocks.METASEQUOIA_LEAVES, PFBlocks.PROTOPICEOXYLON_LEAVES, PFBlocks.PROTOJUNIPEROXYLON_LEAVES, PFBlocks.METASEQUOIA_LEAF_CARPET.get(), PFBlocks.PROTOPICEOXYLON_LEAF_CARPET.get(), PFBlocks.PROTOJUNIPEROXYLON_LEAF_CARPET.get());
		blockcolors.register((p_228063_0_, p_228063_1_, p_228063_2_, p_228063_3_) -> {
			return 0x45AF45;
		}, PFBlocks.ARAUCARIA_LEAVES, PFBlocks.ARAUCARIA_LEAF_CARPET.get(), PFBlocks.ARAUCARIA_HEDGE.get());
		blockcolors.register((state, reader, pos, color) -> {
			return reader != null && pos != null ? BiomeColors.getFoliageColor(reader, pos) : FoliageColors.getDefault();
		}, PFBlocks.ARAUCARIA_LEAVES, PFBlocks.METASEQUOIA_LEAVES, PFBlocks.CONIOPTERIS, PFBlocks.PROTOPICEOXYLON_LEAVES, PFBlocks.PROTOJUNIPEROXYLON_LEAVES, PFBlocks.CLADOPHLEBIS, PFBlocks.ARAUCARIA_LEAF_CARPET.get(), PFBlocks.METASEQUOIA_LEAF_CARPET.get(), PFBlocks.PROTOPICEOXYLON_LEAF_CARPET.get(), PFBlocks.PROTOJUNIPEROXYLON_LEAF_CARPET.get(), PFBlocks.METASEQUOIA_HEDGE.get(), PFBlocks.PROTOPICEOXYLON_HEDGE.get(), PFBlocks.LIRIODENDRITES_HEDGE.get(), PFBlocks.PROTOJUNIPEROXYLON_HEDGE.get(), PFBlocks.HEIDIPHYLLUM_HEDGE.get());
		itemcolors.register((p_210235_1_, p_210235_2_) -> {
			BlockState blockstate = ((BlockItem) p_210235_1_.getItem()).getBlock().getDefaultState();
			return blockcolors.getColor(blockstate, (IBlockDisplayReader) null, (BlockPos) null, p_210235_2_);
		}, PFBlocks.ARAUCARIA_LEAVES, PFBlocks.METASEQUOIA_LEAVES, PFBlocks.CONIOPTERIS, PFBlocks.PROTOPICEOXYLON_LEAVES, PFBlocks.PROTOJUNIPEROXYLON_LEAVES, PFBlocks.CLADOPHLEBIS, PFBlocks.ARAUCARIA_LEAF_CARPET.get(), PFBlocks.METASEQUOIA_LEAF_CARPET.get(), PFBlocks.PROTOPICEOXYLON_LEAF_CARPET.get(), PFBlocks.PROTOJUNIPEROXYLON_LEAF_CARPET.get(), PFBlocks.METASEQUOIA_HEDGE.get(), PFBlocks.PROTOPICEOXYLON_HEDGE.get(), PFBlocks.LIRIODENDRITES_HEDGE.get(), PFBlocks.PROTOJUNIPEROXYLON_HEDGE.get(), PFBlocks.HEIDIPHYLLUM_HEDGE.get(), PFBlocks.ARAUCARIA_HEDGE.get());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.THESCELOSAURUS_ENTITY, ThescelosaurusRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.TRICERATOPS_ENTITY, manager -> new TriceratopsRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.ANKYLOSAURUS_ENTITY, AnkylosaurusRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.TYRANNOSAURUS_ENTITY, manager -> new TyrannosaurusRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.BASILEMYS_ENTITY, manager -> new BasilemysRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.DAKOTARAPTOR_ENTITY, manager -> new DakotaraptorRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.ALLOSAURUS_ENTITY, manager -> new AllosaurusRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.STEGOSAURUS_ENTITY, StegosaurusRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.CERATOSAURUS_ENTITY, manager -> new CeratosaurusRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.DRYOSAURUS_ENTITY, DryosaurusRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.HESPERORNITHOIDES_ENTITY, manager -> new HesperornithoidesRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.EILENODON_ENTITY, manager -> new EilenodonRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.CAMARASAURUS_ENTITY, manager -> new CamarasaurusRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.DIDELPHODON_ENTITY, DidelphodonRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.BOAT, PrehistoricBoatRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.EXAERETODON_ENTITY, manager -> new ExaeretodonRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.CHROMOGISAURUS_ENTITY, manager -> new ChromogisaurusRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.HERRERASAURUS_ENTITY, manager -> new HerrerasaurusRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.HYPERODAPEDON_ENTITY, manager -> new HyperodapedonRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.SILLOSUCHUS_ENTITY, manager -> new SillosuchusRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.TIME_GUARDIAN_ENTITY, BossRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.SAUROSUCHUS_ENTITY, manager -> new SaurosuchusRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.ISCHIGUALASTIA_ENTITY, manager -> new IschigualastiaRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.WALL_FOSSIL, WallFossilRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.TYRANNOSAURUS_SKULL, TyrannosaurusSkullRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.ANKYLOSAURUS_SKULL, AnkylosaurusSkullRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.TRICERATOPS_SKULL, TriceratopsSkullRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.HERRERASAURUS_SKULL, HerrerasaurusSkullRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.SAUROSUCHUS_SKULL, SaurosuchusSkullRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.STEGOSAURUS_SKULL, StegosaurusSkullRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.CERATOSAURUS_SKULL, CeratosaurusSkullRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.TYRANNOSAURUS_SKELETON, manager -> new TyrannosaurusSkeletonRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.TRICERATOPS_SKELETON, manager -> new TriceratopsSkeletonRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.ANKYLOSAURUS_SKELETON, manager -> new AnkylosaurusSkeletonRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.CERATOSAURUS_SKELETON, manager -> new CeratosaurusSkeletonRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.ALLOSAURUS_SKELETON, manager -> new AllosaurusSkeletonRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.HERRERASAURUS_SKELETON, manager -> new HerrerasaurusSkeletonRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.ISCHIGUALASTIA_SKELETON, manager -> new IschigualastiaSkeletonRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.SAUROSUCHUS_SKELETON, manager -> new SaurosuchusSkeletonRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.STEGOSAURUS_SKELETON, manager -> new StegosaurusSkeletonRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.ALLOSAURUS_SKULL, AllosaurusSkullRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.ISCHIGUALASTIA_SKULL, IschigualastiaSkullRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.PALEOPAINTING, PaleopaintingRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.TRILOBITE_SENTINEL_ENTITY, manager -> new TrilobiteSentinelRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.LAND_SENTINEL_ENTITY, manager -> new LandSentinelRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.MYLEDAPHUS, MyledaphusRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.GAR, manager -> new GarRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.CYCLURUS, manager -> new CyclurusRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.CERATODUS, manager -> new CeratodusRenderer());
		RenderingRegistry.registerEntityRenderingHandler(PFEntities.POTAMOCERATODUS, manager -> new PotamoceratodusRenderer());
		ScreenManager.registerFactory(PFContainers.PALEONTOLOGY_TABLE.get(), PaleontologyTableScreen::new);
		ScreenManager.registerFactory(PFContainers.PALEOSCRIBE_CONTAINER, PaleoscribeScreen::new);
		ClientRegistry.bindTileEntityRenderer(PFTileEntities.CHEST.get(), PFChestTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(PFTileEntities.TRAPPED_CHEST.get(), PFChestTileEntityRenderer::new);
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		ClientRegistry.bindTileEntityRenderer(PFTileEntities.PF_SIGNS.get(), PFSignTileEntityRenderer::new);
	}

	@SubscribeEvent
	public static void onStitchEvent(TextureStitchEvent.Pre event)
	{
		ResourceLocation stitching = event.getMap().getTextureLocation();
		if (!stitching.equals(Atlases.SIGN_ATLAS))
			return;

		PFWoodTypes.getValues().forEach(woodType -> event.addSprite(new ResourceLocation(PrehistoricFauna.MOD_ID, "entity/signs/" + woodType.getName())));
	}

}
