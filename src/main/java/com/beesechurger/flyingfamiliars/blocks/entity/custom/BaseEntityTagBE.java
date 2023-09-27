package com.beesechurger.flyingfamiliars.blocks.entity.custom;

import com.beesechurger.flyingfamiliars.blocks.EntityTagBlockHelper;
import com.beesechurger.flyingfamiliars.items.EntityTagItemHelper;
import com.beesechurger.flyingfamiliars.items.custom.SoulItems.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.networking.FFMessages;
import com.beesechurger.flyingfamiliars.networking.packet.BEItemStackS2CPacket;
import com.beesechurger.flyingfamiliars.networking.packet.EntityListS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Clearable;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.*;

public abstract class BaseEntityTagBE extends BlockEntity implements Clearable
{
    public NonNullList<ItemStack> items;
    public CompoundTag entities = new CompoundTag();

    protected int itemCapacityMod = 1;
    protected int entityCapacityMod = 1;

    public BaseEntityTagBE(BlockEntityType<?> type, BlockPos pos, BlockState blockState)
    {
        super(type, pos, blockState);
        items = NonNullList.withSize(getMaxItems(), ItemStack.EMPTY);
    }

    @Override
    public void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, this.items, true);
        tag.put(BASE_ENTITY_TAGNAME, entities);
    }

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        this.items.clear();
        ContainerHelper.loadAllItems(tag, this.items);
        entities = tag.getCompound(BASE_ENTITY_TAGNAME);
    }

// Item related methods:

    public boolean placeItem(ItemStack stack)
    {
        if(getItemCount() < getMaxItems() && stack.getItem() != Items.AIR)
        {
            items.set(getItemCount(), stack.split(1));
            contentsChanged();

            return true;
        }

        return false;
    }

    public boolean removeItem(Level level, BlockPos pos)
    {
        if(getItemCount() > 0)
        {
            ItemStack stack = items.get(getItemCount()-1);

            ItemEntity drop = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, stack);
            drop.setDefaultPickUpDelay();
            level.addFreshEntity(drop);

            items.set(getItemCount()-1, ItemStack.EMPTY);
            contentsChanged();

            return true;
        }

        return false;
    }

    public void setClientItems(NonNullList<ItemStack> list)
    {
        for(int i = 0; i < list.size(); i++)
        {
            items.set(i, list.get(i));
        }
    }

    public int getItemCount()
    {
        for(int i = 0; i < getMaxItems(); i++)
        {
            if(items.get(i).isEmpty()) return i;
        }

        return getMaxItems();
    }

    public int getMaxItems()
    {
        return EntityTagBlockHelper.MAX_ITEMS * itemCapacityMod;
    }

// Entity tag related methods:

    public boolean placeEntity(ItemStack stack)
    {
        EntityTagBlockHelper.ensureTagPopulated(this);

        if(stack.getItem() instanceof BaseEntityTagItem item)
        {
            EntityTagItemHelper.ensureTagPopulated(stack);
            CompoundTag stackTag = stack.getTag();

            ListTag stackList = stackTag.getList(BASE_ENTITY_TAGNAME, 10);
            ListTag blockList = entities.getList(BASE_ENTITY_TAGNAME, 10);

            for(int i = 0; i < getMaxEntities(); i++)
            {
                // Need to use regular Tag object for "Empty" compare here, not CompoundTag
                if(blockList.get(i).toString().contains("Empty"))
                {
                    CompoundTag entityNBT = stackList.getCompound(item.getMaxEntities()-1);

                    blockList.set(i, entityNBT);
                    entities.put(BASE_ENTITY_TAGNAME, blockList);

                    CompoundTag empty = new CompoundTag();
                    empty.putString(BASE_ENTITY_TAGNAME, "Empty");
                    stackList.set(item.getMaxEntities()-1, empty);

                    stackTag.put(BASE_ENTITY_TAGNAME, stackList);
                    stack.setTag(stackTag);
                    contentsChanged();

                    return true;
                }
            }
        }

        return false;
    }

    public boolean removeEntity(ItemStack stack)
    {
        EntityTagBlockHelper.ensureTagPopulated(this);

        if(stack.getItem() instanceof BaseEntityTagItem item)
        {
            EntityTagItemHelper.ensureTagPopulated(stack);
            CompoundTag stackTag = stack.getTag();

            ListTag stackList = stackTag.getList(BASE_ENTITY_TAGNAME, 10);
            ListTag blockList = entities.getList(BASE_ENTITY_TAGNAME, 10);

            for(int i = getMaxEntities(); i > 0; i--)
            {
                if(!blockList.get(i-1).toString().contains("Empty"))
                {
                    CompoundTag entityNBT = blockList.getCompound(i-1);

                    CompoundTag empty = new CompoundTag();
                    empty.putString(BASE_ENTITY_TAGNAME, "Empty");
                    blockList.set(i-1, empty);
                    entities.put(BASE_ENTITY_TAGNAME, blockList);

                    stackList.set(item.getMaxEntities()-1, entityNBT);

                    stackTag.put(BASE_ENTITY_TAGNAME, stackList);
                    stack.setTag(stackTag);
                    contentsChanged();

                    return true;
                }
            }
        }

        return false;
    }

    public void setClientEntities(CompoundTag list)
    {
        entities = list;
    }

    public int getEntityCount()
    {
        EntityTagBlockHelper.ensureTagPopulated(this);

        int entityCount = 0;

        if(entities != null)
        {
            ListTag tagList = entities.getList(BASE_ENTITY_TAGNAME, 10);

            for(int i = 0; i < getMaxEntities(); i++)
            {
                // Need to use regular Tag object here, not CompoundTag
                if(!tagList.get(i).toString().contains("Empty")) entityCount++;
            }
        }

        return entityCount;
    }

    public int getMaxEntities()
    {
        return EntityTagBlockHelper.MAX_ENTITIES * entityCapacityMod;
    }

    public String getID(int listValue, ItemStack stack)
    {
        return stack.getTag().getList(BASE_ENTITY_TAGNAME, 10).getCompound(listValue).getString(BASE_ENTITY_TAGNAME);
    }

    public NonNullList<String> getEntitiesStrings()
    {
        EntityTagBlockHelper.ensureTagPopulated(this);

        ListTag blockList = entities.getList(BASE_ENTITY_TAGNAME, 10);
        NonNullList<String> entityStrings = NonNullList.withSize(getMaxEntities(), "");

        for(int i = 0; i < getMaxEntities(); i++)
        {
            entityStrings.set(i, blockList.getCompound(i).getString(BASE_ENTITY_TAGNAME));
            //System.out.println(blockList.getCompound(i).getString(BLOCK_ENTITIES_TAGNAME));
        }

        return entityStrings;
    }

// General Block Entity methods:

    protected void contentsChanged()
    {
        setChanged();

        if(!level.isClientSide())
        {
            EntityTagBlockHelper.ensureTagPopulated(this);

            FFMessages.sendToClients(new BEItemStackS2CPacket(items, worldPosition));
            FFMessages.sendToClients(new EntityListS2CPacket(entities, worldPosition));
        }
    }

    @Override
    public void clearContent()
    {
        this.items.clear();
        EntityTagBlockHelper.populateTag(this);
    }

    public void drops()
    {
        SimpleContainer inventory = new SimpleContainer(items.size());
        for(int i = 0; i < items.size(); i++)
        {
            inventory.setItem(i, items.get(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
        entities = new CompoundTag();
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag()
    {
        return this.saveWithoutMetadata();
    }
}
