package com.roripantsu.largesign.manager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.roripantsu.largesign.Mod_LargeSign;
import com.roripantsu.largesign.blocks.Block_LargeSign;
import com.roripantsu.largesign.items.Item_LargeSign;

public class ModItemMeshDefinition implements ItemMeshDefinition {

	public static final String MODID = Mod_LargeSign.MODID+":";
	public static final String LargeSign = MODID + NameManager.Registry.ItemLargeSign;
	
	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack) {
		Item item = stack.getItem();
		
		if(item instanceof Item_LargeSign){
			int i = stack.getItemDamage();
			return new ModelResourceLocation(LargeSign+"_"+Block_LargeSign.ESubType.getNames()[i],"inventory");
		}
		
		return null;
	}
	
	public static void register(){
		
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(ModItems.LargeSign, new ModItemMeshDefinition());
		for(String s:Block_LargeSign.ESubType.getNames())
			ModelBakery.addVariantName(ModItems.LargeSign, ModItemMeshDefinition.LargeSign+"_"+s);
	}

}
