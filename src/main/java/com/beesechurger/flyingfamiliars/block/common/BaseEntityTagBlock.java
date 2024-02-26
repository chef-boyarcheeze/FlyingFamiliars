package com.beesechurger.flyingfamiliars.block.common;

import com.beesechurger.flyingfamiliars.block.entity.common.BaseEntityTagBE;
import com.beesechurger.flyingfamiliars.item.EntityTagItemHelper;
import com.beesechurger.flyingfamiliars.item.common.SoulItems.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.item.common.SoulItems.SoulBattery;
import com.beesechurger.flyingfamiliars.registries.FFSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
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

public class BaseEntityTagBlock extends BaseEntityBlock
{
    protected VoxelShape SHAPE = Block.box(0, 1, 0, 16, 16,16);

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

                    if(!(stack.getItem() instanceof SoulBattery && item.getEntityCount(stack) == 0))
                    {
                        if(!EntityTagItemHelper.isSelectionEmpty(stack))
                        {
                            String selectedEntity = EntityTagItemHelper.getSelectedEntity(stack);

                            if(baseEntity.placeEntity(stack))
                            {
                                player.displayClientMessage(Component.translatable("message.flyingfamiliars.entity_tag.place_entity")
                                        .withStyle(ChatFormatting.WHITE)
                                        .append(": " + selectedEntity), true);

                                level.playLocalSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D,
                                        FFSounds.BRAZIER_ADD_ENTITY.get(), SoundSource.BLOCKS, 0.5F, FFSounds.getPitch(), false);
                            }
                        }
                        else
                        {
                            if(baseEntity.removeEntity(stack))
                            {
                                String selectedEntity = EntityTagItemHelper.getSelectedEntity(stack);

                                player.displayClientMessage(Component.translatable("message.flyingfamiliars.entity_tag.remove_entity")
                                        .withStyle(ChatFormatting.WHITE)
                                        .append(": " + selectedEntity), true);

                                level.playLocalSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D,
                                        FFSounds.BRAZIER_REMOVE_ENTITY.get(), SoundSource.BLOCKS, 0.5F, FFSounds.getPitch(), false);
                            }
                        }

                        return InteractionResult.SUCCESS;
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

    protected SoundEvent getPlaceEntitySound()
    {
        return SoundEvents.ITEM_PICKUP;
    }

    protected SoundEvent getRemoveEntitySound()
    {
        return SoundEvents.ITEM_PICKUP;
    }
}
