package com.beesechurger.flyingfamiliars.block.entity;

import com.beesechurger.flyingfamiliars.entity.common.wand_effect.projectile.RunicCubeProjectile;
import com.beesechurger.flyingfamiliars.registries.FFBlockEntities;
import com.beesechurger.flyingfamiliars.registries.FFEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static com.beesechurger.flyingfamiliars.util.FFConstants.STORAGE_ENTITY_TYPE;

public class RunicPedestalBE extends BaseEntityTagBE
{
	private static final String STORAGE_RUNIC_CUBE_PROJECTILE_TAGNAME = "RunicCubeProjectile";

	public Entity runicCubeProjectile = null;
	public CompoundTag runicCubeProjectileTag = new CompoundTag();

	public RunicPedestalBE(BlockPos position, BlockState state)
	{
		super(FFBlockEntities.RUNIC_PEDESTAL_BLOCK_ENTITY.get(), position, state);

		createItems();
		createFluid();
	}
	
	@Override
	public void saveAdditional(CompoundTag tag)
	{
		super.saveAdditional(tag);

		if (runicCubeProjectileTag.contains(STORAGE_ENTITY_TYPE))
		{
			tag.put(STORAGE_RUNIC_CUBE_PROJECTILE_TAGNAME, runicCubeProjectileTag);
		}
	}
	
	@Override
	public void load(CompoundTag tag)
	{
		super.load(tag);
		runicCubeProjectileTag = tag.getCompound(STORAGE_RUNIC_CUBE_PROJECTILE_TAGNAME);

		if (!runicCubeProjectileTag.isEmpty())
		{
			EntityType<?> type = EntityType.byString(runicCubeProjectileTag.getString(STORAGE_ENTITY_TYPE)).orElse(null);

			if (type != null && type == FFEntityTypes.RUNIC_CUBE_PROJECTILE.get())
			{
				runicCubeProjectile = new RunicCubeProjectile(FFEntityTypes.RUNIC_CUBE_PROJECTILE.get(), getLevel());
				runicCubeProjectile.load(runicCubeProjectileTag);
			}
			else
			{
				runicCubeProjectile = null;
			}
		}
		else
		{
			runicCubeProjectile = null;
		}
	}

//////////////////
/// Accessors: ///
//////////////////

/// Booleans:

	public boolean containsRunicCubeItem(ItemStack storedItem)
	{
		return storedItem.is(Items.WHITE_WOOL);
	}

/// Integers:

	@Override
	public int getMaxItems()
	{
		return 1;
	}

	@Override
	public int getMaxEntities()
	{
		return 0;
	}

////////////////////////
/// Storage methods: ///
////////////////////////

	@Override
	protected void contentsChanged()
	{
		boolean flag = (runicCubeProjectile == null);

		for (final ItemStack storedItem : items)
		{
			if (containsRunicCubeItem(storedItem))
			{
				runicCubeProjectile = new RunicCubeProjectile(FFEntityTypes.RUNIC_CUBE_PROJECTILE.get(), getLevel());
				flag = true;
				break;
			}
		}

		if (!flag)
		{
			runicCubeProjectile = null;
		}

		// put runic cube entity into tag to be sent to client version
		if (runicCubeProjectile == null)
		{
			runicCubeProjectileTag = new CompoundTag();
		}
		else
		{
			runicCubeProjectileTag.putString(STORAGE_ENTITY_TYPE, EntityType.getKey(runicCubeProjectile.getType()).toString());
			runicCubeProjectile.saveWithoutId(runicCubeProjectileTag);
		}

		super.contentsChanged();
	}

	@Override
	public void drops()
	{
		super.drops();

		runicCubeProjectile = null;
		runicCubeProjectileTag = new CompoundTag();
	}

/////////////////////////////
/// Block Entity methods: ///
/////////////////////////////

	public static void tick(Level level, BlockPos pos, BlockState state, RunicPedestalBE entity)
	{
		if(level.isClientSide())
		{
			if (entity.runicCubeProjectile != null)
			{
				entity.runicCubeProjectile.tick();
			}

			return;
		}
	}
}
