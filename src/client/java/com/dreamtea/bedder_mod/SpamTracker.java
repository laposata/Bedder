package com.dreamtea.bedder_mod;

import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.joml.Matrix3x2fStack;


public class SpamTracker {
    private enum Mode {
        BED(20),
        BUILD(60),
        NONE(0);
        public final int TIMEOUT;
        private Mode(int timeout){
            this.TIMEOUT = timeout;
        }
    }
    private int clickCount = 0;
    private int tickOfLastClick;
    private Component spammedText;
    private Mode mode = Mode.NONE;
    public boolean active(){
        return clickCount != 0;
    }
    public void setBedMode(){
        this.mode = Mode.BED;
    }
    public void setBuildMode(){
        this.mode = Mode.BUILD;
    }
    public int click(int tick){
        if(tick - tickOfLastClick > mode.TIMEOUT){
            clickCount = 0;
            mode = Mode.NONE;
        } else {
            clickCount ++;
        }
        tickOfLastClick = tick;
        return clickCount;
    }

    public void setSpammedText(Component text){
        if(spammedText != null && spammedText.contains(text)){
            return;
        }
        if(spammedText != null && !spammedText.contains(text)){
            mode = Mode.NONE;
        }
        spammedText = text;
        clickCount = 0;
    }
    public float sizeScale(){
        return switch (mode){
            case BED -> Mth.clamp(1 + (clickCount / 25f), 1, 6f);
            case BUILD -> 1f;
            default -> 0;
        };

    }

    public float yScale(){
        return switch (mode){
            case BED -> 1 + (clickCount / 12f);
            case BUILD -> 0.0F;
            default -> 0;
        };
    }

    public int xTranslate(){
        return switch (mode){
            case BED -> Mth.clamp(clickCount / 2, 0, 28);
            case BUILD -> 0;
            default -> 0;
        };
    }

    public void matricesTranslate(Matrix3x2fStack matrices){
        switch (mode){
            case BED -> {
                matrices.scale(sizeScale(), yScale());
                matrices.translate(xTranslate(), -Mth.clamp(clickCount/25f, 0, 2));
            }
            case BUILD -> {
                matrices.translate(xTranslate(), -(clickCount));
            }
        }
    }

    public int getColor(){
        int scale = Mth.clamp(clickCount, 0, 255);
        return ARGB.color(255,255 - Mth.clamp(clickCount - 255, 0, 150), 255 - scale, 255 - scale);
    }
}
