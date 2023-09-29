package com.beesechurger.flyingfamiliars.entity.common.projectile.SoulWand.capture;

import com.beesechurger.flyingfamiliars.entity.FFEntityTypes;
import com.beesechurger.flyingfamiliars.items.EntityTagItemHelper;
import com.beesechurger.flyingfamiliars.items.FFItems;
import com.beesechurger.flyingfamiliars.items.common.SoulItems.BaseEntityTagItem;

import com.beesechurger.flyingfamiliars.items.common.SoulItems.SoulWand.FieryCrook;
import com.beesechurger.flyingfamiliars.items.common.SoulItems.SoulWand.VoidShard;
import com.beesechurger.flyingfamiliars.items.common.SoulItems.SoulWand.WaterSceptre;
import com.beesechurger.flyingfamiliars.sound.FFSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundSource;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.BASE_ENTITY_TAGNAME;

public class CaptureProjectile extends ThrowableItemProjectile
{
	private ItemStack stack;
	private Player player = null;
	private boolean action = false;
	
	public CaptureProjectile(EntityType<? extends CaptureProjectile> proj, Level level)
	{
		super(proj, level);
	}
	
	public CaptureProjectile(Level level, LivingEntity entity, ItemStack stack, boolean action)
	{
		super(FFEntityTypes.CAPTURE_PROJECTILE.get(), entity, level);
	    this.stack = stack;
		if(entity instanceof Player)
			player = (Player) entity;
	    this.action = action;
	}
	
	public CaptureProjectile(Level level, double x, double y, double z)
	{
	    super(FFEntityTypes.CAPTURE_PROJECTILE.get(), x, y, z, level);
	}
	
	protected Item getDefaultItem()
	{
		return FFItems.CAPTURE_PROJECTILE.get();
	}
	
	@Override
    protected void onHitEntity(EntityHitResult result)
	{
		if(!this.action && !level.isClientSide() && player != null)
		{
			if(capture(result.getEntity()))
			{
				level.broadcastEntityEvent(this, (byte) 3);
				level.playSound((Player)null, getX(), getY(), getZ(),
						FFSounds.SOUL_WAND_THROW.get(), SoundSource.PLAYERS, 0.5f, 2.0f * FFSounds.getPitch());
			}
		}

		this.remove(RemovalReason.KILLED);
    }
	
	private boolean capture(Entity target)
	{
		if(stack.getItem() instanceof BaseEntityTagItem item)
		{
			if(!(target instanceof Player) && target.canChangeDimensions() && target.isAlive() && target instanceof Mob && !level.isClientSide())
			{
				EntityTagItemHelper.ensureTagPopulated(stack);
				CompoundTag stackTag = stack.getTag();
				ListTag stackList = stackTag.getList(BASE_ENTITY_TAGNAME, 10);

				for(int i = 0; i < item.getMaxEntities(); i++)
				{
					// Need to use regular Tag object for "Empty" compare here, not CompoundTag
					if(stackList.get(i).toString().contains("Empty"))
					{
						CompoundTag entityNBT = new CompoundTag();

						entityNBT.putString(BASE_ENTITY_TAGNAME, EntityType.getKey(target.getType()).toString());
						target.saveWithoutId(entityNBT);
						stackList.set(i, entityNBT);

						target.remove(RemovalReason.KILLED);

						stackTag.put(BASE_ENTITY_TAGNAME, stackList);
						stack.setTag(stackTag);

						return true;
					}
				}
			}
		}

		return false;
	}
	
	@Override
    protected void onHitBlock(BlockHitResult result)
    {
		if(this.action && !level.isClientSide() && player != null)
		{
			if(release(result))
			{
				level.broadcastEntityEvent(this, (byte) 3);
				level.playSound((Player)null, getX(), getY(), getZ(),
						FFSounds.SOUL_WAND_THROW.get(), SoundSource.PLAYERS, 0.5f, 2.0f * FFSounds.getPitch());
			}
		}
		
		this.remove(RemovalReason.KILLED);
    }
	
	private boolean release(BlockHitResult result)
	{
		if(stack.getItem() instanceof BaseEntityTagItem item)
		{
			EntityTagItemHelper.ensureTagPopulated(stack);
			CompoundTag stackTag = stack.getTag();
			ListTag stackList = stackTag.getList(BASE_ENTITY_TAGNAME, 10);

			for(int i = item.getMaxEntities(); i > 0; i--)
			{
				// Need to use regular Tag object for "Empty" compare here, not CompoundTag
				if(!stackList.get(i-1).toString().contains("Empty"))
				{
					CompoundTag entityNBT = stackList.getCompound(i-1);

					EntityType<?> type = EntityType.byString(entityNBT.getString(BASE_ENTITY_TAGNAME)).orElse(null);
					if (type != null)
					{
						BlockPos pos = result.getBlockPos();
						Direction dir = result.getDirection();

						double x = pos.getX() + 0.5 + (dir == Direction.EAST ? Math.ceil(type.getWidth()) : dir == Direction.WEST ? -1 * Math.ceil(type.getWidth()) : 0);
						double y = pos.getY() + (dir == Direction.UP ? 1 : dir == Direction.DOWN ? -1 * Math.ceil(type.getHeight()) : 0);
						double z = pos.getZ() + 0.5 + (dir == Direction.SOUTH ? Math.ceil(type.getWidth()) : dir == Direction.NORTH ? -1 * Math.ceil(type.getWidth()) : 0);

						Entity entity = type.create(level);
						entity.load(entityNBT);

						entity.absMoveTo(x, y, z, 0, 0);
						level.addFreshEntity(entity);

						entityNBT.putString(BASE_ENTITY_TAGNAME, "Empty");
						stackList.set(i-1, entityNBT);

						stackTag.put(BASE_ENTITY_TAGNAME, stackList);
						stack.setTag(stackTag);

						return true;
					}
				}
			}
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
	        this.level.addParticle(ParticleTypes.SMOKE, d0 - vec3d.x * 0.25D, d1 - vec3d.y * 0.25D, d2 - vec3d.z * 0.25D, 0, 0, 0);
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
				// capture
                if(i % 5 == 0) level.addParticle(ParticleTypes.CLOUD, getX(), getY(), getZ(), 0.1 * Math.cos(i), 0.05 * (Math.cos(i * 3) * Math.sin(i * 3)), 0.1 * Math.sin(i));
            }
        }
        else if (id == 4) 
        {
            for (int i = 0; i < 360; i++)
            {
				// release
                if(i % 5 == 0) level.addParticle(ParticleTypes.CLOUD, getX(), getY(), getZ(), 0.2 * Math.cos(i), 0.1, 0.2 * Math.sin(i));
            }
        }
    }
}
