package com.beesechurger.flyingfamiliars.entity.client.familiar.deep_jellyfish;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.client.familiar.BaseFamiliarRenderer;
import com.beesechurger.flyingfamiliars.entity.common.familiar.DeepJellyfishEntity;
import com.beesechurger.flyingfamiliars.registries.FFEntityTypes;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DeepJellyfishRenderer extends BaseFamiliarRenderer<DeepJellyfishEntity>
{
	public DeepJellyfishRenderer(Context renderManager)
	{
		super(renderManager, new DeepJellyfishModel());
		this.shadowRadius = 0.8f;
		this.withScale(FFEntityTypes.ENTITY_RENDER_SIZE_MAP.get(FFEntityTypes.DEEP_JELLYFISH.get().toShortString()));
	}

	@Override
	public ResourceLocation getTextureLocation(DeepJellyfishEntity DeepJellyfishEntity)
	{
		return switch (DeepJellyfishEntity.getVariant())
		{
			case "blue" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/deep_jellyfish/deep_jellyfish_blue.png");
			case "green" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/deep_jellyfish/deep_jellyfish_green.png");
			case "purple" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/deep_jellyfish/deep_jellyfish_purple.png");
			default ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/deep_jellyfish/deep_jellyfish_blue.png");
		};
	}
}
