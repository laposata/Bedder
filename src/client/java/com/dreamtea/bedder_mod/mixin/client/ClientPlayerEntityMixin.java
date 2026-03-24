package com.dreamtea.bedder_mod.mixin.client;

import com.dreamtea.bedder_mod.SpamTracker;
import com.dreamtea.bedder_mod.imixin.ISpamClick;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.chat.ChatAbilities;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.stats.StatsCounter;
import net.minecraft.world.entity.player.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class ClientPlayerEntityMixin implements ISpamClick {

    @Unique
    SpamTracker clicker;

    @Override
    public int spamClicker() {
        if(clicker == null) return 0;
        return clicker.click(((LocalPlayer) (Object) this).tickCount);
    }

    @Override
    public SpamTracker getClicker(){
        if(clicker == null) return new SpamTracker();
        return clicker;
    }

    @Inject(method = "<init>", at= @At("TAIL"))
    public void addSpamClicker(
        Minecraft client,
        ClientLevel world,
        ClientPacketListener networkHandler,
        StatsCounter stats,
        ClientRecipeBook recipeBook,
        Input lastPlayerInput,
        boolean lastSprinting,
        ChatAbilities chatAbilities,
        CallbackInfo ci
    ){
        clicker = new SpamTracker();
    }

}