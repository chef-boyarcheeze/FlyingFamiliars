package com.beesechurger.flyingfamiliars.mixin;

import com.beesechurger.flyingfamiliars.item.FFItemHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class LockTooltipPositionMixin
{
    @Inject(method = "renderTooltip(Lnet/minecraft/client/gui/GuiGraphics;II)V", at = @At("HEAD"), cancellable = true)
    private void combinedTooltipRedirect(GuiGraphics guiGraphics, int mouseX, int mouseY, CallbackInfo ci)
    {
        if (FFItemHandler.TooltipLockHandler.INSTANCE.isLocked())
        {
            int lockedX = FFItemHandler.TooltipLockHandler.INSTANCE.getLockedX();
            int lockedY = FFItemHandler.TooltipLockHandler.INSTANCE.getLockedY();
            var lockedStack = FFItemHandler.TooltipLockHandler.INSTANCE.getLockedStack();

            if (!lockedStack.isEmpty())
            {
                guiGraphics.renderTooltip(
                        Minecraft.getInstance().font,
                        lockedStack,
                        lockedX,
                        lockedY
                );

                ci.cancel();
            }
        }
    }
}