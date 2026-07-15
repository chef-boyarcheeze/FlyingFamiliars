package com.beesechurger.flyingfamiliars.wand_effect.common.sentry;

import com.beesechurger.flyingfamiliars.registries.FFSounds;
import com.beesechurger.flyingfamiliars.util.FFTypes;
import com.beesechurger.flyingfamiliars.wand_effect.common.BaseWandEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

import static com.beesechurger.flyingfamiliars.wand_effect.common.WandEffectItemHelper.WAND_EFFECT_DRYADS_CURSE;

public class DryadsCurseWandEffect extends BaseWandEffect
{
//////////////////
/// Accessors: ///
//////////////////

/// Strings:

    @Override
    public String getName()
    {
        return WAND_EFFECT_DRYADS_CURSE;
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
        return state.is(BlockTags.MOSS_REPLACEABLE);
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
    public Map<String, Integer> getCost()
    {
        return Map.of();
    }

    @Override
    public int getCooldown()
    {
        return 100;
    }

    @Override
    public int getColor()
    {
        return FFTypes.FAMILIAR_TYPE_LIFE;
    }

    /// Misc:

    @Override
    public UseAnim getUseAnimation()
    {
        return UseAnim.BOW;
    }

///////////////////////////
/// Wand effect action: ///
///////////////////////////

    @Override
    public void useOn(Level level, Player player, BlockPos pos, int duration)
    {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if (!state.isAir() && state.getDestroyProgress(player, level, pos) > 0)
        {
            // useful for wand effect cost?
            float hardness = state.getDestroySpeed(level, pos);

            if (!level.isClientSide())
            {
                //breakTree(level, pos, player);
            }

            // TODO: make new sound, and maybe new particle
            level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), FFSounds.TECTONIC_SUNDER.get(), SoundSource.BLOCKS, 0.5f, 2.0f * FFSounds.getPitch());
            level.addParticle(ParticleTypes.EXPLOSION, pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, 1, 0, 0);
        }
    }
}
