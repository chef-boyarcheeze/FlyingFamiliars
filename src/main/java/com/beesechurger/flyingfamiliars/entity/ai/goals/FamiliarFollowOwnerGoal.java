package com.beesechurger.flyingfamiliars.entity.ai.goals;

import com.beesechurger.flyingfamiliars.entity.common.BaseFamiliarEntity;
import com.beesechurger.flyingfamiliars.util.FFEnumValues;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

import java.util.EnumSet;

public class FamiliarFollowOwnerGoal extends Goal
{
    private final BaseFamiliarEntity familiar;
    private final double speed;
    Level world;
    float endFollow;
    float beginFollow;
    private LivingEntity owner;
    private int timeToRecalcPath;
    private float oldWaterCost;

    public FamiliarFollowOwnerGoal(BaseFamiliarEntity familiar, double speed, float beginFollow, float endFollow)
    {
        this.familiar = familiar;
        this.world = familiar.level;
        this.speed = speed * familiar.getAttributeValue(Attributes.FLYING_SPEED);
        this.beginFollow = beginFollow;
        this.endFollow = endFollow;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse()
    {
        LivingEntity owner = this.familiar.getOwner();
        if(owner != null)
        {
            if(!((owner instanceof Player && owner.isSpectator())
                    || familiar.isOrderedToSit()
                    || familiar.distanceToSqr(owner) < beginFollow * beginFollow))
            {
                this.owner = owner;
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean canContinueToUse()
    {
        if(super.canContinueToUse() && !noPath()
            && familiar.distanceToSqr(owner) > endFollow * endFollow
            && !familiar.isOrderedToSit())
        {
            this.owner = owner;
            return true;
        }

        return false;
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

        if(familiar.getGoalStatus() != FFEnumValues.FamiliarStatus.FOLLOWING)
            familiar.setGoalStatus(FFEnumValues.FamiliarStatus.FOLLOWING);
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
        if(!familiar.isFlying())
        {
            familiar.startFlying();
            return;
        }

        if(owner != null)
        {
            familiar.getLookControl().setLookAt(owner, 10.0f, familiar.getMaxHeadXRot());

            if (--timeToRecalcPath <= 0)
            {
                timeToRecalcPath = adjustedTickDelay(10);

                if(familiar.distanceToSqr(owner) <= beginFollow * beginFollow)
                {
                    familiar.getNavigation().stop();
                }
                else if(familiar.distanceToSqr(owner) > Math.pow(beginFollow, 3))
                {
                    teleportToOwner();
                }
                else
                {
                    familiar.getNavigation().moveTo(owner.getX(), owner.getY(), owner.getZ(), speed);
                    //familiar.getMoveControl().setWantedPosition(owner.getX(), owner.getY() + owner.getEyeHeight(), owner.getZ(), speed);
                }
            }
        }
    }

    private void teleportToOwner()
    {
        BlockPos blockpos = owner.blockPosition();

        for(int i = 0; i < 10; ++i)
        {
            int j = randomIntInclusive(-3, 3);
            int k = randomIntInclusive(-1, 1);
            int l = randomIntInclusive(-3, 3);
            boolean flag = maybeTeleportTo(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);

            if (flag)
                return;
        }
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
        BlockState blockstate = world.getBlockState(pos);
        return	world.isEmptyBlock(pos.above()) && world.isEmptyBlock(pos.above((int) (familiar.getBbHeight() + 1)));
    }

    private int randomIntInclusive(int p_25301_, int p_25302_)
    {
        return familiar.getRandom().nextInt(p_25302_ - p_25301_ + 1) + p_25301_;
    }
}
