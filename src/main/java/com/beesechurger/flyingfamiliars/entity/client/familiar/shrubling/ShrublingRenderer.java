package com.beesechurger.flyingfamiliars.entity.client.familiar.shrubling;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import com.beesechurger.flyingfamiliars.entity.FFEntityTypes;
import com.beesechurger.flyingfamiliars.entity.client.familiar.BaseFamiliarRenderer;
import com.beesechurger.flyingfamiliars.entity.common.familiar.ShrublingEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShrublingRenderer extends BaseFamiliarRenderer<ShrublingEntity>
{
	public ShrublingRenderer(Context renderManager)
	{
		super(renderManager, new ShrublingModel());
		this.shadowRadius = 0.8f;
		this.withScale(FFEntityTypes.ENTITY_RENDER_SIZE_MAP.get(FFEntityTypes.SHRUBLING.get().toShortString()));
	}

	@Override
	public ResourceLocation getTextureLocation(ShrublingEntity entity)
	{
		return switch (entity.getVariant())
		{
			case "oak" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/shrubling/shrubling_oak.png");
			case "birch" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/shrubling/shrubling_birch.png");
			case "spruce" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/shrubling/shrubling_spruce.png");
			case "jungle" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/shrubling/shrubling_jungle.png");
			case "dark_oak" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/shrubling/shrubling_dark_oak.png");
			case "acacia" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/shrubling/shrubling_acacia.png");
			case "mangrove" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/shrubling/shrubling_mangrove.png");
			case "cherry" ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/shrubling/shrubling_cherry.png");
			default ->
					new ResourceLocation(FlyingFamiliars.MOD_ID, "textures/familiar/shrubling/shrubling_oak.png");
		};
	}
}
