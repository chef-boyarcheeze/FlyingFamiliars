package com.beesechurger.flyingfamiliars.entity.ai.goals;

import com.beesechurger.flyingfamiliars.entity.common.familiar.BaseFamiliarEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

import static com.beesechurger.flyingfamiliars.util.FFConstants.BUILDING_LIMIT_LOW;
import static com.beesechurger.flyingfamiliars.util.FFConstants.RANDOM_MOVE_CHANCE;

public class FamiliarWanderGoal extends Goal
{
    private BaseFamiliarEntity familiar;
    private Level level;
    private final double speedModifier;

    public FamiliarWanderGoal(BaseFamiliarEntity familiar, double speedModifier)
    {
        this.familiar = familiar;
        this.speedModifier = speedModifier;
        this.level = familiar.level();

        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
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
            familiar.getNavigation().moveTo(vec3.x, vec3.y, vec3.z, speedModifier);
    }

    @Nullable
    private Vec3 getPosition()
    {
        Vec3 view = familiar.getViewVector(0.0F);

        if(familiar.getFlyingTime() > 300 || !familiar.isFlying())
            return LandRandomPos.getPos(familiar, 25, 40);
        else
        {
            Vec3 position = HoverRandomPos.getPos(familiar, (int) (5 * familiar.getBbWidth()), (int) (5 * familiar.getBbWidth()), view.x, view.z, Mth.PI / 2, (int) (3 * familiar.getBbWidth()), (int) (familiar.getBbWidth()));
            return position != null ? position : AirAndWaterRandomPos.getPos(familiar, (int) (5 * familiar.getBbWidth()), (int) (5 * familiar.getBbWidth()), -(int) (2 * familiar.getBbWidth()), view.x, view.z, Mth.PI / 2);
        }
    }

    private Vec3 getSolidBlockBelow()
    {
        BlockPos candidate = familiar.blockPosition();

        while(candidate.getY() > BUILDING_LIMIT_LOW)
        {
            candidate = candidate.below();

            if(!level.isEmptyBlock(candidate) && level.getBlockState(candidate).isSolid() && !isTargetBlocked(Vec3.atCenterOf(candidate.above())))
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