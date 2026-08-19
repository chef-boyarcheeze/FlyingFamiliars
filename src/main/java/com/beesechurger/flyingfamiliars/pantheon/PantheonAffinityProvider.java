package com.beesechurger.flyingfamiliars.pantheon;

import com.beesechurger.flyingfamiliars.util.FFTypes;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Mth;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import java.util.HashMap;
import java.util.Map;

public class PantheonAffinityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag>
{
    public static final String PANTHEON_AFFINITY_DATA = "pantheon_affinity_data";

    public static Capability<PantheonAffinity> PANTHEON_AFFINITY = CapabilityManager.get(new CapabilityToken<>() {});

    private PantheonAffinity affinity = null;
    private final LazyOptional<PantheonAffinity> optional = LazyOptional.of(this::getOrCreateAffinity);

    private PantheonAffinity getOrCreateAffinity()
    {
        if (this.affinity == null)
        {
            this.affinity = new PantheonAffinity();
        }

        return this.affinity;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
    {
        if (cap == PANTHEON_AFFINITY)
        {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT()
    {
        CompoundTag nbt = new CompoundTag();
        getOrCreateAffinity().saveNBTData(nbt);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt)
    {
        getOrCreateAffinity().loadNBTData(nbt);
    }

    public static class PantheonAffinity
    {
        public static final String PANTHEON_AFFINITY = "pantheon_affinity";

        private Map<String, Float> affinityMap = new HashMap<>() {{
            put(FFTypes.FAMILIAR_TYPE_WATER.type, 0.0f);
            put(FFTypes.FAMILIAR_TYPE_LIFE.type, 0.0f);
            put(FFTypes.FAMILIAR_TYPE_AIR.type, 0.0f);
            put(FFTypes.FAMILIAR_TYPE_EARTH.type, 0.0f);
            put(FFTypes.FAMILIAR_TYPE_FIRE.type, 0.0f);
            put(FFTypes.FAMILIAR_TYPE_LIGHT.type, 0.0f);
            put(FFTypes.FAMILIAR_TYPE_VOID.type, 0.0f);
        }};

        public PantheonAffinity() {}

        public PantheonAffinity(PantheonAffinity source)
        {
            this.affinityMap = source.affinityMap;
        }

        public void saveNBTData(CompoundTag tag)
        {
            ListTag affinityList = new ListTag();

            for (var entry : affinityMap.entrySet())
            {
                affinityList.add(FloatTag.valueOf(entry.getValue()));
            }

            tag.put(PANTHEON_AFFINITY, affinityList);
        }

        public void loadNBTData(CompoundTag tag)
        {
            if (tag.contains(PANTHEON_AFFINITY))
            {
                ListTag affinityList = tag.getList(PANTHEON_AFFINITY, ListTag.TAG_FLOAT);
                int count = 0;

                for (var entry : affinityMap.entrySet())
                {
                    affinityMap.replace(entry.getKey(), affinityList.getFloat(count++));
                }
            }
        }

        public void add(String type, float amount)
        {
            affinityMap.replace(type, Mth.clamp(affinityMap.get(type) + amount, -100.0f, 100.0f));
        }

        public void multiply(String type, float amount)
        {
            affinityMap.replace(type, Mth.clamp(affinityMap.get(type) * amount, -100.0f, 100.0f));
        }

        public float query(String type)
        {
            return affinityMap.get(type);
        }

        public void set(String type, float amount)
        {
            affinityMap.replace(type, Mth.clamp(amount, -100.0f, 100.0f));
        }
    }
}