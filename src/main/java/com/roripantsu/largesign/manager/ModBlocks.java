package com.roripantsu.largesign.manager;

import net.minecraftforge.fml.common.registry.GameRegistry;

import com.roripantsu.largesign.blocks.Block_LargeSign;
import com.roripantsu.largesign.items.ItemBlock_LargeSign;
import com.roripantsu.largesign.tileentity.TileEntityLargeSign;

public class ModBlocks {

	 public static Block_LargeSign LargeSign;
	 
	 public static void init(){
		 LargeSign = new Block_LargeSign(TileEntityLargeSign.class);
	 }
	 
	 public static void register(){
		 GameRegistry.registerBlock(LargeSign,ItemBlock_LargeSign.class,
					NameManager.Registry.LargeSign);
	 }
}
