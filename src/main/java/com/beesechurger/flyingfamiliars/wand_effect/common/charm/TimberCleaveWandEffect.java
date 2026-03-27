package com.beesechurger.flyingfamiliars.wand_effect.common.charm;

import com.beesechurger.flyingfamiliars.registries.FFSounds;
import com.beesechurger.flyingfamiliars.wand_effect.common.BaseWandEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;

import java.util.HashSet;
import java.util.Set;

import static com.beesechurger.flyingfamiliars.util.FFConstants.CHAT_DARK_GREEN;
import static com.beesechurger.flyingfamiliars.util.FFConstants.MAX_CHARGE_TIME;
import static com.beesechurger.flyingfamiliars.wand_effect.common.WandEffectItemHelper.WAND_EFFECT_TIMBER_CLEAVE;

public class TimberCleaveWandEffect extends BaseWandEffect
{
    private static final int TIMBER_CLEAVE_MAX_HEIGHT = 80;
    private static final int TIMBER_CLEAVE_MAX_WIDTH = 5;

//////////////////
/// Accessors: ///
//////////////////

/// Strings:

    @Override
    public String getName()
    {
        return WAND_EFFECT_TIMBER_CLEAVE;
    }

/// Booleans:

    @Override
    public boolean usableOnBlockOnly()
    {
        return true;
    }

    @Override
    public boolean checkLookedAtBlock(BlockState state)
    {
        return state.is(BlockTags.LOGS);
    }

/// Integers:

    @Override
    public int getUseDurationMin()
    {
        return 20;
    }

    @Override
    public int getUseDurationMax()
    {
        return MAX_CHARGE_TIME;
    }

    @Override
    public int getCost()
    {
        return 0;
    }

    @Override
    public int getCooldown()
    {
        return 20;
    }

    @Override
    public int getBarColor()
    {
        return CHAT_DARK_GREEN;
    }

/// Misc:

    @Override
    public ChatFormatting getTooltipColor()
    {
        return ChatFormatting.DARK_GREEN;
    }

    @Override
    public UseAnim getUseAnimation()
    {
        return UseAnim.BOW;
    }

///////////////////////////
/// Wand effect action: ///
///////////////////////////

    @Override
    public void useOn(Level level, Player player, BlockPos pos)
    {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if (!state.isAir() && state.getDestroyProgress(player, level, pos) > 0)
        {
            // useful for wand effect cost?
            float hardness = state.getDestroySpeed(level, pos);

            if (!level.isClientSide())
            {
                breakTree(level, pos, player);
            }

            level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), FFSounds.TECTONIC_SUNDER.get(), SoundSource.BLOCKS, 0.5f, 2.0f * FFSounds.getPitch());
            level.addParticle(ParticleTypes.EXPLOSION, pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, 1, 0, 0);
        }
    }

    private void breakTree(Level level, BlockPos pos, Player player)
    {
        BlockState state = level.getBlockState(pos);
        Block wood = state.getBlock();

        var height = 0;
        for (;;)
        {
            BlockState block = level.getBlockState(pos.offset(0, height + 1, 0));

            if (block == null || block.getBlock() != wood)
            {
                break;
            }

            height++;
        }

        var numLeaves = 0;
        if (height + 1 < TIMBER_CLEAVE_MAX_HEIGHT)
        {
            for (int xPos = pos.getX() - 1; xPos <= pos.getX() + 1; xPos++)
            {
                for (int yPos = pos.getY() + height - 1; yPos <= pos.getY() + height + 1; yPos++)
                {
                    for (int zPos = pos.getZ() - 1; zPos <= pos.getZ() + 1; zPos++)
                    {
                        BlockState leaves = level.getBlockState(new BlockPos(xPos, yPos, zPos));
                        if (leaves != null && leaves.is(BlockTags.LEAVES))
                        {
                            numLeaves++;
                        }
                    }
                }
            }
        }

        if (numLeaves > 3)
        {
            // TODO: invert order of breaking (block search)
            for (int xPos = pos.getX() - TIMBER_CLEAVE_MAX_WIDTH; xPos <= pos.getX() + TIMBER_CLEAVE_MAX_WIDTH; xPos++)
            {
                for (int yPos = pos.getY(); yPos <= pos.getY() + height + 1; yPos++)
                {
                    for (int zPos = pos.getZ() - TIMBER_CLEAVE_MAX_WIDTH; zPos <= pos.getZ() + TIMBER_CLEAVE_MAX_WIDTH; zPos++)
                    {
                        BlockState block = level.getBlockState(new BlockPos(xPos, yPos, zPos));
                        if (wood == block.getBlock())
                        {
                            int xDist = xPos - pos.getX();
                            int yDist = yPos - pos.getY();
                            int zDist = zPos - pos.getZ();

                            if (9*xDist*xDist + yDist*yDist + 9*zDist*zDist < 2500)
                            {
                                level.destroyBlock(new BlockPos(xPos, yPos, zPos), !player.isCreative());

                                /*if (level.isClientSide())
                                {
                                    breakTree(level, xPos, yPos, zPos, xStart, yStart, zStart, wood);
                                }*/
                            }
                        }
                    }
                }
            }
        }
    }
}
