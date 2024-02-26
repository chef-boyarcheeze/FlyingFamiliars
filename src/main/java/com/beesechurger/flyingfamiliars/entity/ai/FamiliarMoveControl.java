package com.beesechurger.flyingfamiliars.entity.ai;

import com.beesechurger.flyingfamiliars.entity.common.BaseFamiliarEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FamiliarMoveControl
{
    public static class FlightControl extends MoveControl
    {
        private final BaseFamiliarEntity familiar;

        public FlightControl(BaseFamiliarEntity familiar)
        {
            super(familiar);
            this.familiar = familiar;
        }

        @Override
        public void tick()
        {
            if (familiar.canBeControlledByRider())
            {
                operation = Operation.WAIT;
                return;
            }
            else if (operation == Operation.MOVE_TO)
            {
                double speed = familiar.getAttributeValue(Attributes.FLYING_SPEED) * familiar.getFlySpeedMod() * speedModifier;
                Vec3 dist = new Vec3(wantedX - familiar.getX(), wantedY - familiar.getY(), wantedZ - familiar.getZ());

                if (dist.length() < familiar.getBbWidth())
                {
                    operation = Operation.WAIT;
                    familiar.setDeltaMovement(familiar.getDeltaMovement().scale(0.8d));
                }
                else
                {
                    familiar.setYRot(rotlerp(familiar.getYRot(), (float) Math.toDegrees(Mth.atan2(dist.z, dist.x)) - 90f, (float) speed * 200f));
                    familiar.yBodyRot = familiar.getYRot();

                    familiar.setDeltaMovement(familiar.getDeltaMovement().add(dist.scale(speed / (10f * dist.length()))));
                }
            }
        }
    }

    public static class WalkControl extends MoveControl
    {
        private final BaseFamiliarEntity familiar;

        public WalkControl(BaseFamiliarEntity familiar)
        {
            super(familiar);
            this.familiar = familiar;
        }

        @Override
        public void tick()
        {
            if (familiar.canBeControlledByRider())
            {
                operation = Operation.WAIT;
                return;
            }
            else if (operation == Operation.MOVE_TO)
            {
                double speed = familiar.getAttributeValue(Attributes.MOVEMENT_SPEED) * familiar.getWalkSpeedMod() * speedModifier;
                Vec3 dist = new Vec3(wantedX - familiar.getX(), wantedY - familiar.getY(), wantedZ - familiar.getZ());

                if (dist.length() < familiar.getBbWidth())
                {
                    familiar.setZza(0f);
                    return;
                }

                if(dist.y > 1 || dist.length() > 4 * familiar.getAttributeValue(Attributes.FOLLOW_RANGE))
                    familiar.startFlying();

                BlockPos blockpos = familiar.blockPosition();
                BlockState state = familiar.level().getBlockState(blockpos);
                Block block = state.getBlock();
                VoxelShape voxelshape = state.getCollisionShape(familiar.level(), blockpos);

                if (dist.y > (double) familiar.maxUpStep() && dist.x * dist.x + dist.z * dist.z < (double) Math.max(1.0f, familiar.getBbWidth()) || !voxelshape.isEmpty() && familiar.getY() < voxelshape.max(Direction.Axis.Y) + (double) blockpos.getY() && !state.is(BlockTags.DOORS) && !state.is(BlockTags.FENCES))
                {
                    familiar.getJumpControl().jump();
                    operation = MoveControl.Operation.JUMPING;
                }

                familiar.setYRot(rotlerp(familiar.getYRot(), (float) Math.toDegrees(Mth.atan2(dist.z, dist.x)) - 90f, (float) speed * 200f));
                familiar.setSpeed((float) speed);
                operation = Operation.WAIT;
            }
            else
            {
                familiar.setSpeed(0);
                familiar.setXxa(0);
                familiar.setYya(0);
                familiar.setZza(0);
            }
        }
    }
}
