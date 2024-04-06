package com.beesechurger.flyingfamiliars.block.client.obelisk;

import com.beesechurger.flyingfamiliars.block.entity.common.ObeliskBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

@OnlyIn(Dist.CLIENT)
public class ObeliskRenderer extends GeoBlockRenderer<ObeliskBlockEntity>
{
    public ObeliskRenderer(BlockEntityRendererProvider.Context context)
    {
        super(new ObeliskModel());
    }
}
