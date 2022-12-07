package com.beesechurger.flyingfamiliars.entity.custom.projectile;

import com.beesechurger.flyingfamiliars.entity.FFEntityTypes;
import com.beesechurger.flyingfamiliars.init.FFItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class SummoningBellProjectile extends ThrowableItemProjectile
{
	private ItemStack summoning_bell;
	private boolean action = false;
	
	public SummoningBellProjectile(EntityType<? extends SummoningBellProjectile> proj, Level level)
	{
		super(proj, level);
	}
	
	public SummoningBellProjectile(Level level, LivingEntity entity, ItemStack stack, boolean action_type)
	{
	    super(FFEntityTypes.SUMMONING_BELL_PROJECTILE.get(), entity, level);
	    summoning_bell = stack;
	    this.action = action_type;
	}
	
	public SummoningBellProjectile(Level level, double x, double y, double z)
	{
	    super(FFEntityTypes.SUMMONING_BELL_PROJECTILE.get(), x, y, z, level);
	}
	
	protected Item getDefaultItem()
	{
	    //return FFItems.MUSIC_NOTE_1.get();
	    return Items.AIR;
	}
	
	@Override
    protected void onHitBlock(BlockHitResult result)
    {
		// If summoning bell is set to capture/client calling:
		if(!this.action || level.isClientSide()) return;
		
		// Else, summoning bell is set to release:
        BlockPos pos = result.getBlockPos();
        Direction facing = result.getDirection();
        Level worldIn = this.level;

		CompoundTag compound = summoning_bell.getTag();
		if (compound != null)
		{
			ListTag tagList = compound.getList("entity", 10);
			if (tagList.size() > 0)
			{
				CompoundTag entityNBT = tagList.getCompound(tagList.size()-1);
				tagList.remove(tagList.size()-1);
				
		        EntityType<?> type = EntityType.byString(entityNBT.getString("entity")).orElse(null);
	            if (type != null)
	            {
	            	Entity entity;
			        BlockPos blockPos = pos.relative(facing);
	            	
	                entity = type.create(worldIn);
	                entity.load(entityNBT);
	                
	                entity.absMoveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0);
					worldIn.addFreshEntity(entity);
	            }
			}
		}
		
		this.remove(RemovalReason.KILLED);
    }
	
	@Override
    protected void onHitEntity(EntityHitResult result)
	{		
		// If summoning bell is set to release/client calling:
		if (this.action || level.isClientSide()) return;
		
		Entity target = result.getEntity();
			
		if (!(target instanceof Player || !target.canChangeDimensions() || !target.isAlive()) && target instanceof Mob)
		{
			CompoundTag compound = summoning_bell.getTag();
			if (compound == null)
			{
				compound = new CompoundTag();
			}

			ListTag tagList = compound.getList("entity", 10);
			if (tagList.size() < 3)
			{
				CompoundTag entityNBT = new CompoundTag();
				
				entityNBT.putString("entity", EntityType.getKey(target.getType()).toString());
				target.saveWithoutId(entityNBT);
				tagList.addTag(tagList.size(),entityNBT);
				
				target.remove(RemovalReason.KILLED);
			}
			
			compound.put("entity", tagList);
			summoning_bell.setTag(compound);
		}
		
		this.remove(RemovalReason.KILLED);
    }
	
	@Override
	public void tick()
	{
		super.tick();
		if(this.level.isClientSide())
		{
			Vec3 vec3d = this.getDeltaMovement();
	        double d0 = this.getX() + vec3d.x;
	        double d1 = this.getY() + vec3d.y;
	        double d2 = this.getZ() + vec3d.z;
	        this.level.addParticle(ParticleTypes.NOTE, d0 - vec3d.x * 0.25D, d1 - vec3d.y * 0.25D, d2 - vec3d.z * 0.25D, vec3d.x, vec3d.y, vec3d.z);
		}
	}
}
