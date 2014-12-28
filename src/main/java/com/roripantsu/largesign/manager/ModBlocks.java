package com.roripantsu.largesign.manager;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.roripantsu.largesign.blocks.Block_LargeSign;
import com.roripantsu.largesign.tileentity.TileEntityLargeSign;

public class ModBlocks {

	 public static Block_LargeSign LargeSign;
	 
	 public static void init(){
		 LargeSign = (Block_LargeSign)new Block_LargeSign(TileEntityLargeSign.class).setHardness(1.0F)
				 .setStepSound(Block.soundTypeWood).setUnlocalizedName(NameManager.Unlocalized.BlockLargeSign);
	 }
	 
	 public static void register(){
		 GameRegistry.registerBlock(LargeSign,null,NameManager.Registry.BlockLargeSign);
	 }
}
