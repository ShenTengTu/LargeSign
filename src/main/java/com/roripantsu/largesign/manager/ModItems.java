package com.roripantsu.largesign.manager;

import net.minecraftforge.fml.common.registry.GameRegistry;

import com.roripantsu.largesign.Mod_LargeSign;
import com.roripantsu.largesign.items.Item_LargeSign;

public class ModItems {

	 public static Item_LargeSign LargeSign;
	 
	 public static void init(){
		 LargeSign = (Item_LargeSign)new Item_LargeSign().setUnlocalizedName(NameManager.Unlocalized.ItemLargeSign);
	 }
	 
	 public static void register(){
		 GameRegistry.registerItem(LargeSign, NameManager.Registry.ItemLargeSign, Mod_LargeSign.MODID);
	 }
}
