package com.beesechurger.flyingfamiliars.block.common;

import com.beesechurger.flyingfamiliars.block.entity.BaseEntityTagBE;
import com.beesechurger.flyingfamiliars.block.entity.ObeliskBlockEntity;
import com.beesechurger.flyingfamiliars.item.EntityTagItemHelper;
import com.beesechurger.flyingfamiliars.item.common.SoulItems.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.item.common.SoulItems.Phylactery;
import com.beesechurger.flyingfamiliars.registries.FFPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public abstract class BaseEntityTagBlock extends BaseEntityBlock
{
    protected VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16,16);

    protected BaseEntityTagBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if(state.getBlock() != newState.getBlock())
        {
            BlockEntity entity = level.getBlockEntity(pos);
            if(entity instanceof BaseEntityTagBE)
            {
                ((BaseEntityTagBE) entity).drops();
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
    {
        if(!level.isClientSide())
        {
            BlockEntity entity = level.getBlockEntity(pos);
            if(entity instanceof BaseEntityTagBE baseEntity)
            {
                ItemStack stack = player.getItemInHand(hand);

                if(stack.getItem() instanceof BaseEntityTagItem item)
                {
                    EntityTagItemHelper.ensureTagPopulated(stack);

                    if(!(stack.getItem() instanceof Phylactery && item.getEntityCount(stack) == 0))
                    {
                        if(!EntityTagItemHelper.isSelectionEmpty(stack))
                            baseEntity.placeEntity(player, hand);
                        else
                            baseEntity.removeEntity(player, hand);

                        return InteractionResult.SUCCESS;
                    }
                }

                // if stack.getItem() == vita bottle

                if(entity instanceof ObeliskBlockEntity)
                {
                    ObeliskBlockEntity et = (ObeliskBlockEntity) entity;

                    if(et.clicked == false)
                        et.clicked = true;
                    else
                        et.clicked = false;

                    Packet<?> packet = et.getUpdatePacket();
                    if (packet != null)
                    {
                        BlockPos posn = et.getBlockPos();
                        ((ServerChunkCache) level.getChunkSource()).chunkMap
                                .getPlayers(new ChunkPos(posn), false)
                                .forEach(e -> e.connection.send(packet));
                    }
                }

                if(!player.isShiftKeyDown())
                    baseEntity.placeItem(stack);
                else
                    baseEntity.removeItem(level, pos);

                return InteractionResult.SUCCESS;
            }
            else
            {
                throw new IllegalStateException("Flying Familiars BaseEntityTagBE container provider missing");
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return null;
    }

    abstract SoundEvent getPlaceEntitySound();

    abstract SoundEvent getRemoveEntitySound();
}
