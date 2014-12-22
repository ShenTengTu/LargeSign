package com.roripantsu.largesign.manager;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes {

	public static void register(){
		
		ItemStack[] itemStacks=new ItemStack[]{
				new ItemStack(Blocks.planks,1,0),
				new ItemStack(Blocks.planks,1,1),
				new ItemStack(Blocks.planks,1,2),
				new ItemStack(Blocks.planks,1,3),
				new ItemStack(Blocks.planks,1,4),
				new ItemStack(Blocks.planks,1,5)};
		
		for(int i=0;i<itemStacks.length;i++){
			GameRegistry.addRecipe(new ItemStack(ModBlocks.LargeSign, 3,i), new Object[] {
				"SSS", "SIS", "SSS", 'S', itemStacks[i] ,'I',Items.item_frame});
		}
	}
}
