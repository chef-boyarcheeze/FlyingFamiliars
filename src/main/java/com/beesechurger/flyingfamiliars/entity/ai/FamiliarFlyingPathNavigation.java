package com.beesechurger.flyingfamiliars.entity.ai;

import com.beesechurger.flyingfamiliars.entity.common.BaseFamiliarEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.level.Level;

public class FamiliarFlyingPathNavigation extends FlyingPathNavigation
{
    public FamiliarFlyingPathNavigation(BaseFamiliarEntity familiar, Level level)
    {
        super(familiar, level);
    }

    @Override
    public void tick()
    {
        if (!isDone() && canUpdatePath())
        {
            BaseFamiliarEntity familiar = (BaseFamiliarEntity) mob;

            BlockPos target = getTargetPos();
            if (target != null)
            {
                mob.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), 1.0f);

                maxDistanceToWaypoint = mob.getBbWidth() * mob.getBbWidth();
                Vec3i position = new Vec3i(getTempMobPos().x, getTempMobPos().y, getTempMobPos().z);

                if (target.distSqr(position) <= maxDistanceToWaypoint) path = null;
            }
        }
    }

    @Override
    public boolean isStableDestination(BlockPos pos)
    {
        return true;
    }
}
