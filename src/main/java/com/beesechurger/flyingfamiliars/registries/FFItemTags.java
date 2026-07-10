package com.beesechurger.flyingfamiliars.registries;

import com.beesechurger.flyingfamiliars.FlyingFamiliars;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class FFItemTags extends ItemTagsProvider
{
    public static final TagKey<Item> CHARM_CURIO = ItemTags.create(new ResourceLocation("curios", "charm"));

    public FFItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTagProvider, ExistingFileHelper existingFileHelper)
    {
        super(output, lookupProvider, blockTagProvider, FlyingFamiliars.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider)
    {
        tag(CHARM_CURIO).addOptional(FFItems.PHYLACTERY.getId());
    }
}
