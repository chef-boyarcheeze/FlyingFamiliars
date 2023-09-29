package com.beesechurger.flyingfamiliars.blocks;

import com.beesechurger.flyingfamiliars.blocks.entity.common.BaseEntityTagBE;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.BASE_ENTITY_TAGNAME;

public class EntityTagBlockHelper
{
    public final static int MAX_ITEMS = 1;
    public final static int MAX_ENTITIES = 3;

    public static void populateTag(BaseEntityTagBE entity)
    {
        entity.entities = new CompoundTag();

        CompoundTag entityNBT = new CompoundTag();
        ListTag tagList = entityNBT.getList(BASE_ENTITY_TAGNAME, 10);
        entityNBT.putString(BASE_ENTITY_TAGNAME, "Empty");

        for(int i = 0; i < MAX_ENTITIES; i++)
        {
            tagList.addTag(i, entityNBT);
        }

        entity.entities.put(BASE_ENTITY_TAGNAME, tagList);
    }

    public static void ensureTagPopulated(BaseEntityTagBE entity)
    {
        if(entity.entities.getList(BASE_ENTITY_TAGNAME, 10).size() != entity.getMaxEntities())
            EntityTagBlockHelper.populateTag(entity);
    }
}
