package com.roripantsu.largesign.manager;

import com.roripantsu.largesign.blocks.Block_LargeSign;
import com.roripantsu.largesign.items.Item_LargeSign;
import com.roripantsu.largesign.tileentity.TileEntityLargeSign;

public class NameManager {
	
	public static final class Unlocalized{
		/*-----Block-----*/
		public static final String BlockLargeSign = Block_LargeSign.class.getSimpleName();
		/*-----Item-----*/
		public static final String ItemLargeSign = Item_LargeSign.class.getSimpleName();
		
		/*-----Sub BLock/Item Suffix-----*/
		public static final String[] SubLargeSign = 
				new String[]{"Oak","Spruce","Birch","Jungle","Acacia","Dark_Oak"};
	}
	
	public static final class Registry{
		
		/*-----Block(BlockContainder)-----*/
		public static final String BlockLargeSign = "large_sign";
		/*-----item-----*/
		public static final String ItemLargeSign = "large_sign";
		/*-----item-----*/
		/*-----TileEntity-----*/
		public static final String TileEntityLargeSign = TileEntityLargeSign.class.getSimpleName();
	}

}
