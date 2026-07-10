package com.beesechurger.flyingfamiliars.item.common.entity;

import com.beesechurger.flyingfamiliars.item.common.ITieredItem;
import com.beesechurger.flyingfamiliars.item.tooltip.SpiritStorageTooltipComponent;
import com.beesechurger.flyingfamiliars.tags.SpiritTagRef;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

import static com.beesechurger.flyingfamiliars.util.FFConstants.*;

public class Phylactery extends BaseEntityTagItem implements ICurioItem, ITieredItem
{
    public Phylactery(Properties properties)
    {
        super(properties);
    }

//////////////////
/// Accessors: ///
//////////////////

/// Integers:

    @Override
    public int getColor()
    {
        return ChatFormatting.WHITE.getColor();
    }

/////////////////
/// Mutators: ///
/////////////////

/// Tags:

    public static void onPickupItem(ItemEntity entity, Player player)
    {
        ItemStack entityStack = entity.getItem();

        if (!entityStack.isEmpty() && entityStack.getItem() instanceof Spirit)
        {
            int slot; // TODO: curio slot only(?)

            for (int i = 0; i < player.getInventory().getContainerSize(); i++)
            {
                if (i == player.getInventory().selected)
                {
                    continue; // prevent item deletion
                }

                ItemStack phylacteryStack = player.getInventory().getItem(i);

                if (!phylacteryStack.isEmpty() && phylacteryStack.getItem() instanceof Phylactery)
                {
                    ListTag entryList = SpiritTagRef.INSTANCE.getEntryList(phylacteryStack.getOrCreateTag());
                    CompoundTag entryTag = null;
                    CompoundTag emptyTag = null;

                    for (Tag tag : entryList)
                    {
                        String entryName = ((CompoundTag) tag).getString(STORAGE_SPIRIT_TYPE);

                        if (entryName.equals(entityStack.getItem().toString()))
                        {
                            entryTag = (CompoundTag) tag;
                            break;
                        }
                        else if (emptyTag == null && entryName.equals(STORAGE_EMPTY))
                        {
                            emptyTag = (CompoundTag) tag;
                        }
                    }

                    if (entryTag == null)
                    {
                        if (emptyTag != null)
                        {
                            entryTag = emptyTag;
                        }
                        else if (entryList.size() < SpiritTagRef.INSTANCE.getMaxEntries(phylacteryStack.getOrCreateTag()))
                        {
                            CompoundTag newEntry = new CompoundTag();

                            newEntry.putString(STORAGE_SPIRIT_TYPE, entityStack.getItem().toString());
                            newEntry.putInt(STORAGE_SPIRIT_STORAGE, 0);

                            entryTag = newEntry;
                            entryList.add(entryTag);

                            // TODO decide how to add spirit types in order
                        }
                    }

                    if (entryTag != null)
                    {
                        int currentStorage = entryTag.getInt(STORAGE_SPIRIT_STORAGE);
                        int newStorage = Mth.clamp(0, currentStorage + entityStack.getCount() * 10, SpiritTagRef.INSTANCE.getMaxStorage(phylacteryStack.getOrCreateTag())) ; // TODO: enchantment changing how much you get from each fragment
                        int pickedUpCount = (int) Math.ceil((newStorage - currentStorage) / 10.0F);

                        if (pickedUpCount > 0)
                        {
                            entryTag.putInt(STORAGE_SPIRIT_STORAGE, newStorage);
                            entityStack.shrink(pickedUpCount);

                            // resync code for item entity, from Botania's EntityHelper
                            var save = entity.getItem();
                            entity.setItem(ItemStack.EMPTY);
                            entity.setItem(save);

                            player.take(entity, pickedUpCount);

                            System.out.println(phylacteryStack.getOrCreateTag());
                        }
                    }
                }
            }
        }
    }

//////////////////
/// Cosmetics: ///
//////////////////

    @Override
    public List<TooltipComponent> getTooltipComponents(ItemStack stack)
    {
        List<TooltipComponent> componentTooltips = super.getTooltipComponents(stack);

        // Show stored entities if shift is pressed, show spirit fragment levels if not
        if (componentTooltips.isEmpty() || !Screen.hasShiftDown())
        {
            ListTag entryList = SpiritTagRef.INSTANCE.getEntryList(stack.getOrCreateTag());
            int maxSpirit = SpiritTagRef.INSTANCE.getMaxStorage(stack.getOrCreateTag());

            // separate entryTag list into rows of 9, accounting for remainder
            for (Tag tag : entryList)
            {
                CompoundTag entryTag = (CompoundTag) tag;

                componentTooltips.add(new SpiritStorageTooltipComponent(entryTag.getString(STORAGE_SPIRIT_TYPE), entryTag.getInt(STORAGE_SPIRIT_STORAGE), maxSpirit));
            }
        }

        return componentTooltips;
    }
}
