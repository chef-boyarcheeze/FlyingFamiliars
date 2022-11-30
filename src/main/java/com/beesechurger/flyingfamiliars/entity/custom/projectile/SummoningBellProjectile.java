package com.beesechurger.flyingfamiliars.entity.custom.projectile;

import com.beesechurger.flyingfamiliars.entity.ModEntityTypes;
import com.beesechurger.flyingfamiliars.init.FFItems;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class SummoningBellProjectile extends ThrowableItemProjectile
{
	public SummoningBellProjectile(EntityType<? extends SummoningBellProjectile> proj, Level level)
	{
		super(proj, level);
	}
	
	public SummoningBellProjectile(Level level, LivingEntity entity)
	{
	    super(ModEntityTypes.SUMMONING_BELL_PROJECTILE.get(), entity, level);
	}
	
	public SummoningBellProjectile(Level level, double x, double y, double z)
	{
	   super(ModEntityTypes.SUMMONING_BELL_PROJECTILE.get(), x, y, z, level);
	}
	
	protected Item getDefaultItem() {
	   return FFItems.MUSIC_NOTE_1.get();
	}
	
	/*@Override
    protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
        Entity player = this.getOwner();
        ItemInfinityLauncher.PlungerAction action = ItemInfinityLauncher.PlungerAction.getFromId(this.entityData.get(PLUNGER_ACTION));
        if (player instanceof Player && action == ItemInfinityLauncher.PlungerAction.RELEASE) {
            for (ItemStack itemStack : ((Player) player).inventory.items) {
                if (itemStack.getItem() instanceof MobImprisonmentToolItem && itemStack.hasTag()) {
                    ItemStack copy = itemStack.copy();
                    if (((MobImprisonmentToolItem) itemStack.getItem()).release((Player) player, result.getBlockPos(), result.getDirection(), this.level, copy)) {
                        ((Player) player).inventory.removeItem(itemStack);
                        ((Player) player).inventory.add(copy);
                        break;
                    }

                }
            }
            this.onClientRemoval();
        }
    }*/
}
