package com.beesechurger.flyingfamiliars.entity.ai;

import com.beesechurger.flyingfamiliars.entity.common.BaseFamiliarEntity;
import com.beesechurger.flyingfamiliars.util.FFEnumValues;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.phys.Vec3;

import static com.beesechurger.flyingfamiliars.entity.common.BaseFamiliarEntity.BEGIN_FOLLOW_DISTANCE;

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
            /*if (!familiar.isFlying())
            {
                super.tick();
                return;
            }*/

            if(!familiar.level.isClientSide() && operation == Operation.MOVE_TO)
            {
                operation = MoveControl.Operation.WAIT;
                familiar.setNoGravity(true);

                double speed = familiar.getAttributeValue(Attributes.FLYING_SPEED);

                float distX = (float) (wantedX - familiar.getX());
                float distY = (float) (wantedY - familiar.getY());
                float distZ = (float) (wantedZ - familiar.getZ());

                double planeDist = Math.sqrt(distX * distX + distZ * distZ);
                double yDistMod = 1.0D - (double) Mth.abs(distY * 0.7F) / planeDist;

                distX = (float) ((double) distX * yDistMod);
                distZ = (float) ((double) distZ * yDistMod);

                planeDist = Mth.sqrt(distX * distX + distZ * distZ);

                double dist = Math.sqrt(distX * distX + distY * distY + distZ * distZ);
                if(familiar.getGoalStatus() == FFEnumValues.FamiliarStatus.FOLLOWING && dist > BEGIN_FOLLOW_DISTANCE)
                {
                    float yaw = (float) Math.toDegrees(Mth.atan2(distZ, distX)) - 90.0f;
                    float pitch = (float) -Math.toDegrees(Mth.atan2(-distY, planeDist));

                    familiar.setYRot(rotlerp(familiar.getYRot(), yaw, (float) (3 * 10 * familiar.getAttributeValue(Attributes.FLYING_SPEED))));
                    familiar.setXRot(pitch);

                    double xAddVector = Math.cos(Math.toRadians(familiar.getYRot() + 90.0f)) * Math.abs((double) distX / dist);
                    double yAddVector = Math.sin(Math.toRadians(pitch)) * Math.abs((double) distY / dist);
                    double zAddVector = Math.sin(Math.toRadians(familiar.getYRot() + 90.0f)) * Math.abs((double) distZ / dist);

                    xAddVector = dist > BEGIN_FOLLOW_DISTANCE ? xAddVector : 0;
                    zAddVector = dist > BEGIN_FOLLOW_DISTANCE ? zAddVector : 0;

                    xAddVector = Math.abs(xAddVector) > speed * speedModifier ? xAddVector < 0 ? -speed * speedModifier : speed * speedModifier : xAddVector;
                    yAddVector = Math.abs(yAddVector) > speed * speedModifier ? yAddVector < 0 ? -speed * speedModifier : speed * speedModifier : yAddVector;
                    zAddVector = Math.abs(zAddVector) > speed * speedModifier ? zAddVector < 0 ? -speed * speedModifier : speed * speedModifier : zAddVector;

                    familiar.setDeltaMovement(familiar.getDeltaMovement().add(xAddVector, yAddVector, zAddVector));
                }
            }
        }
    }

    static class WalkControl extends MoveControl
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
            if(!familiar.isFlying())
            {
                super.tick();
                return;
            }

            if(operation == Operation.MOVE_TO)
            {
                operation = MoveControl.Operation.WAIT;
                familiar.setNoGravity(true);

                double speed = familiar.getAttributeValue(Attributes.FLYING_SPEED);

                float distX = (float) (wantedX - familiar.getX());
                float distY = (float) (wantedY - familiar.getY());
                float distZ = (float) (wantedZ - familiar.getZ());

                double planeDist = Math.sqrt(distX * distX + distZ * distZ);
                double yDistMod = 1.0D - (double) Mth.abs(distY * 0.7F) / planeDist;

                distX = (float) ((double) distX * yDistMod);
                distZ = (float) ((double) distZ * yDistMod);

                planeDist = Mth.sqrt(distX * distX + distZ * distZ);

                double dist = Math.sqrt(distX * distX + distY * distY + distZ * distZ);
                if (dist > 1.0f)
                {
                    float yaw = (float) Math.toDegrees(Mth.atan2(distZ, distX)) - 90.0f;
                    float pitch = (float) -Math.toDegrees(Mth.atan2(-distY, planeDist));

                    if(dist > BEGIN_FOLLOW_DISTANCE)
                        familiar.setYRot(rotlerp(familiar.getYRot(), yaw, (float) (3 * 10 * familiar.getAttributeValue(Attributes.FLYING_SPEED))));
                    familiar.setXRot(pitch);

                    double xAddVector = Math.cos(Math.toRadians(familiar.getYRot() + 90.0f)) * Math.abs((double) distX / dist);
                    double yAddVector = Math.sin(Math.toRadians(pitch)) * Math.abs((double) distY / dist);
                    double zAddVector = Math.sin(Math.toRadians(familiar.getYRot() + 90.0f)) * Math.abs((double) distZ / dist);

                    xAddVector = dist > BEGIN_FOLLOW_DISTANCE ? xAddVector : 0;
                    zAddVector = dist > BEGIN_FOLLOW_DISTANCE ? zAddVector : 0;

                    xAddVector = Math.abs(xAddVector) > speed * speedModifier ? xAddVector < 0 ? -speed * speedModifier : speed * speedModifier : xAddVector;
                    yAddVector = Math.abs(yAddVector) > speed * speedModifier ? yAddVector < 0 ? -speed * speedModifier : speed * speedModifier : yAddVector;
                    zAddVector = Math.abs(zAddVector) > speed * speedModifier ? zAddVector < 0 ? -speed * speedModifier : speed * speedModifier : zAddVector;

                    familiar.setDeltaMovement(familiar.getDeltaMovement().add(xAddVector, yAddVector, zAddVector));
                }
            }
        }
    }
}
