package com.beesechurger.flyingfamiliars.entity.ai.goals;

import com.beesechurger.flyingfamiliars.entity.common.BaseFamiliarEntity;
import com.beesechurger.flyingfamiliars.util.FFEnumValues;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class FamiliarWanderGoalOld extends WaterAvoidingRandomStrollGoal
{
    private final BaseFamiliarEntity familiar;
    private final double speed;
    private int timeToRecalcPath;

    public FamiliarWanderGoalOld(BaseFamiliarEntity familiar, double speed)
    {
        super(familiar, speed);

        this.familiar = familiar;
        this.speed = speed * familiar.getAttributeValue(Attributes.FLYING_SPEED);
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse()
    {
        return !familiar.isSitting() && familiar.getNavigation().isDone() && familiar.getRandom().nextInt(10) == 0;
    }

    @Override
    public boolean canContinueToUse()
    {
        return !familiar.isSitting() && familiar.getNavigation().isInProgress();
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
        if(familiar.getRandom().nextFloat() <= 0.05)
        {
            Vec3 goal = getPosition();

            if(goal != null)
            {
                if(!familiar.isFlying())
                {
                    familiar.startFlying();
                    return;
                }

                familiar.getLookControl().setLookAt(goal);

                if (--timeToRecalcPath <= 0)
                {
                    timeToRecalcPath = adjustedTickDelay(10);
                    familiar.getNavigation().moveTo(goal.x, goal.y, goal.z, speed);
                    //familiar.getMoveControl().setWantedPosition(goal.x, goal.y, goal.z, speed);
                }
            }
        }
    }

    @Override
    public Vec3 getPosition()
    {
        Vec3 goal = null;

        if(!familiar.isFlying() || familiar.getFlyingTime() > 500)
            goal = LandRandomPos.getPos(familiar, 30, 30);
        else
        {
            Vec3 vec3 = familiar.getLookAngle();
            if (!familiar.isWithinRestriction())
                vec3 = vec3.atLowerCornerOf(familiar.getRestrictCenter()).subtract(familiar.position()).normalize();

            goal = AirRandomPos.getPosTowards(familiar, 30, 30, 10, vec3, Math.PI / 2);
        }

        if (goal != null && goal.y > familiar.getY() + familiar.getBbHeight() && !familiar.isFlying())
            familiar.startFlying();

        return goal == null ? super.getPosition() : goal;
    }
}
