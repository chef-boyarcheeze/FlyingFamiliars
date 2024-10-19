package com.beesechurger.flyingfamiliars.block.entity;

import com.beesechurger.flyingfamiliars.item.common.entity_items.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.registries.FFSounds;
import com.beesechurger.flyingfamiliars.tags.EntityTagRef;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.List;
import java.util.Random;
import java.util.Vector;

import static com.beesechurger.flyingfamiliars.util.FFConstants.STORAGE_ENTITY_TAGNAME;

public abstract class BaseEntityTagBE extends BlockEntity implements Clearable
{
    public final static int MAX_FLUID = 250;

    public EntityTagRef entities;

    public NonNullList<ItemStack> items = null;
    public CompoundTag entityStorageTag = new CompoundTag();
    public FluidTank fluid = null;

    public Random random = new Random();

    public BaseEntityTagBE(BlockEntityType<?> type, BlockPos pos, BlockState blockState)
    {
        super(type, pos, blockState);

        entities = new EntityTagRef(getMaxEntities());
        entityStorageTag = entities.getOrCreateTag(entityStorageTag);
    }

    @Override
    public void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, items, true);

        if (entityStorageTag.contains(STORAGE_ENTITY_TAGNAME))
        {
            tag.put(STORAGE_ENTITY_TAGNAME, entityStorageTag);
        }
    }

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        items.clear();
        ContainerHelper.loadAllItems(tag, items);
        entityStorageTag = tag.getCompound(STORAGE_ENTITY_TAGNAME);
    }

////////////////
// Accessors: //
////////////////

// Booleans:
    public boolean itemsFull()
    {
        return getItemCount() == getMaxItems();
    }

// Integers:
    public int getItemCount()
    {
        for(int i = 0; i < getMaxItems(); i++)
        {
            if(items.get(i).isEmpty()) return i;
        }

        return getMaxItems();
    }

    public abstract int getMaxItems();

    public abstract int getMaxEntities();

///////////////////////////
// Item related methods: //
///////////////////////////

    protected void createItems()
    {
        items = NonNullList.withSize(getMaxItems(), ItemStack.EMPTY);
    }

    public boolean placeItem(ItemStack stack)
    {
        if(getItemCount() < getMaxItems() && stack.getItem() != Items.AIR)
        {
            items.set(getItemCount(), stack.split(1));
            contentsChanged();

            level.playSound(null, getBlockPos(), FFSounds.TAG_BLOCK_ADD_ITEM.get(), SoundSource.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.4F);

            return true;
        }

        return false;
    }

    public boolean removeItem(Level level, BlockPos pos)
    {
        if(getItemCount() > 0)
        {
            ItemStack stack = items.get(getItemCount() - 1);

            ItemEntity drop = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, stack);
            drop.setDefaultPickUpDelay();
            level.addFreshEntity(drop);

            items.set(getItemCount()-1, ItemStack.EMPTY);
            contentsChanged();

            level.playSound(null, getBlockPos(), FFSounds.TAG_BLOCK_REMOVE_ITEM.get(), SoundSource.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.4F);

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

/////////////////////////////////
// Entity tag related methods: //
/////////////////////////////////

    public boolean placeEntity(Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);

        if(stack.getItem() instanceof BaseEntityTagItem item)
        {
            CompoundTag stackTag = stack.getOrCreateTag();

            ListTag stackList = item.entities.getEntryList(stackTag);
            ListTag blockList = entities.getEntryList(entityStorageTag);

            String selectedEntity = EntityTagRef.getEntityID(item.entities.getSelectedEntry(stackTag));

            if (!EntityTagRef.isEntityTamed(item.entities.getSelectedEntry(stackTag)))
            {
                // use BE entityTagRef since it has the correct max entry size for entityStorageTag
                if (entities.moveEntry(stackTag, entityStorageTag))
                {
                    // save updated entity tag list to stack
                    stack.setTag(stackTag);

                    // send packet to update client version of BE
                    contentsChanged();

                    player.displayClientMessage(Component.translatable("message.flyingfamiliars.entity_tag.place_entity")
                            .withStyle(ChatFormatting.WHITE)
                            .append(": " + selectedEntity), true);

                    level.playSound(null, getBlockPos(), FFSounds.TAG_BLOCK_ADD_ENTITY.get(), SoundSource.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.4F);

                    return true;
                }
            }
            else
            {
                player.displayClientMessage(Component.translatable("message.flyingfamiliars.entity_tag.tamed_entity")
                        .withStyle(ChatFormatting.WHITE)
                        .append(": " + selectedEntity), true);
            }
        }

        return false;
    }

    public boolean removeEntity(Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);

        if(stack.getItem() instanceof BaseEntityTagItem item)
        {
            CompoundTag stackTag = stack.getOrCreateTag();

            ListTag stackList = item.entities.getEntryList(stackTag);
            ListTag blockList = entities.getEntryList(entityStorageTag);

            String selectedEntity = EntityTagRef.getEntityID(entities.getSelectedEntry(entityStorageTag));

            // use item entityTagRef since it has the correct max entry size for stackTag
            if (item.entities.moveEntry(entityStorageTag, stackTag))
            {
                // save updated entity tag list to stack
                stack.setTag(stackTag);

                // send packet to update client version of BE
                contentsChanged();

                player.displayClientMessage(Component.translatable("message.flyingfamiliars.entity_tag.remove_entity")
                        .withStyle(ChatFormatting.WHITE)
                        .append(": " + selectedEntity), true);

                level.playSound(null, getBlockPos(), FFSounds.TAG_BLOCK_REMOVE_ENTITY.get(), SoundSource.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.4F);

                return true;
            }
        }

        return false;
    }

    public void setClientEntities(CompoundTag tag)
    {
        entityStorageTag = tag;
    }

    public List<String> getEntitiesStrings()
    {
        Vector<String> entityStrings = new Vector<String>();

        for(Tag entry : entities.getEntryList(entityStorageTag))
        {
            entityStrings.add(EntityTagRef.getEntityID((CompoundTag) entry));
        }

        return entityStrings;
    }

////////////////////////////
// Fluid related methods: //
////////////////////////////

    public void createFluid()
    {
        fluid = new FluidTank(getMaxFluid());
    }

    public int getFluidLevel()
    {
        return fluid.getFluidAmount();
    }

    public int getMaxFluid()
    {
        return MAX_FLUID;
    }

    public boolean fluidFull()
    {
        return getFluidLevel() == getMaxFluid();
    }

//////////////////////
// Storage methods: //
//////////////////////

    protected void contentsChanged()
    {
        setChanged();

        if(!level.isClientSide())
        {
            if(this instanceof IRecipeBE recipeBE)
                recipeBE.findMatch();

            Packet<?> packet = getUpdatePacket();
            if (packet != null)
            {
                BlockPos pos = getBlockPos();
                ((ServerChunkCache) level.getChunkSource()).chunkMap
                        .getPlayers(new ChunkPos(pos), false)
                        .forEach(e -> e.connection.send(packet));
            }
        }
    }

    @Override
    public void clearContent()
    {
        items.clear();
        entityStorageTag = new CompoundTag();
        // clear fluid
    }

    public void drops()
    {
        SimpleContainer inventory = new SimpleContainer(items.size());
        for(int i = 0; i < items.size(); i++)
        {
            inventory.setItem(i, items.get(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
        clearContent();
    }

///////////////////////////
// Block Entity methods: //
///////////////////////////

    public ClientboundBlockEntityDataPacket getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag()
    {
        return this.saveWithoutMetadata();
    }
}
