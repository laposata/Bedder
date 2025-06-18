package com.dreamtea.bedder_mod.mixin.client;

import com.dreamtea.bedder_mod.SpamTracker;
import com.dreamtea.bedder_mod.imixin.ISpamClick;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.stat.StatHandler;
import net.minecraft.util.PlayerInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin implements ISpamClick {

    @Unique
    SpamTracker clicker;

    @Override
    public int spamClicker() {
        if(clicker == null) return 0;
        return clicker.click(((ClientPlayerEntity) (Object) this).age);
    }

    @Override
    public SpamTracker getClicker(){
        if(clicker == null) return new SpamTracker();
        return clicker;
    }

    @Inject(method = "<init>", at= @At("TAIL"))
    public void addSpamClicker(MinecraftClient client, ClientWorld world, ClientPlayNetworkHandler networkHandler, StatHandler stats, ClientRecipeBook recipeBook, PlayerInput lastPlayerInput, boolean lastSprinting, CallbackInfo ci){
        clicker = new SpamTracker();
    }

}