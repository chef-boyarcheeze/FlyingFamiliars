package com.beesechurger.flyingfamiliars.item.tooltip;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.List;

public record EntityStorageTooltipComponent(List<CompoundTag> entryList,
                                            boolean hasMoreEntities
) implements TooltipComponent {
}