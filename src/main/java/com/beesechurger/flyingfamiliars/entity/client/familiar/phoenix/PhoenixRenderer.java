package com.beesechurger.flyingfamiliars.entity.client.familiar.phoenix;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.client.familiar.BaseFamiliarRenderer;
import com.beesechurger.flyingfamiliars.entity.common.familiar.PhoenixEntity;
import com.beesechurger.flyingfamiliars.registries.FFEntityTypes;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PhoenixRenderer extends BaseFamiliarRenderer<PhoenixEntity>
{

	public PhoenixRenderer(Context renderManager)
	{
		super(renderManager, new PhoenixModel());
		this.shadowRadius = 0.35f;
		this.withScale(FFEntityTypes.ENTITY_RENDER_SIZE_MAP.get(FFEntityTypes.PHOENIX.get().toShortString()));
	}

	@Override
	public ResourceLocation getTextureLocation(PhoenixEntity entity)
	{
		return switch (entity.getVariant()) {
			case "red" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/phoenix/phoenix_red.png");
			case "blue" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/phoenix/phoenix_blue.png");
			default ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/phoenix/phoenix_red.png");
		};
	}
}
