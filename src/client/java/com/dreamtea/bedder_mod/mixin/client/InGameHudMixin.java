package com.dreamtea.bedder_mod.mixin.client;

import com.dreamtea.bedder_mod.SpamTracker;
import com.dreamtea.bedder_mod.imixin.ISpamClick;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow @Final private MinecraftClient client;

    @Redirect(
            method = "renderOverlayMessage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithBackground(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIII)V"
            )
    )
    public void adjustFont(
            DrawContext instance,
            TextRenderer textRenderer,
            Text text,
            int x,
            int y,
            int width,
            int color,
            DrawContext context,
            RenderTickCounter tickCounter
    ){
        int drawColor = color;
        if(this.client.player instanceof ISpamClick isc) {
            SpamTracker spam = isc.getClicker();
            spam.setSpammedText(text);
            if(isc.getClicker().active()){
                spam.matricesTranslate(context.getMatrices());
                drawColor = isc.getClicker().getColor();
            }
        }
        context.drawTextWithBackground(textRenderer, text, x, y, width, drawColor);
    }
}