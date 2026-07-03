package com.beesechurger.flyingfamiliars.registries;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class FFBlockTags extends BlockTagsProvider
{
    public FFBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper)
    {
        super(output, lookupProvider, FlyingFamiliars.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                FFBlocks.RUNIC_BRICKS.get(),
                FFBlocks.RUNIC_BRICK_STAIRS.get(),
                FFBlocks.RUNIC_BRICK_SLAB.get(),
                FFBlocks.RUNIC_BRICK_WALL.get(),

                FFBlocks.INSCRIBED_RUNIC_BRICKS.get(),
                FFBlocks.INSCRIBED_RUNIC_BRICK_STAIRS.get(),
                FFBlocks.INSCRIBED_RUNIC_BRICK_SLAB.get(),
                FFBlocks.INSCRIBED_RUNIC_BRICK_WALL.get(),

                FFBlocks.BRAZIER.get(),
                FFBlocks.OBELISK.get(),
                FFBlocks.RUNIC_PEDESTAL.get(),
                FFBlocks.VITA_ALEMBIC.get(),
                FFBlocks.CEREMONIAL_FONT.get()
        );

        tag(BlockTags.NEEDS_IRON_TOOL).add(
                FFBlocks.BRAZIER.get()
        );

        tag(BlockTags.STAIRS).add(
                FFBlocks.RUNIC_BRICK_STAIRS.get(),
                FFBlocks.INSCRIBED_RUNIC_BRICK_STAIRS.get()
        );

        tag(BlockTags.SLABS).add(
                FFBlocks.RUNIC_BRICK_SLAB.get(),
                FFBlocks.INSCRIBED_RUNIC_BRICK_SLAB.get()
        );

        tag(BlockTags.WALLS).add(
                FFBlocks.RUNIC_BRICK_WALL.get(),
                FFBlocks.INSCRIBED_RUNIC_BRICK_WALL.get()
        );
    }
}
