package games.enchanted.mouseskipfix.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import games.enchanted.mouseskipfix.config.ConfigValues;
import games.enchanted.mouseskipfix.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.network.chat.Component;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.MouseHandler.class)
public class MouseHandlerMixin {
    @Shadow @Final private Minecraft minecraft;
    @Shadow private boolean ignoreFirstMove;
    @Unique private int mouseSkipFix$ignoreTicks;

    @Unique
    private void mouseSkipFix$showDebugToast() {
        if(Services.PLATFORM.isDevelopmentEnvironment()) {
            ToastManager toastmanager = minecraft.getToastManager();
            SystemToast.addOrUpdate(toastmanager, SystemToast.SystemToastId.PACK_LOAD_FAILURE, Component.literal("ignoreFirstMove: " + this.ignoreFirstMove), Component.literal("ignoreTicks: " + this.mouseSkipFix$ignoreTicks));
        }
    }

    @ModifyExpressionValue(
        method = "onMove",
        at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/MouseHandler;ignoreFirstMove:Z")
    )
    private boolean skipMoveIfIgnoreTicks(boolean original) {
        mouseSkipFix$showDebugToast();
        return original || this.mouseSkipFix$ignoreTicks >= 0;
    }

    @Inject(
        method = "onMove",
        at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/client/MouseHandler;ignoreFirstMove:Z")
    )
    private void subtractFromIgnoreTicks(long windowPointer, double xpos, double ypos, CallbackInfo ci) {
        mouseSkipFix$ignoreTicks -= 1;
        mouseSkipFix$showDebugToast();
    }

    @Inject(
        method = "grabMouse",
        at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/client/MouseHandler;ignoreFirstMove:Z")
    )
    private void setIgnoreTicksWhenMouseGrabbed(CallbackInfo ci) {
        mouseSkipFix$ignoreTicks = ConfigValues.skippedMouseInputs;
        mouseSkipFix$showDebugToast();
    }

    @Inject(
        method = "setIgnoreFirstMove()V",
        at = @At("TAIL")
    )
    private void setIgnoreTicksWhenSettingIgnoreFirstMove(CallbackInfo ci) {
        mouseSkipFix$ignoreTicks = ConfigValues.skippedMouseInputs;
        mouseSkipFix$showDebugToast();
    }

    @Inject(
        method = "cursorEntered()V",
        at = @At("TAIL")
    )
    private void setIgnoreTicksWhenCursorEntered(CallbackInfo ci) {
        mouseSkipFix$ignoreTicks = ConfigValues.skippedMouseInputs;
        mouseSkipFix$showDebugToast();
    }
}
