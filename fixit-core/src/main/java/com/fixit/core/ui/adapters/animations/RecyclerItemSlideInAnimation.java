package com.fixit.core.ui.adapters.animations;

import android.content.Context;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import com.fixit.core.config.AppConfig;

import java.security.SecureRandom;

/**
 * Created by Kostyantin on 5/30/2016.
 */
public class RecyclerItemSlideInAnimation implements RecyclerItemAnimation {

    private Direction direction;
    private long duration;
    private boolean fade;

    public RecyclerItemSlideInAnimation(Direction direction, long duration, boolean fade) {
        this.direction = direction;
        this.duration = duration;
        this.fade = fade;
    }

    public RecyclerItemSlideInAnimation(Direction direction, long duration) {
        this(direction, duration, false);
    }

    public RecyclerItemSlideInAnimation(Context context, Direction direction, boolean fade) {
        this(direction, AppConfig.getInteger(context, "default_recycler_item_animation_time_millis", 1000), fade);
    }

    public RecyclerItemSlideInAnimation(Context context, Direction direction) {
        this(context, direction, false);
    }

    @Override
    public Animation getAnimation() {
        float fromX = 0F;
        float fromY = 0F;

        switch (direction) {
            case LEFT:
                fromX = -1F;
                break;
            case RIGHT:
                fromX = 1F;
                break;
            case TOP:
                fromY = -1F;
                break;
            case BOTTOM:
                fromY = 1F;
                break;
        }

        TranslateAnimation translate = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, fromX,
                    Animation.RELATIVE_TO_SELF, 0F,
                    Animation.RELATIVE_TO_SELF, fromY,
                    Animation.RELATIVE_TO_SELF, 0F
                );
        translate.setDuration(duration);

        if(!fade) {
            return translate;
        } else {
            AlphaAnimation alpha = new AlphaAnimation(0F, 1F);
            translate.setDuration(duration * 2);

            AnimationSet animSet = new AnimationSet(true);
            animSet.addAnimation(translate);
            animSet.addAnimation(alpha);

            return animSet;
        }
    }

    public enum Direction {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM;

        public static Direction getRandom() {
            SecureRandom random = new SecureRandom();
            Direction[] values = Direction.values();
            return values[random.nextInt(values.length)];
        }
    }
}
