package com.beesechurger.flyingfamiliars.entity.ai.goals;

import com.beesechurger.flyingfamiliars.entity.common.BaseFamiliarEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

import java.util.EnumSet;

public class FamiliarWanderGoal extends WaterAvoidingRandomFlyingGoal
{
    private final BaseFamiliarEntity familiar;
    private final double speed;
    private int timeToRecalcPath;

    public FamiliarWanderGoal(BaseFamiliarEntity familiar, double speed)
    {
        super(familiar, speed);

        this.familiar = familiar;
        this.speed = speed * familiar.getAttributeValue(Attributes.FLYING_SPEED);
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse()
    {
        return super.canUse() && !familiar.isSitting();
    }

    @Override
    public boolean canContinueToUse()
    {
        return super.canContinueToUse() && !familiar.isSitting();
    }

    @Override
    public void start()
    {
        timeToRecalcPath = 0;
        familiar.setPathfindingMalus(BlockPathTypes.WATER, 0);
    }

    @Override
    public void stop()
    {
        familiar.getNavigation().stop();
    }

    @Override
    public void tick()
    {
        if(!familiar.isFlying())
            familiar.startFlying();

        System.out.println(getPosition().x + " " + getPosition().y + " " + getPosition().z);

        if (--timeToRecalcPath <= 0)
        {
            timeToRecalcPath = adjustedTickDelay(10);
            familiar.getMoveControl().setWantedPosition(getPosition().x, getPosition().y, getPosition().z, speed);
        }
    }
}
