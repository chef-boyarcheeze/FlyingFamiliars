package com.beesechurger.flyingfamiliars.wand_effect.common.charm;

import com.beesechurger.flyingfamiliars.recipe.TectonicCrushRecipe;
import com.beesechurger.flyingfamiliars.wand_effect.common.BaseWandEffect;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;

import static com.beesechurger.flyingfamiliars.util.FFConstants.CHAT_GOLD;
import static com.beesechurger.flyingfamiliars.util.FFConstants.MAX_CHARGE_TIME;

public class TectonicCrushWandEffect extends BaseWandEffect
{
    private static final Set<Item> INPUT_ITEMS = new HashSet<>();

////////////////
// Accessors: //
////////////////

// Strings:
    @Override
    public String getName()
    {
        return "tectonic_crush_charm";
    }

    @Override
    public String getTranslatableName()
    {
        return "tooltip.flyingfamiliars.wand_effect.tectonic_crush_charm";
    }

// Booleans:
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

// Integers:

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

    // charge up time?

    @Override
    public int getBarColor()
    {
        return CHAT_GOLD;
    }

// Misc:
    @Override
    public ChatFormatting getTooltipColor()
    {
        return ChatFormatting.GOLD;
    }

    @Override
    public UseAnim getUseAnimation() {
        return UseAnim.BOW;
    }

///////////////
// Mutators: //
///////////////

// Misc:
    public void populateItemList()
    {
        INPUT_ITEMS.clear();

        for(TectonicCrushRecipe entry : Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(TectonicCrushRecipe.Type.INSTANCE))
        {
            INPUT_ITEMS.add(entry.getInputItem().getItem());
        }
    }

/////////////////////////
// Wand effect action: //
/////////////////////////

    @Override
    public void actionOn(Level level, Player player, BlockPos pos)
    {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        ItemStack replacement = getOutputItem(level, new ItemStack(block.asItem()));

        if (replacement != null && !replacement.isEmpty())
        {
            if (!state.isAir() && state.getDestroyProgress(player, level, pos) > 0
                    && state.getBlock().asItem() != replacement.getItem())
            {
                float hardness = state.getDestroySpeed(level, pos);

                if (!level.isClientSide)
                {
                    level.destroyBlock(pos, !player.getAbilities().instabuild, player);

                    BlockHitResult hit = new BlockHitResult(pos.getCenter(), Direction.UP, pos, false);
                    InteractionResult result = substituteUse(new UseOnContext(player, InteractionHand.MAIN_HAND, hit), replacement).getFirst();

                    if (!player.getAbilities().instabuild)
                    {
                        if (result.consumesAction())
                        {
                            /*removeFromInventory(player, rod, replacement, true);
                            displayRemainderCounter(player, rod);*/
                        }
                        else
                        {
                            System.out.println("particle");
                            ((ServerLevel) level).sendParticles(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                                    2, 0.1, 0.1, 0.1, 0);
                        }
                    }
                }
            }
        }
    }
    
    private Pair<InteractionResult, BlockPos> substituteUse(UseOnContext context, ItemStack toUse)
    {
        ItemStack save = ItemStack.EMPTY;
        BlockHitResult hit = new BlockHitResult(context.getClickLocation(), context.getClickedFace(), context.getClickedPos(), context.isInside());
        UseOnContext newcontext;

        if (context.getPlayer() != null) {
            save = context.getPlayer().getItemInHand(context.getHand());
            context.getPlayer().setItemInHand(context.getHand(), toUse);
            // Need to construct a new one still to refresh the itemstack
            newcontext = new UseOnContext(context.getPlayer(), context.getHand(), hit);
        }
        else
        {
            newcontext = new ItemUseContextWithNullPlayer(context.getLevel(), context.getHand(), toUse, hit);
        }

        BlockPos finalPos = new BlockPlaceContext(newcontext).getClickedPos();

        InteractionResult result = toUse.useOn(newcontext);

        if (context.getPlayer() != null) {
            context.getPlayer().setItemInHand(context.getHand(), save);
        }

        return Pair.of(result, finalPos);
    }

    private static class ItemUseContextWithNullPlayer extends UseOnContext
    {
        public ItemUseContextWithNullPlayer(Level world, InteractionHand hand, ItemStack stack, BlockHitResult rayTraceResult)
        {
            super(world, null, hand, stack, rayTraceResult);
        }
    }

    private ItemStack getOutputItem(Level level, ItemStack inputItem)
    {
        boolean found = false;
        for(TectonicCrushRecipe entry : level.getRecipeManager().getAllRecipesFor(TectonicCrushRecipe.Type.INSTANCE))
        {
            if(entry.itemMatches(inputItem))
            {
                return entry.getOutputItem();
            }
        }

        return null;
    }
}
