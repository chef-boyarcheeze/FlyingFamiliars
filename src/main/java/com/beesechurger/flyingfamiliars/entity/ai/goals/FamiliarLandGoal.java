package com.beesechurger.flyingfamiliars.entity.ai.goals;

import com.beesechurger.flyingfamiliars.entity.common.BaseFamiliarEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.fluids.FluidAttributes;

import java.util.EnumSet;

public class FamiliarLandGoal extends Goal
{
    private final BaseFamiliarEntity familiar;
    private final double speed;
    private final int landingSearchDistance;
    private int timeToRecalcPath;
    private BlockPos.MutableBlockPos goal;
    private int distanceToGround = 0;
    private Level world;

    public FamiliarLandGoal(BaseFamiliarEntity familiar, double speed, int landingSearchDistance)
    {
        this.familiar = familiar;
        this.world = familiar.level;
        this.speed = speed * familiar.getAttributeValue(Attributes.FLYING_SPEED);
        this.landingSearchDistance = landingSearchDistance;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse()
    {
        return familiar.isFlying()
                && (familiar.getLandTimer() == 0 || distanceToGround <= 1);
    }

    @Override
    public boolean canContinueToUse()
    {
        return !noPath()
                && familiar.isFlying()
                && (familiar.getLandTimer() == 0 || distanceToGround <= 1)
                && goal != null;
    }

    private void solidBlockBelow()
    {
        for(int i = 0; i < landingSearchDistance; i++)
        {
            var block = familiar.blockPosition().mutable().move(0, -i, 0);

            if(familiar.isFlying() && !world.getBlockState(block).isAir())
            {
                goal = block;
                distanceToGround = i;
                break;
            }
        }
    }

    private boolean noPath()
    {
        return familiar.getNavigation().getPath() == null;
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
        goal = null;
        familiar.getNavigation().stop();
    }

    @Override
    public void tick()
    {
        solidBlockBelow();

        if (--timeToRecalcPath <= 0)
        {
            timeToRecalcPath = adjustedTickDelay(10);

            if(goal != null)
            {
                if(!familiar.isFlying())
                {
                    familiar.getNavigation().stop();
                }
                else
                {
                    familiar.getMoveControl().setWantedPosition(goal.getX(), goal.getY(), goal.getZ(), speed);
                }
            }
        }
    }
}
