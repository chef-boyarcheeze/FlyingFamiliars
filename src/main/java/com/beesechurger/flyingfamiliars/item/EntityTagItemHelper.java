package com.beesechurger.flyingfamiliars.item;

import com.beesechurger.flyingfamiliars.item.common.SoulItems.BaseEntityTagItem;
import com.beesechurger.flyingfamiliars.item.common.SoulItems.IModeCycleItem;
import com.beesechurger.flyingfamiliars.item.common.SoulItems.SoulBattery;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Optional;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.*;

public class EntityTagItemHelper
{
    public final static int MAX_ENTITIES = 3;

    public static void populateTag(ItemStack stack)
    {
        if(stack.getItem() instanceof BaseEntityTagItem item)
        {
            CompoundTag stackTag = new CompoundTag();

            CompoundTag entityNBT = new CompoundTag();
            ListTag tagList = entityNBT.getList(BASE_ENTITY_TAGNAME, 10);
            entityNBT.putString(BASE_ENTITY_TAGNAME, ENTITY_EMPTY);

            for(int i = 0; i < item.getMaxEntities(); i++)
            {
                tagList.addTag(i, entityNBT);
            }

            stackTag.put(BASE_ENTITY_TAGNAME, tagList);

            if(stack.getItem() instanceof IModeCycleItem)
            {
                CompoundTag modeTag = new CompoundTag();
                modeTag.putInt(ITEM_MODE_TAGNAME, 0);

                stackTag.put(ITEM_MODE_TAGNAME, modeTag);
            }

            stack.setTag(stackTag);
        }
    }

    public static void ensureTagPopulated(ItemStack stack)
    {
        if(stack.getItem() instanceof BaseEntityTagItem item && stack.hasTag())
        {
            if(stack.getTag().getList(BASE_ENTITY_TAGNAME, 10).size() != item.getMaxEntities())
                EntityTagItemHelper.populateTag(stack);
        }
        else if(!stack.hasTag())
            EntityTagItemHelper.populateTag(stack);
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

    public static ItemStack getOffHandBattery(Player player)
    {
        ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);

        if(offHand != null && offHand.getItem() instanceof SoulBattery item)
            return offHand;

            /*int entityCount = item.getEntityCount(offHand);

            if(!FFKeys.SOUL_WAND_SHIFT.isDown())
            {
                if(entityCount < item.getMaxEntities())
                    return offHand;
            }
            else
            {
                if(entityCount > 0)
                    return offHand;
            }*/

        return null;
    }

    public static ItemStack getCurioCharmBattery(Player player)
    {
        Optional<SlotResult>[] checkCurios = new Optional[7];
        checkCurios[0] = CuriosApi.getCuriosHelper().findFirstCurio(player, FFItems.SOUL_BATTERY_BLUE.get());
        checkCurios[1] = CuriosApi.getCuriosHelper().findFirstCurio(player, FFItems.SOUL_BATTERY_GREEN.get());
        checkCurios[2] = CuriosApi.getCuriosHelper().findFirstCurio(player, FFItems.SOUL_BATTERY_YELLOW.get());
        checkCurios[3] = CuriosApi.getCuriosHelper().findFirstCurio(player, FFItems.SOUL_BATTERY_GOLD.get());
        checkCurios[4] = CuriosApi.getCuriosHelper().findFirstCurio(player, FFItems.SOUL_BATTERY_RED.get());
        checkCurios[5] = CuriosApi.getCuriosHelper().findFirstCurio(player, FFItems.SOUL_BATTERY_BLACK.get());
        checkCurios[6] = CuriosApi.getCuriosHelper().findFirstCurio(player, FFItems.SOUL_BATTERY_WHITE.get());

        ItemStack curioCharm = null;

        for(Optional<SlotResult> battery : checkCurios)
        {
            if(battery != null && battery.isPresent())
            {
                curioCharm = battery.get().stack();
                break;
            }
        }

        if(curioCharm != null && curioCharm.getItem() instanceof SoulBattery item)
            return curioCharm;

            /*int entityCount = item.getEntityCount(curioCharm);

            if(!FFKeys.SOUL_WAND_SHIFT.isDown())
            {
                if(entityCount < item.getMaxEntities())
                    return curioCharm;
            }
            else
            {
                if(entityCount > 0)
                    return curioCharm;
            }*/

        return null;
    }
}
