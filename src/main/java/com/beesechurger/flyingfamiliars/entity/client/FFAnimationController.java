package com.beesechurger.flyingfamiliars.entity.client;

import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationController;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.ANIMATION_EMPTY;

public class FFAnimationController<T extends GeoAnimatable> extends AnimationController<T>
{
    public int progress;
    public int buffer;

    public String currentAnimation = ANIMATION_EMPTY;
    public String previousAnimation = ANIMATION_EMPTY;

    public FFAnimationController(T animatable, String name, int transitionLengthTicks, int bufferLengthTicks, AnimationStateHandler handler)
    {
        super(animatable, name, transitionLengthTicks, handler);

        this.progress = 0;
        this.buffer = bufferLengthTicks;
    }

    public void updateProgress()
    {
        if(isAnimInProgress())
            progress++;
    }

    public void updateCurrentAnimation(String animation)
    {
        currentAnimation = animation;
    }

    public void updatePreviousAnimation()
    {
        previousAnimation = currentAnimation;
    }

    public void resetProgress()
    {
        progress = 0;
    }

    public boolean hasAnimChanged()
    {
        return currentAnimation != previousAnimation;
    }

    public boolean isAnimInProgress()
    {
        if(getCurrentAnimation() != null)
            return progress < getCurrentAnimation().animation().length() + buffer;

        return false;
    }
}
