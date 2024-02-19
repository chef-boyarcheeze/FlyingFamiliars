package com.beesechurger.flyingfamiliars.entity.util;

import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.controller.AnimationController;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.ANIMATION_EMPTY;

public class FFAnimationController<T extends IAnimatable> extends AnimationController<T>
{
    public int progress;
    public int buffer;

    public String currentAnimation = ANIMATION_EMPTY;
    public String previousAnimation = ANIMATION_EMPTY;

    public FFAnimationController(T animatable, String name, float transitionLengthTicks, int bufferLengthTicks, IAnimationPredicate<T> animationPredicate)
    {
        super(animatable, name, transitionLengthTicks, animationPredicate);

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
            return progress < getCurrentAnimation().animationLength + buffer;

        return false;
    }
}
