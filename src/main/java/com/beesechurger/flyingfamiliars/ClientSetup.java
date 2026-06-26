package com.beesechurger.flyingfamiliars;

import com.beesechurger.flyingfamiliars.entity.client.familiar.cloud_ray.CloudRayRenderer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.crystal_tressym.CrystalTressymRenderer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.deep_jellyfish.DeepJellyfishRenderer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.griffonfly.GriffonflyRenderer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.magic_carpet.MagicCarpetRenderer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.mirror_shield.MirrorShieldRenderer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.phoenix.PhoenixRenderer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.shadewyrm.ShadewyrmRenderer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.shrubling.ShrublingRenderer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.void_moth.VoidMothRenderer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.zephyr_fish.ZephyrFishRenderer;
import com.beesechurger.flyingfamiliars.entity.client.wand_effect.charm.crystal_spike_charm.CrystalSpikeCharmRenderer;
import com.beesechurger.flyingfamiliars.entity.client.wand_effect.projectile.capture_projectile.CaptureProjectileRenderer;
import com.beesechurger.flyingfamiliars.entity.client.wand_effect.projectile.capture_projectile.RunicCubeProjectileRenderer;
import com.beesechurger.flyingfamiliars.entity.client.wand_effect.projectile.fireball_projectile.FireballProjectileRenderer;
import com.beesechurger.flyingfamiliars.entity.client.wand_effect.projectile.flamethrower_projectile.FlamethrowerProjectileRenderer;
import com.beesechurger.flyingfamiliars.item.client.PhylacteryRenderer;
import com.beesechurger.flyingfamiliars.item.tooltip.EntityStorageTooltipComponent;
import com.beesechurger.flyingfamiliars.item.tooltip.SpiritStorageTooltipComponent;
import com.beesechurger.flyingfamiliars.registries.FFBlocks;
import com.beesechurger.flyingfamiliars.registries.FFEntityTypes;
import com.beesechurger.flyingfamiliars.registries.FFFluids;
import com.beesechurger.flyingfamiliars.registries.FFItems;
import com.beesechurger.flyingfamiliars.wand_effect.client.WandEffectSelectionScreen;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

import static com.beesechurger.flyingfamiliars.wand_effect.client.WandEffectSelectionScreen.WAND_EFFECT_WHEEL;

public class ClientSetup
{
    public static final ClientSetup INSTANCE = new ClientSetup();

    public void register(final IEventBus modEventBus)
    {
        modEventBus.addListener(this::registerRenderers);
        modEventBus.addListener(this::registerOverlays);
        modEventBus.addListener(this::registerClientTooltipComponents);
    }

    private void registerRenderers(final FMLClientSetupEvent event)
    {
        // Familiars:
        EntityRenderers.register(FFEntityTypes.CLOUD_RAY.get(), CloudRayRenderer::new);
        EntityRenderers.register(FFEntityTypes.GRIFFONFLY.get(), GriffonflyRenderer::new);
        //EntityRenderers.register(FFEntityTypes.THUNDERBIRD.get(), ThunderBirdRenderer::new);
        EntityRenderers.register(FFEntityTypes.MAGIC_CARPET.get(), MagicCarpetRenderer::new);
        //EntityRenderers.register(FFEntityTypes.DRAGON.get(), DragonRenderer::new);
        EntityRenderers.register(FFEntityTypes.SHADEWYRM.get(), ShadewyrmRenderer::new);
        //EntityRenderers.register(FFEntityTypes.SUNDOG.get(), SundogRenderer::new);

        EntityRenderers.register(FFEntityTypes.DEEP_JELLYFISH.get(), DeepJellyfishRenderer::new);
        EntityRenderers.register(FFEntityTypes.SHRUBLING.get(), ShrublingRenderer::new);
        EntityRenderers.register(FFEntityTypes.ZEPHYR_FISH.get(), ZephyrFishRenderer::new);
        EntityRenderers.register(FFEntityTypes.CRYSTAL_TRESSYM.get(), CrystalTressymRenderer::new);
        EntityRenderers.register(FFEntityTypes.PHOENIX.get(), PhoenixRenderer::new);
        EntityRenderers.register(FFEntityTypes.VOID_MOTH.get(), VoidMothRenderer::new);
        EntityRenderers.register(FFEntityTypes.MIRROR_SHIELD.get(), MirrorShieldRenderer::new);

        // Wand effects:
        // Charms:
        EntityRenderers.register(FFEntityTypes.CRYSTAL_SPIKE_CHARM.get(), CrystalSpikeCharmRenderer::new);

        // Projectiles:
        EntityRenderers.register(FFEntityTypes.CAPTURE_PROJECTILE.get(), CaptureProjectileRenderer::new);
        EntityRenderers.register(FFEntityTypes.FIREBALL_PROJECTILE.get(), FireballProjectileRenderer::new);
        EntityRenderers.register(FFEntityTypes.FLAMETHROWER_PROJECTILE.get(), FlamethrowerProjectileRenderer::new);
        EntityRenderers.register(FFEntityTypes.RUNIC_CUBE_PROJECTILE.get(), RunicCubeProjectileRenderer::new);

        // Block entities:
        ItemBlockRenderTypes.setRenderLayer(FFBlocks.BRAZIER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(FFBlocks.RUNIC_PEDESTAL.get(), RenderType.cutout());
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
        CuriosRendererRegistry.register(FFItems.PHYLACTERY.get(), PhylacteryRenderer::new);
    }

    private void registerOverlays(RegisterGuiOverlaysEvent event)
    {
        event.registerAbove(VanillaGuiOverlay.PLAYER_LIST.id(), WAND_EFFECT_WHEEL, WandEffectSelectionScreen.INSTANCE);
    }

    private void registerClientTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event)
    {
        event.register(EntityStorageTooltipComponent.class, EntityStorageTooltipComponent.Client::new);
        event.register(SpiritStorageTooltipComponent.class, SpiritStorageTooltipComponent.Client::new);
    }
}
