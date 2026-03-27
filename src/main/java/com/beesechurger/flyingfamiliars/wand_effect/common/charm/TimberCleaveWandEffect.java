package com.beesechurger.flyingfamiliars.wand_effect.common.charm;

import com.beesechurger.flyingfamiliars.item.FFItemHandler;
import com.beesechurger.flyingfamiliars.recipe.TectonicSunderRecipe;
import com.beesechurger.flyingfamiliars.registries.FFSounds;
import com.beesechurger.flyingfamiliars.wand_effect.common.BaseWandEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.HashSet;
import java.util.Set;

import static com.beesechurger.flyingfamiliars.util.FFConstants.*;
import static com.beesechurger.flyingfamiliars.wand_effect.common.WandEffectItemHelper.WAND_EFFECT_TECTONIC_SUNDER;

public class TrunkCleaveWandEffect extends BaseWandEffect
{
    private static final Set<Item> INPUT_ITEMS = new HashSet<>();

//////////////////
/// Accessors: ///
//////////////////

/// Strings:

    @Override
    public String getName()
    {
        return WAND_EFFECT_TRUNK_CLEAVE;
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
        return 50;
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
        return 50;
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
                level.destroyBlock(pos, !player.getAbilities().instabuild, player);

                // destroy more blocks
            }

            level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), FFSounds.TECTONIC_SUNDER.get(), SoundSource.BLOCKS, 0.5f, 2.0f * FFSounds.getPitch());
            level.addParticle(ParticleTypes.EXPLOSION, pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, 1, 0, 0);
        }
    }
}
