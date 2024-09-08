package com.beesechurger.flyingfamiliars.item;

import com.beesechurger.flyingfamiliars.item.common.soul_items.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.item.common.soul_items.Phylactery;
import com.beesechurger.flyingfamiliars.item.common.soul_items.IWandEffectItem;
import com.beesechurger.flyingfamiliars.registries.FFItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.*;

public class EntityTagItemHelper
{
    public final static int MAX_ENTITIES = 3;

    public static void populateTag(ItemStack stack)
    {
        if(stack.getItem() instanceof BaseEntityTagItem tagItem)
        {
            CompoundTag stackTag = new CompoundTag();

            CompoundTag entityTag = new CompoundTag();
            ListTag tagList = entityTag.getList(BASE_ENTITY_TAGNAME, 10);
            entityTag.putString(BASE_ENTITY_TAGNAME, ENTITY_EMPTY);

            for(int i = 0; i < tagItem.getMaxEntities(); i++)
            {
                tagList.addTag(i, entityTag);
            }

            stackTag.put(BASE_ENTITY_TAGNAME, tagList);

            if(stack.getItem() instanceof IWandEffectItem effectItem)
            {
                // set default wand effect selection as capture projectile
                CompoundTag selectionTag = new CompoundTag();
                selectionTag.putString(ITEM_WAND_EFFECT_SELECTION_TAGNAME, "capture_projectile");

                stackTag.put(ITEM_WAND_EFFECT_SELECTION_TAGNAME, selectionTag);
            }

            stack.setTag(stackTag);
        }
    }

    public static void ensureTagPopulated(ItemStack stack)
    {
        if(stack.getItem() instanceof BaseEntityTagItem item)
        {
            if(!stack.hasTag())
                EntityTagItemHelper.populateTag(stack);
            else if(stack.getTag().getList(BASE_ENTITY_TAGNAME, 10).size() != item.getMaxEntities())
                EntityTagItemHelper.populateTag(stack);
        }
    }

    public static String getSelectedEntity(ItemStack stack)
    {
        if(stack.getItem() instanceof BaseEntityTagItem item)
        {
            if(stack.hasTag())
            {
                CompoundTag stackTag = stack.getTag();
                ListTag stackList = stackTag.getList(BASE_ENTITY_TAGNAME, 10);

                return stackList.getCompound(item.getMaxEntities()-1).getString(BASE_ENTITY_TAGNAME);
            }
        }

        return ENTITY_EMPTY;
    }

    public static boolean isSelectionEmpty(ItemStack stack)
    {
        if(stack.getItem() instanceof BaseEntityTagItem item)
        {
            if(stack.hasTag())
            {
                CompoundTag stackTag = stack.getTag();
                ListTag stackList = stackTag.getList(BASE_ENTITY_TAGNAME, 10);

                return stackList.get(item.getMaxEntities()-1).toString().contains(ENTITY_EMPTY);
            }
        }

        return true;
    }

    public static boolean isEmpty(ItemStack stack)
    {
        int entityCount = 0;

        if(stack.getItem() instanceof BaseEntityTagItem item)
        {
            if(stack.hasTag())
            {
                CompoundTag stackTag = stack.getTag();
                ListTag stackList = stackTag.getList(BASE_ENTITY_TAGNAME, 10);

                for(int i = 0; i < item.getMaxEntities(); i++)
                {
                    // Need to use regular Tag object here, not CompoundTag
                    if(!stackList.get(i).toString().contains(ENTITY_EMPTY)) entityCount++;
                }
            }
        }

        return entityCount == 0;
    }

    public static ItemStack getOffHandTagItem(Player player)
    {
        ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);

        if(offHand != null && offHand.getItem() instanceof Phylactery item)
            return offHand;

        return null;
    }

    public static ItemStack getCurioCharmTagItem(Player player)
    {
        ItemStack[] checkCurios = new ItemStack[7];
        checkCurios[0] = findItem(player, FFItems.PHYLACTERY_BLUE.get());
        checkCurios[1] = findItem(player, FFItems.PHYLACTERY_GREEN.get());
        checkCurios[2] = findItem(player, FFItems.PHYLACTERY_YELLOW.get());
        checkCurios[3] = findItem(player, FFItems.PHYLACTERY_GOLD.get());
        checkCurios[4] = findItem(player, FFItems.PHYLACTERY_RED.get());
        checkCurios[5] = findItem(player, FFItems.PHYLACTERY_BLACK.get());
        checkCurios[6] = findItem(player, FFItems.PHYLACTERY_WHITE.get());

        ItemStack curioCharm = null;

        for(ItemStack stack : checkCurios)
        {
            if(stack != null)
            {
                curioCharm = stack;
                break;
            }
        }

        if(curioCharm != null && curioCharm.getItem() instanceof Phylactery item)
            return curioCharm;

        return null;
    }

    public static ItemStack findItem(Player player, Item item)
    {
        return CuriosApi.getCuriosInventory(player)
                .map(i -> i.findFirstCurio(item).map(SlotResult::stack).orElse(ItemStack.EMPTY))
                .orElse(ItemStack.EMPTY);
    }
}
