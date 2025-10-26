package com.beesechurger.flyingfamiliars.entity.client.familiar.griffonfly;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.client.familiar.BaseFamiliarRenderer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.layer.rider.BaseFamiliarRiderLayer;
import com.beesechurger.flyingfamiliars.entity.client.familiar.layer.rider.GriffonflyRiderLayer;
import com.beesechurger.flyingfamiliars.entity.common.familiar.GriffonflyEntity;
import com.beesechurger.flyingfamiliars.registries.FFEntityTypes;

import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GriffonflyRenderer extends BaseFamiliarRenderer<GriffonflyEntity>
{
	public GriffonflyRenderer(Context renderManager)
	{
		super(renderManager, new GriffonflyModel());
		this.shadowRadius = 1.2f;
		this.withScale(FFEntityTypes.ENTITY_RENDER_SIZE_MAP.get(FFEntityTypes.GRIFFONFLY.get().toShortString()));
        this.addRenderLayer(new GriffonflyRiderLayer(this));
	}
	
	@Override
	public ResourceLocation getTextureLocation(GriffonflyEntity griffonflyEntity)
	{
        return switch (griffonflyEntity.getVariant())
		{
            case "yellow" ->
                    new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/griffonfly/griffonfly_yellow.png");
            case "green" ->
                    new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/griffonfly/griffonfly_green.png");
            case "blue" ->
                    new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/griffonfly/griffonfly_blue.png");
            case "purple" ->
                    new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/griffonfly/griffonfly_purple.png");
            case "red" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/griffonfly/griffonfly_red.png");
            default ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/griffonfly/griffonfly_yellow.png");
        };
	}
}
