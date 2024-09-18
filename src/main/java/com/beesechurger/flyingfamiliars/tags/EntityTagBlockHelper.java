package com.beesechurger.flyingfamiliars.tags;

import com.beesechurger.flyingfamiliars.block.entity.BaseExtraTagBE;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import static com.beesechurger.flyingfamiliars.util.FFStringConstants.BASE_ENTITY_TAGNAME;
import static com.beesechurger.flyingfamiliars.util.FFStringConstants.ENTITY_EMPTY;

public class EntityTagBlockHelper implements IExtraTagHelper<BaseExtraTagBE>
{
    public final static EntityTagBlockHelper INSTANCE = new EntityTagBlockHelper();

    public final static int MAX_ITEMS = 1;
    public final static int MAX_ENTITIES = 1;
    public final static int MAX_FLUID = 250;

    public void populateTag(BaseExtraTagBE entity)
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

    public void ensureTagPopulated(BaseExtraTagBE entity)
    {
        if(entity.entities.getList(BASE_ENTITY_TAGNAME, 10).size() != entity.getMaxEntities())
            EntityTagBlockHelper.INSTANCE.populateTag(entity);
    }
}
