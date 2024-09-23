package com.beesechurger.flyingfamiliars.block.entity;

import com.beesechurger.flyingfamiliars.item.common.entity_items.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.registries.FFSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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

import java.util.Random;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.BASE_ENTITY_TAGNAME;
import static com.beesechurger.flyingfamiliars.util.FFStringConstants.ENTITY_EMPTY;

public abstract class BaseEntityTagBE extends BlockEntity implements Clearable, IEntityTagBE
{
    public final static int MAX_ITEMS = 1;
    public final static int MAX_ENTITIES = 1;
    public final static int MAX_FLUID = 250;

    protected int itemCapacityMod = 1;
    protected int entityCapacityMod = 1;
    protected int fluidCapacityMod = 4;

    public NonNullList<ItemStack> items = null;
    public CompoundTag entities = null;
    public FluidTank fluid = null;

    public Random random = new Random();

    public BaseEntityTagBE(BlockEntityType<?> type, BlockPos pos, BlockState blockState)
    {
        super(type, pos, blockState);
    }

    @Override
    public void saveAdditional(CompoundTag tag)
    {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, items, true);
        tag.put(BASE_ENTITY_TAGNAME, entities);
    }

    @Override
    public void load(CompoundTag tag)
    {
        super.load(tag);
        items.clear();
        ContainerHelper.loadAllItems(tag, items);
        entities = tag.getCompound(BASE_ENTITY_TAGNAME);
    }

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
            ItemStack stack = items.get(getItemCount()-1);

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
        return MAX_ITEMS * itemCapacityMod;
    }

    public boolean itemsFull()
    {
        return getItemCount() == getMaxItems();
    }

/////////////////////////////////
// Entity tag related methods: //
/////////////////////////////////

    protected void createEntities()
    {
        entities = new CompoundTag();
    }

    public boolean placeEntity(Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);

        if(stack.getItem() instanceof BaseEntityTagItem item)
        {
            CompoundTag stackTag = item.getOrCreateTag(stack);

            ListTag stackList = stackTag.getList(BASE_ENTITY_TAGNAME, 10);
            ListTag blockList = entities.getList(BASE_ENTITY_TAGNAME, 10);

            String selectedEntity = item.getSelectedEntity(stack);

            for(int i = 0; i < getMaxEntities(); i++)
            {
                // Need to use regular Tag object for ENTITY_EMPTY compare here, not CompoundTag
                if(blockList.get(i).toString().contains(ENTITY_EMPTY))
                {
                    CompoundTag entityNBT = stackList.getCompound(item.getMaxEntities()-1);

                    if(!entityNBT.contains("Owner"))
                    {
                        blockList.set(i, entityNBT);
                        entities.put(BASE_ENTITY_TAGNAME, blockList);

                        CompoundTag empty = new CompoundTag();
                        empty.putString(BASE_ENTITY_TAGNAME, ENTITY_EMPTY);
                        stackList.set(item.getMaxEntities()-1, empty);

                        stackTag.put(BASE_ENTITY_TAGNAME, stackList);
                        stack.setTag(stackTag);
                        contentsChanged();

                        player.displayClientMessage(Component.translatable("message.flyingfamiliars.entity_tag.place_entity")
                                .withStyle(ChatFormatting.WHITE)
                                .append(": " + selectedEntity), true);

                        level.playSound(null, getBlockPos(), FFSounds.TAG_BLOCK_ADD_ENTITY.get(), SoundSource.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.4F);

                        return true;
                    }
                    else
                    {
                        player.displayClientMessage(Component.translatable("message.flyingfamiliars.entity_tag.tamed_entity")
                                .withStyle(ChatFormatting.WHITE)
                                .append(": " + selectedEntity), true);
                    }
                }
            }
        }

        return false;
    }

    public boolean removeEntity(Player player, InteractionHand hand)
    {
        ensureTagPopulated(this);

        ItemStack stack = player.getItemInHand(hand);

        if(stack.getItem() instanceof BaseEntityTagItem item)
        {
            CompoundTag stackTag = item.getOrCreateTag(stack);

            ListTag stackList = stackTag.getList(BASE_ENTITY_TAGNAME, 10);
            ListTag blockList = entities.getList(BASE_ENTITY_TAGNAME, 10);

            for(int i = getMaxEntities(); i > 0; i--)
            {
                if(!blockList.get(i-1).toString().contains(ENTITY_EMPTY))
                {
                    CompoundTag entityNBT = blockList.getCompound(i-1);

                    CompoundTag empty = new CompoundTag();
                    empty.putString(BASE_ENTITY_TAGNAME, ENTITY_EMPTY);
                    blockList.set(i-1, empty);
                    entities.put(BASE_ENTITY_TAGNAME, blockList);

                    stackList.set(item.getMaxEntities()-1, entityNBT);

                    stackTag.put(BASE_ENTITY_TAGNAME, stackList);
                    stack.setTag(stackTag);
                    contentsChanged();

                    String selectedEntity = item.getSelectedEntity(stack);

                    player.displayClientMessage(Component.translatable("message.flyingfamiliars.entity_tag.remove_entity")
                            .withStyle(ChatFormatting.WHITE)
                            .append(": " + selectedEntity), true);

                    level.playSound(null, getBlockPos(), FFSounds.TAG_BLOCK_REMOVE_ENTITY.get(), SoundSource.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.4F);

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
        ensureTagPopulated(this);

        int entityCount = 0;

        if(entities != null)
        {
            ListTag tagList = entities.getList(BASE_ENTITY_TAGNAME, 10);

            for(int i = 0; i < getMaxEntities(); i++)
            {
                // Need to use regular Tag object here, not CompoundTag
                if(!tagList.get(i).toString().contains(ENTITY_EMPTY)) entityCount++;
            }
        }

        return entityCount;
    }

    public int getMaxEntities()
    {
        return MAX_ENTITIES * entityCapacityMod;
    }

    public String getID(int listValue, ItemStack stack)
    {
        return stack.getTag().getList(BASE_ENTITY_TAGNAME, 10).getCompound(listValue).getString(BASE_ENTITY_TAGNAME);
    }

    public NonNullList<String> getEntitiesStrings()
    {
        ensureTagPopulated(this);

        ListTag blockList = entities.getList(BASE_ENTITY_TAGNAME, 10);
        NonNullList<String> entityStrings = NonNullList.withSize(getMaxEntities(), "");

        for(int i = 0; i < getMaxEntities(); i++)
        {
            entityStrings.set(i, blockList.getCompound(i).getString(BASE_ENTITY_TAGNAME));
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
        return MAX_FLUID * fluidCapacityMod;
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
            ensureTagPopulated(this);

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
        populateTag(this);
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
