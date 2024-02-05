package com.beesechurger.flyingfamiliars.entity.ai;

import com.beesechurger.flyingfamiliars.entity.common.BaseFamiliarEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
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
        private final double speed;

        public FlightControl(BaseFamiliarEntity familiar)
        {
            super(familiar);
            this.familiar = familiar;
            this.speed = familiar.getAttributeValue(Attributes.FLYING_SPEED);
        }

        @Override
        public void tick()
        {
            if (operation == Operation.MOVE_TO)
            {
                Vec3 dist = new Vec3(wantedX - familiar.getX(), wantedY - familiar.getY(), wantedZ - familiar.getZ());

                if (dist.length() < familiar.getBbWidth())
                {
                    this.operation = MoveControl.Operation.WAIT;
                    familiar.setDeltaMovement(familiar.getDeltaMovement().scale(0.8d));
                }
                else
                {
                    familiar.setYRot(rotlerp(familiar.getYRot(), (float) Math.toDegrees(Mth.atan2(dist.z, dist.x)) - 90f, (float) (25 * speed)));
                    familiar.yBodyRot = familiar.getYRot();

                    familiar.setDeltaMovement(familiar.getDeltaMovement().add(dist.scale(speed / (20 * dist.length()))));
                }
            }
        }
    }

    public static class WalkControl extends MoveControl
    {
        private final BaseFamiliarEntity familiar;
        private final double speed;

        public WalkControl(BaseFamiliarEntity familiar)
        {
            super(familiar);
            this.familiar = familiar;
            this.speed = familiar.getAttributeValue(Attributes.MOVEMENT_SPEED);
        }

        @Override
        public void tick()
        {
            if (operation == Operation.MOVE_TO)
            {
                Vec3 dist = new Vec3(wantedX - familiar.getX(), wantedY - familiar.getY(), wantedZ - familiar.getZ());

                if (dist.length() < familiar.getBbWidth())
                {
                    this.operation = MoveControl.Operation.WAIT;
                    familiar.setDeltaMovement(familiar.getDeltaMovement().scale(0.8d));
                }
                else
                {
                    familiar.setYRot(rotlerp(familiar.getYRot(), (float) Math.toDegrees(Mth.atan2(dist.z, dist.x)) - 90f, (float) (25 * speed)));
                    familiar.yBodyRot = familiar.getYRot();

                    familiar.setDeltaMovement(familiar.getDeltaMovement().add(dist.scale(speed / (20 * dist.length()))));

                    BlockPos blockpos = familiar.blockPosition();
                    BlockState blockstate = familiar.level.getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate.getCollisionShape(familiar.level, blockpos);

                    if (dist.y > (double) familiar.maxUpStep && dist.x * dist.x + dist.z * dist.z < (double) Math.max(1.0F, familiar.getBbWidth())
                            || !voxelshape.isEmpty() && familiar.getY() < voxelshape.max(Direction.Axis.Y) + (double) blockpos.getY())
                    {
                        familiar.getJumpControl().jump();
                        this.operation = MoveControl.Operation.JUMPING;
                    }
                }
            }
            else if (this.operation == MoveControl.Operation.JUMPING)
            {
                familiar.setSpeed((float) (this.speedModifier * familiar.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                if (familiar.isOnGround())
                    this.operation = MoveControl.Operation.WAIT;
            }
            else
                familiar.setZza(0.0F);
        }
    }
}
