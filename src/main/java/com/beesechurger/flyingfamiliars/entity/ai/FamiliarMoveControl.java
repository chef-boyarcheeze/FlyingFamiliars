package com.beesechurger.flyingfamiliars.entity.ai;

import com.beesechurger.flyingfamiliars.entity.common.BaseFamiliarEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

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
            if (!familiar.isFlying())
            {
                super.tick();
                return;
            }

            // custom
            if (!familiar.level.isClientSide() && operation == MoveControl.Operation.MOVE_TO)
            {
                Vec3 dist = new Vec3(wantedX - familiar.getX(), wantedY - familiar.getY(), wantedZ - familiar.getZ());

                if (dist.length() < familiar.getBoundingBox().getSize())
                {
                    this.operation = MoveControl.Operation.WAIT;
                    familiar.setDeltaMovement(familiar.getDeltaMovement().scale(0.8d));
                }
                else
                {
                    familiar.setDeltaMovement(familiar.getDeltaMovement().add(dist.scale(this.speedModifier * 0.08d / dist.length())));

                    familiar.setYRot(rotlerp(familiar.getYRot(), (float) Math.toDegrees(Mth.atan2(dist.z, dist.x)) - 90f,
                            (float) (25 * familiar.getAttributeValue(Attributes.FLYING_SPEED))));
                    familiar.yBodyRot = familiar.getYRot();
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
            if (!familiar.isFlying())
            {
                super.tick();
                return;
            }

            // custom
            if (!familiar.level.isClientSide() && operation == MoveControl.Operation.MOVE_TO)
            {
                Vec3 dist = new Vec3(wantedX - familiar.getX(), wantedY - familiar.getY(), wantedZ - familiar.getZ());

                if (dist.length() < familiar.getBoundingBox().getSize())
                {
                    this.operation = MoveControl.Operation.WAIT;
                    familiar.setDeltaMovement(familiar.getDeltaMovement().scale(0.8d));
                }
                else
                {
                    familiar.setDeltaMovement(familiar.getDeltaMovement().add(dist.scale(this.speedModifier * 0.05d / dist.length())));

                    familiar.setYRot(rotlerp(familiar.getYRot(), (float) Math.toDegrees(Mth.atan2(dist.z, dist.x)) - 90f,
                            (float) (25 * familiar.getAttributeValue(Attributes.FLYING_SPEED))));
                    familiar.yBodyRot = familiar.getYRot();
                }
            }
        }
    }
}
