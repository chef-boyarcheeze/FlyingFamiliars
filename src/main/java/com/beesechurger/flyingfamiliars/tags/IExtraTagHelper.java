package com.beesechurger.flyingfamiliars.tags;

public interface IExtraTagHelper<T>
{
    public abstract void populateTag(T object);

    public abstract void ensureTagPopulated(T object);
}
