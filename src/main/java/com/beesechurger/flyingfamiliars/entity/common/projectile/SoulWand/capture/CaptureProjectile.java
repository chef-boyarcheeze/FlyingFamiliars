package com.beesechurger.flyingfamiliars.entity.common.projectile.SoulWand.capture;

import com.beesechurger.flyingfamiliars.entity.FFEntityTypes;
import com.beesechurger.flyingfamiliars.item.EntityTagItemHelper;
import com.beesechurger.flyingfamiliars.item.FFItems;
import com.beesechurger.flyingfamiliars.item.common.SoulItems.BaseEntityTagItem;

import com.beesechurger.flyingfamiliars.sound.FFSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundSource;
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

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.BASE_ENTITY_TAGNAME;
import static com.beesechurger.flyingfamiliars.util.FFStringConstants.ENTITY_EMPTY;

public class CaptureProjectile extends ThrowableItemProjectile
{
	private NonNullList<ItemStack> stacks = NonNullList.create();
	private Player player = null;
	private boolean action = false;
	
	public CaptureProjectile(EntityType<? extends CaptureProjectile> proj, Level level)
	{
		super(proj, level);
	}
	
	public CaptureProjectile(Level level, LivingEntity entity, boolean action)
	{
		super(FFEntityTypes.CAPTURE_PROJECTILE.get(), entity, level);

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
		NonNullList<ItemStack> stacks = NonNullList.create();

		ItemStack mainHand = player.getMainHandItem();
		ItemStack offHand = EntityTagItemHelper.getOffHandBattery(player);
		ItemStack curioCharm = EntityTagItemHelper.getCurioCharmBattery(player);

		if(mainHand != null)
			stacks.add(mainHand);
		if(offHand != null)
			stacks.add(offHand);
		if(curioCharm != null)
			stacks.add(curioCharm);

		ListTag totalList = new ListTag();

		// Add all item tags into one ListTag
		for(ItemStack stack : stacks)
		{
			if(stack.getItem() instanceof BaseEntityTagItem item)
			{
				EntityTagItemHelper.ensureTagPopulated(stack);
				CompoundTag stackTag = stack.getTag();
				ListTag tempItem = stackTag.getList(BASE_ENTITY_TAGNAME, 10);

				for(Tag tag : tempItem)
				{
					totalList.add(tag);
				}
			}
		}

		boolean passFlag = false;

		if(!(target instanceof Player) && target.canChangeDimensions() && target.isAlive() && target instanceof Mob && !level.isClientSide())
		{
			// Default fill soul wand:
			for(int i = 0; i < totalList.size(); i++)
			{
				// Need to use regular Tag object for ENTITY_EMPTY compare here, not CompoundTag
				if(totalList.get(i).toString().contains(ENTITY_EMPTY))
				{
					CompoundTag entityNBT = new CompoundTag();

					entityNBT.putString(BASE_ENTITY_TAGNAME, EntityType.getKey(target.getType()).toString());
					target.saveWithoutId(entityNBT);
					totalList.set(i, entityNBT);

					target.remove(RemovalReason.KILLED);

					passFlag = true;
					break;
				}
			}
		}

		if(passFlag)
		{
			for(ItemStack stack : stacks)
			{
				if(stack.getItem() instanceof BaseEntityTagItem item)
				{
					CompoundTag stackTag = stack.getTag();
					ListTag stackList = new ListTag();

					for(int i = 0; i < item.getMaxEntities(); i++)
					{
						stackList.add(totalList.get(0));
						totalList.remove(0);
					}

					stackTag.put(BASE_ENTITY_TAGNAME, stackList);
					stack.setTag(stackTag);
				}
			}

			return true;
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
		ItemStack mainHand = player.getMainHandItem();
		EntityTagItemHelper.ensureTagPopulated(mainHand);
		String selectedEntity = EntityTagItemHelper.getSelectedEntity(mainHand);

		if(selectedEntity != ENTITY_EMPTY)
		{
			CompoundTag stackTag = mainHand.getTag();
			ListTag stackList = stackTag.getList(BASE_ENTITY_TAGNAME, 10);
			CompoundTag entityNBT = stackList.getCompound(stackList.size()-1);

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

				entityNBT.putString(BASE_ENTITY_TAGNAME, ENTITY_EMPTY);
				stackList.set(stackList.size()-1, entityNBT);

				stackTag.put(BASE_ENTITY_TAGNAME, stackList);
				mainHand.setTag(stackTag);

				return true;
			}
		}

		NonNullList<ItemStack> stacks = NonNullList.create();

		ItemStack offHand = EntityTagItemHelper.getOffHandBattery(player);
		ItemStack curioCharm = EntityTagItemHelper.getCurioCharmBattery(player);

		if(mainHand != null)
			stacks.add(mainHand);
		if(offHand != null)
			stacks.add(offHand);
		if(curioCharm != null)
			stacks.add(curioCharm);

		ListTag totalList = new ListTag();

		// Add all item tags into one ListTag
		for(ItemStack stack : stacks)
		{
			if(stack.getItem() instanceof BaseEntityTagItem item)
			{
				EntityTagItemHelper.ensureTagPopulated(stack);
				CompoundTag stackTag = stack.getTag();
				ListTag tempItem = stackTag.getList(BASE_ENTITY_TAGNAME, 10);

				for(Tag tag : tempItem)
				{
					totalList.add(tag);
				}
			}
		}

		boolean passFlag = false;

		for(int i = totalList.size(); i > 0; i--)
		{
			// Need to use regular Tag object for ENTITY_EMPTY compare here, not CompoundTag
			if(!totalList.get(i-1).toString().contains(ENTITY_EMPTY))
			{
				CompoundTag entityNBT = totalList.getCompound(i-1);

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

					entityNBT.putString(BASE_ENTITY_TAGNAME, ENTITY_EMPTY);
					totalList.set(i-1, entityNBT);

					passFlag = true;
					break;
				}
			}
		}

		if(passFlag)
		{
			for(ItemStack stack : stacks)
			{
				if(stack.getItem() instanceof BaseEntityTagItem item)
				{
					CompoundTag stackTag = stack.getTag();
					ListTag stackList = new ListTag();

					for(int i = 0; i < item.getMaxEntities(); i++)
					{
						stackList.add(totalList.get(0));
						totalList.remove(0);
					}

					stackTag.put(BASE_ENTITY_TAGNAME, stackList);
					stack.setTag(stackTag);
				}
			}

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
	        this.level.addParticle(ParticleTypes.END_ROD, d0 - vec3d.x * 0.25D, d1 - vec3d.y * 0.25D, d2 - vec3d.z * 0.25D, 0, 0, 0);
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
                if(i % 5 == 0) level.addParticle(ParticleTypes.CLOUD, getX(), getY(), getZ(), 0.1 * Math.cos(i), 0.05 * (Math.cos(i * 9) * Math.sin(i * 9)), 0.1 * Math.sin(i));
            }
        }
    }
}
