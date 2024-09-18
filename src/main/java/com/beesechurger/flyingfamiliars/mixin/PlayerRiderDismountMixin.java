package com.beesechurger.flyingfamiliars.mixin;

import com.beesechurger.flyingfamiliars.item.common.entity_items.SoulWand.BaseSoulWand;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class PlayerRiderDismountMixin<T extends LivingEntity>
{
    @Inject(method = "Lnet/minecraft/world/entity/LivingEntity;stopRiding()V", at = @At("HEAD"), cancellable = true)
    public void familiarStopRiding(CallbackInfo ci)
    {
        if((Object) this instanceof Player player)
        {
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
            if(!stack.isEmpty() && stack.getItem() instanceof BaseSoulWand)
                ci.cancel();
        }
    }
}
