package com.roripantsu.largesign.manager;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ModCreativeTabs {
	
	public static final CreativeTabs tab_LargeSign = new CreativeTabs("tab_LargeSign") {
		@Override
		public Item getTabIconItem() {
			return ModItems.LargeSign;
		}
	};
}
