package com.beesechurger.flyingfamiliars.entity.ai.goals;

import com.beesechurger.flyingfamiliars.entity.common.familiar.BaseFamiliarEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

import java.util.EnumSet;

public class FamiliarFollowOwnerGoal extends Goal
{
    private final BaseFamiliarEntity familiar;
    private Level level;
    private LivingEntity owner;
    private int timeToRecalcPath;
    private float oldWaterCost;
    private final double speedModifier;
    private final double followDist;

    public FamiliarFollowOwnerGoal(BaseFamiliarEntity familiar, double speedModifier)
    {
        this.familiar = familiar;
        this.level = familiar.level();
        this.speedModifier = speedModifier;
        this.followDist = familiar.getAttributeValue(Attributes.FOLLOW_RANGE);

        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse()
    {
        owner = familiar.getOwner();

        if(familiar.isSitting() || familiar.isLeashed() || familiar.hasRestriction())
            return false;
        else if(owner == null || owner.isSpectator())
            return false;
        else
            return familiar.distanceToSqr(familiar.getOwner()) > 4 * followDist * followDist;
    }

    @Override
    public boolean canContinueToUse()
    {
        if(familiar.isSitting() || familiar.isLeashed() || noPath())
            return false;
        else if(owner == null || owner.isSpectator())
            return false;
        else
            return familiar.distanceToSqr(familiar.getOwner()) > followDist * followDist;
    }

    private boolean noPath()
    {
        return familiar.getNavigation().getPath() == null;
    }

    @Override
    public void start()
    {
        timeToRecalcPath = 0;
        oldWaterCost = familiar.getPathfindingMalus(BlockPathTypes.WATER);
        familiar.setPathfindingMalus(BlockPathTypes.WATER, 0);
    }

    @Override
    public void stop()
    {
        owner = null;
        familiar.getNavigation().stop();
        familiar.setPathfindingMalus(BlockPathTypes.WATER, oldWaterCost);
    }

    @Override
    public void tick()
    {
        if(owner != null)
        {
            familiar.getLookControl().setLookAt(owner, 10.0f, familiar.getMaxHeadXRot());

            if (--timeToRecalcPath <= 0)
            {
                timeToRecalcPath = adjustedTickDelay(10);

                if(familiar.distanceToSqr(owner) > 8 * followDist * followDist * followDist)
                    teleportToOwner();
                else
                    familiar.getMoveControl().setWantedPosition(owner.getX(), owner.getY(), owner.getZ(), speedModifier);
            }
        }
    }

    private boolean teleportToOwner()
    {
        BlockPos blockpos = owner.blockPosition();

        for(int i = 0; i < 10; ++i)
        {
            int j = randomIntInclusive(-3, 3);
            int k = randomIntInclusive(-1, 1);
            int l = randomIntInclusive(-3, 3);
            boolean flag = maybeTeleportTo(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);

            if (flag)
                return true;
        }

        return false;
    }

    private boolean maybeTeleportTo(int currentX, int currentY, int currentZ)
    {
        if(Math.abs((double) currentX - owner.getX()) < 2.0D && Math.abs((double) currentZ - owner.getZ()) < 2.0D)
        {
            return false;
        }
        else if(!canTeleportTo(new BlockPos(currentX, currentY, currentZ)))
        {
            return false;
        }
        else
        {
            familiar.moveTo((double) currentX + 0.5D, (double) currentY, (double) currentZ + 0.5D, familiar.getYRot(), familiar.getXRot());
            familiar.getNavigation().stop();
            return true;
        }
    }

    protected boolean canTeleportTo(BlockPos pos)
    {
        BlockState blockstate = level.getBlockState(pos);
        return	level.isEmptyBlock(pos.above()) && level.isEmptyBlock(pos.above((int) (familiar.getBbHeight() + 1)));
    }

    private int randomIntInclusive(int p_25301_, int p_25302_)
    {
        return familiar.getRandom().nextInt(p_25302_ - p_25301_ + 1) + p_25301_;
    }
}
