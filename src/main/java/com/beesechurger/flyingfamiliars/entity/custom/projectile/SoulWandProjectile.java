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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SoulWandProjectile extends ThrowableItemProjectile
{
	private ItemStack soul_wand;
	private boolean action = false;
	
	public SoulWandProjectile(EntityType<? extends SoulWandProjectile> proj, Level level)
	{
		super(proj, level);
	}
	
	public SoulWandProjectile(Level level, LivingEntity entity, ItemStack stack, boolean action_type)
	{
		super(FFEntityTypes.SOUL_WAND_PROJECTILE.get(), entity, level);
	    soul_wand = stack;
	    this.action = action_type;
	}
	
	public SoulWandProjectile(Level level, double x, double y, double z)
	{
	    super(FFEntityTypes.SOUL_WAND_PROJECTILE.get(), x, y, z, level);
	}
	
	protected Item getDefaultItem()
	{
	    return FFItems.SOUL_WAND_PROJECTILE.get();
	}
	
	@Override
    protected void onHitBlock(BlockHitResult result)
    {
		if(this.action && !level.isClientSide())
		{
			if(release(result)) level.broadcastEntityEvent(this, (byte) 4);
		}
		
		this.remove(RemovalReason.KILLED);
    }
	
	private boolean release(BlockHitResult result)
	{
		BlockPos pos = new BlockPos(result.getLocation());
        Direction facing = this.getDirection();
        Level worldIn = this.level;

		CompoundTag compound = soul_wand.getTag();
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
					
					return true;
	            }
			}
		}
		
		return false;
	}
	
	@Override
    protected void onHitEntity(EntityHitResult result)
	{
		if(!this.action && !level.isClientSide())
		{
			if(capture(result.getEntity())) level.broadcastEntityEvent(this, (byte) 3);
		}
		
		this.remove(RemovalReason.KILLED);
    }
	
	private boolean capture(Entity target)
	{		
		if (!(target instanceof Player) && target.canChangeDimensions() && target.isAlive() && target instanceof Mob && !level.isClientSide())
		{
			CompoundTag compound = soul_wand.getTag();
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
			else
			{
				return false;
			}
			
			compound.put("entity", tagList);
			soul_wand.setTag(compound);
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public void tick()
	{
		super.tick();
		if(level.isClientSide())
		{
			Vec3 vec3d = this.getDeltaMovement();
	        double d0 = this.getX() + vec3d.x;
	        double d1 = this.getY() + vec3d.y;
	        double d2 = this.getZ() + vec3d.z;
	        this.level.addParticle(ParticleTypes.BUBBLE_POP, d0 - vec3d.x * 0.25D, d1 - vec3d.y * 0.25D, d2 - vec3d.z * 0.25D, 0, 0, 0);
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
    public void handleEntityEvent(byte id)
	{
        if (id == 3) 
        {
            for (int i = 0; i < 360; i++)
            {
                if(i % 5 == 0) level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, getX(), getY(), getZ(), 0.1 * Math.cos(i), 0.05 * (Math.cos(i * 3) * Math.sin(i * 3)), 0.1 * Math.sin(i));
            }
        }
        else if (id == 4) 
        {
            for (int i = 0; i < 360; i++)
            {
                if(i % 5 == 0) level.addParticle(ParticleTypes.SOUL, getX(), getY(), getZ(), 0.2 * Math.cos(i), 0.1, 0.2 * Math.sin(i));
            }
        }
    }
}