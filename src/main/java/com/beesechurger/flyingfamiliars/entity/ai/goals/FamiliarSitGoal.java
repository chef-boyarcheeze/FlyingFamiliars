package com.beesechurger.flyingfamiliars.entity.ai.goals;

import com.beesechurger.flyingfamiliars.entity.common.familiar.BaseFamiliarEntity;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;

public class FamiliarSitGoal extends SitWhenOrderedToGoal
{
    private BaseFamiliarEntity familiar;
    private final double speedModifier;

    public FamiliarSitGoal(BaseFamiliarEntity familiar, double speedModifier)
    {
        super(familiar);
        this.familiar = familiar;
        this.speedModifier = speedModifier;
    }

    @Override
    public void tick()
    {
        if (familiar.isFlying() && familiar.getNavigation().isDone())
        {
            Vec3 vec3 = LandRandomPos.getPos(familiar, 10, 10);
            familiar.getNavigation().moveTo(vec3.x, vec3.y, vec3.z, speedModifier);
        }
    }
}
