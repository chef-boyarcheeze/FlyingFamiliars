package com.beesechurger.flyingfamiliars.entity.custom;

import org.jetbrains.annotations.Nullable;

import com.beesechurger.flyingfamiliars.FFKeys;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import net.minecraftforge.client.event.EntityViewRenderEvent;

public abstract class AbstractFamiliarEntity extends TamableAnimal
{
	public static final float FLIGHT_THRESHOLD = 0.5f;

	protected AbstractFamiliarEntity(EntityType<? extends TamableAnimal> entity, Level level)
	{
		super(entity, level);
		this.setTame(false);
	}
	
	public void setCamera(EntityViewRenderEvent.CameraSetup event)
    {
		//event.getCamera();
        /*    event.getInfo().move(ClientEvents.getViewCollision(-5d, this), 0.75d, 0);
        else
            event.getInfo().move(ClientEvents.getViewCollision(-3, this), 0.3, 0);*/
    }
	
	public boolean isMoving()
	{
		double d0 = this.getX() - this.xo;
		double d1 = this.getZ() - this.zo;
		return d0 * d0 + d1 * d1 > 2.5000003E-7F;
	}
	
	@Override
	public boolean causeFallDamage(float p_148750_, float p_148751_, DamageSource p_148752_)
	{
		return false;
	}
	
	@Override
	protected void checkFallDamage(double p_27754_, boolean p_27755_, BlockState p_27756_, BlockPos p_27757_)
	{
	}
	
	public boolean shouldFly()
	{
		var pointer = blockPosition().mutable().move(0, -1, 0);
		var min = level.dimensionType().minY();
		var i = 0;

		while (i <= FLIGHT_THRESHOLD && pointer.getY() > min && !level.getBlockState(pointer).getMaterial().isSolid())
			pointer.setY(getBlockY() - ++i);

		return i >= FLIGHT_THRESHOLD;
	}
	
	@Override
	public boolean canBeControlledByRider()
	{
		return this.getControllingPassenger() instanceof LivingEntity;
	}
	
	@Override
	public Team getTeam()
	{
		return super.getTeam();
	}
	
	@Nullable
	@Override
	public Entity getControllingPassenger()
	{
		return this.getFirstPassenger();
	}
	
	public boolean isTamedFor(Player player)
	{
		return isTame() && isOwnedBy(player);
	}
	
	public Vec3 getHoverVector(Vec3 vec3, LivingEntity driver)
	{
		double xMove = vec3.x + driver.xxa;
		double yMove = vec3.y;
		double zMove = vec3.z + driver.zza;
		
		if(FFKeys.ascend.isDown()) yMove += 0.6;
		if(FFKeys.descend.isDown()) yMove -= 0.6;
		
		return new Vec3(xMove, yMove, zMove);
	}
	
	public void setRidingPlayer(Player player)
	{
		player.setYRot(getYRot());
		player.setXRot(getXRot());
		player.startRiding(this);
	}
	
	@Override
	public void onPassengerTurned(Entity rider)
	{
		rider.setYBodyRot(this.getYRot());
	}
}
