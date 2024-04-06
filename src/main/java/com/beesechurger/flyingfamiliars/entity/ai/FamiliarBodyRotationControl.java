package com.beesechurger.flyingfamiliars.entity.ai;

import com.beesechurger.flyingfamiliars.entity.common.familiar.BaseFamiliarEntity;
import com.beesechurger.flyingfamiliars.registries.FFKeys;
import com.beesechurger.flyingfamiliars.util.FFEnumValues;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.control.BodyRotationControl;

public class FamiliarBodyRotationControl extends BodyRotationControl
{
    private final BaseFamiliarEntity familiar;

    private final FFEnumValues.FamiliarMoveTypes rotationType;
    private final double angleLimit;
    private final double angleInterval;
    private int headStableTime;
    private float lastStableYHeadRot;

    public FamiliarBodyRotationControl(BaseFamiliarEntity familiar, FFEnumValues.FamiliarMoveTypes rotationType, double angleLimit, double angleInterval)
    {
        super(familiar);
        this.familiar = familiar;
        this.rotationType = rotationType;
        this.angleLimit = angleLimit;
        this.angleInterval = angleInterval;
    }

    @Override
    public void clientTick()
    {
        if(familiar.notCarryingPassengers())
            rotationAI();
        else
            switch(rotationType)
            {
                case HOVER -> rotationHover();
                case FORWARD -> rotationForward();
                default -> rotationNone();
            }

        if(familiar.isMoving())
        {
            familiar.yBodyRot = familiar.getYRot();
            rotateHeadIfNecessary();
            lastStableYHeadRot = familiar.yHeadRot;
            headStableTime = 0;
        }
        else
        {
            if(familiar.notCarryingPassengers())
            {
                if(Math.abs(familiar.yHeadRot - lastStableYHeadRot) > 15.0F)
                {
                    headStableTime = 0;
                    lastStableYHeadRot = familiar.yHeadRot;
                    rotateBodyIfNecessary();
                }
                else
                {
                    ++headStableTime;
                    if(headStableTime > 10)
                        rotateHeadTowardsFront();
                }
            }
        }
    }

    private void rotationHover()
    {
        LivingEntity driver = (LivingEntity) familiar.getControllingPassenger();

        float forwardMove = driver != null ? driver.zza : familiar.zza;
        float sideMove = driver != null ? driver.xxa : familiar.xxa;

        if(familiar.isFlying() && driver != null)
        {
            if(forwardMove > 0)
                incrementPitch();
            else if(forwardMove < 0)
                decrementPitch();
            else
                centerPitch();

            if(sideMove > 0)
                incrementRoll();
            else if(sideMove < 0)
                decrementRoll();
            else
                centerRoll();
        }
        else
        {
            centerPitch();
            centerRoll();
        }
    }

    private void rotationForward()
    {
        LivingEntity driver = (LivingEntity) familiar.getControllingPassenger();

        float wantedRotY;

        if (driver != null)
            wantedRotY = driver.getYRot();
        else
            wantedRotY = familiar.getYRot();

        float yRotDifference = Mth.wrapDegrees(familiar.getYRot() - wantedRotY);

        if(familiar.isFlying() && driver != null)
        {
            if(FFKeys.FAMILIAR_ASCEND.isDown() && FFKeys.FAMILIAR_DESCEND.isDown())
                centerPitch();
            else if(FFKeys.FAMILIAR_ASCEND.isDown())
                decrementPitch();
            else if(FFKeys.FAMILIAR_DESCEND.isDown())
                incrementPitch();
            else
                centerPitch();

            if(yRotDifference > 0.2f * angleLimit)
                incrementRoll();
            else if(yRotDifference < -0.2f * angleLimit)
                decrementRoll();
            else
                centerRoll();
        }
        else
        {
            centerPitch();
            centerRoll();
        }
    }

    private void rotationNone()
    {
        centerPitch();
        centerRoll();
    }

    private void rotationAI()
    {
        centerPitch();

        if(familiar.isFlying() && familiar.isMoving())
        {
            float yRotDifference = Mth.wrapDegrees(familiar.yRotO - familiar.getYRot());

            if(yRotDifference > 0.2f * angleLimit)
                incrementRoll();
            else if(yRotDifference < -0.2f * angleLimit)
                decrementRoll();
            else
                centerRoll();
        }
        else
            centerRoll();
    }

    private void incrementPitch()
    {
        familiar.pitchO = familiar.pitch;
        if(familiar.pitch < angleLimit)
            familiar.pitch += angleInterval;
    }

    private void decrementPitch()
    {
        familiar.pitchO = familiar.pitch;
        if(familiar.pitch > -angleLimit)
            familiar.pitch -= angleInterval;
    }

    private void incrementRoll()
    {
        familiar.rollO = familiar.roll;
        if(familiar.roll < angleLimit)
            familiar.roll += angleInterval;
    }

    private void decrementRoll()
    {
        familiar.rollO = familiar.roll;
        if(familiar.roll > -angleLimit)
            familiar.roll -= angleInterval;
    }

    private void centerPitch()
    {
        familiar.pitchO = familiar.pitch;
        if(familiar.pitch > 0)
            familiar.pitch -= angleInterval;
        if(familiar.pitch < 0)
            familiar.pitch += angleInterval;
    }

    private void centerRoll()
    {
        familiar.rollO = familiar.roll;
        if(familiar.roll > 0)
            familiar.roll -= angleInterval;
        if(familiar.roll < 0)
            familiar.roll += angleInterval;
    }

    private void rotateBodyIfNecessary() {
        familiar.yBodyRot = Mth.rotateIfNecessary(familiar.yBodyRot, familiar.yHeadRot, (float) familiar.getMaxHeadYRot());
    }

    private void rotateHeadIfNecessary() {
        familiar.yHeadRot = Mth.rotateIfNecessary(familiar.yHeadRot, familiar.yBodyRot, (float) familiar.getMaxHeadYRot());
    }

    private void rotateHeadTowardsFront() {
        int i = headStableTime - 10;
        float f = Mth.clamp((float)i / 10.0F, 0.0F, 1.0F);
        float f1 = (float) familiar.getMaxHeadYRot() * (1.0F - f);
        familiar.yBodyRot = Mth.rotateIfNecessary(familiar.yBodyRot, familiar.yHeadRot, f1);
    }
}