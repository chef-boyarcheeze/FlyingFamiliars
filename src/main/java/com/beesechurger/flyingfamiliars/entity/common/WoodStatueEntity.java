package com.beesechurger.flyingfamiliars.entity.common;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WoodStatueEntity extends LivingEntity
{
    public WoodStatueEntity(EntityType<? extends LivingEntity> entity, Level level)
    {
        super(entity, level);
    }

    @Override
    public void push(Entity entity) {
    }

    @Override
    public void baseTick() {}

    @Override
    public void kill()
    {
        this.remove(RemovalReason.KILLED);
    }

    @Override
    public Iterable<ItemStack> getArmorSlots()
    {
        return ImmutableList.of();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot p_21127_)
    {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot p_21036_, ItemStack p_21037_) {

    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }
}
