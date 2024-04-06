package com.beesechurger.flyingfamiliars.mixin;

import com.beesechurger.flyingfamiliars.entity.common.familiar.BaseFamiliarEntity;
import com.beesechurger.flyingfamiliars.entity.common.familiar.GriffonflyEntity;
import com.beesechurger.flyingfamiliars.entity.common.familiar.MagicCarpetEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class PlayerRiderRendererMixin<T extends LivingEntity>
{	
    @Inject(method = "setupRotations(Lnet/minecraft/world/entity/LivingEntity;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V", at = @At("RETURN"))
    private void setupFamiliarRidingRotations(T entity, PoseStack stack, float ageInTicks, float rotationYaw, float partialTicks, CallbackInfo ci)
    {
        if (entity.getVehicle() instanceof BaseFamiliarEntity familiar)
        {
        	float renderPitch = (float) familiar.getPitch(partialTicks);
        	float renderRoll = (float) familiar.getRoll(partialTicks);
            double renderOffset = familiar.getPassengersRidingOffset() + entity.getMyRidingOffset();

            if(entity.getVehicle() instanceof GriffonflyEntity griffonfly)
                renderOffset = griffonfly.getRiderPosition(entity).y;
            else if(entity.getVehicle() instanceof MagicCarpetEntity magicCarpet)
                renderOffset = magicCarpet.getRiderPosition(entity).y;
        	
        	stack.translate(0, -renderOffset, 0);
        	stack.mulPose(Axis.XP.rotationDegrees(-renderPitch));
            stack.mulPose(Axis.ZP.rotationDegrees(renderRoll));
            stack.translate(0, renderOffset, 0);
        }
    }

    @Redirect(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getVehicle()Lnet/minecraft/world/entity/Entity;", ordinal = 2))
    private Entity redirectPlayerRotation(LivingEntity entity)
    {
        if (entity.getVehicle() instanceof BaseFamiliarEntity familiar) return null;
        else return entity.getVehicle();
    }
}
