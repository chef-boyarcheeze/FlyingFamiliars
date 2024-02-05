package com.beesechurger.flyingfamiliars.entity.ai.goals;

import com.beesechurger.flyingfamiliars.entity.common.BaseFamiliarEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

import static com.beesechurger.flyingfamiliars.util.FFValueConstants.BUILDING_LIMIT_LOW;
import static com.beesechurger.flyingfamiliars.util.FFValueConstants.RANDOM_MOVE_CHANCE;

public class FamiliarWanderGoal extends Goal
{
    private BaseFamiliarEntity familiar;
    private Level level;
    private double speed;

    public FamiliarWanderGoal(BaseFamiliarEntity familiar)
    {
        this.setFlags(EnumSet.of(Flag.MOVE));
        this.familiar = familiar;
        this.level = familiar.level;
        this.speed = familiar.getAttributeValue(Attributes.FLYING_SPEED);
    }

    public boolean canUse()
    {
        return familiar.getNavigation().isDone() && familiar.getRandom().nextInt(RANDOM_MOVE_CHANCE) == 0;
    }

    public boolean canContinueToUse()
    {
        return familiar.getNavigation().isInProgress();
    }

    public void start()
    {
        Vec3 vec3 = getPosition();

        if (vec3 != null)
            familiar.getNavigation().moveTo(familiar.getNavigation().createPath(new BlockPos(vec3), 1), 1.0);
    }

    @Nullable
    private Vec3 getPosition()
    {
        Vec3 view = familiar.getViewVector(0.0F);

        if(familiar.getFlyingTime() > 500)
            return getSolidBlockBelow();
        else
        {
            Vec3 position = HoverRandomPos.getPos(familiar, 8, 7, view.x, view.z, Mth.PI / 2, 3, 1);
            return position != null ? position : AirAndWaterRandomPos.getPos(familiar, 8, 4, -2, view.x, view.z, Mth.PI / 2);
        }
    }

    private Vec3 getSolidBlockBelow()
    {
        BlockPos candidate = new BlockPos(familiar.position());

        while(candidate.getY() > BUILDING_LIMIT_LOW)
        {
            candidate = candidate.below();

            if(!level.isEmptyBlock(candidate) && level.getBlockState(candidate).getMaterial().isSolidBlocking() && !isTargetBlocked(Vec3.atCenterOf(candidate.above())))
                return Vec3.atCenterOf(candidate);
        }

        return familiar.position();
    }

    public boolean isTargetBlocked(Vec3 target)
    {
        Vec3 Vector3d = new Vec3(familiar.getX(), familiar.getEyeY(), familiar.getZ());
        return level.clip(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, familiar)).getType() != HitResult.Type.MISS;
    }
}