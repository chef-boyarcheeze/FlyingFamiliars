package com.beesechurger.flyingfamiliars.registries;

import com.beesechurger.flyingfamiliars.entity.client.familiar.cloud_ray.CloudRayRenderer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.cormorant.CormorantRenderer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.griffonfly.GriffonflyRenderer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.magic_carpet.MagicCarpetRenderer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.phoenix.PhoenixRenderer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.shadewyrm.ShadewyrmRenderer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.void_moth.VoidMothRenderer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.zephyr_fish.ZephyrFishRenderer;
import com.beesechurger.flyingfamiliars.entity.client.wand_effect.charm.crystal_spike_charm.CrystalSpikeCharmRenderer;
import com.beesechurger.flyingfamiliars.entity.client.wand_effect.projectile.capture_projectile.CaptureProjectileRenderer;
import com.beesechurger.flyingfamiliars.entity.client.wand_effect.projectile.fireball_projectile.FireballProjectileRenderer;
import com.beesechurger.flyingfamiliars.item.client.PhylacteryRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

public class FFClientSetup
{
    public static void registerRenderers()
    {
        // Familiars:
        EntityRenderers.register(FFEntityTypes.CLOUD_RAY.get(), CloudRayRenderer::new);
        EntityRenderers.register(FFEntityTypes.GRIFFONFLY.get(), GriffonflyRenderer::new);
        //EntityRenderers.register(FFEntityTypes.THUNDERBIRD.get(), ThunderBirdRenderer::new);
        EntityRenderers.register(FFEntityTypes.MAGIC_CARPET.get(), MagicCarpetRenderer::new);
        //EntityRenderers.register(FFEntityTypes.DRAGON.get(), DragonRenderer::new);
        EntityRenderers.register(FFEntityTypes.SHADEWYRM.get(), ShadewyrmRenderer::new);
        //EntityRenderers.register(FFEntityTypes.SUNDOG.get(), SundogRenderer::new);

        EntityRenderers.register(FFEntityTypes.CORMORANT.get(), CormorantRenderer::new);
        // plant
        EntityRenderers.register(FFEntityTypes.ZEPHYR_FISH.get(), ZephyrFishRenderer::new);
        //EntityRenderers.register(FFEntityTypes.CRYSTAL_TRESSYM.get(), CrystalTressymRenderer::new);
        EntityRenderers.register(FFEntityTypes.PHOENIX.get(), PhoenixRenderer::new);
        EntityRenderers.register(FFEntityTypes.VOID_MOTH.get(), VoidMothRenderer::new);
        // mirror shield

        // Wand effects:
        // Charms:
        EntityRenderers.register(FFEntityTypes.CRYSTAL_SPIKE_CHARM.get(), CrystalSpikeCharmRenderer::new);

        // Projectiles:
        EntityRenderers.register(FFEntityTypes.CAPTURE_PROJECTILE.get(), CaptureProjectileRenderer::new);
        EntityRenderers.register(FFEntityTypes.FIREBALL_PROJECTILE.get(), FireballProjectileRenderer::new);

        // Block entities:
        ItemBlockRenderTypes.setRenderLayer(FFBlocks.BRAZIER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(FFBlocks.VITA_ALEMBIC.get(), RenderType.translucent());

        // Fluids:
        ItemBlockRenderTypes.setRenderLayer(FFFluids.SOURCE_BLUE_VITALITY.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(FFFluids.FLOWING_BLUE_VITALITY.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(FFFluids.SOURCE_GREEN_VITALITY.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(FFFluids.FLOWING_GREEN_VITALITY.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(FFFluids.SOURCE_YELLOW_VITALITY.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(FFFluids.FLOWING_YELLOW_VITALITY.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(FFFluids.SOURCE_GOLD_VITALITY.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(FFFluids.FLOWING_GOLD_VITALITY.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(FFFluids.SOURCE_RED_VITALITY.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(FFFluids.FLOWING_RED_VITALITY.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(FFFluids.SOURCE_BLACK_VITALITY.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(FFFluids.FLOWING_BLACK_VITALITY.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(FFFluids.SOURCE_WHITE_VITALITY.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(FFFluids.FLOWING_WHITE_VITALITY.get(), RenderType.translucent());

        // Curio items:
        CuriosRendererRegistry.register(FFItems.PHYLACTERY_BLUE.get(), PhylacteryRenderer::new);
        CuriosRendererRegistry.register(FFItems.PHYLACTERY_GREEN.get(), PhylacteryRenderer::new);
        CuriosRendererRegistry.register(FFItems.PHYLACTERY_YELLOW.get(), PhylacteryRenderer::new);
        CuriosRendererRegistry.register(FFItems.PHYLACTERY_GOLD.get(), PhylacteryRenderer::new);
        CuriosRendererRegistry.register(FFItems.PHYLACTERY_RED.get(), PhylacteryRenderer::new);
        CuriosRendererRegistry.register(FFItems.PHYLACTERY_BLACK.get(), PhylacteryRenderer::new);
        CuriosRendererRegistry.register(FFItems.PHYLACTERY_WHITE.get(), PhylacteryRenderer::new);
    }
}
