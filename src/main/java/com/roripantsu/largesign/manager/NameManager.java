package com.roripantsu.largesign.manager;

import com.roripantsu.largesign.blocks.Block_LargeSign;
import com.roripantsu.largesign.items.ItemBlock_LargeSign;
import com.roripantsu.largesign.tileentity.TileEntityLargeSign;

public class NameManager {
	
	public static final class Unlocalized{
		/*-----Block-----*/
		public static final String BlockLargeSign = Block_LargeSign.class.getSimpleName();
		/*-----ItemBLock-----*/
		public static final String ItemLargeSign = ItemBlock_LargeSign.class.getSimpleName();
		
		/*-----Sub BLock/Item Suffix-----*/
		public static final String[] SubLargeSign = 
				new String[]{"Oak","Spruce","Birch","Jungle","Acacia","Dark_Oak"};
	}
	
	public static final class Registry{
		/*-----Block(TileEntity Provider)-----*/
		public static final String LargeSign = "large_sign";
		/*-----TileEntity-----*/
		public static final String TileEntityLargeSign = TileEntityLargeSign.class.getSimpleName();
	}

}
