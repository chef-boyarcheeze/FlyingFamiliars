package com.beesechurger.flyingfamiliars.wand_effect.common.charm;

import com.beesechurger.flyingfamiliars.client.FFSounds;
import com.beesechurger.flyingfamiliars.item.FFItemHandler;
import com.beesechurger.flyingfamiliars.item.FFItems;
import com.beesechurger.flyingfamiliars.recipe.TectonicSunderRecipe;
import com.beesechurger.flyingfamiliars.util.FFTypes;
import com.beesechurger.flyingfamiliars.wand_effect.common.BaseWandEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
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
import java.util.Map;
import java.util.Set;

import static com.beesechurger.flyingfamiliars.wand_effect.common.WandEffectItemHelper.WAND_EFFECT_TECTONIC_SUNDER;

public class TectonicSunderWandEffect extends BaseWandEffect
{
    private static final Set<Item> INPUT_ITEMS = new HashSet<>();

//////////////////
/// Accessors: ///
//////////////////

/// Strings:
    @Override
    public String getName()
    {
        return WAND_EFFECT_TECTONIC_SUNDER;
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
        if (INPUT_ITEMS.isEmpty())
        {
            populateItemList();
        }

        return INPUT_ITEMS.contains(state.getBlock().asItem());
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
    public Map<String, Integer> getCost()
    {
        return Map.ofEntries(
                Map.entry(FFItems.STONY_SPIRIT_FRAGMENT.get().toString(), 10)
        );
    }

    @Override
    public int getCooldown()
    {
        return 5;
    }

    @Override
    public int getColor()
    {
        return FFTypes.FAMILIAR_TYPE_EARTH.color;
    }

/// Misc:

    @Override
    public UseAnim getUseAnimation()
    {
        return UseAnim.BOW;
    }

/////////////////
/// Mutators: ///
/////////////////

/// Misc:
    private void populateItemList()
    {
        INPUT_ITEMS.clear();

        for(TectonicSunderRecipe entry : Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(TectonicSunderRecipe.Type.INSTANCE))
        {
            INPUT_ITEMS.add(entry.getInputItem().getItem());
        }
    }

///////////////////////////
/// Wand effect action: ///
///////////////////////////

    @Override
    public void useOn(Level level, Player player, BlockPos pos, int duration)
    {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        ItemStack replacement = getOutputItem(level, new ItemStack(block.asItem()));

        if (replacement != null && !replacement.isEmpty())
        {
            if (!state.isAir() && state.getDestroyProgress(player, level, pos) > 0
                    && state.getBlock().asItem() != replacement.getItem())
            {
                // useful for wand effect cost?
                float hardness = state.getDestroySpeed(level, pos);

                if (!level.isClientSide())
                {
                    level.destroyBlock(pos, false, player);
                    BlockHitResult hit = new BlockHitResult(pos.getCenter(), Direction.UP, pos, false);
                    FFItemHandler.substituteUse(new UseOnContext(player, InteractionHand.MAIN_HAND, hit), replacement).getFirst();
                }

                level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), FFSounds.TECTONIC_SUNDER.get(), SoundSource.BLOCKS, 0.5f, 2.0f * FFSounds.getPitch());
                level.addParticle(ParticleTypes.EXPLOSION, pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, 1, 0, 0);
            }
        }
    }

    private ItemStack getOutputItem(Level level, ItemStack inputItem)
    {
        boolean found = false;
        for(TectonicSunderRecipe entry : level.getRecipeManager().getAllRecipesFor(TectonicSunderRecipe.Type.INSTANCE))
        {
            if(entry.itemMatches(inputItem))
            {
                return entry.getOutputItem();
            }
        }

        return null;
    }
}
