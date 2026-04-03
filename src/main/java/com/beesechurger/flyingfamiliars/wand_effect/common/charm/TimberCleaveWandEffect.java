package com.beesechurger.flyingfamiliars.wand_effect.common.charm;

import com.beesechurger.flyingfamiliars.registries.FFSounds;
import com.beesechurger.flyingfamiliars.wand_effect.common.BaseWandEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

import static com.beesechurger.flyingfamiliars.util.FFConstants.CHAT_DARK_GREEN;
import static com.beesechurger.flyingfamiliars.wand_effect.common.WandEffectItemHelper.WAND_EFFECT_TIMBER_CLEAVE;

public class TimberCleaveWandEffect extends BaseWandEffect
{
    private static final int TIMBER_CLEAVE_MAX_BLOCK_COUNT = 500;

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
        return 60;
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
        return 100;
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

            // TODO: make new sound, and maybe new particle
            level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), FFSounds.TECTONIC_SUNDER.get(), SoundSource.BLOCKS, 0.5f, 2.0f * FFSounds.getPitch());
            level.addParticle(ParticleTypes.EXPLOSION, pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, 1, 0, 0);
        }
    }

    private void breakTree(Level level, BlockPos pos, Player player)
    {
        BlockState state = level.getBlockState(pos);

        List<BlockPos> treeBlocks = new ArrayList<>();
        List<BlockPos> toCheck = new ArrayList<>();
        List<BlockPos> visited = new ArrayList<>();

        toCheck.add(pos);
        visited.add(pos);

        while (!toCheck.isEmpty())
        {
            BlockPos traversePos = toCheck.remove(0);
            treeBlocks.add(traversePos);

            for (int x = -1; x <= 1; x++)
            {
                for (int y = -1; y <= 1; y++)
                {
                    for (int z = -1; z <= 1; z++)
                    {
                        BlockPos neighbor = traversePos.offset(x, y, z);
                        BlockState neighborState = level.getBlockState(neighbor);

                        if (neighborState.getBlock() == state.getBlock() && !visited.contains(neighbor))
                        {
                            toCheck.add(neighbor);
                            visited.add(neighbor);
                        }
                    }
                }
            }
        }

        if (treeBlocks.size() <= TIMBER_CLEAVE_MAX_BLOCK_COUNT)
        {
            for (int i = treeBlocks.size() - 1; i >= 0; i--)
            {
                level.destroyBlock(treeBlocks.get(i), !player.isCreative());
                // cost per block
            }
        }
        else
        {
            // failure
        }
    }
}
