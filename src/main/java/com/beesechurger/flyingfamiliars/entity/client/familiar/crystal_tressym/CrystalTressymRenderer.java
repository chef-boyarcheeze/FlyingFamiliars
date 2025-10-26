package com.beesechurger.flyingfamiliars.entity.client.familiar.crystal_tressym;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.client.familiar.BaseFamiliarRenderer;
import com.beesechurger.flyingfamiliars.entity.common.familiar.CrystalTressymEntity;
import com.beesechurger.flyingfamiliars.registries.FFEntityTypes;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CrystalTressymRenderer extends BaseFamiliarRenderer<CrystalTressymEntity>
{
	public CrystalTressymRenderer(Context renderManager)
	{
		super(renderManager, new CrystalTressymModel());
		this.shadowRadius = 0.8f;
		this.withScale(FFEntityTypes.ENTITY_RENDER_SIZE_MAP.get(FFEntityTypes.CRYSTAL_TRESSYM.get().toShortString()));
	}

	@Override
	public ResourceLocation getTextureLocation(CrystalTressymEntity CrystalTressymEntity)
	{
		return switch (CrystalTressymEntity.getVariant())
		{
			case "green" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/crystal_tressym/crystal_tressym_green.png");
			case "blue" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/crystal_tressym/crystal_tressym_blue.png");
			case "purple" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/crystal_tressym/crystal_tressym_purple.png");
			default ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/crystal_tressym/crystal_tressym_green.png");
		};
	}
}
