package com.beesechurger.flyingfamiliars.block.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.BASE_ENTITY_TAGNAME;
import static com.beesechurger.flyingfamiliars.util.FFStringConstants.ENTITY_EMPTY;

public interface IEntityTagBE
{
    public final static int MAX_ITEMS = 1;
    public final static int MAX_ENTITIES = 1;
    public final static int MAX_FLUID = 250;

/////////////////
// BE actions: //
/////////////////

    default void populateTag(BaseEntityTagBE entity)
    {
        entity.entities = new CompoundTag();

        CompoundTag entityNBT = new CompoundTag();
        ListTag tagList = entityNBT.getList(BASE_ENTITY_TAGNAME, 10);
        entityNBT.putString(BASE_ENTITY_TAGNAME, ENTITY_EMPTY);

        for(int i = 0; i < entity.getMaxEntities(); i++)
        {
            tagList.addTag(i, entityNBT);
        }

        entity.entities.put(BASE_ENTITY_TAGNAME, tagList);
    }

    default void ensureTagPopulated(BaseEntityTagBE entity)
    {
        if(entity.entities.getList(BASE_ENTITY_TAGNAME, 10).size() != entity.getMaxEntities())
            populateTag(entity);
    }
}
