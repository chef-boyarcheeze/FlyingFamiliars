package com.beesechurger.flyingfamiliars.item.tooltip;

import net.minecraft.util.Tuple;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.List;

public record FragmentStorageTooltipComponent(List<Tuple<String, Integer>> entryList
) implements TooltipComponent {
}
